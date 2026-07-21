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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/order") // 注解映射访问路径，前端通过 http://域名/项目名/order 访问
public class OrderServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 统一设置请求参数编码为UTF-8，解决中文参数（如收货地址）乱码问题
        req.setCharacterEncoding("utf-8");

        // 获取前端传递的action参数，区分具体操作类型
        // 示例：order?action=add → 提交订单；order?action=list → 查看个人订单
        String action = req.getParameter("action");

        // 创建订单Dao对象，调用订单数据库操作方法
        OrderDao orderDao = new OrderDao();

        // 获取当前用户会话，用于读取登录用户信息
        HttpSession session = req.getSession();
        // 从会话中获取已登录用户对象（登录时存入session，key为"user"）
        User loginUser = (User) session.getAttribute("user");

        // 创建用户Dao对象，用于查询用户状态（判断是否被禁用）
        UserDao userDao = new UserDao();

        // ========= 分支1：提交订单（action=add）- 核心业务逻辑，含多层拦截 =========
        if ("add".equals(action)) {
            // 第一层拦截：未登录用户禁止提交订单，重定向到登录页
            if (loginUser == null) {
                resp.sendRedirect("login.jsp");
                return; // 终止后续代码执行
            }

            // 第二层拦截：查询用户最新状态，判断是否为【禁用的普通用户】
            // 注：需重新查询用户（避免session中用户状态过期）
            User user = userDao.findUserById(loginUser.getUid());
            // 拦截规则：status=0（禁用） + type=0（普通用户） → 禁止下单
            if(user.getStatus() == 0 && user.getType() == 0){
                // 1. 查询当前用户购物车列表，用于回显到购物车页面
                CartDao cartDao = new CartDao();
                List<Cart> cartList = cartDao.findCartByUid(loginUser.getUid());
                req.setAttribute("cartList", cartList);

                // 2. 设置禁用提示文案（与库存不足提示共用区域，样式统一）
                req.setAttribute("stockError", "<span style='color:#dc3545;font-weight:500;'>您的账号已被禁用，无法提交订单购买商品！</span>");

                // 3. 转发到购物车页面，展示提示信息
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
                return; // 终止后续代码执行
            }

            // ========= 订单参数解析与用户地址更新 =========
            // 解析前端传递的订单总价（去除首尾空格，避免空字符串转换异常）
            BigDecimal totalPrice = new BigDecimal(req.getParameter("totalPrice").trim());
            // 解析前端传递的收货地址
            String address = req.getParameter("address");
            // 更新用户收货地址（将本次下单地址同步到用户表）
            updateUserAddress(loginUser.getUid(), address);

            // ========= 获取选中的购物车商品ID列表 =========
            String[] selectedCids = req.getParameterValues("cid");
            
            // 校验是否有选中商品
            if(selectedCids == null || selectedCids.length == 0){
                CartDao cartDao = new CartDao();
                List<Cart> cartList = cartDao.findCartByUid(loginUser.getUid());
                req.setAttribute("cartList", cartList);
                req.setAttribute("stockError", "<span style='color:#dc3545;font-weight:500;'>请至少选择一件商品！</span>");
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
                return;
            }

            // ========= 构建选中商品的购物车列表 =========
            CartDao cartDao = new CartDao();
            GoodsDao goodsDao = new GoodsDao();
            List<Cart> allCartList = cartDao.findCartByUid(loginUser.getUid());
            List<Cart> selectedCartList = new ArrayList<>();
            
            // 从完整购物车列表中筛选出选中的商品
            for(String cidStr : selectedCids){
                int cid = Integer.parseInt(cidStr);
                for(Cart cart : allCartList){
                    if(cart.getCid() == cid){
                        selectedCartList.add(cart);
                        break;
                    }
                }
            }

            // ========= 库存校验：检查选中商品库存是否充足 =========
            StringBuilder lackStockMsg = new StringBuilder();
            boolean isStockEnough = true;

            // 遍历选中的购物车商品，逐个校验库存
            for (Cart cart : selectedCartList) {
                // 查询商品最新库存
                Goods goods = goodsDao.findById(cart.getGid());
                // 若选购数量 > 商品库存 → 库存不足
                if (cart.getNum() > goods.getStock()) {
                    isStockEnough = false;
                    // 拼接库存不足提示（换行符<br/>适配页面展示）
                    lackStockMsg.append(cart.getGoodsName()).append(" 库存不足！当前库存：").append(goods.getStock()).append("件，您选购了").append(cart.getNum()).append("件<br/>");
                }
            }

            // 若库存不足 → 转发到购物车页面，展示提示信息
            if (!isStockEnough) {
                req.setAttribute("stockError", lackStockMsg.toString());
                req.setAttribute("cartList", allCartList);
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
                return; // 终止后续代码执行
            }

            // ========= 库存扣减：选中商品库存充足，扣减对应库存 =========
            for (Cart cart : selectedCartList) {
                Goods goods = goodsDao.findById(cart.getGid());
                // 扣减库存：商品库存 = 原库存 - 选购数量
                goods.setStock(goods.getStock() - cart.getNum());
                // 更新商品库存到数据库
                goodsDao.updateGoods(goods);
            }

            // ========= 拼接订单商品名称：将选中商品名称用顿号分隔 =========
            StringBuilder goodsNameSb = new StringBuilder();
            for (int i = 0; i < selectedCartList.size(); i++) {
                goodsNameSb.append(selectedCartList.get(i).getGoodsName());
                // 最后一个商品后不加顿号
                if(i < selectedCartList.size()-1){
                    goodsNameSb.append("、");
                }
            }
            String goodsName = goodsNameSb.toString();

            // ========= 创建订单：封装订单信息并入库 =========
            Order order = new Order();
            order.setUid(loginUser.getUid());          // 下单用户ID
            order.setTotalPrice(totalPrice);           // 订单总价
            order.setAddress(address);                 // 收货地址
            order.setGoodsName(goodsName);             // 订单商品名称（拼接后的字符串）
            order.setUsername(loginUser.getUsername());// 下单用户名
            // 调用Dao创建订单（数据库自动生成orderTime和status）
            orderDao.addOrder(order);

            // ========= 清空选中的购物车商品：订单创建成功后，删除已下单的商品 =========
            for(String cidStr : selectedCids){
                int cid = Integer.parseInt(cidStr);
                cartDao.deleteCart(cid);
            }

            // 重定向到个人订单列表页，展示刚创建的订单
            resp.sendRedirect("order?action=list");

            // =========分支2：查看个人订单列表（action=list） =========
        } else if ("list".equals(action)) {
            // 登录校验：未登录用户重定向到登录页
            if(loginUser == null){
                resp.sendRedirect("login.jsp");
                return;
            }
            // 查询当前用户的所有订单
            List<Order> orderList = orderDao.findOrderByUid(loginUser.getUid());
            // 将订单列表存入请求域，供前台订单页渲染
            req.setAttribute("orderList", orderList);
            // 转发到前台订单列表页
            req.getRequestDispatcher("order.jsp").forward(req, resp);

            // =========分支3：后台-订单管理列表（action=manage） =========
        } else if ("manage".equals(action)) {
            // 查询所有订单（关联用户表获取用户名）
            List<Order> orderList = orderDao.findAllOrder();
            // 将订单列表存入请求域，供后台管理页渲染
            req.setAttribute("orderList", orderList);
            // 转发到后台订单管理页
            req.getRequestDispatcher("admin/order_manage.jsp").forward(req, resp);

            // =========分支4：后台-修改订单状态（action=updateStatus） =========
        } else if ("updateStatus".equals(action)) {
            // 解析前端传递的订单ID和新状态
            int oid = Integer.parseInt(req.getParameter("oid"));
            int status = Integer.parseInt(req.getParameter("status"));
            // 调用Dao修改订单状态
            orderDao.updateStatus(oid, status);
            // 重定向到后台订单管理页，刷新数据
            resp.sendRedirect("order?action=manage");

            // =========分支5：前台-删除个人订单（action=delete） =========
        } else if ("delete".equals(action)) {
            // 登录校验：未登录用户重定向到登录页
            if (loginUser == null) {
                resp.sendRedirect("login.jsp");
                return;
            }
            // 解析要删除的订单ID
            int oid = Integer.parseInt(req.getParameter("oid"));
            // 调用Dao删除指定订单
            orderDao.deleteOrderById(oid);
            // 重定向到个人订单列表页，刷新数据
            resp.sendRedirect("order?action=list");
        }
    }

    /**
     * 私有工具方法：更新用户收货地址
     * 核心逻辑：将用户本次下单的收货地址同步更新到user表的address字段

     */
    private void updateUserAddress(Integer uid, String address) {
        // 获取数据库连接
        Connection conn = DBUtil.getConn();
        // SQL：更新用户表的address字段
        String sql = "UPDATE `user` SET address = ? WHERE uid = ?";
        PreparedStatement ps = null;

        try {
            // 创建预编译Statement对象
            ps = conn.prepareStatement(sql);
            // 为占位符赋值：地址 + 用户ID
            ps.setString(1, address);
            ps.setInt(2, uid);
            // 执行更新操作
            ps.executeUpdate();
        } catch (SQLException e) {
            // 捕获SQL异常并打印堆栈（实际项目建议替换为日志记录）
            e.printStackTrace();
        } finally {
            // 释放数据库资源
            DBUtil.close(ps, conn);
        }
    }

    /**
     * 处理所有POST类型的订单请求
     * 设计思路：POST请求逻辑与GET完全一致，直接调用doGet，避免重复编码
     * 应用场景：前端表单提交（如提交订单）使用POST方式时触发
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}