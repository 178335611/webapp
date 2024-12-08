<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Merchant Register Page</title>
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
        .register-container {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 300px;
        }
        .register-container h2 {
            text-align: center;
            color: #333;
        }
        .input-group {
            margin-bottom: 15px;
        }
        .input-group label {
            display: block;
            margin-bottom: 5px;
        }
        .input-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .input-group button {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 4px;
            background-color: #5cb85c;
            color: white;
            cursor: pointer;
        }
        .input-group button:hover {
            background-color: #4cae4c;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h2>商家注册</h2>
    <form action="merchantLoginServlet" method="post"> <!-- 指定表单动作 -->
        <input type="hidden" name="action" value="register"> <!-- 指定表单动作 -->
        <div class="input-group">
            <label for="name">商家名称:</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div class="input-group">
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="input-group">
            <button type="submit">注册</button>
            <input type="hidden" name="action" value="register">
        </div>
        <div class="input-group">
            <button type="submit">返回商家登录</button>
            <input type="hidden" name="action" value="backlogin">
        </div>
    </form>
</div>
</body>
</html>
