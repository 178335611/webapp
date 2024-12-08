<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%
    // 设置字符编码
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");


    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        String jdbcUrl = "jdbc:mysql://localhost:3306/mysql"; // 替换为你的数据库名称
        String dbUser = "root"; // 数据库用户名
        String dbPassword = "123456"; // 数据库密码

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

        // 获取用户编号
        Integer userno = (Integer) session.getAttribute("userno");
        String sql = "SELECT * FROM 01shop WHERE sno = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userno);
        rs = pstmt.executeQuery();

        String sname = null;
        String spassword = null;
        String mail = null;
        if (rs.next()) {
            sname = rs.getString("sname");
            spassword = rs.getString("spassword"); // 假设有密码字段
            mail = rs.getString("mail");
        }
%>

<html>
<head>
    <title>商家账户页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .header {
            margin-bottom: 20px;
        }
        .btn {
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .delete-btn {
            background-color: #d9534f;
        }
        .delete-btn:hover {
            background-color: #c9302c;
        }
    </style>
</head>
<body>

<div class="header">
    <h2>商家账户信息</h2>
</div>

<form action="shopAccountServlet" method="post">
    <input type="hidden" name="action" value="update">
    <div>
        <label for="sname">商家名称:</label>
        <input type="text" id="sname" name="sname" value="<%= sname %>" required>
    </div>
    <div>
        <label for="spassword">密码:</label>
        <input type="password" id="spassword" name="spassword" value="<%= spassword %>" required>
    </div>
    <div>
        <label for="mail">mail:</label>
        <input type="text" id="mail" name="mail" value="<%= mail %>" required>
    </div>
    <br><br>
    <div>
        <input type="submit" value="修改信息" class="btn">
    </div>
</form>

<form action="shopAccountServlet" method="post" style="display:inline;">
    <input type="hidden" name="action" value="delete">
    <input type="submit" value="删除账号" class="btn delete-btn" onclick="return confirm('确定要删除此账号吗？');">
</form>
<br><br> <!-- 添加两个空行 -->
<div>
    <a href="warehouse.jsp" class="btn">返回上一页面</a>
</div>
<br><br>
<div>

</div>
<br><br>
<title>商家订单查询</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 20px;
    }
    .header {
        margin-bottom: 20px;
    }
    .btn {
        padding: 5px 10px;
        border: none;
        border-radius: 4px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
    }
    .btn:hover {
        background-color: #0056b3;
    }
    table {
        width: 100%;
        border-collapse: collapse; /* 合并边框 */
        margin-bottom: 20px; /* 底部间隙 */
    }
    th, td {
        padding: 12px; /* 单元格内边距 */
        text-align: left; /* 左对齐文本 */
        border-bottom: 1px solid #ddd; /* 下边框 */
    }
    th {
        background-color: yellowgreen; /* 表头背景颜色 */
        color: white; /* 表头字体颜色 */
    }
    tr:hover {
        background-color: #f1f1f1; /* 悬停效果 */
    }
</style>

<div class="header">

</div>
<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<script>
    alert("<%= message %>");
</script>
<%
    }
%>
<div style="display: flex; gap: 10px;">
    <form action="shopAccount.jsp" method="get">
        <input type="hidden" name="filter" value="all">
        <input type="submit" value="显示所有订单" class="btn">
    </form>
    <form action="shopAccount.jsp" method="get">
        <input type="hidden" name="filter" value="completed">
        <input type="submit" value="显示已完成订单" class="btn">
    </form>
    <form action="shopAccount.jsp" method="get">
        <input type="hidden" name="filter" value="rejected">
        <input type="submit" value="显示已拒绝订单" class="btn">
    </form>
    <form action="shopAccount.jsp" method="get">
        <input type="hidden" name="filter" value="waiting">
        <input type="submit" value="显示当前订单" class="btn">
    </form>
</div>

<%
    ResultSet rsOrder = null;
    String filter = request.getParameter("filter");
    if (filter == null) {
        filter = "waiting";
    }
    if (true) {
        int sno = Integer.parseInt(session.getAttribute("userno").toString());
        // 根据商家编号查询订单信息
        String sql2 = "SELECT * FROM 01order WHERE sno = ?";
        if ("completed".equals(filter)) {
            sql2 += " AND state = '商家已完成' or state = '收到，订单已结束'";
        } else if ("rejected".equals(filter)) {
            sql2 += " AND state = '商家已拒绝' or state = '取消'";
        }else if ("waiting".equals(filter)) {
            sql2 += " AND state != '商家已完成' AND state != '商家已拒绝' AND state != '收到，订单已结束' AND state != '取消'";
        }
        pstmt = conn.prepareStatement(sql2);
        pstmt.setInt(1, sno);
        rsOrder = pstmt.executeQuery();

        if (!rsOrder.isBeforeFirst()) {
            out.println("<p>没有找到相关订单信息。</p>");
        } else {
%>
<table>
    <thead>
    <tr>
        <th>产品编号</th>
        <th>订单编号</th>
        <th>产品名称</th>
        <th>顾客编号</th>
        <th>订单状态</th>
        <th>购买数量</th>
        <th>价格</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        while (rsOrder.next()) {
            int pno = rsOrder.getInt("pno"); // 产品编号
            String state = rsOrder.getString("state"); // 订单状态
            int sum = rsOrder.getInt("sum"); // 购买数量
            double price=rsOrder.getDouble("price");
            int cno=rsOrder.getInt("cno");
            int ono=rsOrder.getInt("ono");
            String pname="";
            String sql3 = "SELECT * FROM 01product WHERE pno = ?";
            pstmt = conn.prepareStatement(sql3);
            pstmt.setInt(1, pno);
            ResultSet rsProduct = pstmt.executeQuery();
            if (rsProduct.next()) {
                pname = rsProduct.getString("pname"); // 产品名称
            }
    %>
    <tr>
        <td><%= pno %></td>
        <td><%= ono %></td>
        <td><%= pname %></td>
        <td><%= cno %></td>
        <td><%= state %></td>
        <td><%= sum  %></td>
        <td><%= price %></td>
        <td>
            <form action="shopAccountServlet" method="post" style="display:inline;">
                <input type="hidden" name="ono" value="<%= ono %>">
                <input type="submit" value="完成" class="btn">
                <input type="hidden" name=action value="finish" class="btn delete-btn" >
            </form>
            <form action="shopAccountServlet" method="post" style="display:inline;">
                <input type="hidden" name="ono" value="<%= ono %>">
                <input type="submit" value="拒绝" class="btn">
                <input type="hidden" name=action value="reject" class="btn delete-btn" >
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<%
        }
    }
%>

<%
    } catch (Exception e) {
        out.println("数据库错误: " + e.getMessage());
    } finally {
        // 关闭数据库连接
        if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
        if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) {} }
        if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
    }
%>


</body>
</html>
