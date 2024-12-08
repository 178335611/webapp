
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//下面的name要注意填写，否则映照不了。urlPatterns属性定义了Servlet可以响应的URL路径。也要对应
@WebServlet(name = "addProductServlet", urlPatterns = {"/addProductServlet"})
public class addProductServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为你的数据库名称
    private static final String DB_USER = "root"; // 数据库用户名
    private static final String DB_PASSWORD = "123456"; // 数据库密码

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //注意这两个要在函数最前面否则都不行。filter没用很麻烦
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // 添加产品逻辑
            boolean success = addProduct(request, response);

            if (success) {
                // 将结果保存在请求属性，用于JSP显示
                request.setAttribute("message", "Product added successfully!");
            } else {
                request.setAttribute("message", "Failed to add product.");
            }

            // 转发回原来的 JSP 页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("addProduct.jsp");
            dispatcher.forward(request, response);
        }
    }


    // 添加产品的函数
    public boolean addProduct(HttpServletRequest request,HttpServletResponse response) {

        String sql = "INSERT INTO 01product (pno, sno, pname, description, price,type) VALUES (?, ?, ?, ?, ?,?)";

        String pno1 = DatabaseHelper.getFirstRowColumnValue("01number","pno");
        int pno=Integer.parseInt(pno1);
        DatabaseHelper.increaseNumber(JDBC_URL,DB_USER,DB_PASSWORD,"pno");

        HttpSession session = request.getSession();
        Integer snoObj = (Integer) session.getAttribute("userno"); // 从会话中获取sno！注意写的是userno
        if (snoObj == null) { // 检查sno是否为null// 设置响应状态
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            // 发送错误信息到客户端
            try (PrintWriter out = response.getWriter()) {
                out.print("Session attribute 'sno' is not set or does not exist.");
                DatabaseHelper.displaySessionStatus(request);
            } catch (IOException e) {
                e.printStackTrace(); // 处理异常
            }
            return false; // 停止方法执行
        }
        int sno = snoObj; // 确保此处不会抛出异常
        String type = request.getParameter("type");
        String pname = request.getParameter("pname");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pno);
            statement.setInt(2, sno);
            statement.setString(3, pname);
            statement.setString(4, description);
            statement.setDouble(5, price);
            statement.setString(6, type);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // 如果插入成功，返回 true
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 如果发生异常，返回 false
        }
    }
}
