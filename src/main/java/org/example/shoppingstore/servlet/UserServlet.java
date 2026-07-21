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

//注解映射地址：/user  对应首页的链接 /user?action=center
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //解决中文乱码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        //获取请求参数 action
        String action = req.getParameter("action");
        if ("center".equals(action)) {
            //跳转到【用户中心】的核心逻辑
            this.toUserCenter(req, resp);
        } else if ("update".equals(action)) {
            // ============✅ 新增这行核心判断，处理修改信息请求 ============
            this.updateUserInfo(req, resp);
        }
    }

    //跳转到用户中心页面
    private void toUserCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取当前登录的用户session
        HttpSession session = req.getSession();
        User loginUser = (User) session.getAttribute("user");

        //2. 未登录则跳转到登录页，防止非法访问
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        //3. 根据登录用户的ID，查询完整的用户信息（地址、注册时间等）
        User user = userDao.findUserById(loginUser.getUid());

        //4. 把完整用户信息存入request，传到用户中心页面展示
        req.setAttribute("user", user);

        //5. 跳转到用户中心JSP页面
        req.getRequestDispatcher("/user_center.jsp").forward(req, resp);
    }

    private void updateUserInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //1. 获取表单提交的参数
            Integer uid = Integer.parseInt(req.getParameter("uid"));
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");

            //2. 调用Dao层修改方法
            int row = userDao.updatePhoneAndAddress(uid, phone, address);

            //3. 判断修改结果，封装提示信息
            if (row > 0) {
                req.setAttribute("successMsg", "修改成功！信息已更新");
            } else {
                req.setAttribute("errorMsg", "修改失败，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "修改失败：" + e.getMessage());
        } finally {
            //4. 重新查询用户最新信息，回显到页面（从session获取uid，避免重复传参）
            User loginUser = (User) req.getSession().getAttribute("user");
            User user = userDao.findUserById(loginUser.getUid());
            req.setAttribute("user", user);

            //5. 跳转回用户中心页面
            req.getRequestDispatcher("/user_center.jsp").forward(req, resp);
        }
    }
}