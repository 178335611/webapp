import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Date;

public class DatabaseHelper {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为您的数据库名
    private static final String DB_USER = "root"; // 替换为您的数据库用户名
    private static final String DB_PASSWORD = "123456"; // 替换为您的数据库密码

    // 获取某个表的第一行某列的值
    public static String getFirstRowColumnValue(String tableName, String columnName) {
        String value = null;
        String query = String.format("SELECT %s FROM %s LIMIT 1", columnName, tableName);

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                value = resultSet.getString(columnName); // 获取第一行指定列的值
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value; // 返回获取到的值，可能为 null（若表为空）
    }

    public static void increaseNumber( String JDBC_URL, String DB_USER,  String DB_PASSWORD, String rowName) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            // 这里假设 01number 表只有一行记录并且 cno 是整型字段
            String sql = "UPDATE 01number SET " +rowName +" = "+ rowName+" + 1"; // 将 cno 加1
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // 处理异常
        }
    }


    public static void displaySessionStatus(HttpServletRequest request) {
        // 获取当前会话
        HttpSession session = request.getSession(false); // false表示如果没有会话则返回null

        if (session == null) {
            System.out.println("No active session found."); // 如果会话不存在，输出信息
            return;
        }

        // 输出会话ID
        System.out.println("Session ID: " + session.getId());

        // 输出会话创建时间
        System.out.println("Creation Time: " + new Date(session.getCreationTime()));

        // 输出上次访问时间
        System.out.println("Last Accessed Time: " + new Date(session.getLastAccessedTime()));

        // 输出会话的最大无操作时间
        System.out.println("Max Inactive Interval: " + session.getMaxInactiveInterval() + " seconds");

        // 输出会话中的属性
        System.out.println("Session Attributes:");
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            System.out.println(attributeName + ": " + attributeValue);
        }
    }

}


