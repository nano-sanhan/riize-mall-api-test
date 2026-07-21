package org.example.shoppingstore.dao;

import org.example.shoppingstore.entity.User;
import org.example.shoppingstore.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    /**
     * 用户登录验证
     * 核心逻辑：根据用户名+密码精准查询用户，验证登录信息是否正确
     * @param username 前端输入的用户名
     * @param password 前端输入的密码（注：实际项目需加密存储/验证，此处为明文演示）
     * @return User 用户对象：登录成功返回封装好的用户信息，失败返回null
     * 应用场景：用户登录页面，验证账号密码是否正确
     */
    public User login(String username, String password) {
        // 1. 获取数据库连接
        Connection conn = DBUtil.getConn();
        // 2. 定义登录验证SQL：根据用户名和密码查询用户
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null; // 初始化用户对象为null（登录失败返回null）

        try {
            // 3. 创建预编译Statement对象
            ps = conn.prepareStatement(sql);
            // 4. 为占位符赋值：用户名 + 密码
            ps.setString(1, username);
            ps.setString(2, password);
            // 5. 执行查询，返回结果集
            rs = ps.executeQuery();

            // 6. 若结果集有数据（用户名+密码匹配），封装用户对象
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));          // 用户ID（主键）
                user.setUsername(username);             // 用户名（直接使用入参，无需重复读取）
                user.setType(rs.getInt("type"));        // 用户类型（0-普通用户，1-管理员）
                user.setAddress(rs.getString("address"));// 收货地址
                user.setPhone(rs.getString("phone"));    // 手机号
                user.setRegTime(rs.getString("regTime"));// 注册时间
            }
        } catch (SQLException e) {
            // 捕获SQL异常并打印堆栈（实际项目建议替换为日志记录）
            e.printStackTrace();
        } finally {
            // 7. 释放所有数据库资源（ResultSet → PreparedStatement → Connection）
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }

    /**
     * 用户注册（新增用户）
     * 核心规则：
     * - 仅插入用户名、密码、手机号、注册时间字段
     * - 注册时间自动生成当前系统时间（Timestamp类型转String）
     * - 用户类型(type)、状态(status)使用数据库默认值（通常type=0，status=1）
     * @param user 封装注册信息的User对象（包含username、password、phone）
     * @return int 受影响行数：1表示注册成功，0表示失败（用户名重复等）
     * 应用场景：用户注册页面，提交注册信息入库
     */
    public int register(User user) {
        Connection conn = DBUtil.getConn();
        // 定义注册SQL：插入用户名、密码、手机号、注册时间
        String sql = "INSERT INTO user(username,password,phone,regTime) VALUES(?,?,?,?)";
        PreparedStatement ps = null;
        int row = 0; // 初始化受影响行数

        try {
            ps = conn.prepareStatement(sql);
            // 为占位符赋值：用户名 + 密码 + 手机号 + 注册时间
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            // 自动生成当前时间：Timestamp转String，适配数据库字段类型
            ps.setString(4, new java.sql.Timestamp(System.currentTimeMillis()).toString());
            // 执行插入操作，返回受影响行数
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源（ResultSet为null，仅关闭Statement和Connection）
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 查询所有普通用户（排除管理员）
     * 核心规则：仅查询type=0的普通用户，管理员（type=1）不展示
     * @return List<User> 普通用户列表：包含所有正常/禁用的普通用户，无数据则返回空列表（非null）
     * 应用场景：后台用户管理页面，展示所有普通用户信息
     */
    public List<User> findAllUser() {
        Connection conn = DBUtil.getConn();
        // SQL：仅查询type=0的普通用户
        String sql = "SELECT * FROM user WHERE type=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>(); // 初始化用户列表，避免返回null

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // 遍历结果集，封装每个普通用户对象
            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("username"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setType(rs.getInt("type"));
                user.setRegTime(rs.getString("regTime"));
                user.setStatus(rs.getInt("status")); // 用户状态（0-禁用，1-正常）
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 根据用户名模糊搜索普通用户
     * 核心规则：
     * - 仅搜索type=0的普通用户
     * - 使用LIKE + %通配符实现模糊匹配（包含关键词即可）
     * @param username 搜索关键词（前端输入的用户名片段）
     * @return List<User> 匹配的普通用户列表：无匹配则返回空列表
     * 应用场景：后台用户管理页面，按用户名搜索用户
     */
    public List<User> findUserByUsername(String username) {
        Connection conn = DBUtil.getConn();
        // SQL：模糊搜索普通用户，%为通配符（匹配任意字符）
        String sql = "SELECT * FROM user WHERE type=0 AND username LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sql);
            // 为模糊查询赋值：拼接%通配符（如输入"张" → "%张%"，匹配所有含"张"的用户名）
            ps.setString(1, "%"+username+"%");
            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("username"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setType(rs.getInt("type"));
                user.setRegTime(rs.getString("regTime"));
                user.setStatus(rs.getInt("status"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 统计普通用户总数
     * 核心规则：仅统计type=0的普通用户，不含管理员
     * @return int 普通用户总数：0表示无普通用户，大于0表示对应数量
     * 应用场景：后台数据统计（如用户管理页分页、首页展示用户总数）
     */
    public int countAllUser() {
        Connection conn = DBUtil.getConn();
        // SQL：统计type=0的用户数量（COUNT(*)高效统计总数）
        String sql = "SELECT COUNT(*) FROM user WHERE type=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0; // 初始化统计数为0

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            // COUNT(*)仅返回一行一列结果，取第一列值
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return count;
    }

    /**
     * 根据用户ID查询单个用户的完整信息
     * 核心特点：查询所有类型用户（普通/管理员），返回完整字段（含状态、注册时间等）
     * @param uid 用户ID（主键）
     * @return User 用户对象：存在则返回完整信息，不存在则返回null
     * 应用场景：
     * - 订单提交时校验用户状态（是否禁用）
     * - 后台用户管理页查看单个用户详情
     */
    public User findUserById(Integer uid) {
        Connection conn = DBUtil.getConn();
        // SQL：根据主键uid精准查询单个用户
        String sql = "SELECT * FROM user WHERE uid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid); // 赋值用户ID占位符
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("username"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setType(rs.getInt("type"));
                // 空值处理：注册时间为null时显示"暂无数据"，避免前端展示空值
                user.setRegTime(rs.getString("regTime") == null ? "暂无数据" : rs.getString("regTime"));
                user.setStatus(rs.getInt("status")); // 用户状态（关键：用于判断是否禁用）
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }

    /**
     * 修改用户手机号和收货地址
     * 核心逻辑：根据用户ID更新指定字段，其他字段不变
     * @param uid     用户ID（主键，定位要修改的用户）
     * @param phone   新的手机号
     * @param address 新的收货地址
     * @return int 受影响行数：1表示修改成功，0表示用户不存在
     * 应用场景：
     * - 用户个人中心修改联系方式/地址
     * - 后台管理员修改用户地址
     */
    public int updatePhoneAndAddress(Integer uid, String phone, String address) {
        Connection conn = DBUtil.getConn();
        // SQL：仅更新手机号和地址字段
        String sql = "UPDATE user SET phone=?, address=? WHERE uid=?";
        PreparedStatement ps = null;
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            // 占位符赋值：手机号 + 地址 + 用户ID
            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setInt(3, uid);
            row = ps.executeUpdate(); // 执行更新操作
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 修改用户状态（启用/禁用）
     * 核心规则：status=0-禁用（禁止下单），status=1-正常（允许所有操作）
     * @param uid    用户ID（主键）
     * @param status 新的用户状态（0/1）
     * @return int 受影响行数：1表示修改成功，0表示用户不存在
     * 应用场景：后台用户管理页，禁用违规用户/启用解禁用户
     */
    public int updateUserStatus(Integer uid, Integer status) {
        Connection conn = DBUtil.getConn();
        String sql = "UPDATE user SET status=? WHERE uid=?";
        PreparedStatement ps = null;
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, uid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return User 用户对象：存在则返回，不存在则返回null
     * 应用场景：注册时校验手机号是否已注册
     */
    public User findUserByPhone(String phone) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM user WHERE phone=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setType(rs.getInt("type"));
                user.setRegTime(rs.getString("regTime"));
                user.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }

    /**
     * 根据用户名精确查询用户（用于注册时检查用户名是否存在）
     * @param username 用户名
     * @return User 用户对象：存在则返回，不存在则返回null
     */
    public User findUserByUsernameExact(String username) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM user WHERE username=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setType(rs.getInt("type"));
                user.setRegTime(rs.getString("regTime"));
                user.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }

    /**
     * 检查手机号是否已被注册
     * @param phone 手机号
     * @return boolean true-手机号已被注册，false-手机号可用
     * 应用场景：注册时校验手机号唯一性
     */
    public boolean isPhoneRegistered(String phone) {
        User user = findUserByPhone(phone);
        return user != null;
    }
}