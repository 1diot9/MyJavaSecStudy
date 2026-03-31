#!/usr/bin/env python3
"""Unified Smartbi RMI request/response codec helper."""

from __future__ import annotations

from typing import Dict, Tuple
from urllib.parse import quote, quote_plus, unquote, unquote_plus


SF1_CODE_ARRAY = [
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 80, 0, 0, 0, 47, 0, 110,
    65, 69, 115, 43, 0, 102, 113, 37, 55, 49,
    117, 78, 75, 74, 77, 57, 39, 109, 123, 0,
    0, 0, 0, 0, 0, 79, 86, 116, 84, 97,
    120, 72, 114, 99, 118, 108, 56, 70, 51, 111,
    76, 89, 106, 87, 42, 122, 90, 33, 66, 41,
    85, 93, 0, 91, 0, 121, 0, 40, 126, 105,
    104, 112, 95, 45, 73, 82, 46, 71, 83, 100,
    54, 119, 53, 48, 52, 68, 107, 81, 103, 98,
    67, 50, 88, 58, 0, 0, 101, 0,
]

RESPONSE_JAVA_DECODE_ARRAY = [
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    32, 87, 0, 0, 0, 47, 0, 56, 97, 89, 84, 43, 0, 103, 106, 37,
    113, 49, 121, 78, 114, 112, 110, 48, 76, 55, 123, 0, 0, 0, 0, 0,
    0, 40, 88, 120, 115, 41, 77, 107, 71, 104, 53, 52, 80, 54, 51, 65,
    33, 117, 105, 108, 68, 90, 66, 83, 122, 81, 86, 93, 0, 91, 0, 102,
    0, 69, 119, 73, 109, 126, 45, 118, 100, 99, 82, 116, 75, 57, 39, 79,
    101, 46, 72, 42, 67, 50, 74, 111, 70, 95, 85, 58, 0, 0, 98, 0,
]

RESPONSE_JAVA_ENCODE_ARRAY = [
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 80, 0, 0, 0, 47, 0, 110, 65, 69, 115, 43, 0, 102, 113, 37,
    55, 49, 117, 78, 75, 74, 77, 57, 39, 109, 123, 0, 0, 0, 0, 0,
    0, 79, 86, 116, 84, 97, 120, 72, 114, 99, 118, 108, 56, 70, 51, 111,
    76, 89, 106, 87, 42, 122, 90, 33, 66, 41, 85, 93, 0, 91, 0, 121,
    0, 40, 126, 105, 104, 112, 95, 45, 73, 82, 46, 71, 83, 100, 54, 119,
    53, 48, 52, 68, 107, 81, 103, 98, 67, 50, 88, 58, 0, 0, 101, 0,
]

HEX_SPECIAL_CHARS = [
    33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
    46, 47, 58, 59, 60, 61, 62, 63, 64, 91, 92, 93, 94,
    95, 96, 120, 123, 124, 125, 126,
]

URI_SAFE = "-_.!~*'()"
HEX_PREFIX = "0x"
HEX_SUFFIX = "x9"


def _build_map(code_array) -> Dict[str, str]:
    mapping: Dict[str, str] = {}
    for idx, value in enumerate(code_array):
        if value:
            mapping[chr(idx)] = chr(value)
    return mapping


SF1_REQUEST_ENCODE_MAP = _build_map(SF1_CODE_ARRAY)
SF1_REQUEST_DECODE_MAP = {value: key for key, value in SF1_REQUEST_ENCODE_MAP.items()}
SF1_RESPONSE_ENCODE_MAP = _build_map(RESPONSE_JAVA_DECODE_ARRAY)
SF1_RESPONSE_DECODE_MAP = _build_map(RESPONSE_JAVA_ENCODE_ARRAY)
SF2_HEX_ENCODE_MAP = {chr(code): format(code, "x") for code in HEX_SPECIAL_CHARS}
SF2_HEX_DECODE_MAP = {value: key for key, value in SF2_HEX_ENCODE_MAP.items()}


def _translate(text: str, mapping: Dict[str, str]) -> str:
    return "".join(mapping.get(ch, ch) for ch in text)


def encode_uri_component(text: str) -> str:
    return quote(text, safe=URI_SAFE)


def decode_uri_component(text: str) -> str:
    return unquote(text)


def _response_encode_sf1(plain: str) -> str:
    out = []
    for ch in plain:
        if ch not in ("%", "/"):
            out.append(SF1_RESPONSE_ENCODE_MAP.get(ch, ch))
        else:
            out.append(ch)
    return "".join(out)


