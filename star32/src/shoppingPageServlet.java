import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "shoppingPageServlet", urlPatterns = {"/shoppingPageServlet"})
public class shoppingPageServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为你的数据库名称
    private static final String DB_USER = "root"; // 数据库用户名
    private static final String DB_PASSWORD = "123456"; // 数据库密码

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("list".equals(action)) {
            listProducts(request, response);
        } else if ("search".equals(action)) {
            searchProducts(request, response);
        }
    }

    // 列出所有产品
    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> productList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM 01product";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pno = resultSet.getInt("pno");
                String pname = resultSet.getString("pname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int p_sno = resultSet.getInt("sno");

                Product product = new Product(pno, p_sno, pname, description, price);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "获取产品列表时出错");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("productList", productList);
        request.getRequestDispatcher("shop.jsp").forward(request, response);
    }

    // 搜索产品
    private void searchProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Product> productList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM 01product WHERE pname LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pno = resultSet.getInt("pno");
                String pname = resultSet.getString("pname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int p_sno = resultSet.getInt("sno");

                Product product = new Product(pno, p_sno, pname, description, price);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "搜索产品时出错");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("productList", productList);
        request.getRequestDispatcher("shop.jsp").forward(request, response);
    }

    // 处理下单请求
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("order".equals(action)) {
            placeOrder(request, response);
        }
    }

    // 处理下单逻辑
    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从 session 中获取 cno（客户编号）
        Integer cno = (Integer) request.getSession().getAttribute("userno");
        if (cno == null) {
            request.setAttribute("message", "用户未登录，请先登录。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 从 request 中获取 sno（商店编号）和 pno（产品编号）
        int sno = Integer.parseInt(request.getParameter("sno"));
        int pno = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        // 从 request 中获取产品价格
        double pricePerUnit = Double.parseDouble(request.getParameter("price"));
        double totalPrice = pricePerUnit * quantity; // 计算总价

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 连接数据库
            String jdbcUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为你的数据库名称
            String dbUser = "root"; // 数据库用户名
            String dbPassword = "123456"; // 数据库密码
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            // 从 01number 表中获取 ono（订单编号）
            int ono=Integer.parseInt(DatabaseHelper.getFirstRowColumnValue("01number","ono")); // 获取当前最大订单编号
            DatabaseHelper.increaseNumber(JDBC_URL, DB_USER, DB_PASSWORD, "ono");


            // 将订单信息插入到 01order 表中
            String insertOrderSql = "INSERT INTO 01order (ono, cno, sno, pno, price,state,sum) VALUES (?, ?, ?, ?, ?,?,?)";
            pstmt = conn.prepareStatement(insertOrderSql);
            pstmt.setInt(1, ono);
            pstmt.setInt(2, cno);
            pstmt.setInt(3, sno);
            pstmt.setInt(4, pno);
            pstmt.setDouble(5, totalPrice);
            pstmt.setString(6, "未付款");
            pstmt.setDouble(7, quantity);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {

            } else {
                request.setAttribute("message", "下单失败，请重试。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "下单出现错误: " + e.getMessage());
        } finally {
            // 关闭数据库连接
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) {} }
            if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
        }

        // 回到商品列表页面并显示消息
        request.getRequestDispatcher("shoppingPage.jsp").forward(request, response);
    }

}
