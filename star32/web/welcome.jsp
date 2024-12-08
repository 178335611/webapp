<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>欢迎页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .welcome-container {
            position: relative; /* 添加相对定位 */
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        .welcome-container h1 {
            color: #333;
        }
        /* 新增样式用于按钮 */
        .login-button {
            position: absolute; /* 绝对定位，基于.m-container */
            top: 10px; /* 距离顶部10像素 */
            right: 10px; /* 距离右侧10像素 */
            padding: 5px 10px; /* 添加一些内边距 */
            background-color: #007bff; /* 按钮背景颜色 */
            color: white; /* 按钮文字颜色 */
            text-decoration: none; /* 移除下划线 */
            border-radius: 3px; /* 圆角 */
        }
        .login-button:hover {
            background-color: #0056b3; /* 悬停时变化的背景颜色 */
        }
    </style>
</head>
<body>
<div class="welcome-container">
    <a href="login.jsp" class="login-button">登录</a> <!-- 添加登录按钮 -->
    <h1>欢迎来到我们的网站！</h1>
    <p>感谢您的登录。</p>
</div>
</body>
</html>
