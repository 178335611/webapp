import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "userProfileServlet", urlPatterns = {"/userProfileServlet"})
public class userProfileServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户信息和订单
        Integer cno = (Integer) request.getSession().getAttribute("userno");
        if (cno == null) {
            response.sendRedirect("login.jsp"); // 用户未登录，重定向到登录页面
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            // 获取用户信息
            String userInfoSql = "SELECT * FROM 01customer WHERE cno = ?";
            PreparedStatement pstmt = conn.prepareStatement(userInfoSql);
            pstmt.setInt(1, cno);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                request.setAttribute("username", rs.getString("cname"));
                request.setAttribute("email", rs.getString("mail"));
                // 添加其他需要显示的用户信息
            }

            // 获取用户订单
            List<Order> orderList = new ArrayList<>();
            String orderSql = "SELECT * FROM 01order WHERE cno = ?";
            pstmt = conn.prepareStatement(orderSql);
            pstmt.setInt(1, cno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOno(rs.getInt("ono"));
                order.setPno(rs.getInt("pno"));
                order.setTotalPrice(rs.getDouble("price"));
                orderList.add(order);
            }
            request.setAttribute("orderList", orderList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "获取信息时出错: " + e.getMessage());
        }

        request.getRequestDispatcher("userProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Integer cno = (Integer) request.getSession().getAttribute("userno");
        if (cno == null) {
            response.sendRedirect("login.jsp"); // 用户未登录，重定向到登录页面
            return;
        }

        String action = request.getParameter("action");
        if ("update".equals(action)) {
            String newUsername = request.getParameter("cname");
            String newEmail = request.getParameter("mail");
            Integer newMoney = Integer.parseInt(request.getParameter("money"));
            Integer newPassword = Integer.parseInt(request.getParameter("cpassword"));

            if(newUsername == null || newUsername.isEmpty()){
                request.setAttribute("errorMessage", "姓名不能为空！");
                response.sendRedirect("userProfile.jsp");
                return;
            }

            String passwordParam = request.getParameter("cpassword");
            if (passwordParam == null ) {
                request.setAttribute("errorMessage", "密码不能为空！");
                response.sendRedirect("userProfile.jsp");
                return;
            }

            String moneyParam = request.getParameter("money");
            if (moneyParam != null && !moneyParam.isEmpty()) {
                newMoney = Integer.parseInt(moneyParam);
            }

            // 更新用户信息
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                // 构建 SQL 更新语句
                String updateSql = "UPDATE 01customer SET cname = ?, cpassword = ?, money = ?, mail = ? WHERE cno = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateSql);

                pstmt.setString(1, newUsername); // 姓名
                pstmt.setInt(2, newPassword);  // 密码
                // 对于 newMoney 和 newEmail，若为 null，则传入 null
                pstmt.setInt(3, newMoney);

                pstmt.setString(4, newEmail); // 邮箱
                // 假设 cno 是一个已知的用户编号
                pstmt.setInt(5, cno);

                // 执行更新
                pstmt.executeUpdate();

                request.setAttribute("successMessage", "信息更新成功！");
                response.sendRedirect("userProfile.jsp");

            } catch (SQLException e) {
                request.setAttribute("errorMessage", "数据库错误: " + e.getMessage());
                response.sendRedirect("userProfile.jsp");
            }
        }
        else if ("delete".equals(action)) {
            // 删除账号的逻辑
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String deleteSql = "DELETE FROM 01customer WHERE cno = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                pstmt.setInt(1, cno);
                pstmt.executeUpdate();
                request.getSession().invalidate(); // 注销用户
                response.sendRedirect("login.jsp"); // 删除后重定向到登录页面
                return;
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "删除账号时出错: " + e.getMessage());
            }
        }
        if("receive".equals(action)) {
            String ono = request.getParameter("ono");

            String updateSql = "UPDATE 01order SET state = ? WHERE ono = ? AND state = ?";
            try (Connection conn =DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
                pstmtUpdate.setString(1, "收到,订单已结束"); // 设置新的状态为完成
                pstmtUpdate.setInt(2, Integer.parseInt(ono)); // 设置订单编号
                pstmtUpdate.setString(3, "商家已完成"); // 仅在当前状态为未完成时更新

                int rowsAffected = pstmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                } else {
                    // 如果没有更新任何行，设置失败消息
                    request.setAttribute("message", "未找到相应订单或试图收到商家未完成的订单。");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("userProfile.jsp");
            dispatcher.forward(request, response);
        }
        if("reject".equals(action)) {
            String ono = request.getParameter("ono");
            String updateSql = "UPDATE 01order SET state = ? WHERE ono = ? AND (state = '未完成' OR state = '未付款')";
            //在这里磨了好久，最后不如直接这样写
            try (Connection conn =DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
                pstmtUpdate.setString(1, "取消"); // 设置新的状态为完成
                pstmtUpdate.setInt(2, Integer.parseInt(ono)); // 设置订单编号

                int rowsAffected = pstmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    // 如果更新成功，设置成功消息
                    request.setAttribute("message", "订单状态已成功更新为：取消。");
                } else {
                    // 如果没有更新任何行，设置失败消息
                    request.setAttribute("message", "未找到相应订单或试图取消不是未完成状态的订单。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("userProfile.jsp");
            dispatcher.forward(request, response);//注意要这样写request才能转过去
        }
        if("pay".equals(action)) {
            String ono = request.getParameter("ono");
            String updateSql = "UPDATE 01order SET state = ? WHERE ono = ? AND state = ?";
            try (Connection conn =DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
                pstmtUpdate.setString(1, "未完成"); // 设置新的状态为完成
                pstmtUpdate.setInt(2, Integer.parseInt(ono)); // 设置订单编号
                pstmtUpdate.setString(3, "未付款"); // 仅在当前状态为未完成时更新

                int rowsAffected = pstmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    // 如果更新成功，设置成功消息
                } else {
                    // 如果没有更新任何行，设置失败消息
                    request.setAttribute("message", "未找到相应订单或试图付款不是未付款状态的订单。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("userProfile.jsp");
            dispatcher.forward(request, response);//注意要这样写request才能转过去
        }

    }
}
