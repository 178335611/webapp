
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%
    // 设置字符编码
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ResultSet rsOrder = null;
    ResultSet rsShop = null;
    ResultSet rsProduct = null;
    session = request.getSession();
    int cno = Integer.parseInt(session.getAttribute("userno").toString()); // 获取用户ID

    String jdbcUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8";
    String dbUser = "root";
    String dbPassword = "123456";

    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

    // 获取用户信息
    String sql = "SELECT cname, cpassword, money, mail FROM 01customer WHERE cno = ?";
    pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, cno);
    rs = pstmt.executeQuery();
%>

<html>
<head>
    <title>用户个人信息</title>
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
    </style>
</head>
<style>
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
</style>
<body>

<div class="header">
    <h2>个人信息</h2>
</div>

<table>
    <%
        if (rs.next()) {
            String cname = rs.getString("cname");
            String mail = rs.getString("mail");
            int money = rs.getInt("money");
            String cpassword = rs.getString("cpassword");
    %>
    <form action="userProfileServlet" method="post">
        <input type="hidden" name="action" value="update">
        <div>
            <label for="cname">账户名称:</label>
            <input type="text" id="cname" name="cname" value="<%= cname %>" required>
        </div>
        <div>
            <label for="cpassword">密码:</label>
            <input type="password" id="cpassword" name="cpassword" value="<%= cpassword %>" required>
        </div>
        <div>
            <label for="money">存款:</label>
            <input type="text" id="money" name="money" value="<%= money %>" required>
        </div>
        <div>
            <label for="mail">mail:</label>
            <input type="text" id="mail" name="mail" value="<%= mail %>" required>
        </div>
        <div>
            <input type="submit" value="更新" class="btn">
        </div>
        <br>
    </form>
    <form action="userProfileServlet" method="post">
        <input type="hidden" name="action" value="delete">
        <input type="submit" value="注销账户" class="btn">
    </form>
    <% } else { %>
    <tr>
        <td colspan="2">用户信息未找到。</td>
    </tr>
    <% } %>
</table>

<h3>操作</h3>
<div style="display: flex; gap: 10px;">
    <a href="shoppingPage.jsp" class="btn">回到购物页面</a>
    <form action="userProfile.jsp" method="get">
        <input type="hidden" name="filter" value="all">
        <input type="submit" value="显示所有订单" class="btn">
        <input type="hidden" name="filter" value="all">
    </form>
    <form action="userProfile.jsp" method="get">
        <input type="hidden" name="filter" value="completed">
        <input type="submit" value="显示已完成订单" class="btn">
        <input type="hidden" name="filter" value="completed">
    </form>
    <form action="userProfile.jsp" method="get">
        <input type="hidden" name="filter" value="rejected">
        <input type="submit" value="显示已拒绝订单" class="btn">
        <input type="hidden" name="filter" value="rejected">
    </form>
    <form action="userProfile.jsp" method="get">
        <input type="hidden" name="filter" value="waiting">
        <input type="submit" value="显示当前订单" class="btn">
        <input type="hidden" name="filter" value=" waiting ">
    </form>
</div>



<%
    // 查询订单信息
    String sql2 = "SELECT * FROM 01order WHERE cno = ?";
    pstmt = conn.prepareStatement(sql2);
    pstmt.setInt(1, cno);
    rsOrder = pstmt.executeQuery();

    if (!rsOrder.isBeforeFirst()) {
        out.println("<p>没有找到相关订单信息。</p>");
    } else {
%>
<br><br>
<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<script>
    alert("<%= message %>");
</script>
<%
    }
    String filter = request.getParameter("filter");
    if (filter == null) {
        filter = "waiting";
    }
    if (true) {
        int sno = Integer.parseInt(session.getAttribute("userno").toString());
        // 根据用户编号查询订单信息
        String sql3 = "SELECT * FROM 01order WHERE cno = ?";
        if ("completed".equals(filter)) {
            sql3 += " AND state = '收到,订单已结束'";
        } else if ("rejected".equals(filter)) {
            sql3 += " AND state = '取消' or state = '商家已拒绝'";
        } else if ("waiting".equals(filter)) {
            sql3 += " AND state != '取消' AND state != '收到,订单已结束' AND state != '商家已拒绝'";
        }
        pstmt = conn.prepareStatement(sql3);
        pstmt.setInt(1, cno);
        rsOrder = pstmt.executeQuery();

        if (!rsOrder.isBeforeFirst()) {
            out.println("<p>没有找到相关订单信息。</p>");
        } else {
%>
<table>
    <thead>
    <tr>
        <th>商品名称</th>
        <th>类型</th>
        <th>商家</th>
        <th>描述</th>
        <th>价格</th>
        <th>购买数量</th>
        <th>订单状态</th>
        <th>订单编号</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        while (rsOrder.next()) {
            int pno = rsOrder.getInt("pno"); // 产品编号
            String state = rsOrder.getString("state"); // 订单状态
            int sum=rsOrder.getInt("sum");
            int ono = rsOrder.getInt("ono");

            String shopName = "";
            PreparedStatement stmtShop = conn.prepareStatement("SELECT sname FROM 01shop WHERE sno = ?");
            stmtShop.setInt(1, sno);
            rsShop = stmtShop.executeQuery();
            if (rsShop.next()) {
                shopName = rsShop.getString("sname");
            }
            rsShop.close();
            stmtShop.close();

            String pname = "";
            String description = "";
            String type = "";
            double price = 0.0;
            PreparedStatement stmtProduct = conn.prepareStatement("SELECT pname, description, type, price,sum FROM 01product WHERE pno = ?");
            stmtProduct.setInt(1, pno);
            rsProduct = stmtProduct.executeQuery();
            if (rsProduct.next()) {
                pname = rsProduct.getString("pname");
                description = rsProduct.getString("description");
                type = rsProduct.getString("type");
                price = rsProduct.getDouble("price");
            }
            rsProduct.close();
            stmtProduct.close();
    %>
    <tr>
        <td><%= pname %></td>
        <td><%= type %></td>
        <td><%= shopName %></td>
        <td><%= description %></td>
        <td><%= price %></td>
        <td><%= sum  %></td>
        <td><%= state %></td>
        <td><%= rsOrder.getInt("ono") %></td>
        <td>
            <form action="userProfileServlet" method="post" style="display:inline;">
                <input type="hidden" name="ono" value="<%= ono %>">
                <input type="submit" value="收到" class="btn">
                <input type="hidden" name=action value="receive" class="btn delete-btn" >
            </form>
            <form action="userProfileServlet" method="post" style="display:inline;">
                <input type="hidden" name="ono" value="<%= ono %>">
                <input type="submit" value="取消或删除失效订单" class="btn">
                <input type="hidden" name=action value="reject" class="btn delete-btn" >
            </form>
            <form action="userProfileServlet" method="post" style="display:inline;">
                <input type="hidden" name="ono" value="<%= ono %>">
                <input type="submit" value="支付订单" class="btn">
                <input type="hidden" name=action value="pay" class="btn delete-btn" >
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
%>
</body>
</html>
<%
    // 关闭所有资源
    rsOrder.close();
    rs.close();
    pstmt.close();
    conn.close();}}
%>