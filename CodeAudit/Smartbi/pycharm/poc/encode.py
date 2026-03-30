from urllib.parse import quote


class ReplaceCoder:
    def __init__(self):
        self.code_array = [
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 80, 0, 0, 0, 47, 0, 110, 65, 69, 115, 43, 0, 102, 113, 37, 55, 49, 117, 78, 75, 74, 77, 57,
            39, 109, 123, 0, 0, 0, 0, 0, 0, 79, 86, 116, 84, 97, 120, 72, 114, 99, 118, 108, 56, 70, 51, 111,
            76, 89, 106, 87, 42, 122, 90, 33, 66, 41, 85, 93, 0, 91, 0, 121, 0, 40, 126, 105, 104, 112, 95,
            45, 73, 82, 46, 71, 83, 100, 54, 119, 53, 48, 52, 68, 107, 81, 103, 98, 67, 50, 88, 58, 0, 0, 101, 0
        ]

        self.encode_map = {}
        self.decode_map = {}

        self.init_maps()

    def init_maps(self):
        for i, c in enumerate(self.code_array):
            if c != 0:
                ic = chr(i)
                ec = chr(c)
                self.encode_map[ic] = ec
                self.decode_map[ec] = ic

        # 特殊处理
        self.decode_map['/'] = '/'
        self.decode_map['%'] = '%'

    def encode(self, data: str) -> str:
        result = []
        for ch in data:
            result.append(self.encode_map.get(ch, ch))
        return ''.join(result)

    def decode(self, data: str) -> str:
        result = []
        for ch in data:
            result.append(self.decode_map.get(ch, ch))
        return ''.join(result)


if __name__ == "__main__":
    coder = ReplaceCoder()
    className = "ConfigClientService"
    methodName = "getConfigValue"
    params = "[\"START_MODULE\"]"
    text = className + " " + methodName + " " + params
    urlEnc = quote(text)

    enc = coder.encode(urlEnc)
    dec = coder.decode(urlEnc)

    print("原文:", urlEnc)
    print("加密:", enc)
    print("解密:", dec)