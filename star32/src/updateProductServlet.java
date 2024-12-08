import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// Servlet注解
@WebServlet(name = "updateProductServlet", urlPatterns = {"/updateProductServlet"})
public class updateProductServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 数据库连接URL
    private static final String DB_USER = "root"; // 数据库用户名
    private static final String DB_PASSWORD = "123456"; // 数据库密码

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            // 修改产品逻辑
            boolean success = updateProduct(request, response);

            if (success) {
                // 将结果保存在请求属性，用于JSP显示
                request.setAttribute("message", "Product updated successfully!");
            } else {
                request.setAttribute("message", "Failed to update product.");
            }

            // 转发回原来的 JSP 页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("updateProduct.jsp");
            dispatcher.forward(request, response);
        }
    }

    // 修改产品的函数
    public boolean updateProduct(HttpServletRequest request, HttpServletResponse response) {
        String sql = "UPDATE 01product SET pname = ?, description = ?, price = ? WHERE pno = ? AND sno = ?";

        HttpSession session = request.getSession();
        Integer snoObj = (Integer) session.getAttribute("userno"); // 从会话中获取sno
        if (snoObj == null) { // 检查sno是否为null
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            // 发送错误信息到客户端
            try (PrintWriter out = response.getWriter()) {
                out.print("Session attribute 'sno' is not set or does not exist.");
            } catch (IOException e) {
                e.printStackTrace(); // 处理异常
            }
            return false; // 停止方法执行
        }
        int sno = snoObj; // 确保此处不会抛出异常

        // 从请求中获取产品编号
        int pno = Integer.parseInt(request.getParameter("pno"));
        String pname = request.getParameter("pname");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 设置更新语句中的参数
            statement.setString(1, pname);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, pno);
            statement.setInt(5, sno); // 确保只更新该商家的产品

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // 如果更新成功，返回 true
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 如果发生异常，返回 false
        }
    }
}