def _response_decode_sf1(encoded: str) -> str:
    out = []
    for ch in encoded:
        if ch != "%":
            out.append(SF1_RESPONSE_DECODE_MAP.get(ch, ch))
        else:
            out.append(ch)
    return "".join(out)


def _request_encode_sf1(plain: str) -> str:
    return _translate(plain, SF1_REQUEST_ENCODE_MAP)


def _request_decode_sf1(encoded: str) -> str:
    return _translate(encoded, SF1_REQUEST_DECODE_MAP)


def _hex_escape(text: str) -> str:
    out = []
    for ch in text:
        hex_value = SF2_HEX_ENCODE_MAP.get(ch)
        if hex_value is None:
            out.append(ch)
        else:
            out.append(f"{HEX_PREFIX}{hex_value}{HEX_SUFFIX}")
    return "".join(out)


def _hex_unescape_request(text: str) -> str:
    out = []
    pos = 0
    while True:
        begin = text.find(HEX_PREFIX, pos)
        if begin < 0:
            out.append(text[pos:])
            break
        out.append(text[pos:begin])
        end = text.find(HEX_SUFFIX, begin)
        if end < 0:
            out.append(text[begin:])
            break
        hex_key = text[begin + len(HEX_PREFIX):end]
        out.append(SF2_HEX_DECODE_MAP.get(hex_key, text[begin:end + len(HEX_SUFFIX)]))
        pos = end + len(HEX_SUFFIX)
    return "".join(out)


def _hex_unescape_response(text: str) -> str:
    out = []
    pos = 0
    while True:
        begin = text.find(HEX_PREFIX, pos)
        if begin < 0:
            out.append(text[pos:])
            break
        out.append(text[pos:begin])
        end = text.find(HEX_SUFFIX, begin)
        if end < 0:
            out.append(text[begin:])
            break
        hex_key = text[begin + len(HEX_PREFIX):end]
        out.append(SF2_HEX_DECODE_MAP.get(hex_key, hex_key))
        pos = end + len(HEX_SUFFIX)
    return "".join(out)


class Algorithm:
    def __init__(self, name: str) -> None:
        self.name = name

    def encode_request(self, plain: str) -> str:
        if self.name == "SF1":
            return _request_encode_sf1(plain)
        if self.name == "SF2":
            return _hex_escape(_request_encode_sf1(plain))
        if self.name == "SF3":
            return plain
        raise ValueError(f"unsupported algorithm: {self.name}")

    def decode_request(self, encoded: str) -> str:
        if self.name == "SF1":
            return _request_decode_sf1(encoded)
        if self.name == "SF2":
            return _request_decode_sf1(_hex_unescape_request(encoded))
        if self.name == "SF3":
            return encoded
        raise ValueError(f"unsupported algorithm: {self.name}")

    def encode_response(self, plain: str) -> str:
        if self.name == "SF1":
            return _response_encode_sf1(plain)
        if self.name == "SF2":
            return _hex_escape(_response_encode_sf1(plain))
        if self.name == "SF3":
            return plain
        raise ValueError(f"unsupported algorithm: {self.name}")

    def decode_response(self, encoded: str) -> str:
        if self.name == "SF1":
            return _response_decode_sf1(encoded)
        if self.name == "SF2":
            return _response_decode_sf1(_hex_unescape_response(encoded))
        if self.name == "SF3":
            return encoded
        raise ValueError(f"unsupported algorithm: {self.name}")


ALGORITHMS = {
    "SF1": Algorithm("SF1"),
    "SF2": Algorithm("SF2"),
    "SF3": Algorithm("SF3"),
}


