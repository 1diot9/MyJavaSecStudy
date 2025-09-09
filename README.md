# MyJavaSecStudy
记录一下我的Java安全学习历程。会不定时更新索引和代码仓库。

<br>

- [Java基础](#Java基础)
- [Java安全基础](#Java安全基础)
- [反序列化](#反序列化)
  - CC链
  - hessian
  - 其他链子&姿势
- [JNDI](#JNDI)
  - 基础内容
  - 高版本JDK绕过
    - 基于BeanFactory
    - 其他Factory绕过(主要结合JDBC打)

- [JDBC](#JDBC)

- [shiro](#shiro)

  - shiro反序列化
  - shiro越权

- [Fastjson&Jackson&SnakeYaml](#Fastjson&Jackson&SnakeYaml)

- [内存马&回显技术](#内存马&回显技术)

- [RASP](#RASP)

- [SpringBoot](#SpringBoot)

- [工具开发/二开](#devTools)

- [代码审计](#CodeAudit)

  - 若依

  - WebGoat
  - 泛微Ecology9
  - 用友U8Cloud

- [代码审计辅助工具](#代码审计辅助工具)

  - jar-analyzer
  - tabby
  - CodeQL

- [学习路线整合](#学习路线整合)

- [工具推荐](#工具推荐)



<br>

# Java基础<a id="Java基础"></a>

主要目标是学会Java基础语法就行，不用一下子学太深，之后遇到不会的再回来查就行

[柏码知识库 | JavaSE 笔记（一）走进Java语言](https://www.itbaima.cn/zh-CN/document/8egfulw98v3h680j) 这个比较推荐，看章节目录的(一)~(六)就行，后面SpringMVC，SpringBoot也可以看这个网站

[简介 - Java教程 - 廖雪峰的官方网站](https://liaoxuefeng.com/books/java/introduction/index.html)

[JavaGuide（Java学习&面试指南） | JavaGuide](https://javaguide.cn/home.html)

[【狂神说Java】Java零基础学习视频通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV12J41137hu/?spm_id_from=333.337.search-card.all.click) b站看其他的也行，比如黑马，找自己适合的就行

<br>

# Java安全基础 <a id="Java安全基础"></a>

主要是学习反射，类加载，动态代理，各种命令执行方式等概念，为后面的学习打下基础。

一开始看不懂很正常，能有个印象就行，后面在具体问题中发现自己不会时再回来看；多问AI

<br>

## 基础内容

想快一点的话，可以只看这部分文章。选取了两位师傅的文章，可以相互补充。

[2. 读文件](https://www.yuque.com/pmiaowu/gpy1q8/in10on)  看里面的IO操作，反射，代理模式(可以先略看)，命令执行方式，Unsafe，类加载器和序列化与反序列化就行

[Java反序列化基础篇-02-Java反射与URLDNS链分析 | Drunkbaby's Blog](https://drun1baby.top/2022/05/20/Java反序列化基础篇-02-Java反射与URLDNS链分析/) 先看里面反射的部分就行

[Java反序列化基础篇-03-Java反射进阶 | Drunkbaby's Blog](https://drun1baby.top/2022/05/29/Java反序列化基础篇-03-Java反射进阶/#0x02-反射的进阶知识) 

[Java反序列化基础篇-05-类的动态加载 | Drunkbaby's Blog](https://drun1baby.top/2022/06/03/Java反序列化基础篇-05-类的动态加载/) 类加载学完要知道，类加载时，会触发哪些部分的代码

[Java反序列化基础篇-01-反序列化概念与利用 | Drunkbaby's Blog](https://drun1baby.top/2022/05/17/Java反序列化基础篇-01-反序列化概念与利用/) 看完这个可以把上面那个URLDNS链看一下

<br>

## 补充内容

[Java反序列化漏洞专题-基础篇(21/09/05更新类加载部分)_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV16h411z7o9/?spm_id_from=333.1387.upload.video_card.click&vd_source=42c469cbf5707e7a96bb0dda4b39e6ef)  视频教程，内容和上面差不多

[ClassLoader · 攻击Java Web应用-Java Web安全](https://www.javasec.org/javase/ClassLoader/) 也是上面的内容，可以看看有什么不一样

[JavaSec/1.基础知识/ClassLoader(类加载机制)/ClassLoader(类加载机制).md at main · Y4tacker/JavaSec](https://github.com/Y4tacker/JavaSec/blob/main/1.基础知识/ClassLoader(类加载机制)/ClassLoader(类加载机制).md) 

[MyJavaSecStudy/docs/Java安全漫谈.pdf at main · 1diot9/MyJavaSecStudy](https://github.com/1diot9/MyJavaSecStudy/blob/main/docs/Java安全漫谈.pdf) phith0n师傅的Java安全漫谈，也很不错，其中07.反序列化篇(1)中讲了php，python和java反序列化的异同，可以看看

[phith0n/JavaThings: Share Things Related to Java - Java安全漫谈笔记相关内容](https://github.com/phith0n/JavaThings) Java安全漫谈的配套代码

[Java 反序列化漏洞（一） - 前置知识 & URLDNS | 素十八](https://su18.org/post/ysoserial-su18-1/) 涉及到了反序列化具体原理，比较深入









<br>



# 反序列化  <a id="反序列化"></a>

## CC链

CC链是Java反序列化的开始，每个人都应该好好学习。

[Java反序列化Commons-Collections篇01-CC1链 | Drunkbaby's Blog](https://drun1baby.top/2022/06/06/Java反序列化Commons-Collections篇01-CC1链/) 小叮师傅的博客讲了所有的CC链，同时也有环境搭建的示例，而且还有一张全CC链的流程图[JavaSecurityLearning/链子流程图 at main · Drun1baby/JavaSecurityLearning](https://github.com/Drun1baby/JavaSecurityLearning/tree/main/链子流程图) 

[MyJavaSecStudy/docs/Java安全漫谈.pdf at main · 1diot9/MyJavaSecStudy](https://github.com/1diot9/MyJavaSecStudy/blob/main/docs/Java安全漫谈.pdf) 挑07之后涉及CC链的内容看，18、19和涉及shiro的可以先不看(其实shiro反序列化的原理差不多，都是找链子，不过因为用的类加载器不一样，会出现一些变化)

[Java 反序列化漏洞（二） - Commons Collections | 素十八](https://su18.org/post/ysoserial-su18-2/#commonscollections1) su18师傅的反序列化取经路

=========================快速入门的话，可以只看上面的CC链=========================

<br>

## hessian

[Java安全学习——Hessian反序列化漏洞 - 枫のBlog](https://goodapple.top/archives/1193)

[Hessian 反序列化知一二 | 素十八](https://su18.org/post/hessian/)

<br>

## 其他链子&姿势

[Java反序列化之C3P0链 | Drunkbaby's Blog](https://drun1baby.top/2022/10/06/Java反序列化之C3P0链/) c3p0，可以打二次反序列化

[分析尝试利用tabby挖掘-SpringAOP链 - Potat0w0](https://blog.potatowo.top/2025/03/31/从复现到尝试用tabby挖掘-SpringAOP链/)

[realworldctf old system复盘（jdk1.4 getter jndi gadget）-先知社区](https://xz.aliyun.com/news/8630)	LdapAttribute链

[利用特殊反序列化组件攻击原生反序列化入口-先知社区](https://xz.aliyun.com/news/12356)	fj，jk结合ROME，LdapAttribute等

[Java利用无外网（下）：ClassPathXmlApplicationContext的不出网利用 | 离别歌](https://www.leavesongs.com/PENETRATION/springboot-xml-beans-exploit-without-network.html)	第一次知道tomcat和php有自动保存临时文件的机制。里面非预期的脏字符jar包构造没见过



<br>



# JNDI <a id="JNDI"></a>

## 基础内容

[Java反序列化之RMI专题01-RMI基础 | Drunkbaby's Blog](https://drun1baby.top/2022/07/19/Java反序列化之RMI专题01-RMI基础/) RMI先简单过一遍就行

[Java反序列化之JNDI学习 | Drunkbaby's Blog](https://drun1baby.top/2022/07/28/Java反序列化之JNDI学习/) JNDI要好好看，后面用的很多，学完后可以看一下log4j2漏洞

[JNDI · 攻击Java Web应用-Java Web安全](https://www.javasec.org/javase/JNDI/#jndi) 里面引用了一篇命名和目录服务基本概念的文章，记得看，对后面理解RMI和LDAP很有帮助，很多东西都会在调试里遇到

=========================快速入门的话，可以先只看上面部分的=========================

<br>

## 高版本JDK绕过

### 基于BeanFactory

[探索高版本 JDK 下 JNDI 漏洞的利用方法 - 跳跳糖](https://tttang.com/archive/1405/#toc_0x01-beanfactory)  除了最基本的EL表达式执行，还有Snakeyaml，XStream等方式（高版本tomcat的forceString被禁）

<br>

### 其他Factory绕过

[SolarWinds Security Event Manager AMF 反序列化 RCE (CVE-2024-0692) - X1r0z Blog](https://exp10it.io/2024/03/solarwinds-security-event-manager-amf-deserialization-rce-cve-2024-0692/#hikaricp-jndi-注入)	Hikari跟Druid一样，都可以实现JNDI+JDBC，都是可以执行初始化sql语句

[高版本JNDI注入-高版本Tomcat利用方案-先知社区](https://xz.aliyun.com/news/16156)

[探索高版本 JDK 下 JNDI 漏洞的利用方法 - 跳跳糖](https://tttang.com/archive/1405/#toc_snakeyaml)	jdk17的题特别喜欢考JNDI+JDBC

[JNDI jdk高版本绕过—— Druid-先知社区](https://xz.aliyun.com/news/10104)

<br>

## 未分类

[RMI协议分析 - lvyyevd's 安全博客](http://www.lvyyevd.cn/archives/rmi-xie-yi-fen-xi)

[Java RMI 攻击由浅入深 | 素十八](https://su18.org/post/rmi-attack/)

[MyJavaSecStudy/docs/Java安全漫谈.pdf at main · 1diot9/MyJavaSecStudy](https://github.com/1diot9/MyJavaSecStudy/blob/main/docs/Java安全漫谈.pdf) 04-06详细讲了RMI的通信过程

[京麟CTF 2024 ezldap 分析-先知社区](https://xz.aliyun.com/news/14103)  com.sun.jndi.ldap.object.trustSerialData false的绕过

[奇安信攻防社区-【2024补天白帽黑客大会】JNDI新攻击面探索](https://forum.butian.net/share/3857)

<br>

# JDBC <a id="JDBC"></a>

## mysql

[MySQL jdbc 反序列化分析 | Drunkbaby's Blog](https://drun1baby.top/2023/01/13/MySQL-jdbc-反序列化分析/) 最经典的mysql-jdbc

============如果真的很急，JDBC可以先只看上面这一个mysql，其他的遇到了再学=================

[从JDBC MySQL不出网攻击到spring临时文件利用-先知社区](https://xz.aliyun.com/news/17830)  这个打法比较新，其中的临时文件上传适用性广

<br>

## h2sql

[NCTF2024 Web方向题解-CSDN博客](https://blog.csdn.net/Err0r233/article/details/146484415) h2Revenge

[SolarWinds Security Event Manager AMF 反序列化 RCE (CVE-2024-0692) - X1r0z Blog](https://exp10it.io/2024/03/solarwinds-security-event-manager-amf-deserialization-rce-cve-2024-0692/#hikaricp-jndi-注入)	h2可以结合其他依赖写文件

<br>

## sqlite

[JavaSec/9.JDBC Attack/SQLite/index.md at main · Y4tacker/JavaSec](https://github.com/Y4tacker/JavaSec/blob/main/9.JDBC%20Attack/SQLite/index.md)

[CISCN2024 writeup（web部分）](https://jaspersec.top/posts/3286688009.html#ezjava)	ezjava

[从一道题看利用sqlite打jdbc达到RCE-先知社区](https://xz.aliyun.com/news/14234)

<br>



## Derby

[derby数据库如何实现RCE - lvyyevd's 安全博客](http://www.lvyyevd.cn/archives/derby-shu-ju-ku-ru-he-shi-xian-rce)

[N1CTF Junior 2024 Web Official Writeup - X1r0z Blog](https://exp10it.io/2024/02/n1ctf-junior-2024-web-official-writeup/#derby)

[因为项目中遇到Nacos挺多的...-知识星球](https://wx.zsxq.com/group/2212251881/topic/1524448452142582)

<br>



## 汇总

[JDBC Connection URL 攻击](https://paper.seebug.org/1832/)

[JDBC-Attack 攻击利用汇总-先知社区](https://xz.aliyun.com/news/13371)

[JDBC-Attack 利用汇总 - Boogiepop Doesn't Laugh](https://boogipop.com/2023/10/01/JDBC-Attack%20利用汇总/#前言)

[Jdbc碎碎念三：内存数据库 | m0d9's blog](https://m0d9.me/2021/04/26/Jdbc碎碎念三：内存数据库/)	h2sql，hsql，sqlite，derby

[yulate/jdbc-tricks: 《深入JDBC安全：特殊URL构造与不出网反序列化利用技术揭秘》对应研究总结项目 "Deep Dive into JDBC Security: Special URL Construction and Non-Networked Deserialization Exploitation Techniques Revealed" - Research Summary Project](https://github.com/yulate/jdbc-tricks)

<br>

# shiro<a id="shiro"></a>

## shiro反序列化

[Java反序列化Shiro篇01-Shiro550流程分析 | Drunkbaby's Blog](https://drun1baby.top/2022/07/10/Java反序列化Shiro篇01-Shiro550流程分析/)

[Java反序列化Shiro篇02-Shiro721流程分析 | Drunkbaby's Blog](https://drun1baby.top/2023/03/08/Java反序列化Shiro篇02-Shiro721流程分析/)

=========================shiro反序列化快速入门的话，可以只看上面的=========================

[Shiro RememberMe 漏洞检测的探索之路 - CT Stack 安全社区](https://stack.chaitin.com/techblog/detail/39) 通过密钥正常错误时，回显中rememberMe字段的不同来实现密钥爆破

[一种另类的 shiro 检测方式](https://mp.weixin.qq.com/s/do88_4Td1CSeKLmFqhGCuQ)

[Shiro绕过Header长度限制进阶利用 | Bmth's blog](http://www.bmth666.cn/2024/11/03/Shiro绕过Header长度限制进阶利用/index.html) 里面还提到pen4uin师傅的文章，也可以去看看

<br>

## shiro越权

待完善。。。



<br>

# Fastjson&Jackson&SnakeYaml <a id="Fastjson&Jackson&SnakeYaml"></a>

## Fastjson

[Java反序列化Fastjson篇01-FastJson基础 | Drunkbaby's Blog](https://drun1baby.top/2022/08/04/Java反序列化Fastjson篇01-Fastjson基础/) 小叮师傅写的不错，可以先看他的fastjson系列文章，一共5篇，前四篇必看，里面列举poc的可以略看，主要先搞懂各版本为什么能这样绕过

[Fastjson 反序列化漏洞 · 攻击Java Web应用-Java Web安全](https://www.javasec.org/java-vuls/FastJson.html) 

=============================快速入门的话，可以只看上面的=============================

[FastJsonParty/Fastjson全版本检测及利用-Poc.md at main · lemono0/FastJsonParty](https://github.com/lemono0/FastJsonParty/blob/main/Fastjson全版本检测及利用-Poc.md) fastjson主要是在黑盒条件下利用，这个项目给了各版本的探测利用姿势

[safe6Sec/Fastjson: Fastjson姿势技巧集合](https://github.com/safe6Sec/Fastjson)

[FastJson与原生反序列化](https://y4tacker.github.io/2023/03/20/year/2023/3/FastJson与原生反序列化/#为什么fastjson1的1-2-49以后不再能利用) fastjson在原生反序列化中起到toString-->getter的作用

[FastJson与原生反序列化(二)](https://y4tacker.github.io/2023/04/26/year/2023/4/FastJson与原生反序列化-二/) 讲了高版本fastjson怎么在原生反序列化中进行绕过

<br>

## SnakeYaml

[Java反序列化之 SnakeYaml 链 | Drunkbaby's Blog](https://drun1baby.top/2022/10/16/Java反序列化之-SnakeYaml-链/) 比较SnakeYaml反序列化的fastjson的异同；了解SPI机制是怎么被利用的

[SnakeYaml利用总结 | 1diot9's Blog](https://1diot9.github.io/2025/08/03/SnakeYaml利用总结/)  列举了常用的poc；我仓库有对应poc，可以直接用

[Yaml文件写法总结 | 1diot9's Blog](https://1diot9.github.io/2025/08/04/Yaml文件写法总结/)

=============================快速入门的话，可以先只看上面的=============================

[Java利用无外网（上）：从HertzBeat聊聊SnakeYAML反序列化 | 离别歌](https://www.leavesongs.com/PENETRATION/jdbc-injection-with-hertzbeat-cve-2024-42323.html)

[奇安信攻防社区-SnakeYaml 不出网 RCE 新链（JDK原生链）挖掘](https://forum.butian.net/share/4486) 



<br>

# 内存马&回显技术 <a id="内存马&回显技术"></a>

## 内存马

[Java内存马系列-01-基础内容学习 | Drunkbaby's Blog](https://drun1baby.top/2022/08/19/Java内存马系列-01-基础内容学习/) 可以先看01，02，03，05，里面提到的方法适用于传统JavaWeb项目(Tomcat为中间件的)

[基于内存 Webshell 的无文件攻击技术研究-安全KER - 安全资讯平台](https://www.anquanke.com/post/id/198886) 详细讲了Spring环境下，如何一步步动态注册一个Controller或者Interceptor来实现内存马注入

[针对Spring MVC的Interceptor内存马 - bitterz - 博客园](https://www.cnblogs.com/bitterz/p/14859766.html) 是上面landgrey师傅文章的二创

[奇安信攻防社区-利用 intercetor 注入 spring 内存 webshell](https://forum.butian.net/share/102) 也是landgrey师傅的

[Spring内存马学习 | Bmth's blog](http://www.bmth666.cn/2022/09/27/Spring内存马学习/index.html) 这里没讲原理，主要是给出了能直接用的内存马

=========================快速入门的话，可以先只看上面的几篇文章=========================

[bitterzzZZ/MemoryShellLearn: 分享几个直接可用的内存马，记录一下学习过程中看过的文章](https://github.com/bitterzzZZ/MemoryShellLearn)

[Getshell/Mshell: Memshell-攻防内存马研究](https://github.com/Getshell/Mshell)

[W01fh4cker/LearnJavaMemshellFromZero: 【三万字原创】完全零基础从0到1掌握Java内存马，公众号：追梦信安](https://github.com/W01fh4cker/LearnJavaMemshellFromZero)

上面这三篇都是内存马学习的相关项目，里面有很多优秀的文章和案例代码

[奇安信攻防社区-Solon框架注入内存马](https://forum.butian.net/share/3700)  里面提到的Java Object Searcher值得学习

[c0ny1/java-object-searcher: java内存对象搜索辅助工具](https://github.com/c0ny1/java-object-searcher)

[Shiro RememberMe 漏洞检测的探索之路 - CT Stack 安全社区](https://stack.chaitin.com/techblog/detail/39)  这里也有用到Java-object-searcher 构造tomcat回显

[半自动化挖掘request实现多种中间件回显 | 回忆飘如雪](https://gv7.me/articles/2020/semi-automatic-mining-request-implements-multiple-middleware-echo/)  java-object-searcher工具的作者

[内存对象搜索原理剖析-先知社区](https://xz.aliyun.com/news/11303)  java-object-searcher原理

<br>

### 内存马工具

[pen4uin/java-memshell-generator: 一款支持自定义的 Java 内存马生成工具｜A customizable Java in-memory webshell generation tool.](https://github.com/pen4uin/java-memshell-generator)

[ReaJason/MemShellParty: 一款专注于 Java 主流 Web 中间件的内存马快速生成工具，致力于简化安全研究人员和红队成员的工作流程，提升攻防效率](https://github.com/ReaJason/MemShellParty)

<br>

## 回显技术

[pen4uin/java-echo-generator: 一款支持自定义的 Java 回显载荷生成工具｜A customizable Java echo payload generation tool.](https://github.com/pen4uin/java-echo-generator) 回显技术的工具



<br>

# RASP <a id="RASP"></a>

[JNI攻击 · 攻击Java Web应用-Java Web安全](https://www.javasec.org/java-vuls/JNI.html)

<br>

# SpringBoot <a id="SpringBoot"></a>

[LandGrey/SpringBootVulExploit: SpringBoot 相关漏洞学习资料，利用方法和技巧合集，黑盒安全评估 check list](https://github.com/LandGrey/SpringBootVulExploit) 总结了SpringBoot的常见利用方式

<br>

## heapdump分析

主要是jdk自带的VisualVM看jdk版本，heapdump_tools分析依赖和密码

[Springboot信息泄露以及heapdump的利用_heapdump信息泄露-CSDN博客](https://blog.csdn.net/weixin_44309905/article/details/127279561)

[京麟CTF 2024 ezldap 分析-先知社区](https://xz.aliyun.com/news/14103?time__1311=eqUxuiDt5WqYqY5DsD7mPD%3DIZK7q9hGBbD&u_atoken=b94f9c93564049e1d2601ebb22a1098b&u_asig=0a472f9217433333617862864e004b)

<br>

## 文件缓存机制

[从JDBC MySQL不出网攻击到spring临时文件利用-先知社区](https://xz.aliyun.com/news/17830)



<br>



# 工具开发/二开<a id="devTools"></a>

[新年快乐 | ysoserial 分析与魔改](https://mp.weixin.qq.com/s?__biz=MzkwMzQyMTg5OA==&mid=2247486647&idx=1&sn=2e2ce3bad829dacd4807cbdb88e4ba2f&chksm=c097c612f7e04f0411454885e3d3248607f32ab6722592cc005eb610973220e8156999e75751&scene=178&cur_album_id=3744968375202660352&search_click_id=#rd)

[yhy0/ExpDemo-JavaFX: 图形化漏洞利用Demo-JavaFX版](https://github.com/yhy0/ExpDemo-JavaFX) GUI工具的开发框架

<br>



# JavaWeb基础<a id="JavaWeb基础"></a>

待完善。。。



<br>

# 代码审计<a id="CodeAudit"></a>

[Java安全慢游记](https://www.yuque.com/pmiaowu/gpy1q8) 非常好整理，强烈推荐，有基础也有例子，还讲了Tabby和CodeQL在审计中的运用

## 若依

[若依各版本漏洞 | 1diot9's Blog](https://1diot9.github.io/2025/08/02/若依各版本漏洞/)

[奇安信攻防社区-若依(RuoYi)框架漏洞战争手册](https://forum.butian.net/share/4328)

[♪(^∇^*)欢迎肥来！RuoYi历史漏洞 | 高木のBlog](https://blog.takake.com/posts/7219/#2-5-4-1-代码审计)

[SecurityList/Java_OA/RuoYi.md at main · ax1sX/SecurityList](https://github.com/ax1sX/SecurityList/blob/main/Java_OA/RuoYi.md)

[charonlight/RuoYiExploitGUI: 若依最新定时任务SQL注入可导致RCE漏洞的一键利用工具](https://github.com/charonlight/RuoYiExploitGUI?tab=readme-ov-file)

<br>

## WebGoat

[WebGoat靶场-身份认证缺陷 | 1diot9's Blog](https://1diot9.github.io/2025/07/22/WebGoat靶场-身份认证缺陷/)

[WebGoat代码审计-03-目录遍历漏洞 | Drunkbaby's Blog](https://drun1baby.top/2022/03/22/WebGoat代码审计-03-目录遍历漏洞/)

<br>

## 泛微Ecology9

[泛微ecology9前置 | 1diot9's Blog](https://1diot9.github.io/2025/08/10/泛微ecology9前置/)

[ecology9代码审计 | Sn1pEr's blog](https://sn1per-ssd.github.io/2024/08/15/ecology9代码审计/)

[泛微e-cology9 browser.jsp SQL注入漏洞分析 | _0xf4n9x_'s Blog](https://0xf4n9x.github.io/weaver-ecology9-browser-sqli.html)

[【漏洞复现】泛微E-Cology V9 browser.jspSQL注入漏洞及分析](https://mp.weixin.qq.com/s/YCzAQroLfBOw6OrxcIfb1A)

[微信公众平台](https://mp.weixin.qq.com/s/jNn0PqjP9yYBuPtqW4IdEA)

[changeUserInfo信息泄露+ofsLogin任意用户登录 | 1diot9's Blog](https://1diot9.github.io/2025/08/10/changeUserInfo信息泄露-ofsLogin任意用户登录/)

[泛微e-cology9 changeUserInfo信息泄漏及ofsLogin任意用户登录漏洞分析](https://mp.weixin.qq.com/s?__biz=MzI0NzEwOTM0MA==&mid=2652502015&idx=1&sn=39a4dd93fe5cc0a85dcb4aae28c6bf9c&chksm=f258544cc52fdd5a3ef748e125527cbe76d325b0b403ce359b686362a5cd923963e16faa2d45&scene=126&sessionid=1685092163&key=79faf193ca39ac845d45b240e517ccf717a50d07a9efad057991dbb878a24c00e9e8e4c2f3c84761361f7ff6a20040112d0d939914828f699229867b029a53fa957167f7b7be31f03cc8f249ba8f24232b359ecbc12c17027d3143b22e4915b41d3a6506ca566b13c76ce44a1e998cfa82968ee5fa4b159a3d52661d7480b3a9&ascene=15&uin=MzgxODQ4MjMz&devicetype=Windows+10+x64&version=63060012&lang=zh_CN&session_us=gh_7c749a8346d4&countrycode=GY&exportkey=n_ChQIAhIQ%2BcZx3tWxO0E8DrQjq2wpEhLvAQIE97dBBAEAAAAAADhYIKpLxZwAAAAOpnltbLcz9gKNyK89dVj021DG4x9QVpW9CXybpPpZ9qPTtZ8Qi0IYkOJTsU0z01YuxLeoWHBWWnq6ahSSdj2YdyvXZJVQNRmXDajYswlKJonxlRiXhKW%2Buu%2BNT%2BRFdiemTUgrCWyDH%2FFRsuXV%2FCeFYKdgPyKsjVNsv2nkl%2FurlVE%2F%2ByKVBB6ZktCegyDjbLg3wbFJ3cPplsGjjO4U%2FbW%2BRb7MPyBGa7xALwKMKjBejakftbNF63xcQG7CKN9s8CV73KbORpi3c5JXMk2DVZoNxFcynBMtEoc8&acctmode)

<br>

## 用友U8cloud

[用友U8Cloud环境搭建 | 1diot9's Blog](https://1diot9.github.io/2025/08/16/U8Cloud环境搭建/)

[用友U8cloud-esnserver接口RCE | 1diot9's Blog](https://1diot9.github.io/2025/08/16/用友U8cloud-esnserver接口RCE/)

[用友U8cloud-ServiceDispacherServlet反序列化 | 1diot9's Blog](https://1diot9.github.io/2025/08/16/用友U8cloud-ServiceDispacherServlet反序列化/)

[微信公众平台](https://mp.weixin.qq.com/s/gwdzmBCu5PjYdzVeWEcpDQ)

[用友U8cloud-LoginVideoServlet接口反序列化 | 1diot9's Blog](https://1diot9.github.io/2025/08/16/用友U8cloud-LoginVideoServlet接口反序列化/)

<br>

# 代码审计辅助工具<a id="代码审计辅助工具"></a>

## jar-analyzer

用起来比tabby和codeql都要简单，而且找到的链子也比较全，目前的不足是：对source，sink，sanitizer的设置不太灵活。总体来说，非常适合当作最先接触的辅助工具。

[Jar Analyzer 官方文档](https://docs.qq.com/doc/DV3pKbG9GS0pJS0tk)

<br>

## tabby 

[1. Neo4j CQL - 数据类型](https://www.yuque.com/pmiaowu/gpy1q8/arufc2k5gdmkesau/)  这个写的很详细，有基础，也有案例，还提供了很多模板查询语句，十分推荐

[自动化代码审计实践 | mayylu's blog](https://mayylu.github.io/2024/08/02/java/自动化代码审计实践/)

[4. 案例-Tabby自动化挖掘JavaSecCode](https://www.yuque.com/pmiaowu/gpy1q8/ng9b5mu7ltkyi0to)

<br>

## CodeQL 

[Codeql全新版本从0到1-先知社区](https://xz.aliyun.com/news/16918)  25年的文章，比较新

[1. 案例-CodeQL自动化挖掘JavaSecCode](https://www.yuque.com/pmiaowu/gpy1q8/upavb10n5vnit3y3)

[利用Github Actions生成CodeQL数据库 -- 以AliyunCTF2024 Chain17的反序列化链挖掘为例 - KingBridge - 博客园](https://www.cnblogs.com/kingbridge/articles/18100619)

[aliyun ctf chain17 回顾(超详细解读)-先知社区](https://xz.aliyun.com/news/16179)

[CodeQL从入门到入土 - FreeBuf网络安全行业门户](https://www.freebuf.com/articles/web/391242.html)

[原创 Paper | CodeQL 入门和基本使用 | CTF导航](https://www.ctfiot.com/215157.html)

[细谈使用CodeQL进行反序列化链的挖掘过程-SecIN](https://www.sec-in.com/article/2043)	相关文章关键词：codeql java反序列化

[利用codeql查找hsqldb2.7.3最新反序列化链-先知社区](https://xz.aliyun.com/news/14260)  里面的参考文章也值得看

[safe6Sec/CodeqlNote: Codeql学习笔记](https://github.com/safe6Sec/CodeqlNote?tab=readme-ov-file)

[自动化代码审计实践 | mayylu's blog](https://mayylu.github.io/2024/08/02/java/自动化代码审计实践/)

<br>







# 学习路线整合 <a id="学习路线整合"></a>

这里是其他师傅整理的学习路线，我这里仅列举了我看过的一些，如果有其他推荐的，可以私聊我

[前言 · 攻击Java Web应用-[Java Web安全]](https://www.javasec.org/)

[Y4tacker/JavaSec: a rep for documenting my study, may be from 0 to 0.1](https://github.com/Y4tacker/JavaSec?tab=readme-ov-file)

[Java安全慢游记](https://www.yuque.com/pmiaowu/gpy1q8)  着眼于代码审计，里面较为系统的讲了Tabby和CodeQL的使用

[Drun1baby/JavaSecurityLearning: 记录一下 Java 安全学习历程，也算是半条学习路线了](https://github.com/Drun1baby/JavaSecurityLearning)

[phith0n/JavaThings: Share Things Related to Java - Java安全漫谈笔记相关内容](https://github.com/phith0n/JavaThings?tab=readme-ov-file) 

[B站最全的Java安全学习路线_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Sv4y1i7jf/?spm_id_from=333.1007.top_right_bar_window_custom_collection.content.click) 许少，推荐一下他写的工具，jar-analyzer

[LyleMi/Learn-Web-Hacking: Study Notes For Web Hacking / Web安全学习笔记](https://github.com/LyleMi/Learn-Web-Hacking)  这个是Web安全的

<br>

# 工具推荐<a id="工具推荐"></a>

[vulhub/java-chains: Vulhub Vulnerability Reproduction Designated Platform](https://github.com/vulhub/java-chains) 利用链神器

[jar-analyzer/jar-analyzer: Jar Analyzer - 一个 JAR 包 GUI 分析工具，方法调用关系搜索，方法调用链 DFS 算法分析，模拟 JVM 的污点分析验证 DFS 结果，字符串搜索，Java Web 组件入口分析，CFG 程序分析，JVM 栈帧分析，自定义表达式搜索。官方文档：https://docs.qq.com/doc/DV3pKbG9GS0pJS0tk](https://github.com/jar-analyzer/jar-analyzer)    比tabby和codeql更简单的审计工具，功能也很丰富

[ReaJason/MemShellParty: 一款专注于 Java 主流 Web 中间件的内存马快速生成工具，致力于简化安全研究人员和红队成员的工作流程，提升攻防效率](https://github.com/ReaJason/MemShellParty)   web版的内存马生成工具，可以和jmg一起用

