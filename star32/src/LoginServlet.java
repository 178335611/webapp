import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    // 数据库连接参数
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cname = request.getParameter("name");
        Integer cpassword = Integer.valueOf(request.getParameter("password"));
        //注意后面的参数名要按照jsp里的来写。
        Integer cno = Integer.valueOf(DatabaseHelper.getFirstRowColumnValue("01number", "cno"));
        DatabaseHelper.increaseNumber(JDBC_URL, DB_USER, DB_PASSWORD,"cno");
        //编号从某个表里面获取
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO `01customer` (cname, cpassword, cno) VALUES (?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cname);
            statement.setInt(2, cpassword); // 实际应用中请考虑使用加密
            statement.setInt(3, cno);
            //修改了正确的类型，有些是int不是string。改正了表，
            //linestring是给几何类型数据用的因此一直报错，应该选varchar才对。

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // 注册成功，重定向到登录页面
                response.sendRedirect("login.jsp");
            } else {
                // 注册失败，返回注册页面
                response.sendRedirect("register.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 输出异常信息
            request.setAttribute("errorMessage", "注册失败: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("name");//注意要和jsp里的name对应
        Integer password = Integer.valueOf(request.getParameter("password"));

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM 01customer WHERE cname = ? AND cpassword = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, password); // 实际应用中请考虑使用加密
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // 登录成功，重定向到欢迎页面或主页
                response.sendRedirect("shoppingPage.jsp");
                HttpSession session = request.getSession();
                session.setAttribute("usertype","c");
                int cno=resultSet.getInt("cno");
                session.setAttribute("userno",cno);
                //添加到session中
            } else {
                // 登录失败，重定向回登录页面
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            // 针对SQLException进行处理
            if (e.getMessage().contains("Table '01customer' doesn't exist")) {
                // 表不存在的情况
                request.setAttribute("errorMessage", "登录失败：用户信息表不存在。");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else if (e.getMessage().contains("Unknown column 'cpassword'")) {
                // 列不存在的情况
                request.setAttribute("errorMessage", "登录失败：用户密码列不存在。");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                // 处理其他SQLException
                e.printStackTrace();
                request.setAttribute("errorMessage", "登录失败：数据库错误，请稍后重试。");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            // 处理密码非整型输入的情况
            request.setAttribute("errorMessage", "登录失败：密码格式不正确。");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            // 处理其他异常
            e.printStackTrace();
            request.setAttribute("errorMessage", "登录失败：发生未知错误。");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

}
