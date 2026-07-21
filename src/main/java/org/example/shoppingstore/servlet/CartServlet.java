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
        resp.setContentType("text/html;charset=utf-8");

        String action = req.getParameter("action");
        CartDao dao = new CartDao();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // 未登录 → 跳转到登录页（修复跳转失败）
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if ("add".equals(action)) {
            int gid = Integer.parseInt(req.getParameter("gid"));
            int buyNum = req.getParameter("buyNum") == null ? 1 : Integer.parseInt(req.getParameter("buyNum"));
            for (int i = 0; i < buyNum; i++) {
                dao.addCart(user.getUid(), gid);
            }
            resp.sendRedirect(req.getContextPath() + "/cart?action=list");

        } else if ("list".equals(action)) {
            List<Cart> cartList = dao.findCartByUid(user.getUid());
            req.setAttribute("cartList", cartList);
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);

        } else if ("delete".equals(action)) {
            int cid = Integer.parseInt(req.getParameter("cid"));
            dao.deleteCart(cid);
            resp.sendRedirect(req.getContextPath() + "/cart?action=list");

        } else if ("update".equals(action)) {
            int cid = Integer.parseInt(req.getParameter("cid"));
            int num = Integer.parseInt(req.getParameter("num"));

            Cart cart = dao.findCartByCid(cid);
            if (cart != null && cart.getGoods() != null) {
                int stock = cart.getGoods().getStock();
                if (num >= 1 && num <= stock) {
                    dao.updateCartNum(cid, num);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/cart?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}