package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.UserDao;
import org.example.shoppingstore.dao.GoodsDao;
import org.example.shoppingstore.dao.OrderDao;
import org.example.shoppingstore.entity.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        String action = req.getParameter("action");
        UserDao dao = new UserDao();
        GoodsDao goodsDao = new GoodsDao();
        OrderDao orderDao = new OrderDao();
        if ("userManage".equals(action)) {
            List<User> userList = dao.findAllUser();
            req.setAttribute("userList", userList);
            req.getRequestDispatcher("admin/user_manage.jsp").forward(req, resp);
        }// ✅ 只新增这个分支：处理搜索用户请求
        else if ("searchUser".equals(action)) {
            String username = req.getParameter("username");
            List<User> userList;
            // 搜索框为空 → 查询全部用户；有内容 → 搜索匹配用户
            if (username == null || username.trim().isEmpty()) {
                userList = dao.findAllUser();
            } else {
                userList = dao.findUserByUsername(username.trim());
            }
            req.setAttribute("userList", userList);
            req.getRequestDispatcher("admin/user_manage.jsp").forward(req, resp);
        }
        // 新增：数据统计分支
        else if ("dataStat".equals(action)) {
            // 1. 统计商品总数
            int goodsCount = goodsDao.countAllGoods();
            // 2. 统计普通用户总数
            int userCount = dao.countAllUser();
            // 3. 统计订单总数
            int orderCount = orderDao.countAllOrder();
            // 4. 统计销量TOP3商品
            List<Map<String, Object>> topGoodsList = orderDao.getTop3GoodsBySale();

            // 把统计数据传到页面
            req.setAttribute("goodsCount", goodsCount);
            req.setAttribute("userCount", userCount);
            req.setAttribute("orderCount", orderCount);
            req.setAttribute("topGoodsList", topGoodsList);
            req.getRequestDispatcher("admin/data_stat.jsp").forward(req, resp);
        }else if ("updateUserStatus".equals(action)) {
            // 获取参数：用户ID + 要修改的状态
            Integer uid = Integer.parseInt(req.getParameter("uid"));
            Integer status = Integer.parseInt(req.getParameter("status"));
            // 调用dao修改状态
            dao.updateUserStatus(uid, status);
            // 修改完成后，重定向回用户管理页面，刷新数据
            resp.sendRedirect(req.getContextPath() + "/admin?action=userManage");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}