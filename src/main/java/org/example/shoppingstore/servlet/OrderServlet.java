package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.OrderDao;
import org.example.shoppingstore.dao.CartDao;
import org.example.shoppingstore.dao.GoodsDao;
import org.example.shoppingstore.dao.UserDao;
import org.example.shoppingstore.entity.Order;
import org.example.shoppingstore.entity.User;
import org.example.shoppingstore.entity.Cart;
import org.example.shoppingstore.entity.Goods;
import org.example.shoppingstore.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        OrderDao orderDao = new OrderDao();
        HttpSession session = req.getSession();
        User loginUser = (User) session.getAttribute("user");
        UserDao userDao = new UserDao();

        // =========✅ 1. 只对【提交订单(add)】做拦截，其他操作(list/delete/manage)全部放行 =========
        if ("add".equals(action)) {
            if (loginUser == null) {
                resp.sendRedirect("login.jsp");
                return;
            }
            // =========✅ 核心：查询用户状态，禁用用户(status=0+普通用户type=0)拦截提交订单 =========
            User user = userDao.findUserById(loginUser.getUid());
            if(user.getStatus() == 0 && user.getType() == 0){
                // 拿到购物车列表，回显到购物车页面
                CartDao cartDao = new CartDao();
                List<Cart> cartList = cartDao.findCartByUid(loginUser.getUid());
                req.setAttribute("cartList", cartList);
                // 禁用提示文案，和库存不足提示同区域显示，样式统一醒目
                req.setAttribute("stockError", "<span style='color:#dc3545;font-weight:500;'>您的账号已被禁用，无法提交订单购买商品！</span>");
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
                return;
            }

            BigDecimal totalPrice = new BigDecimal(req.getParameter("totalPrice").trim());
            String address = req.getParameter("address");
            updateUserAddress(loginUser.getUid(), address);

            CartDao cartDao = new CartDao();
            GoodsDao goodsDao = new GoodsDao();
            List<Cart> cartList = cartDao.findCartByUid(loginUser.getUid());

            StringBuilder lackStockMsg = new StringBuilder();
            boolean isStockEnough = true;
            for (Cart cart : cartList) {
                Goods goods = goodsDao.findById(cart.getGid());
                if (cart.getNum() > goods.getStock()) {
                    isStockEnough = false;
                    lackStockMsg.append(cart.getGoodsName()).append(" 库存不足！当前库存：").append(goods.getStock()).append("件，您选购了").append(cart.getNum()).append("件<br/>");
                }
            }
            if (!isStockEnough) {
                req.setAttribute("stockError", lackStockMsg.toString());
                req.setAttribute("cartList", cartList);
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
                return;
            }

            for (Cart cart : cartList) {
                Goods goods = goodsDao.findById(cart.getGid());
                goods.setStock(goods.getStock() - cart.getNum());
                goodsDao.updateGoods(goods);
            }

            StringBuilder goodsNameSb = new StringBuilder();
            for (int i = 0; i < cartList.size(); i++) {
                goodsNameSb.append(cartList.get(i).getGoodsName());
                if(i < cartList.size()-1){
                    goodsNameSb.append("、");
                }
            }
            String goodsName = goodsNameSb.toString();

            Order order = new Order();
            order.setUid(loginUser.getUid());
            order.setTotalPrice(totalPrice);
            order.setAddress(address);
            order.setGoodsName(goodsName);
            order.setUsername(loginUser.getUsername());
            orderDao.addOrder(order);

            cartDao.deleteCartByUid(loginUser.getUid());
            resp.sendRedirect("order?action=list");
        }
        // =========✅ 2. 查看订单、删除订单、订单管理 全部放行，无任何限制 =========
        else if ("list".equals(action)) {
            if(loginUser == null){resp.sendRedirect("login.jsp");return;}
            List<Order> orderList = orderDao.findOrderByUid(loginUser.getUid());
            req.setAttribute("orderList", orderList);
            req.getRequestDispatcher("order.jsp").forward(req, resp);
        } else if ("manage".equals(action)) {
            List<Order> orderList = orderDao.findAllOrder();
            req.setAttribute("orderList", orderList);
            req.getRequestDispatcher("admin/order_manage.jsp").forward(req, resp);
        } else if ("updateStatus".equals(action)) {
            int oid = Integer.parseInt(req.getParameter("oid"));
            int status = Integer.parseInt(req.getParameter("status"));
            orderDao.updateStatus(oid, status);
            resp.sendRedirect("order?action=manage");
        }else if ("delete".equals(action)) {
            if (loginUser == null) {resp.sendRedirect("login.jsp");return;}
            int oid = Integer.parseInt(req.getParameter("oid"));
            orderDao.deleteOrderById(oid);
            resp.sendRedirect("order?action=list");
        }
    }

    private void updateUserAddress(Integer uid, String address) {
        Connection conn = DBUtil.getConn();
        String sql = "UPDATE `user` SET address = ? WHERE uid = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, address);
            ps.setInt(2, uid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}