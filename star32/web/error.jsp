<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>错误页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: auto;
            padding: 20px;
            background: white;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #d9534f;
        }
        p {
            color: #333;
        }
        a {
            color: #5bc0de;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>发生错误</h1>
    <p>抱歉，系统出现了一个问题。</p>
    <p>错误信息: <%= request.getAttribute("javax.servlet.error.message") %></p>
    <p>请返回<a href="index.jsp">首页</a>，或联系管理员获得帮助。</p>
</div>

</body>
</html>
