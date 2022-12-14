package top.xiaorang.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author liulei
 * @description JDBC测试类
 * @github <a href="https://github.com/xihuanxiaorang/jdbc-study">jdbc-study</a>
 * @Copyright 博客：<a href="https://xiaorang.top">小让的糖果屋</a>  - show me the code
 * @since 2022/9/14 16:27
 */
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

    @Test
    public void testPreparedStatementAdd() throws SQLException {
        String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "小白");
        preparedStatement.setInt(2, 18);
        preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
        preparedStatement.setFloat(4, 18000.0f);
        preparedStatement.setString(5, "销售");
        int count = preparedStatement.executeUpdate();
        System.out.println("【数据更新行数】：" + count);
    }

    @Test
    public void testPreparedStatementDelete() throws SQLException {
        String sql = "DELETE FROM `user` WHERE `uid` = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 6);
        int count = preparedStatement.executeUpdate();
        System.out.println("【数据更新行数】：" + count);
    }

    @Test
    public void testPreparedStatementQuery() throws SQLException {
        String sql = "SELECT * FROM `user`;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
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
    public void testPreparedStatementSQLInjection() throws SQLException {
        String sql = "SELECT * FROM `user` where `name` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "'小白' or 1 = 1");
        ResultSet rs = preparedStatement.executeQuery();
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
    public void testSupportsBatchUpdates() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        boolean supportsBatchUpdates = databaseMetaData.supportsBatchUpdates();
        System.out.println("是否支持批处理？" + supportsBatchUpdates);
    }

    /**
     * 用于测试批量处理功能是否正常开启
     *
     * @throws SQLException
     */
    @Test
    public void testPreparedStatementBatchAdd() throws SQLException {
        connection.setAutoCommit(false);
        try {
            String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < 5; i++) {
                preparedStatement.setString(1, "小白" + i);
                preparedStatement.setInt(2, 18);
                preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
                preparedStatement.setFloat(4, 18000.0f);
                preparedStatement.setString(5, "销售");
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }

    /**
     * 当max_allowed_packet=200*1024，即200K的时候，会出现com.mysql.cj.jdbc.exceptions.PacketTooBigException异常
     *
     * @throws SQLException
     */
    @Test
    public void testPreparedStatementBatchAdd2() throws SQLException {
        connection.setAutoCommit(false);
        try {
            String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < 1000000; i++) {
                preparedStatement.setString(1, "小白" + i);
                preparedStatement.setInt(2, 18);
                preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
                preparedStatement.setFloat(4, 18000.0f);
                preparedStatement.setString(5, "销售");
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }

    /**
     * 优化后的批处理代码
     *
     * @throws SQLException
     */
    @Test
    public void testPreparedStatementBatchAdd3() throws SQLException {
        long start = System.currentTimeMillis();
        connection.setAutoCommit(false);
        try {
            String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                preparedStatement.setString(1, "小白" + i);
                preparedStatement.setInt(2, 18);
                preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
                preparedStatement.setFloat(4, 18000.0f);
                preparedStatement.setString(5, "销售");
                preparedStatement.addBatch();
                if (i % 500 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
        System.out.println("百万条数据插入用时：" + (System.currentTimeMillis() - start) + "【单位：毫秒】");
    }

    @Test
    public void testTransferNonTransaction() {
        try {
            String sql1 = "UPDATE `user` SET `salary` = `salary` - ? WHERE `uid` = ?;";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setFloat(1, 1000.0f);
            preparedStatement1.setInt(2, 1);
            preparedStatement1.executeUpdate();
            int i = 1 / 0;
            String sql2 = "UPDATE `user` SET `salary` = `salary` + ? WHERE `uid` = ?;";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setFloat(1, 1000.0f);
            preparedStatement2.setInt(2, 2);
            preparedStatement2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransferWithTransaction() throws SQLException {
        connection.setAutoCommit(false);
        try {
            String sql1 = "UPDATE `user` SET `salary` = `salary` - ? WHERE `uid` = ?;";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setFloat(1, 1000.0f);
            preparedStatement1.setInt(2, 1);
            preparedStatement1.executeUpdate();
//            int i = 1 / 0;
            String sql2 = "UPDATE `user` SET `salary` = `salary` + ? WHERE `uid` = ?;";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setFloat(1, 1000.0f);
            preparedStatement2.setInt(2, 2);
            preparedStatement2.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}