class SmartbiRMICodec:
    def __init__(self, algorithm: str = "SF1") -> None:
        algorithm_name = algorithm.upper()
        if algorithm_name not in ALGORITHMS:
            raise ValueError(f"unsupported algorithm: {algorithm}")
        self.algorithm = ALGORITHMS[algorithm_name]

    @staticmethod
    def build_plain_request_text(class_name: str, method_name: str, params_text: str) -> str:
        return f"{class_name} {method_name} {encode_uri_component(params_text)}"

    @staticmethod
    def split_plain_request_text(plain: str) -> Tuple[str, str, str]:
        parts = plain.split(" ", 2)
        if len(parts) != 3:
            raise ValueError(f"invalid plain RMI request text: {plain!r}")
        class_name, method_name, encoded_params = parts
        return class_name, method_name, decode_uri_component(encoded_params)

    @staticmethod
    def normalize_captured_value(text: str) -> str:
        return unquote_plus(text[7:] if text.startswith("encode=") else text)

    def encode_request_value(self, class_name: str, method_name: str, params_text: str) -> str:
        plain = self.build_plain_request_text(class_name, method_name, params_text)
        encoded_value = self.algorithm.encode_request(plain)
        return quote_plus(encoded_value, safe="()/")

    def encode_request_body(self, class_name: str, method_name: str, params_text: str) -> str:
        encoded_value = self.encode_request_value(class_name, method_name, params_text)
        return "encode=" + encoded_value

    def decode_request_value(self, encoded_text: str) -> Tuple[str, str, str]:
        normalized = self.normalize_captured_value(encoded_text)
        plain = self.algorithm.decode_request(normalized)
        return self.split_plain_request_text(plain)

    def encode_response_text(self, plain_text: str) -> str:
        return self.algorithm.encode_response(plain_text)

    def decode_response_text(self, encoded_text: str) -> str:
        normalized = self.normalize_captured_value(encoded_text)
        return self.algorithm.decode_response(normalized)


# Edit the values in this section and run this file directly.
ALGORITHM = "SF1"

REQUEST_CLASS_NAME = "CatalogService"
REQUEST_METHOD_NAME = "getDBVersion"
REQUEST_PARAMS_TEXT = "[]"
REQUEST_ENCODED_VALUE = "t(k(Sw-Wp4gRip+-pkTVZp4DRw6+/JV/JT"

