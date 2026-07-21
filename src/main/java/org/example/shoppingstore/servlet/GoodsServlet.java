package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.GoodsDao;
import org.example.shoppingstore.entity.Goods;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/goods")
public class GoodsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        GoodsDao dao = new GoodsDao();

        if ("list".equals(action)) {
            List<Goods> goodsList = dao.findAll();
            req.setAttribute("goodsList", goodsList);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else if ("detail".equals(action)) {
            int gid = Integer.parseInt(req.getParameter("gid"));
            Goods goods = dao.findById(gid);
            req.setAttribute("goods", goods);
            req.getRequestDispatcher("goods_detail.jsp").forward(req, resp);
        } else if ("toAdd".equals(action)) {
            req.getRequestDispatcher("admin/goods_add.jsp").forward(req, resp);
        } else if ("add".equals(action)) {
            String gname = req.getParameter("gname");
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            int stock = Integer.parseInt(req.getParameter("stock"));
            String gdesc = req.getParameter("gdesc");
            String gimg = req.getParameter("gimg"); // ✅ 新增：接收图片名称参数

            Goods goods = new Goods();
            goods.setGname(gname);
            goods.setPrice(price);
            goods.setStock(stock);
            goods.setGdesc(gdesc);
            goods.setGimg(gimg); // ✅ 新增：给商品对象设置图片名称
            dao.addGoods(goods);
            resp.sendRedirect("goods?action=manage");
        } else if ("manage".equals(action)) {
            List<Goods> goodsList = dao.findAll();
            req.setAttribute("goodsList", goodsList);
            req.getRequestDispatcher("admin/goods_manage.jsp").forward(req, resp);
        } else if ("toEdit".equals(action)) {
            int gid = Integer.parseInt(req.getParameter("gid"));
            Goods goods = dao.findById(gid);
            req.setAttribute("goods", goods);
            req.getRequestDispatcher("admin/goods_edit.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            int gid = Integer.parseInt(req.getParameter("gid"));
            String gname = req.getParameter("gname");
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            int stock = Integer.parseInt(req.getParameter("stock"));
            String gdesc = req.getParameter("gdesc");
            String gimg = req.getParameter("gimg"); // ✅ 新增：接收修改后的图片名称参数

            Goods goods = new Goods();
            goods.setGid(gid);
            goods.setGname(gname);
            goods.setPrice(price);
            goods.setStock(stock);
            goods.setGdesc(gdesc);
            goods.setGimg(gimg); // ✅ 新增：给商品对象设置修改后的图片名称
            dao.updateGoods(goods);
            resp.sendRedirect("goods?action=manage");
        } else if ("delete".equals(action)) {
            int gid = Integer.parseInt(req.getParameter("gid"));
            dao.deleteGoods(gid);
            resp.sendRedirect("goods?action=manage");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}