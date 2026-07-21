package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.UserDao;
import org.example.shoppingstore.entity.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDao dao = new UserDao();
        User user = dao.login(username, password);
        HttpSession session = req.getSession();

        if (user != null) {
            session.setAttribute("user", user);
            if (user.getType() == 1) {
                resp.sendRedirect("admin/admin_index.jsp"); // 管理员跳后台
            } else {
                resp.sendRedirect("goods?action=list"); // 用户跳前台
            }
        } else {
            req.setAttribute("msg", "用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}