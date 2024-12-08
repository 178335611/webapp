
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "warehouseServlet", urlPatterns = {"/warehouseServlet"})
public class warehouseServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为你的数据库名称
    private static final String DB_USER = "root"; // 数据库用户名
    private static final String DB_PASSWORD = "123456"; // 数据库密码

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("list".equals(action)) {
            String sno = request.getParameter("sno"); // 获取商铺的sno
            listProducts(request, response, sno); // 将sno传递给listProducts方法
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateProduct(request, response);
            // 转发到 warehouse.jsp 页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("warehouse.jsp");
        }


    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response, String sno) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        List<Product> productList = new ArrayList<>();

        // 查询本店的产品
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM 01product WHERE sno = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, sno); // 使用商铺的sno进行查询
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pno = resultSet.getInt("pno");
                String pname = resultSet.getString("pname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int p_sno=resultSet.getInt("sno");

                Product product = new Product(pno,p_sno, pname, description, price);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "获取产品列表时出错");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // 将产品列表添加到请求属性中，然后转发到产品列表页面
        request.setAttribute("productList", productList);
        request.getRequestDispatcher("productList.jsp").forward(request, response);
    }
    protected void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "UPDATE 01product SET pname = ?, description = ?, " +
                "price = ?,type = ? WHERE pno = ?";

        String pname = request.getParameter("pname");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        int pno = Integer.parseInt(request.getParameter("pno"));
        //忘写pno卡了很久，还是异常检测不给力。
        // 处理 sum 参数，若为 null 则直接赋值为 null


        // 处理 price 参数，若为 null 则直接赋值为 null
        String priceParam = request.getParameter("price");
        Double price = null;
        if (priceParam != null && !priceParam.isEmpty()) {
            price = Double.parseDouble(priceParam);
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pname);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setString(4, type);
            statement.setInt(5,pno);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
            } else {
                request.setAttribute("message", "Failed to update product.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("warehouse.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("warehouse.jsp");
            dispatcher.forward(request, response);
        }
    }

}
