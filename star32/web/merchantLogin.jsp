<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Merchant Login Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* 视图高度保持满屏 */
        }
        .login-container {
            position: relative; /* 添加相对定位 */
            background-color: white;
            padding: 30px; /* 增加内边距 */
            border-radius: 8px; /* 增加圆角 */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 增加阴影 */
            width: 500px; /* 增加宽度 */
            max-width: 90%; /* 至少享受90%的屏幕宽度 */
        }
        .login-container h2 {
            text-align: center;
            color: #333;
            font-size: 24px; /* 增加字体大小 */
        }
        .input-group {
            margin-bottom: 20px; /* 增加间距 */
        }
        .input-group label {
            display: block;
            margin-bottom: 5px;
        }
        .input-group input {
            width: 100%;
            padding: 12px; /* 增加内边距 */
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 16px; /* 增加字体大小 */
        }
        .input-group button {
            width: 100%;
            padding: 12px; /* 增加内边距 */
            border: none;
            border-radius: 4px;
            background-color: #5cb85c;
            color: white;
            font-size: 16px; /* 增加字体大小 */
            cursor: pointer;
        }
        .input-group button:hover {
            background-color: #4cae4c;
        }
        .register-link {
            text-align: center;
            margin-top: 20px; /* 增加间距 */
        }
        .register-link a {
            text-decoration: none;
            color: #5cb85c;
        }
        .customer-login-button {
            margin-top: 10px; /* 额外的顶部间距 */
            display: block; /* 使按钮在独立行上显示 */
            width: 100%; /* 按钮宽度占满 */
            padding: 10px; /* 添加内边距 */
            background-color: #007bff; /* 背景颜色 */
            color: white; /* 文字颜色 */
            border: none; /* 无边框 */
            border-radius: 4px; /* 圆角 */
            cursor: pointer; /* 手指指针 */
        }
        .customer-login-button:hover {
            background-color: #0056b3; /* 悬停时变化的背景颜色 */
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>商家登录</h2>
    <form action="merchantLoginServlet" method="post">
        <div class="input-group">
            <label for="merchantName">商家名称:</label>
            <input type="text" id="merchantName" name="name" required>
        </div>
        <div class="input-group">
            <label for="merchantPassword">密码:</label>
            <input type="password" id="merchantPassword" name="password" required>
        </div>
        <div class="input-group">
            <button type="submit">登录</button>
            <input type="hidden" name="action" value="login"> <!-- 添加隐藏输入 -->
        </div>
    </form>
    <div class="register-link">
        <p>没有账户吗？ <a href="merchantRegister.jsp">在这里注册</a></p> <!-- 添加商家注册链接 -->
    </div>
    <button class="customer-login-button" onclick="window.location.href='login.jsp';">普通用户登录</button> <!-- 普通用户登录按钮 -->
</div>
</body>
</html>
