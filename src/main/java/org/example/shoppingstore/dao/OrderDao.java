package org.example.shoppingstore.dao;

import org.example.shoppingstore.entity.Order;
import org.example.shoppingstore.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class OrderDao {

    /**
     * 创建订单（新增订单记录）
     * 核心规则：
     * - 数据库自动生成orderTime（下单时间）和status（订单状态，默认值）
     * - 手动传入uid、totalPrice、address、goodsName、username字段
     * @param order 封装订单信息的Order实体对象（包含uid、totalPrice、address、goodsName、username）
     * @return int 受影响行数：1表示创建成功，0表示失败
     * 应用场景：用户提交订单时，将订单信息入库
     */
    public int addOrder(Order order) {
        // 1. 获取数据库连接
        Connection conn = DBUtil.getConn();
        // 2. 定义插入SQL：order是SQL关键字，需用反引号`转义；新增username字段入库
        String sql = "INSERT INTO `order`(uid,totalPrice,address,goodsName,username) VALUES(?,?,?,?,?)";
        PreparedStatement ps = null;
        int row = 0; // 初始化受影响行数

        try {
            // 3. 创建预编译Statement对象
            ps = conn.prepareStatement(sql);
            // 4. 为占位符赋值，顺序与SQL字段顺序一致
            ps.setInt(1, order.getUid());                // 用户ID
            ps.setBigDecimal(2, order.getTotalPrice());  // 订单总价（BigDecimal避免精度丢失）
            ps.setString(3, order.getAddress());         // 收货地址
            ps.setString(4, order.getGoodsName());       // 商品名称
            ps.setString(5, order.getUsername());        // 用户名（新增字段）
            // 5. 执行插入操作，返回受影响行数
            row = ps.executeUpdate();
        } catch (SQLException e) {
            // 捕获SQL异常并打印堆栈（实际项目建议替换为日志记录）
            e.printStackTrace();
        } finally {
            // 6. 释放数据库资源（关闭Statement和Connection）
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 根据用户ID查询该用户的所有订单（极简正确版）
     * 核心特点：无语法错误、无异常，仅查询订单表基础字段，不关联其他表
     * @param uid 用户ID（订单所属用户）
     * @return List<Order> 订单列表：包含该用户所有订单，无数据则返回空列表（非null）
     * 应用场景：用户个人中心查看自己的订单列表
     */
    public List<Order> findOrderByUid(int uid) {
        Connection conn = DBUtil.getConn();
        // SQL：根据uid查询订单表所有字段，仅返回当前用户的订单
        String sql = "SELECT * FROM `order` WHERE uid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>(); // 初始化订单列表

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid); // 赋值用户ID占位符
            rs = ps.executeQuery(); // 执行查询，返回结果集

            // 遍历结果集，封装Order对象
            while (rs.next()) {
                Order order = new Order();
                order.setOid(rs.getInt("oid"));               // 订单ID（主键）
                order.setUid(rs.getInt("uid"));               // 用户ID
                order.setTotalPrice(rs.getBigDecimal("totalPrice")); // 订单总价
                order.setOrderTime(rs.getTimestamp("orderTime"));     // 下单时间（Timestamp类型）
                order.setStatus(rs.getInt("status"));         // 订单状态（如0-待付款、1-已付款）
                order.setAddress(rs.getString("address"));    // 收货地址
                order.setGoodsName(rs.getString("goodsName"));// 商品名称
                list.add(order); // 添加到订单列表
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放所有资源（ResultSet → PreparedStatement → Connection）
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 查询所有订单（核心修改版）
     * 核心规则：
     * - 关联order表和user表（LEFT JOIN），通过uid匹配获取用户名
     * - 即使订单无关联用户（uid不存在），也会返回订单数据（LEFT JOIN特性）
     * @return List<Order> 所有订单列表：包含订单基础信息+关联的用户名，无数据则返回空列表
     * 应用场景：后台管理系统查看所有订单（需展示用户名、商品名称、收货地址）
     */
    public List<Order> findAllOrder() {
        Connection conn = DBUtil.getConn();
        // SQL：左连接order表和user表，查询订单所有字段+用户名；o/u为表别名，简化SQL
        String sql = "SELECT o.*,u.username FROM `order` o LEFT JOIN `user` u ON o.uid = u.uid";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOid(rs.getInt("oid"));               // 订单ID
                order.setUid(rs.getInt("uid"));               // 用户ID
                order.setUsername(rs.getString("username"));  // 关联查询的用户名
                order.setTotalPrice(rs.getBigDecimal("totalPrice")); // 订单总价
                order.setOrderTime(rs.getTimestamp("orderTime"));     // 下单时间
                order.setStatus(rs.getInt("status"));         // 订单状态
                order.setGoodsName(rs.getString("goodsName"));// 商品名称
                order.setAddress(rs.getString("address"));    // 收货地址
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 修改订单状态
     * @param oid    订单ID（主键，定位要修改的订单）
     * @param status 新的订单状态（如0-待付款、1-已付款、2-已发货、3-已完成）
     * @return int 受影响行数：1表示修改成功，0表示订单不存在
     * 应用场景：后台更新订单状态（如发货后修改为已发货）、用户付款后修改为已付款
     */
    public int updateStatus(int oid, int status) {
        Connection conn = DBUtil.getConn();
        // SQL：根据订单ID更新状态字段
        String sql = "UPDATE `order` SET status=? WHERE oid=?";
        PreparedStatement ps = null;
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            // 占位符赋值：先status，后oid（与SQL中顺序一致）
            ps.setInt(1, status);
            ps.setInt(2, oid);
            row = ps.executeUpdate(); // 执行更新操作
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 根据订单ID删除订单（物理删除）
     * @param oid 订单ID（主键）
     * @return int 受影响行数：1表示删除成功，0表示订单不存在
     * 应用场景：后台管理系统删除无效订单（实际项目建议逻辑删除，而非物理删除）
     */
    public int deleteOrderById(int oid) {
        Connection conn = DBUtil.getConn();
        // SQL：根据订单ID删除指定记录
        String sql = "DELETE FROM `order` WHERE oid = ?";
        PreparedStatement ps = null;
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, oid);
            row = ps.executeUpdate(); // 执行删除操作
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 统计订单总数
     * @return int 订单总数量：0表示无订单，大于0表示对应数量
     * 应用场景：后台数据统计（如首页展示订单总数）、分页查询时计算总页数
     */
    public int countAllOrder() {
        Connection conn = DBUtil.getConn();
        // SQL：使用COUNT(*)统计表中所有记录数（高效，仅返回计数，不返回具体数据）
        String sql = "SELECT COUNT(*) FROM `order`";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0; // 初始化统计数

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            // COUNT(*)仅返回一行一列结果，直接取第一列值
            if (rs.next()) {
                count = rs.getInt(1); // 1表示结果集的第1列（唯一列）
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return count;
    }

    /**
     * 统计销量TOP3商品（按商品名称分组统计）
     * 核心逻辑：
     * - GROUP BY goodsName：按商品名称分组
     * - COUNT(*)：统计每组订单数（即销量）
     * - ORDER BY saleNum DESC：按销量降序排列
     * - LIMIT 3：仅取前3名
     * @return List<Map<String, Object>> 销量TOP3列表：每个Map包含goodsName（商品名称）、saleNum（销量）
     * 应用场景：后台数据报表、首页展示热销商品
     */
    public List<Map<String, Object>> getTop3GoodsBySale() {
        Connection conn = DBUtil.getConn();
        // SQL：分组统计销量，取前3；AS为字段起别名，方便结果集解析
        String sql = "SELECT goodsName AS goodsName, COUNT(*) AS saleNum FROM `order` GROUP BY goodsName ORDER BY saleNum DESC LIMIT 3";
        PreparedStatement ps = null;
        ResultSet rs = null;
        // 初始化结果列表：用Map存储商品名称和销量（无需创建实体类，灵活存储）
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("goodsName", rs.getString("goodsName")); // 商品名称
                map.put("saleNum", rs.getInt("saleNum"));         // 销量（订单数）
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }
}