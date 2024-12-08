import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet(name = "deleteProductServlet", urlPatterns = {"/deleteProductServlet"})
public class deleteProductServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为你的数据库名称
    private static final String DB_USER = "root"; // 数据库用户名
    private static final String DB_PASSWORD = "123456"; // 数据库密码

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action"); // 从请求中获取要执行的操作
        String pno = request.getParameter("pno"); // 从请求中获取要删除的产品编号

        if (pno == null || pno.isEmpty()) {
            request.setAttribute("errorMessage", "产品编号不能为空");
            request.getRequestDispatcher("deleteProduct.jsp").forward(request, response);
            return;
        }

        // 执行删除操作
        if("delete".equals(action)) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM 01product WHERE pno = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(pno));

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    // 删除成功，重定向到产品列表页面
                    response.sendRedirect("deleteProduct.jsp?message=deleteSuccess" );
                    //request删去可正常跳转
                } else {
                    // 产品编号未找到
                    request.setAttribute("errorMessage", "未找到该产品，删除失败");
                    request.getRequestDispatcher("deleteProduct.jsp").forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "删除产品时出错");
                request.getRequestDispatcher("deleteProduct.jsp").forward(request, response);
            }
        }
    }
}
