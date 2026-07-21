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
    // 创建订单 正常入库即可，数据库自动生成orderTime和status
    public int addOrder(Order order) {
        Connection conn = DBUtil.getConn();
        // SQL新增username字段
        String sql = "INSERT INTO `order`(uid,totalPrice,address,goodsName,username) VALUES(?,?,?,?,?)";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order.getUid());
            ps.setBigDecimal(2, order.getTotalPrice());
            ps.setString(3, order.getAddress());
            ps.setString(4, order.getGoodsName());
            ps.setString(5, order.getUsername()); // 新增：插入用户名
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    // 根据用户ID查询订单 【极简正确版，无任何红杠，无任何异常】
    public List<Order> findOrderByUid(int uid) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM `order` WHERE uid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOid(rs.getInt("oid"));
                order.setUid(rs.getInt("uid"));
                order.setTotalPrice(rs.getBigDecimal("totalPrice"));
                // ✅ 就这一行，最简单的取值，绝对正确，绝对无红杠
                order.setOrderTime(rs.getTimestamp("orderTime"));
                order.setStatus(rs.getInt("status"));
                order.setAddress(rs.getString("address"));
                order.setGoodsName(rs.getString("goodsName"));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }


    // ========== ✅ 修改核心：查询所有订单+关联用户表 获取【用户名】+【商品名称】+【收货地址】 ==========
    public List<Order> findAllOrder() {
        Connection conn = DBUtil.getConn();
        // 关联order表和user表，通过uid匹配，获取用户名
        String sql = "SELECT o.*,u.username FROM `order` o LEFT JOIN `user` u ON o.uid = u.uid";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOid(rs.getInt("oid"));
                order.setUid(rs.getInt("uid"));
                order.setUsername(rs.getString("username")); // 赋值用户名
                order.setTotalPrice(rs.getBigDecimal("totalPrice"));
                order.setOrderTime(rs.getTimestamp("orderTime"));
                order.setStatus(rs.getInt("status"));
                order.setGoodsName(rs.getString("goodsName")); // 赋值商品名称
                order.setAddress(rs.getString("address")); // 赋值收货地址
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    // 修改订单状态
    public int updateStatus(int oid, int status) {
        Connection conn = DBUtil.getConn();
        String sql = "UPDATE `order` SET status=? WHERE oid=?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, oid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }
    // ========== ✅ 新增核心方法：根据订单ID删除订单 ==========
    public int deleteOrderById(int oid) {
        Connection conn = DBUtil.getConn();
        String sql = "DELETE FROM `order` WHERE oid = ?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, oid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }
    // 在OrderDao中新增
// 统计订单总数
    public int countAllOrder() {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT COUNT(*) FROM `order`";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
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

    // 统计销量TOP3商品（按商品名称分组统计）
    public List<Map<String, Object>> getTop3GoodsBySale() {
        Connection conn = DBUtil.getConn();
        // 按goodsName分组统计，取销量前3
        String sql = "SELECT goodsName AS goodsName, COUNT(*) AS saleNum FROM `order` GROUP BY goodsName ORDER BY saleNum DESC LIMIT 3";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("goodsName", rs.getString("goodsName"));
                map.put("saleNum", rs.getInt("saleNum"));
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