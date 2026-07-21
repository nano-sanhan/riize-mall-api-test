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


@WebServlet("/user")
public class UserServlet extends HttpServlet {
    // 初始化UserDao对象（全局复用，避免多次创建）
    private UserDao userDao = new UserDao();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 全局解决中文乱码：
        // 1. 设置请求参数编码为UTF-8（处理前端提交的中文参数，如地址）
        req.setCharacterEncoding("UTF-8");
        // 2. 设置响应内容类型和编码（保证页面展示中文无乱码）
        resp.setContentType("text/html;charset=UTF-8");

        // 获取前端传递的action参数，区分具体操作类型
        String action = req.getParameter("action");

        // 请求分发：根据action调用对应处理方法
        if ("center".equals(action)) {
            // 跳转到用户中心页面（带权限校验）
            this.toUserCenter(req, resp);
        } else if ("update".equals(action)) {
            // ============处理用户信息修改请求（核心新增逻辑） ============
            this.updateUserInfo(req, resp);
        }
    }

    /**
     * 私有方法：跳转到用户中心页面（核心逻辑：权限校验 + 数据回显）
     */
    private void toUserCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取当前用户会话，读取登录状态
        HttpSession session = req.getSession();
        // 从session中获取已登录用户对象（登录时存入，key为"user"）
        User loginUser = (User) session.getAttribute("user");

        // 2. 权限拦截：未登录用户禁止访问用户中心，重定向到登录页
        // req.getContextPath()：获取项目根路径，避免硬编码路径导致部署异常
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return; // 终止后续代码执行，防止非法访问
        }

        // ==================== 【逻辑业务层代码开始】====================
        // 核心业务逻辑：根据用户ID查询完整用户信息
        // 归属：应抽离到 UserService.getUserInfoById(Integer uid) 方法中
        // 业务规则：查询用户完整信息（地址、手机号、注册时间等），用于页面回显
        User user = userDao.findUserById(loginUser.getUid());
        // ==================== 【逻辑业务层代码结束】====================

        // 4. 将完整用户信息存入request域，供用户中心页面（user_center.jsp）渲染
        req.setAttribute("user", user);

        // 5. 请求转发到用户中心页面（保留request数据，实现数据回显）
        req.getRequestDispatcher("/user_center.jsp").forward(req, resp);
    }

    /**
     * 私有方法：修改用户信息（手机号 + 收货地址）
     * 核心逻辑：参数解析 → 数据库修改 → 结果反馈 → 最新数据回显
     */
    private void updateUserInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 1. 解析前端表单提交的参数（控制层代码：参数接收）
            // 用户ID：转为Integer类型（主键，定位要修改的用户）
            Integer uid = Integer.parseInt(req.getParameter("uid"));
            // 新手机号：前端表单提交的手机号
            String phone = req.getParameter("phone");
            // 新收货地址：前端表单提交的地址（支持中文）
            String address = req.getParameter("address");

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：修改用户手机号和收货地址
            // 归属：应抽离到 UserService.updateUserPhoneAndAddress(Integer uid, String phone, String address) 方法中
            // 业务规则：仅修改手机号和地址字段，返回修改结果（受影响行数）
            int row = userDao.updatePhoneAndAddress(uid, phone, address);
            // ==================== 【逻辑业务层代码结束】====================

            // 3. 根据修改结果封装提示信息（供页面展示）（控制层代码：结果处理）
            if (row > 0) {
                // 修改成功：存入成功提示
                req.setAttribute("successMsg", "修改成功！信息已更新");
            } else {
                // 修改失败（无受影响行）：存入失败提示
                req.setAttribute("errorMsg", "修改失败，请稍后重试");
            }
        } catch (Exception e) {
            // 全局异常捕获：处理参数转换失败、SQL异常等所有异常
            e.printStackTrace(); // 打印异常堆栈（实际项目建议替换为日志记录）
            // 封装异常提示，告知用户具体错误原因
            req.setAttribute("errorMsg", "修改失败：" + e.getMessage());
        } finally {
            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：重新查询用户最新信息（保证数据一致性）
            // 归属：应抽离到 UserService.getUserInfoById(Integer uid) 方法中
            // 业务规则：无论修改成功/失败，都返回用户最新完整信息
            User loginUser = (User) req.getSession().getAttribute("user");
            User user = userDao.findUserById(loginUser.getUid());
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：将最新用户信息存入request，供页面回显
            req.setAttribute("user", user);

            // 控制层代码：转发回用户中心页面，展示提示信息和最新用户信息
            req.getRequestDispatcher("/user_center.jsp").forward(req, resp);
        }
    }
}