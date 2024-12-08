<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>中文测试</title>
</head>
<body>
<h1>接收到的中文信息：</h1>

<!-- 表单部分 -->
<form action="tempServlet" method="post"> <!-- 修改这里，将表单提交到 Servlet -->
    <label for="message">请输入中文信息：</label>
    <input type="text" id="message" name="message" required>
    <input type="submit" value="提交">
</form>

<!-- 原有的结果显示部分可以删除 -->
</body>
</html>
