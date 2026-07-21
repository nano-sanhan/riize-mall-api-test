package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet("/sendCode")
public class SendCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        String phone = req.getParameter("phone");
        UserDao dao = new UserDao();

        // 1. 检查手机号是否已注册
        if (dao.isPhoneRegistered(phone)) {
            resp.getWriter().write("exist");
            return;
        }

        // 2. 生成6位验证码
        String code = String.format("%06d", new Random().nextInt(999999));

        // 3. 存入session
        req.getSession().setAttribute("registerCode", code);
        req.getSession().setAttribute("registerPhone", phone);

        // 4. 返回给前端（模拟短信）
        resp.getWriter().write(code);
    }
}