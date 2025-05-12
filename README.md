# MyJavaSecStudy
记录一下我的Java安全学习历程。目前入门和进阶内容放一起了，以后有空的话会整理得更清楚一些。

- [反序列化](#反序列化)
- [JNDI](#JNDI)
- [JDBC](#JDBC)
- [Fastjson&Jackson&SnakeYaml](#Fastjson&Jackson&SnakeYaml)
- [shiro](#shiro)
- [内存马&回显](#内存马&回显)
- [RASP](#RASP)
- [各种链子&姿势](#各种链子&姿势)
- [SpringBoot](#SpringBoot)
- [tabby](#tabby)
- [CodeQL](#CodeQL)
- [学习路线整合](#学习路线整合)





# 反序列化  <a id="反序列化"></a>

## hessian

[Java安全学习——Hessian反序列化漏洞 - 枫のBlog](https://goodapple.top/archives/1193)

[Hessian 反序列化知一二 | 素十八](https://su18.org/post/hessian/)









# JNDI <a id="JNDI"></a>

[JNDI · 攻击Java Web应用-[Java Web安全\]](https://www.javasec.org/javase/JNDI/#jndi) 里面引用了一篇命名和目录服务基本概念的文章，记得看，对后面理解RMI和LDAP很有帮助，很多东西都会在调试里遇到

[RMI协议分析 - lvyyevd's 安全博客](http://www.lvyyevd.cn/archives/rmi-xie-yi-fen-xi)

[Java RMI 攻击由浅入深 | 素十八](https://su18.org/post/rmi-attack/)

[京麟CTF 2024 ezldap 分析-先知社区](https://xz.aliyun.com/news/14103)  com.sun.jndi.ldap.object.trustSerialData false的绕过

[奇安信攻防社区-【2024补天白帽黑客大会】JNDI新攻击面探索](https://forum.butian.net/share/3857)





## 基于BeanFactory

[探索高版本 JDK 下 JNDI 漏洞的利用方法 - 跳跳糖](https://tttang.com/archive/1405/#toc_0x01-beanfactory)  除了最基本的EL表达式执行，还有Snakeyaml，XStream等方式（高版本tomcat的forceString被禁）



## 其他Factory绕过

[SolarWinds Security Event Manager AMF 反序列化 RCE (CVE-2024-0692) - X1r0z Blog](https://exp10it.io/2024/03/solarwinds-security-event-manager-amf-deserialization-rce-cve-2024-0692/#hikaricp-jndi-注入)	Hikari跟Druid一样，都可以实现JNDI+JDBC，都是可以执行初始化sql语句

[高版本JNDI注入-高版本Tomcat利用方案-先知社区](https://xz.aliyun.com/news/16156)

[探索高版本 JDK 下 JNDI 漏洞的利用方法 - 跳跳糖](https://tttang.com/archive/1405/#toc_snakeyaml)	jdk17的题特别喜欢考JNDI+JDBC

[JNDI jdk高版本绕过—— Druid-先知社区](https://xz.aliyun.com/news/10104)





# JDBC <a id="JDBC"></a>

[JDBC Connection URL 攻击](https://paper.seebug.org/1832/)

[JDBC-Attack 攻击利用汇总-先知社区](https://xz.aliyun.com/news/13371)

[JDBC-Attack 利用汇总 - Boogiepop Doesn't Laugh](https://boogipop.com/2023/10/01/JDBC-Attack%20利用汇总/#前言)

[Jdbc碎碎念三：内存数据库 | m0d9's blog](https://m0d9.me/2021/04/26/Jdbc碎碎念三：内存数据库/)	h2sql，hsql，sqlite，derby

[yulate/jdbc-tricks: 《深入JDBC安全：特殊URL构造与不出网反序列化利用技术揭秘》对应研究总结项目 "Deep Dive into JDBC Security: Special URL Construction and Non-Networked Deserialization Exploitation Techniques Revealed" - Research Summary Project](https://github.com/yulate/jdbc-tricks)





## mysql

[从JDBC MySQL不出网攻击到spring临时文件利用-先知社区](https://xz.aliyun.com/news/17830)  这个打法比较新，其中的临时文件上传适用性广







## h2sql

[NCTF2024 Web方向题解-CSDN博客](https://blog.csdn.net/Err0r233/article/details/146484415) h2Revenge

[SolarWinds Security Event Manager AMF 反序列化 RCE (CVE-2024-0692) - X1r0z Blog](https://exp10it.io/2024/03/solarwinds-security-event-manager-amf-deserialization-rce-cve-2024-0692/#hikaricp-jndi-注入)	h2可以结合其他依赖写文件



## sqlite

[JavaSec/9.JDBC Attack/SQLite/index.md at main · Y4tacker/JavaSec](https://github.com/Y4tacker/JavaSec/blob/main/9.JDBC%20Attack/SQLite/index.md)

[CISCN2024 writeup（web部分）](https://jaspersec.top/posts/3286688009.html#ezjava)	ezjava

[从一道题看利用sqlite打jdbc达到RCE-先知社区](https://xz.aliyun.com/news/14234)





## Derby

[derby数据库如何实现RCE - lvyyevd's 安全博客](http://www.lvyyevd.cn/archives/derby-shu-ju-ku-ru-he-shi-xian-rce)

[N1CTF Junior 2024 Web Official Writeup - X1r0z Blog](https://exp10it.io/2024/02/n1ctf-junior-2024-web-official-writeup/#derby)

[因为项目中遇到Nacos挺多的...-知识星球](https://wx.zsxq.com/group/2212251881/topic/1524448452142582)







# Fastjson&Jackson&SnakeYaml <a id="Fastjson&Jackson&SnakeYaml"></a>

[Java利用无外网（上）：从HertzBeat聊聊SnakeYAML反序列化 | 离别歌](https://www.leavesongs.com/PENETRATION/jdbc-injection-with-hertzbeat-cve-2024-42323.html)







# shiro <a id="shiro"></a>

[Java反序列化Shiro篇01-Shiro550流程分析 | Drunkbaby's Blog](https://drun1baby.top/2022/07/10/Java反序列化Shiro篇01-Shiro550流程分析/)

[Shiro RememberMe 漏洞检测的探索之路 - CT Stack 安全社区](https://stack.chaitin.com/techblog/detail/39)

[一种另类的 shiro 检测方式](https://mp.weixin.qq.com/s/do88_4Td1CSeKLmFqhGCuQ)







# 内存马&回显技术 <a id="内存马&回显"></a>

[奇安信攻防社区-Solon框架注入内存马](https://forum.butian.net/share/3700)  里面提到的Java Object Searcher值得学习

[c0ny1/java-object-searcher: java内存对象搜索辅助工具](https://github.com/c0ny1/java-object-searcher)

[Shiro RememberMe 漏洞检测的探索之路 - CT Stack 安全社区](https://stack.chaitin.com/techblog/detail/39)  这里也有用到Java-object-searcher 构造tomcat回显

[半自动化挖掘request实现多种中间件回显 | 回忆飘如雪](https://gv7.me/articles/2020/semi-automatic-mining-request-implements-multiple-middleware-echo/)  java-object-searcher工具的作者

[内存对象搜索原理剖析-先知社区](https://xz.aliyun.com/news/11303)  java-object-searcher原理



# RASP <a id="RASP"></a>

[JNI攻击 · 攻击Java Web应用-[Java Web安全\]](https://www.javasec.org/java-vuls/JNI.html)





# 各种链子&姿势 <a id="各种链子&姿势"></a>

[分析尝试利用tabby挖掘-SpringAOP链 - Potat0w0](https://blog.potatowo.top/2025/03/31/从复现到尝试用tabby挖掘-SpringAOP链/)

[realworldctf old system复盘（jdk1.4 getter jndi gadget）-先知社区](https://xz.aliyun.com/news/8630)	LdapAttribute链

[利用特殊反序列化组件攻击原生反序列化入口-先知社区](https://xz.aliyun.com/news/12356)	fj，jk结合ROME，LdapAttribute等

[Java利用无外网（下）：ClassPathXmlApplicationContext的不出网利用 | 离别歌](https://www.leavesongs.com/PENETRATION/springboot-xml-beans-exploit-without-network.html)	第一次知道tomcat和php有自动保存临时文件的机制。里面非预期的脏字符jar包构造没见过



# SpringBoot <a id="SpringBoot"></a>

[LandGrey/SpringBootVulExploit: SpringBoot 相关漏洞学习资料，利用方法和技巧合集，黑盒安全评估 check list](https://github.com/LandGrey/SpringBootVulExploit)

## heapdump分析

主要是jdk自带的VisualVM看jdk版本，heapdump_tools分析依赖和密码

[Springboot信息泄露以及heapdump的利用_heapdump信息泄露-CSDN博客](https://blog.csdn.net/weixin_44309905/article/details/127279561)

[京麟CTF 2024 ezldap 分析-先知社区](https://xz.aliyun.com/news/14103?time__1311=eqUxuiDt5WqYqY5DsD7mPD%3DIZK7q9hGBbD&u_atoken=b94f9c93564049e1d2601ebb22a1098b&u_asig=0a472f9217433333617862864e004b)

## 文件缓存机制

[从JDBC MySQL不出网攻击到spring临时文件利用-先知社区](https://xz.aliyun.com/news/17830)













# tabby <a id="tabby"></a>

[自动化代码审计实践 | mayylu's blog](https://mayylu.github.io/2024/08/02/java/自动化代码审计实践/)









# CodeQL <a id="CodeQL"></a>

[Codeql全新版本从0到1-先知社区](https://xz.aliyun.com/news/16918)  25年的文章，比较新

[利用Github Actions生成CodeQL数据库 -- 以AliyunCTF2024 Chain17的反序列化链挖掘为例 - KingBridge - 博客园](https://www.cnblogs.com/kingbridge/articles/18100619)

[aliyun ctf chain17 回顾(超详细解读)-先知社区](https://xz.aliyun.com/news/16179)

[CodeQL从入门到入土 - FreeBuf网络安全行业门户](https://www.freebuf.com/articles/web/391242.html)

[原创 Paper | CodeQL 入门和基本使用 | CTF导航](https://www.ctfiot.com/215157.html)

[细谈使用CodeQL进行反序列化链的挖掘过程-SecIN](https://www.sec-in.com/article/2043)	相关文章关键词：codeql java反序列化

[利用codeql查找hsqldb2.7.3最新反序列化链-先知社区](https://xz.aliyun.com/news/14260)  里面的参考文章也值得看

[safe6Sec/CodeqlNote: Codeql学习笔记](https://github.com/safe6Sec/CodeqlNote?tab=readme-ov-file)

[自动化代码审计实践 | mayylu's blog](https://mayylu.github.io/2024/08/02/java/自动化代码审计实践/)





<span id="jump">跳转到的地方</span>

# 学习路线整合 <a id="学习路线整合"></a>

[前言 · 攻击Java Web应用-[Java Web安全\]](https://www.javasec.org/)

[Y4tacker/JavaSec: a rep for documenting my study, may be from 0 to 0.1](https://github.com/Y4tacker/JavaSec?tab=readme-ov-file)

[Drun1baby/JavaSecurityLearning: 记录一下 Java 安全学习历程，也算是半条学习路线了](https://github.com/Drun1baby/JavaSecurityLearning)

[phith0n/JavaThings: Share Things Related to Java - Java安全漫谈笔记相关内容](https://github.com/phith0n/JavaThings?tab=readme-ov-file)


