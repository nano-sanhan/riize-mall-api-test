package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.UserDao;
import org.example.shoppingstore.entity.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        User user = new User(username, password);
        user.setPhone(phone);
        UserDao dao = new UserDao();
        int row = dao.register(user);

        if (row > 0) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=registerSuccess");
        } else {
            req.setAttribute("msg", "注册失败，用户名已存在");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}