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
        String code = req.getParameter("code");

        // 前端trim
        if (username != null) username = username.trim();

        // 用户名非空
        if (username == null || username.isEmpty()) {
            req.setAttribute("msg", "用户名不能为空");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }
        // 2. 用户名长度限制：16位
        if (username.length() > 16) {
            req.setAttribute("msg", "用户名长度必须小于16位");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 密码校验
        if (password == null || password.trim().isEmpty()) {
            req.setAttribute("msg", "密码不能为空或纯空格");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }
        // 【核心修复】自动去除密码前后空格
        password = password.trim();
        if (password.length() < 6 || password.length() > 20) {
            req.setAttribute("msg", "密码长度必须6-20位");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 手机号格式
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            req.setAttribute("msg", "请输入正确的11位手机号");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 验证码非空
        if (code == null || code.trim().isEmpty()) {
            req.setAttribute("msg", "请输入验证码");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 从session获取验证码和手机号
        String sessionCode = (String) req.getSession().getAttribute("registerCode");
        String sessionPhone = (String) req.getSession().getAttribute("registerPhone");

        // 验证码过期/错误
        if (sessionCode == null || sessionPhone == null) {
            req.setAttribute("msg", "验证码已过期，请重新获取");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }
        if (!sessionPhone.equals(phone)) {
            req.setAttribute("msg", "手机号与获取验证码的不一致");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }
        if (!code.equals(sessionCode)) {
            req.setAttribute("msg", "验证码错误");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 检查用户名是否存在
        UserDao dao = new UserDao();
        if (dao.findUserByUsernameExact(username) != null) {
            req.setAttribute("msg", "用户名已存在");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 密码MD5
        String md5Pwd = md5(password);
        User user = new User(username, md5Pwd);
        user.setPhone(phone);

        // 注册
        int row = dao.register(user);
        if (row > 0) {
            // 注册成功，清除验证码
            req.getSession().removeAttribute("registerCode");
            req.getSession().removeAttribute("registerPhone");

            resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=registerSuccess");
        } else {
            req.setAttribute("msg", "注册失败");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }

    // MD5加密
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