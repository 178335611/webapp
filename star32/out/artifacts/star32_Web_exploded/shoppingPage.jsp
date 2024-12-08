<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%
    // 设置字符编码
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    Connection conn2 = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs2 = null;

    Integer cno = (Integer) request.getSession().getAttribute("userno");
    if (cno == null) {
        request.setAttribute("message", "用户未登录，请先登录。");
        request.getRequestDispatcher("login.jsp").forward(request, response);
        return;
    }
    try {
        String jdbcUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8"; // 替换为你的数据库名称
        String dbUser = "root"; // 数据库用户名
        String dbPassword = "123456"; // 数据库密码
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

        String sql = "SELECT p.*, s.sname FROM 01product p JOIN 01shop s ON p.sno = s.sno"; // 查询所有产品和对应商家
        String filter = request.getParameter("filter"); // 获取过滤条件
        String keyword = request.getParameter("keyword"); // 获取搜索关键词

        // 检查过滤条件是否为 pname，并且关键词非空
        if (filter != null && filter.equals("pname") && keyword != null && !keyword.trim().isEmpty()) {
            sql =sql+ " WHERE p.pname = ?"; // 添加 WHERE 子句
            System.out.println(sql);
        }
// 如果需要过滤条件，就设置参数
        pstmt = conn.prepareStatement(sql);
        if (filter != null && filter.equals("pname") && keyword != null && !keyword.trim().isEmpty()) {
            pstmt.setString(1, keyword); // 将用户输入的产品名设置为参数
        }
        System.out.println(sql);
        // 准备 SQL 语句

        rs = pstmt.executeQuery(); // 执行查询

        conn2 = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        String sql2 = "SELECT * FROM 01customer WHERE cno =?"; // 查询所有产品和对应商家

        pstmt2 = conn2.prepareStatement(sql2);
        pstmt2.setInt(1, cno);//这里忘记补2
        rs2 = pstmt2.executeQuery();
        rs2.next();//必须要有next，移动到结果集的第一行
        String cname = rs2.getString("cname"); // 获取当前登录

%>

<html>
<head>
    <title>用户商城</title>
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
    <h2>商城商品列表</h2>
    <div>
        <div>
            <span>欢迎您，尊敬的顾客 </span>
            <strong><%= cname != null ? cname : "未知用户" %></strong>
            <a href="userProfile.jsp" class="btn">账号页面</a>
        </div>
    </div>
</div>

<h2>搜索商品</h2>
<form action="shoppingPage.jsp" method="get">
    <input type="text" name="keyword" placeholder="搜索产品名" >
    <input type="submit" value="搜索">
    <input type="hidden" name="filter" value="pname">
</form>

<table>
    <thead>
    <tr>
        <th>产品名称</th>
        <th>描述</th>
        <th>种类</th>
        <th>商家</th>
        <th>价格</th>
        <th>购买数量</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        while (rs.next()) {
            int pno = rs.getInt("pno"); // 产品编号
            String pname = rs.getString("pname"); // 产品名称
            String description = rs.getString("description"); // 描述
            String type = rs.getString("type"); // 类型
            double price = rs.getDouble("price"); // 价格
            String shopName = rs.getString("sname"); // 商家名称
            int sno = rs.getInt("sno"); // 商家编号
    %>
    <tr>
        <td><%= pname %></td>
        <td><%= description %></td>
        <td><%= type %></td>
        <td><%= shopName %></td>
        <td><%= price %></td>

        <form action="shoppingPageServlet" method="post" style="display:inline;">
            <td><input type="hidden" name="productId" value="<%= pno %>">
            <input type="number" name="quantity" min="1" required style="width: 50px;"></td>
            <td><input type="submit" value="下单" class="btn" >
                <input type="hidden" name="action" value="order">
                <input type="hidden" name="sno" value="<%= sno %>">
                <input type="hidden" name="price" value="<%= price %>" ></td>
        </form>

    </tr>
    <% } %>
    </tbody>
</table>

<a href="login.jsp" class="btn">登出</a> <!-- 登出链接 -->
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
