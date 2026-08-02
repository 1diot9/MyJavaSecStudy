#!/usr/bin/env python3
"""修改 .class 实际类名，保持原文件名；默认可打包为同名无扩展名 jar。

用法:
  # 默认：改类名并输出无扩展名 jar（内含原名.class）
  python classNameModefier.py Calc.class com.sun.rowset.JdbcRowSetImpl
  python classNameModefier.py Calc.class com/example/Evil -o out

  # 只改 .class 实际类名，不打包 jar（输出文件名=输入文件名）
  python classNameModefier.py CalcJType1.class "file:.D:.CalcJType" -c
  python classNameModefier.py Calc.class com.example.Evil -c -o out

  python classNameModefier.py Calc.class NewName --dry-run
"""

from __future__ import annotations

import argparse
import struct
import sys
import zipfile
from pathlib import Path


# ---------------------------------------------------------------------------
# Constant pool tags
# ---------------------------------------------------------------------------
CONSTANT_Utf8 = 1
CONSTANT_Integer = 3
CONSTANT_Float = 4
CONSTANT_Long = 5
CONSTANT_Double = 6
CONSTANT_Class = 7
CONSTANT_String = 8
CONSTANT_Fieldref = 9
CONSTANT_Methodref = 10
CONSTANT_InterfaceMethodref = 11
CONSTANT_NameAndType = 12
CONSTANT_MethodHandle = 15
CONSTANT_MethodType = 16
CONSTANT_Dynamic = 17
CONSTANT_InvokeDynamic = 18
CONSTANT_Module = 19
CONSTANT_Package = 20

# 占 2 个常量池槽位的类型
_DOUBLE_SLOT = {CONSTANT_Long, CONSTANT_Double}


def _to_binary_name(name: str) -> str:
    """普通包名 com.example.Foo -> com/example/Foo；已含 / 或 : 的保持原样。"""
    name = name.strip()
    # file:...、已是二进制名、或刻意保留点号的名字，不做 . -> / 转换
    if "/" in name or ":" in name:
        return name
    return name.replace(".", "/")


def _read_u1(data: bytes, off: int) -> tuple[int, int]:
    return data[off], off + 1


def _read_u2(data: bytes, off: int) -> tuple[int, int]:
    return struct.unpack_from(">H", data, off)[0], off + 2


def _read_u4(data: bytes, off: int) -> tuple[int, int]:
    return struct.unpack_from(">I", data, off)[0], off + 4


def parse_constant_pool(data: bytes, off: int, count: int):
    """解析常量池，返回 (entries, new_offset)。

    entries[i] = (tag, payload_bytes, decoded)
      - Utf8: decoded 为 str
      - Class/String/Module/Package/MethodType: decoded 为 name_index (int)
      - 其它: decoded 为 None
    索引从 1 开始；Long/Double 后一槽为 None。
    """
    entries: list = [None] * count
    i = 1
    while i < count:
        tag, off = _read_u1(data, off)
        start = off - 1

        if tag == CONSTANT_Utf8:
            length, off = _read_u2(data, off)
            raw = data[off : off + length]
            off += length
            entries[i] = (tag, data[start:off], raw.decode("utf-8"))
        elif tag in (CONSTANT_Integer, CONSTANT_Float):
            off += 4
            entries[i] = (tag, data[start:off], None)
        elif tag in _DOUBLE_SLOT:
            off += 8
            entries[i] = (tag, data[start:off], None)
            if i + 1 < count:
                entries[i + 1] = None
            i += 2
            continue
        elif tag in (
            CONSTANT_Class,
            CONSTANT_String,
            CONSTANT_MethodType,
            CONSTANT_Module,
            CONSTANT_Package,
        ):
            idx, off = _read_u2(data, off)
            entries[i] = (tag, data[start:off], idx)
        elif tag in (
            CONSTANT_Fieldref,
            CONSTANT_Methodref,
            CONSTANT_InterfaceMethodref,
            CONSTANT_NameAndType,
            CONSTANT_Dynamic,
            CONSTANT_InvokeDynamic,
        ):
            off += 4
            entries[i] = (tag, data[start:off], None)
        elif tag == CONSTANT_MethodHandle:
            off += 3
            entries[i] = (tag, data[start:off], None)
        else:
            raise ValueError(f"未知常量池 tag={tag} @ index={i}")

        i += 1

    return entries, off


def get_this_class_name(data: bytes) -> str:
    magic, off = _read_u4(data, 0)
    if magic != 0xCAFEBABE:
        raise ValueError("不是合法的 .class 文件 (magic 不匹配)")

    _minor, off = _read_u2(data, off)
    _major, off = _read_u2(data, off)
    cp_count, off = _read_u2(data, off)
    entries, off = parse_constant_pool(data, off, cp_count)

    _access, off = _read_u2(data, off)
    this_class, off = _read_u2(data, off)

    class_entry = entries[this_class]
    if class_entry is None or class_entry[0] != CONSTANT_Class:
        raise ValueError(f"this_class=#{this_class} 不是 CONSTANT_Class")

    name_idx = class_entry[2]
    utf8 = entries[name_idx]
    if utf8 is None or utf8[0] != CONSTANT_Utf8:
        raise ValueError(f"类名 UTF8 #{name_idx} 无效")
    return utf8[2]


