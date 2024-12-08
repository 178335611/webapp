<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %> <!-- 引入ArrayList接口 -->
<%@ page import="java.util.List" %> <!-- 引入List接口 -->
<%@ page import="javax.servlet.*" %> <!-- 引入Servlet接口 -->
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
    <title>添加产品</title>
</head>
<body>
    <h2>添加新产品</h2>
    <form action="addProductServlet" method="post">

        <label for="pname">产品名称:</label>
        <input type="text" id="pname" name="pname" required><br>

        <label for="description">产品描述:</label>
        <input type="text" id="description" name="description" required><br>

        <label for="type">产品种类:</label>
        <input type="text" id="type" name="type" required><br>

        <label for="price">产品价格:</label>
        <input type="number" id="price" name="price" step="0.01" required><br>

        <div class="input-group">
            <button type="submit">添加</button>
            <input type="hidden" name="action" value="add"> <!-- 添加隐藏输入 -->
        <div class="input-group">
            <button type="button"
            onclick="window.location.href='warehouse.jsp'">返回</button>
        </div>
        </div>
    </form>
</body>
</html>
