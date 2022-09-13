package top.xiaorang.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcTests {
    private Connection connection;

    @Before
    public void before() throws IOException, SQLException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("【建立连接】：" + connection);
    }

    @After
    public void after() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("【" + connection + "连接关闭】");
        }
    }

    @Test
    public void testConnection() {

    }

    @Test
    public void testAdd() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('小让', 18, '1995-07-13', 16000.0, '程序员');";
        int count = statement.executeUpdate(sql);
        System.out.println("【数据更新行数】：" + count);
    }

    @Test
    public void testDelete() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "DELETE FROM `user` WHERE `uid` = 1;";
        int count = statement.executeUpdate(sql);
        System.out.println("【数据更新行数】：" + count);
    }

    @Test
    public void testQuery() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM `user`;";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            int uid = rs.getInt("uid");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            Date birthday = rs.getDate("birthday");
            float salary = rs.getFloat("salary");
            String note = rs.getString("note");
            System.out.println("User{" +
                    "uid=" + uid +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", birthday=" + birthday +
                    ", salary=" + salary +
                    ", note='" + note + '\'' +
                    '}');
        }
    }

    @Test
    public void testSQLInjection() throws SQLException {
        Statement statement = connection.createStatement();
        String username = "' 小白' or 1 = 1";
        String sql = "SELECT * FROM `user` where `name` = " + username;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            int uid = rs.getInt("uid");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            Date birthday = rs.getDate("birthday");
            float salary = rs.getFloat("salary");
            String note = rs.getString("note");
            System.out.println("User{" +
                    "uid=" + uid +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", birthday=" + birthday +
                    ", salary=" + salary +
                    ", note='" + note + '\'' +
                    '}');
        }
    }
}
