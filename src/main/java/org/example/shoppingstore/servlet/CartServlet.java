package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.CartDao;
import org.example.shoppingstore.entity.Cart;
import org.example.shoppingstore.entity.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        CartDao dao = new CartDao();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if ("add".equals(action)) {
            int gid = Integer.parseInt(req.getParameter("gid"));
            int buyNum = req.getParameter("buyNum") == null ? 1 : Integer.parseInt(req.getParameter("buyNum"));
            // ✅ 【核心修改】只加入购物车，不查询库存、不扣减库存，库存完全不变
            for (int i = 0; i < buyNum; i++) {
                dao.addCart(user.getUid(), gid);
            }
            resp.sendRedirect("cart?action=list");
        } else if ("list".equals(action)) {
            List<Cart> cartList = dao.findCartByUid(user.getUid());
            req.setAttribute("cartList", cartList);
            req.getRequestDispatcher("cart.jsp").forward(req, resp);
        } else if ("delete".equals(action)) {
            int cid = Integer.parseInt(req.getParameter("cid"));
            dao.deleteCart(cid);
            resp.sendRedirect("cart?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}