RESPONSE_PLAIN_TEXT = "{\"retCode\":0,\"result\":{\"smartbi.eagle.question\":\"0.0.5\",\"smartbi.macro\":\"0.0.46\",\"smartbi.uploadimg\":\"0.0.2\",\"smartbix.dataagent.module\":\"0.0.3\",\"smartbix.smartbi\":\"0.0.104\",\"smartbi.extension\":\"0.0.1\",\"smartbi.module.socialcontact\":\"0.0.6\",\"smartbi.ext.function\":\"0.0.3\",\"smartbi.repository\":\"0.0.73\",\"smartbi.insight\":\"0.0.24\",\"smartbi.decisionpanel\":\"0.0.65\",\"smartbi.daq\":\"0.0.20\",\"smartbi.message\":\"0.0.10\",\"smartbi.module.export\":\"0.0.5\",\"smartbi.spreadsheetreport\":\"0.0.21\",\"smartbi.freequery.watermark\":\"0.0.1\",\"smartbi.industrycase\":\"0.0.1\",\"smartbi.hotreport\":\"0.0.2\",\"smartbi.aiext\":\"0.0.13\",\"smartbi.chart\":\"0.0.54\",\"smartbi.catalogtree\":\"0.0.31\",\"smartbi.scheduletask\":\"0.0.31\",\"smartbi.module.socialcontactshare\":\"0.0.12\",\"smartbi.ipad\":\"0.0.16\",\"smartbi.module.exportrule\":\"0.0.3\",\"smartbi.olap\":\"0.0.53\",\"smartbi.timeconsuming\":\"0.0.4\",\"smartbi.websheet\":\"0.0.1\",\"smartbi.config\":\"0.0.1\",\"smartbi.datapackage\":\"0.0.4\",\"smartbi.mobileportal\":\"0.0.6\",\"smartbi.composite\":\"0.0.43\",\"smartbi.usermanager\":\"0.0.81\",\"smartbi.exptemplate\":\"0.0.4\",\"smartbi.metadata\":\"0.0.12\",\"smartbi.eagle.appstore\":\"0.0.10\",\"smartbi.eagle.navigation\":\"0.0.2\",\"smartbi.alert\":\"0.0.1\",\"smartbi.oltp\":\"2.2.134\",\"smartbi.param\":\"0.0.2\",\"smartbi.workflow\":\"0.0.15\",\"smartbi.officereport\":\"0.0.3\",\"smartbi.auditing\":\"0.0.8\",\"smartbi.combinedquery\":\"0.0.19\",\"smartbi.offline\":\"0.0.2\",\"smartbi.beforestartup\":\"0.0.1\"},\"duration\":1}"
RESPONSE_ENCODED_TEXT = """
:"H~CxOm~"{q,"H~*2KC"{:"*9EHCwcj9EIHO"{"qjqjr0","*9EHCwcj2eKOEmc9v"{"qjqjy","*9EHCwcFj*9EHCwc"{"qjqj11n","*9EHCwcj~FC~'*cO'"{"qjqj1","*9EHCwcj~FCjIO99O'"{"qjqjp","*9EHCwcj~FCj-2'ICcO'"{"qjqjN","*9EHCwcjH~eO*cCOH_"{"qjqj00","*9EHCwcjm~Ic*cO'eE'~K"{"qjqjnp","*9EHCwcj9~**Ev~"{"qjqj1q","*9EHCwcj9Om2K~j~FeOHC"{"qjqjp","*9EHCwcj*eH~Em*d~~CH~eOHC"{"qjqjy1","*9EHCwcjEcIdEC"{"qjqjNL","*9EHCwcjdOCH~eOHC"{"qjqjy","*9EHCwcj9Om2K~j*OIcEKIO'CEIC*dEH~"{"qjqj1N","*9EHCwcjOKEe"{"qjqjpr","*9EHCwcjCc9~IO'*29c'v"{"qjqjr","*9EHCwcjIO'-cv"{"qjqj1","*9EHCwcjIO9eO*cC~"{"qjqjrN","*9EHCwcj~FeC~9eKEC~"{"qjqjr","*9EHCwcj~EvK~jEee*COH~"{"qjqj1q","*9EHCwcj9Om2K~j*dOHCI2COeCcO'"{"qjqjy","*9EHCwcjEK~HC"{"qjqj1","*9EHCwcjeEHE9"{"qjqjy","*9EHCwcjO--cI~H~eOHC"{"qjqjN","*9EHCwcjw~-OH~*CEHC2e"{"qjqj1","*9EHCwcj~EvK~j.2~*CcO'"{"qjqjp","*9EHCwcFjmECEEv~'Cj9Om2K~"{"qjqj0","*9EHCwcFjEcIdECjt'OoK~mv~j9Om2K~"{"qjqjn","*9EHCwcj9Om2K~j*OIcEKIO'CEIC"{"qjqjn","*9EHCwcjc'*cvdC"{"qjqjyr","*9EHCwcjmE."{"qjqjyq","*9EHCwcj-H~~.2~H_joEC~H9EHt"{"qjqj1","*9EHCwcjc'm2*CH_IE*~"{"qjqj1","*9EHCwcjEc~FC"{"qjqj1L","*9EHCwcjIdEHC"{"qjqjpr","*9EHCwcjIECEKOvCH~~"{"qjqjNr","*9EHCwcj*Id~m2K~CE*t"{"qjqjNy","*9EHCwcjH~*dE'mOJ~H"{"qjqj1","*9EHCwcjceEm"{"qjqj1n","*9EHCwcj9Om2K~j~FeOHCH2K~"{"qjqjr","*9EHCwcjo~w*d~~C"{"qjqj1","*9EHCwcjmECEeEItEv~"{"qjqjr","*9EHCwcj9OwcK~eOHCEK"{"qjqjn","*9EHCwcj2*~H9E'Ev~H"{"qjqjLn","*9EHCwcj9~CEmECE"{"qjqj1N","*9EHCwcj~EvK~j'EJcvECcO'"{"qjqjy","*9EHCwcjOKCe"{"yjyj1Nn","*9EHCwcjoOHt-KOo"{"qjqj1n","*9EHCwcjE2mcCc'v"{"qjqjL","*9EHCwcjIO9wc'~m.2~H_"{"qjqj17","*9EHCwcjO--Kc'~"{"qjqjy"},"m2HECcO'"{N}
"""


def demo_request(codec: SmartbiRMICodec) -> None:
    request_value = codec.encode_request_value(REQUEST_CLASS_NAME, REQUEST_METHOD_NAME, REQUEST_PARAMS_TEXT)
    request_body = codec.encode_request_body(REQUEST_CLASS_NAME, REQUEST_METHOD_NAME, REQUEST_PARAMS_TEXT)
    decoded_class, decoded_method, decoded_params = codec.decode_request_value(REQUEST_ENCODED_VALUE)
    print("=== Request Encode ===")
    print(request_value)
    print(request_body)
    print("=== Request Decode ===")
    print(decoded_class)
    print(decoded_method)
    print(decoded_params)


def demo_response(codec: SmartbiRMICodec) -> None:
    response_encoded = codec.encode_response_text(RESPONSE_PLAIN_TEXT)
    response_decoded = codec.decode_response_text(RESPONSE_ENCODED_TEXT)
    print("=== Response Encode ===")
    print(response_encoded)
    print("=== Response Decode ===")
    print(response_decoded)


if __name__ == "__main__":
    codec = SmartbiRMICodec(ALGORITHM)
    demo_request(codec)
    demo_response(codec)
