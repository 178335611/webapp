import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/tempServlet")
public class tempServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 获取用户输入的消息
        String message = request.getParameter("message");

        // 创建响应内容
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<!DOCTYPE html>")
                .append("<html lang='zh'>")
                .append("<head><meta charset='UTF-8'><title>提交结果</title></head>")
                .append("<body>")
                .append("<h1>接收到的中文信息：</h1>");

        if (message != null && !message.trim().isEmpty()) {
            htmlResponse.append("<p>您输入的信息是：").append(message).append("</p>");
        } else {
            htmlResponse.append("<p>没有输入任何信息。</p>");
        }

        htmlResponse.append("</body></html>");

        // 输出响应
        response.getWriter().write(htmlResponse.toString());
    }
}
