import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/shopAccountServlet")
public class shopAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 设置响应的字符编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String jdbcUrl = "jdbc:mysql://localhost:3306/mysql"; // 替换为你的数据库名称
            String dbUser = "root";
            String dbPassword = "123456";

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            String action = request.getParameter("action");
            Integer userno = (Integer) request.getSession().getAttribute("userno");

            if ("update".equals(action)) {
                String sname = request.getParameter("sname");
                String spassword = request.getParameter("spassword");
                String mail= request.getParameter("mail");
                String sql = "UPDATE 01shop SET sname = ?, spassword = ?, mail = ? WHERE sno = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, sname);
                pstmt.setString(2, spassword);
                pstmt.setString(3, mail);
                pstmt.setInt(4, userno);
                pstmt.executeUpdate();
                response.sendRedirect("shopAccount.jsp");
            }
            else if ("delete".equals(action)) {
                String sql = "DELETE FROM 01shop WHERE sno = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userno);
                pstmt.executeUpdate();

                // 删除成功后使用户会话失效
                request.getSession().invalidate();

                // 重定向到登录页面
                response.sendRedirect("merchantLogin.jsp");
            }
            if("finish".equals(action)) {
                String ono = request.getParameter("ono");
                String updateSql = "UPDATE 01order SET state = ? WHERE ono = ? AND state = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                    pstmtUpdate.setString(1, "商家已完成"); // 设置新的状态为完成
                    pstmtUpdate.setInt(2, Integer.parseInt(ono)); // 设置订单编号
                    pstmtUpdate.setString(3, "未完成"); // 仅在当前状态为未完成时更新

                    int rowsAffected = pstmtUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        // 如果更新成功，设置成功消息
                        request.setAttribute("message", "订单状态已成功更新为：商家已完成。");
                    } else {
                        // 如果没有更新任何行，设置失败消息
                        request.setAttribute("message", "未找到相应订单或试图修改不是未完成的订单。");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("shopAccount.jsp");
                dispatcher.forward(request, response);
            }
            if("reject".equals(action)) {
                String ono = request.getParameter("ono");
                String updateSql = "UPDATE 01order SET state = ? WHERE ono = ? AND state = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                    pstmtUpdate.setString(1, "商家已拒绝"); // 设置新的状态为完成
                    pstmtUpdate.setInt(2, Integer.parseInt(ono)); // 设置订单编号
                    pstmtUpdate.setString(3, "未完成"); // 仅在当前状态为未完成时更新

                    int rowsAffected = pstmtUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        // 如果更新成功，设置成功消息
                        request.setAttribute("message", "订单状态已成功更新为：商家已完成。");
                    } else {
                        // 如果没有更新任何行，设置失败消息
                        request.setAttribute("message", "未找到相应订单或试图修改不是未完成状态的订单。");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("shopAccount.jsp");
                dispatcher.forward(request, response);//注意要这样写request才能转过去
            }


        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("merchantAccount.jsp?message=数据库错误: " + e.getMessage());
        } finally {
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) {} }
            if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
        }
    }
}
