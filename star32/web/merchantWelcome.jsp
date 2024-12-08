<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商家欢迎页面</title>
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
        .welcome-container {
            background-color: white;
            padding: 30px; /* 增加内边距 */
            border-radius: 8px; /* 圆角 */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
            width: 400px; /* 宽度 */
            text-align: center; /* 居中对齐内容 */
        }
        .welcome-container h1 {
            color: #333; /* 标题颜色 */
            font-size: 28px; /* 标题字体大小 */
        }
        .welcome-container p {
            color: #666; /* 段落颜色 */
            font-size: 16px; /* 段落字体大小 */
        }
        .logout-button {
            display: block; /* 按钮占一行 */
            margin-top: 20px; /* 顶部间距 */
            width: 100%; /* 按钮宽度 */
            padding: 10px; /* 内边距 */
            background-color: #d9534f; /* 背景颜色 */
            color: white; /* 文字颜色 */
            border: none; /* 无边框 */
            border-radius: 4px; /* 圆角 */
            cursor: pointer; /* 手指指针 */
            text-decoration: none; /* 移除下划线 */
            font-size: 16px; /* 字体大小 */
        }
        .logout-button:hover {
            background-color: #c9302c; /* 悬停时变化的背景颜色 */
        }
    </style>
</head>
<body>
<div class="welcome-container">
    <h1>欢迎商家!</h1>
    <p>感谢您加入我们的平台。您现在可以开始管理您的业务和订单。</p>
    <a href="merchantLogin.jsp" class="logout-button">登出</a> <!-- 登出按钮，返回商家登录页面 -->
</div>
</body>
</html>
