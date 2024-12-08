<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%
    // 设置字符编码
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        String jdbcUrl = "jdbc:mysql://localhost:3306/mysql"; // 替换为你的数据库名称
        String dbUser = "root"; // 数据库用户名
        String dbPassword = "123456"; // 数据库密码

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        Integer userno = (Integer) session.getAttribute("userno");
        String sql = "SELECT * FROM 01product WHERE sno = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userno); // 将userno作为参数传入
        rs = pstmt.executeQuery();

        String sql2 = "SELECT sname FROM 01shop WHERE sno = ?";
        PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        pstmt2.setInt(1, userno);
        ResultSet rs2 = pstmt2.executeQuery();
        String username = null;
        if (rs2.next()) {
            username = rs2.getString("sname");
        }
        rs2.close();
        pstmt2.close();
%>

<html>
<head>
    <title>仓库物品列表</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #5cb85c;
            color: white;
        }
        .btn {
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            background-color: #007bff; /* 按钮颜色 */
            color: white;
            text-decoration: none;
        }
        .btn:hover {
            background-color: #0056b3; /* 悬停颜色 */
        }
        .delete-btn {
            background-color: #d9534f; /* 删除按钮颜色 */
        }
        .delete-btn:hover {
            background-color: #c9302c; /* 删除按钮悬停颜色 */
        }
        .edit-input {
            width: 50px; /* 调整输入框的宽度 */
        }
    </style>
</head>
<body>

<div class="header">
    <h2>仓库物品列表</h2>
    <div>
        <span>欢迎您，商家 </span>
        <strong><%= username != null ? username : "未知用户" %></strong>
        <a href="shopAccount.jsp" class="btn">账号页面</a>
    </div>
</div>

<div>
    <a href="addProduct.jsp" class="btn">新增商品</a> <!-- 新增商品按钮 -->
    <form action="deleteProductServlet" method="post" style="display:inline;">
        <input type="submit" value="删除商品" class="btn delete-btn">
        <input type="hidden" name="action" value="delete"> <!-- 隐藏域，传递用户编号 -->
    </form>
</div>

<%
    String message = request.getParameter("message");
    if (message != null && !message.isEmpty()) {
%>
<script>
    alert("<%= message %>"); // 使用 alert 弹出消息
</script>
<%
    }
%>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>名称</th>
        <th>描述</th>
        <th>类型</th>
        <th>价格</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
<% while (rs.next()) {
    int pno = rs.getInt("pno"); // 实际的列名
    String pname = rs.getString("pname"); // 实际的列名
    String description = rs.getString("description"); // 实际的列名
    String type = rs.getString("type"); // 实际的列名
    double price = rs.getDouble("price"); // 实际的列名
%>
<tr>
    <td><%= pno %></td>
    <!-- 显示商品名称这样写就可以输入了，request也能读到 -->
    <form action="warehouseServlet" method="post">
        <input type="hidden" name="pno" value="<%= pno %>">
        <td><input type="text" name="pname" value="<%= pname %>" required></td>
        <td><input type="text" name="description" value="<%= description %>" required></td>
        <td><input type="text" name="type" value="<%= type %>" required></td>
        <td><input type="number" name="price" value="<%= price %>" step="0.01" required></td>
        <td>
            <input type="hidden" name="action" value="update">
            <input type="submit" value="修改">
        </td>
    </form>

    <td>
    </td>
</tr>
<% } %>
</tbody>

</table>

<a href="merchantLogin.jsp" class="btn">登出</a> <!-- 登出链接 -->
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