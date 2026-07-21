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
    private static final int MAX_LOGIN_TIMES = 5;
    private static final long LOCK_TIME = 10 * 60 * 1000;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        HttpSession session = req.getSession();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        password = password.trim();
        if (username != null) {
            username = username.trim();
        }

        if (session.getAttribute("lockTime") != null) {
            long lockTime = (long) session.getAttribute("lockTime");
            if (System.currentTimeMillis() < lockTime) {
                session.setAttribute("msg", "尝试次数过多，请10分钟后再试！");
                resp.sendRedirect("login.jsp");
                return;
            } else {
                session.removeAttribute("lockTime");
                session.removeAttribute("loginFailCount");
            }
        }

        if (username == null || username.isEmpty()) {
            session.setAttribute("msg", "用户名不能为空");
            resp.sendRedirect("login.jsp");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            session.setAttribute("msg", "密码不能为空");
            resp.sendRedirect("login.jsp");
            return;
        }

        if (username.length() < 2 || username.length() > 20) {
            session.setAttribute("msg", "用户名长度超出限制（2-20位）");
            resp.sendRedirect("login.jsp");
            return;
        }

        if (password.length() < 6 || password.length() > 20) {
            session.setAttribute("msg", "密码长度超出限制（6-20位）");
            resp.sendRedirect("login.jsp");
            return;
        }

        String md5Pwd = md5(password);
        UserDao dao = new UserDao();
        User user = dao.login(username, md5Pwd);

        if (user != null) {
            session.removeAttribute("loginFailCount");
            session.setAttribute("user", user);
            if (user.getType() == 1) {
                resp.sendRedirect("admin/admin_index.jsp");
            } else {
                resp.sendRedirect("goods?action=list");
            }
        } else {
            int failCount = 1;
            if (session.getAttribute("loginFailCount") != null) {
                failCount = (int) session.getAttribute("loginFailCount") + 1;
            }
            session.setAttribute("loginFailCount", failCount);

            if (failCount >= MAX_LOGIN_TIMES) {
                session.setAttribute("lockTime", System.currentTimeMillis() + LOCK_TIME);
                session.setAttribute("msg", "尝试次数过多，请10分钟后再试！");
            } else {
                session.setAttribute("msg", "用户名或密码错误，已失败 " + failCount + "/" + MAX_LOGIN_TIMES);
            }
            resp.sendRedirect("login.jsp");
        }
    }

    private String md5(String s) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(s.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}