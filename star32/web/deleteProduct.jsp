<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.List" %> <!--替换为实际的Product类包名-->
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
%>

<html>
<head>
    <title>产品列表</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
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
    </style>
</head>
<body>

<h2>产品列表</h2>
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
        <th>价格</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        while (rs.next()) {
            int pno = rs.getInt("pno");
            String pname = rs.getString("pname");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
    %>
    <tr>
        <td><%= pno %></td>
        <td><%= pname %></td>
        <td><%= description %></td>
        <td><%= price %></td>
        <td>
            <form action="deleteProductServlet" method="post" style="display:inline;">
                <input type="hidden" name="pno" value="<%= pno %>">
                <input type="submit" value="delete" class="btn delete-btn">
                <input type="hidden" name="action" value="delete"><!-- 隐藏的action参数，用于区分是删除还是添加?-->
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<a href="addProduct.jsp" class="btn">添加新产品</a> <!-- 返回添加新产品的链接 -->
<a href="warehouse.jsp" class="btn">返回仓库页面</a> <!-- 登出链接 -->
<a href="deleteProduct.jsp" class="btn">删除产品</a> <!-- 返回到删除产品的链接 -->
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