def rewrite_class_name(data: bytes, new_binary_name: str) -> bytes:
    """替换 this_class 指向的类名，并同步替换相关 UTF8（含 Lxxx; 描述符）。"""
    magic, off = _read_u4(data, 0)
    if magic != 0xCAFEBABE:
        raise ValueError("不是合法的 .class 文件 (magic 不匹配)")

    header = data[:8]  # magic + minor + major
    off = 8
    cp_count, off = _read_u2(data, off)
    entries, after_cp = parse_constant_pool(data, off, cp_count)

    access_off = after_cp
    _access, tmp = _read_u2(data, access_off)
    this_class, _ = _read_u2(data, tmp)

    class_entry = entries[this_class]
    if class_entry is None or class_entry[0] != CONSTANT_Class:
        raise ValueError(f"this_class=#{this_class} 不是 CONSTANT_Class")

    name_idx = class_entry[2]
    old_name = entries[name_idx][2]
    new_name = new_binary_name

    if old_name == new_name:
        return data

    old_desc = f"L{old_name};"
    new_desc = f"L{new_name};"

    def rewrite_utf8(text: str) -> str:
        if text == old_name:
            return new_name
        if text == old_desc:
            return new_desc
        # 描述符 / 签名中嵌入的 Lold;
        if old_desc in text:
            text = text.replace(old_desc, new_desc)
        return text

    out = bytearray()
    out += header
    out += struct.pack(">H", cp_count)

    i = 1
    while i < cp_count:
        entry = entries[i]
        if entry is None:
            i += 1
            continue

        tag, raw, decoded = entry
        if tag == CONSTANT_Utf8:
            new_text = rewrite_utf8(decoded)
            encoded = new_text.encode("utf-8")
            if len(encoded) > 0xFFFF:
                raise ValueError(f"UTF8 常量过长: {new_text!r}")
            out.append(CONSTANT_Utf8)
            out += struct.pack(">H", len(encoded))
            out += encoded
        else:
            out += raw

        i += 2 if tag in _DOUBLE_SLOT else 1

    out += data[after_cp:]
    return bytes(out)


def build_jar(class_bytes: bytes, entry_name: str, jar_path: Path) -> None:
    """写入 jar，entry_name 为 jar 内路径（如 Calc.class）。"""
    jar_path.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(jar_path, "w", compression=zipfile.ZIP_DEFLATED) as zf:
        # 简单 MANIFEST，兼容多数工具
        manifest = (
            b"Manifest-Version: 1.0\r\n"
            b"Created-By: classNameModefier\r\n"
            b"\r\n"
        )
        zf.writestr("META-INF/MANIFEST.MF", manifest)
        zf.writestr(entry_name, class_bytes)


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(
        description="修改 class 实际类名；默认打无扩展名 jar，可用 -c 只输出 .class"
    )
    parser.add_argument("class_file", type=Path, help="输入的 .class 文件")
    parser.add_argument(
        "new_name",
        help="新的实际类名，支持 com.example.Foo 或 com/example/Foo",
    )
    parser.add_argument(
        "-o",
        "--output-dir",
        type=Path,
        default=None,
        help="输出目录，默认与输入 class 同目录",
    )
    parser.add_argument(
        "-c",
        "--class-only",
        action="store_true",
        help="只修改实际类名并输出 .class，不打包 jar（输出文件名与输入文件名相同）",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="只打印将要做的修改，不写文件",
    )
    args = parser.parse_args(argv)

    class_path: Path = args.class_file
    if not class_path.is_file():
        print(f"[!] 文件不存在: {class_path}", file=sys.stderr)
        return 1
    if class_path.suffix.lower() != ".class":
        print(f"[!] 请输入 .class 文件: {class_path}", file=sys.stderr)
        return 1

    data = class_path.read_bytes()
    try:
        old_name = get_this_class_name(data)
    except ValueError as e:
        print(f"[!] 解析失败: {e}", file=sys.stderr)
        return 1

    new_name = _to_binary_name(args.new_name)
    # 输出文件名跟随「输入文件名」，不跟随内部类名
    # 例如输入 CalcJType1.class（内部仍是 CalcJType）-> 输出 CalcJType1.class / CalcJType1
    file_stem = class_path.stem
    entry_name = f"{file_stem}.class"
    out_dir = args.output_dir or class_path.parent
    out_dir.mkdir(parents=True, exist_ok=True)

    if args.class_only:
        out_path = out_dir / entry_name
        mode_desc = "仅 .class"
    else:
        out_path = out_dir / file_stem
        mode_desc = "无扩展名 jar"

    print(f"[*] 输入文件 : {class_path}")
    print(f"[*] 原实际类名: {old_name}")
    print(f"[*] 新实际类名: {new_name}")
    print(f"[*] 输出模式  : {mode_desc}")
    if not args.class_only:
        print(f"[*] jar 内路径: {entry_name}")
    print(f"[*] 输出文件  : {out_path}")

    if args.dry_run:
        print("[*] dry-run，未写入文件")
        return 0

    try:
        new_bytes = rewrite_class_name(data, new_name)
        verify = get_this_class_name(new_bytes)
    except ValueError as e:
        print(f"[!] 改写失败: {e}", file=sys.stderr)
        return 1

    if verify != new_name:
        print(f"[!] 校验失败: this_class={verify}, 期望={new_name}", file=sys.stderr)
        return 1

    if args.class_only:
        out_path.write_bytes(new_bytes)
        print(f"[+] 完成: {out_path} (仅改类名, {len(new_bytes)} bytes)")
    else:
        build_jar(new_bytes, entry_name, out_path)
        print(f"[+] 完成: {out_path} (无扩展名 jar, class {len(new_bytes)} bytes)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
