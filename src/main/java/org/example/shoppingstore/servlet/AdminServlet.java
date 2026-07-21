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

@WebServlet("/admin") // 注解映射访问路径，管理员后台请求统一入口为 /admin
public class AdminServlet extends HttpServlet {

    /**
     * 处理GET类型的后台请求（核心业务分发逻辑）
     * @param req  HttpServletRequest：封装请求参数、请求信息等
     * @param resp HttpServletResponse：用于页面跳转、数据响应
     * @throws ServletException Servlet执行异常（如页面转发失败）
     * @throws IOException      IO操作异常（如页面重定向失败）
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 设置请求参数编码为UTF-8，解决中文搜索关键词/数据乱码问题（控制层代码：参数预处理）
        req.setCharacterEncoding("utf-8");
        // 2. 设置响应内容类型和编码，保证后台页面中文展示无乱码（控制层代码：响应预处理）
        resp.setContentType("text/html;charset=utf-8");

        // 3. 获取前端传递的action参数，区分具体后台功能（控制层代码：参数接收）
        String action = req.getParameter("action");

        // 4. 初始化各功能对应的Dao对象（数据访问层）
        UserDao dao = new UserDao();         // 用户相关操作Dao
        GoodsDao goodsDao = new GoodsDao(); // 商品相关操作Dao
        OrderDao orderDao = new OrderDao(); // 订单相关操作Dao

        // ============ 分支1：用户管理（展示所有普通用户） ============
        if ("userManage".equals(action)) {
            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：查询所有普通用户
            // 归属：应抽离到 AdminService.findAllNormalUser() 方法中
            // 业务规则：仅查询type=0的普通用户，排除管理员（type=1）
            List<User> userList = dao.findAllUser();
            // ==================== 【逻辑业务层代码结束】====================

            // 将用户列表存入request域，供user_manage.jsp页面渲染（控制层代码：数据回显）
            req.setAttribute("userList", userList);
            // 请求转发到用户管理页面（保留request数据，展示用户列表）（控制层代码：页面跳转）
            req.getRequestDispatcher("admin/user_manage.jsp").forward(req, resp);
        }
        // ============ 分支2：搜索用户（按用户名模糊匹配） ============
        else if ("searchUser".equals(action)) {
            // 解析前端搜索框提交的用户名关键词（控制层代码：参数接收）
            String username = req.getParameter("username");
            List<User> userList;

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：按用户名模糊搜索普通用户
            // 归属：应抽离到 AdminService.searchNormalUserByUsername(String username) 方法中
            // 业务规则：
            // 1. 关键词为空/仅空格 → 查询全部普通用户
            // 2. 关键词非空 → 去除首尾空格后模糊匹配用户名
            // 3. 仅返回type=0的普通用户
            if (username == null || username.trim().isEmpty()) {
                userList = dao.findAllUser();
            } else {
                // 去除关键词首尾空格，避免无效匹配
                userList = dao.findUserByUsername(username.trim());
            }
            // ==================== 【逻辑业务层代码结束】====================

            // 将搜索结果存入request域，回显到用户管理页面（控制层代码：数据回显）
            req.setAttribute("userList", userList);
            // 转发回用户管理页面，展示搜索结果（控制层代码：页面跳转）
            req.getRequestDispatcher("admin/user_manage.jsp").forward(req, resp);
        }
        // ============ 分支3：数据统计（核心运营数据展示） ============
        else if ("dataStat".equals(action)) {
            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：统计后台核心运营数据
            // 归属：应抽离到 AdminService.getAdminDataStatistics() 方法中
            // 业务规则：
            // 1. 统计商品总数、普通用户总数、订单总数
            // 2. 统计销量TOP3商品（按销量降序排列，取前3条）
            // 3. 返回多维度统计数据，供前端运营看板展示
            int goodsCount = goodsDao.countAllGoods();       // 商品总数
            int userCount = dao.countAllUser();              // 普通用户总数
            int orderCount = orderDao.countAllOrder();       // 订单总数
            List<Map<String, Object>> topGoodsList = orderDao.getTop3GoodsBySale(); // 销量TOP3商品
            // ==================== 【逻辑业务层代码结束】====================

            // 将所有统计数据存入request域，供数据统计页面渲染（控制层代码：数据回显）
            req.setAttribute("goodsCount", goodsCount);
            req.setAttribute("userCount", userCount);
            req.setAttribute("orderCount", orderCount);
            req.setAttribute("topGoodsList", topGoodsList);

            // 转发到数据统计页面，展示运营数据（控制层代码：页面跳转）
            req.getRequestDispatcher("admin/data_stat.jsp").forward(req, resp);
        }
        // ============ 分支4：修改用户状态（启用/禁用） ============
        else if ("updateUserStatus".equals(action)) {
            // 解析前端传递的参数：用户ID（主键） + 要修改的状态（0=禁用，1=正常）（控制层代码：参数接收）
            Integer uid = Integer.parseInt(req.getParameter("uid"));
            Integer status = Integer.parseInt(req.getParameter("status"));

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：修改用户状态（启用/禁用）
            // 归属：应抽离到 AdminService.updateUserStatus(Integer uid, Integer status) 方法中
            // 业务规则：
            // 1. status=0：禁用用户（禁止下单/登录）
            // 2. status=1：启用用户（恢复正常权限）
            // 3. 仅修改普通用户（type=0）状态，管理员状态不可修改
            dao.updateUserStatus(uid, status);
            // ==================== 【逻辑业务层代码结束】====================

            // 修改完成后重定向回用户管理页面（刷新最新用户状态）（控制层代码：页面跳转）
            // req.getContextPath()：获取项目根路径，避免硬编码路径部署异常
            resp.sendRedirect(req.getContextPath() + "/admin?action=userManage");
        }
    }

    /**
     * 处理POST类型的后台请求
     * 设计思路：直接调用doGet方法，让POST请求复用GET的核心业务分发逻辑，兼容不同请求方式
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Servlet执行异常
     * @throws IOException      IO操作异常
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}