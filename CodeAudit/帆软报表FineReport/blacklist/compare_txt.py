#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
比较两个txt文件的行差异
找出增加的行和减少的行
"""

import argparse
from pathlib import Path


def compare_files(file1: str, file2: str) -> tuple[set, set]:
    """
    比较两个文件的行差异

    Args:
        file1: 原始文件路径（基准文件）
        file2: 新文件路径

    Returns:
        tuple: (增加的行集合, 减少的行集合)
    """
    # 读取文件内容，去除每行首尾空白
    with open(file1, 'r', encoding='utf-8') as f:
        lines1 = {line.strip() for line in f if line.strip()}

    with open(file2, 'r', encoding='utf-8') as f:
        lines2 = {line.strip() for line in f if line.strip()}

    # 计算差异
    added = lines2 - lines1    # 在新文件中增加的行
    removed = lines1 - lines2  # 在新文件中减少的行

    return added, removed


def format_line(line: str, keyword: str | None) -> str:
    """
    格式化输出行，如果包含关键词则用*标注

    Args:
        line: 要输出的行内容
        keyword: 关键词（可选）

    Returns:
        格式化后的行
    """
    if keyword and keyword in line:
        return f"  * {line}  <--"
    return f"    {line}"


def main():
    parser = argparse.ArgumentParser(
        description='比较两个txt文件的行差异',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
示例:
  python compare_txt.py old.txt new.txt
  python compare_txt.py old.txt new.txt "Servlet"
  python compare_txt.py old.txt new.txt "Servlet" --output result.txt
        '''
    )
    parser.add_argument('file1', help='原始文件路径（基准文件）')
    parser.add_argument('file2', help='新文件路径')
    parser.add_argument('keyword', nargs='?', default=None, help='关键词，匹配的行会用*标注（可选）')
    parser.add_argument('--output', '-o', help='输出结果到指定文件（可选）')
    parser.add_argument('--sort', '-s', action='store_true', help='按字母顺序排序输出')

    args = parser.parse_args()

    # 检查文件是否存在
    if not Path(args.file1).exists():
        print(f"错误: 文件 '{args.file1}' 不存在")
        return
    if not Path(args.file2).exists():
        print(f"错误: 文件 '{args.file2}' 不存在")
        return

    # 比较文件
    added, removed = compare_files(args.file1, args.file2)

    # 构建输出内容
    output_lines = []
    output_lines.append("=" * 60)
    output_lines.append(f"基准文件: {args.file1}")
    output_lines.append(f"比较文件: {args.file2}")
    if args.keyword:
        output_lines.append(f"关键词: {args.keyword}")
    output_lines.append("=" * 60)

    # 输出减少的行
    output_lines.append(f"\n【减少的行】({len(removed)} 个)")
    output_lines.append("-" * 40)
    if removed:
        sorted_removed = sorted(removed) if args.sort else removed
        for line in sorted_removed:
            output_lines.append(format_line(line, args.keyword))
    else:
        output_lines.append("  (无)")

    # 输出增加的行
    output_lines.append(f"\n【增加的行】({len(added)} 个)")
    output_lines.append("-" * 40)
    if added:
        sorted_added = sorted(added) if args.sort else added
        for line in sorted_added:
            output_lines.append(format_line(line, args.keyword))
    else:
        output_lines.append("  (无)")

    # 统计信息
    # 读取文件获取行数
    with open(args.file1, 'r', encoding='utf-8') as f:
        count1 = len([l for l in f if l.strip()])
    with open(args.file2, 'r', encoding='utf-8') as f:
        count2 = len([l for l in f if l.strip()])

    output_lines.append("\n" + "=" * 60)
    output_lines.append("统计信息:")
    output_lines.append(f"  基准文件行数: {count1}")
    output_lines.append(f"  比较文件行数: {count2}")
    output_lines.append(f"  减少行数: {len(removed)}")
    output_lines.append(f"  增加行数: {len(added)}")
    output_lines.append("=" * 60)

    # 输出结果
    result = '\n'.join(output_lines)
    print(result)

    # 保存到文件
    if args.output:
        with open(args.output, 'w', encoding='utf-8') as f:
            f.write(result)
        print(f"\n结果已保存到: {args.output}")


if __name__ == '__main__':
    main()