import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import javax.servlet.http.HttpSession;

@WebServlet(name = "merchantLoginServlet", urlPatterns = {"/merchantLoginServlet"})
public class merchantLoginServlet extends HttpServlet {
    // 数据库连接参数
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 请根据自己的数据库信息修改
    private static final String DB_USER = "root"; // 数据库用户名
    private static final String DB_PASSWORD = "123456"; // 数据库密码

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("login".equals(action)) {
            loginMerchant(request, response);
        }
        if ("register".equals(action)) {
            registerMerchant(request, response);
        }//登录或者注册
        if ("backlogin".equals(action)) {
            response.sendRedirect("merchantLogin.jsp");

        }
    }

    private void loginMerchant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String merchantName = request.getParameter("name");
        String password = request.getParameter("password");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM 01shop WHERE sname = ? AND spassword = ?"; // 修改为实际的表和列名
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, merchantName);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // 登录成功，重定向到商家欢迎页面
                response.sendRedirect("warehouse.jsp");
                int sno = resultSet.getInt("sno");
                request.getSession().setAttribute("userno", sno);
                HttpSession session = request.getSession();
                session.setAttribute("usertype","s");


            } else {
                // 登录失败，重定向回商家登录页面
                request.setAttribute("errorMessage", "商家名称或密码不正确。");
                request.getRequestDispatcher("merchantLogin.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "登录失败：发生错误，请稍后重试。");
            request.getRequestDispatcher("merchantLogin.jsp").forward(request, response);
        }
    }

    private void registerMerchant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sname = request.getParameter("name");
        Integer spassword = Integer.valueOf(request.getParameter("password"));
        //注意后面的参数名要按照jsp里的来写。
        Integer sno = Integer.valueOf(DatabaseHelper.getFirstRowColumnValue("01number", "sno"));
        DatabaseHelper.increaseNumber(JDBC_URL, DB_USER, DB_PASSWORD,"sno");
        //编号从某个表里面获取
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO `01shop` (sname, spassword, sno) VALUES (?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, sname);
            statement.setInt(2, spassword); // 实际应用中请考虑使用加密
            statement.setInt(3, sno);
            //修改了正确的类型，有些是int不是string。改正了表，
            //linestring是给几何类型数据用的因此一直报错，应该选varchar才对。

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // 注册成功，重定向到登录页面
                response.sendRedirect("merchantLogin.jsp");
            } else {
                // 注册失败，返回注册页面
                response.sendRedirect("merchantRegister.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 输出异常信息
            request.setAttribute("errorMessage", "注册失败: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

    }

}
