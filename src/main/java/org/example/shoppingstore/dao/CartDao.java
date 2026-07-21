package org.example.shoppingstore.dao;

import org.example.shoppingstore.entity.Cart;
import org.example.shoppingstore.entity.Goods;
import org.example.shoppingstore.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    // 添加商品到购物车 ✅ 修改：入库时存入【用户名+商品名称】
    public int addCart(int uid, int gid) {
        Connection conn = DBUtil.getConn();
        PreparedStatement ps = null;
        int row = 0;
        try {
            // 1. 根据uid查询【用户名】
            String sqlGetUser = "SELECT username FROM user WHERE uid=?";
            PreparedStatement psUser = conn.prepareStatement(sqlGetUser);
            psUser.setInt(1, uid);
            ResultSet rsUser = psUser.executeQuery();
            String userName = "";
            if(rsUser.next()){
                userName = rsUser.getString("username");
            }

            // 2. 根据gid查询【商品名称】
            String sqlGetName = "SELECT gname FROM goods WHERE gid=?";
            PreparedStatement psName = conn.prepareStatement(sqlGetName);
            psName.setInt(1, gid);
            ResultSet rs = psName.executeQuery();
            String goodsName = "";
            if(rs.next()){
                goodsName = rs.getString("gname");
            }

            // 3. 新增购物车，带【用户名+商品名】入库
            String sql = "INSERT INTO cart(uid,userName,gid,goodsName) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE num=num+1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            ps.setString(2, userName);
            ps.setInt(3, gid);
            ps.setString(4, goodsName);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    // 根据用户ID查询购物车 ✅ 修改：赋值【用户名+商品名称】字段
    public List<Cart> findCartByUid(int uid) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM cart WHERE uid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Cart> list = new ArrayList<>();
        GoodsDao goodsDao = new GoodsDao();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCid(rs.getInt("cid"));
                cart.setUid(uid);
                cart.setUserName(rs.getString("userName")); // ✅ 赋值用户名
                cart.setGid(rs.getInt("gid"));
                cart.setGoodsName(rs.getString("goodsName"));// ✅ 赋值商品名称
                cart.setNum(rs.getInt("num"));
                cart.setGoods(goodsDao.findById(rs.getInt("gid"))); // 保留原有关联
                list.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    // 删除购物车商品
    public int deleteCart(int cid) {
        Connection conn = DBUtil.getConn();
        String sql = "DELETE FROM cart WHERE cid=?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    // 根据用户ID清空购物车（提交订单后自动清空）
    public int deleteCartByUid(int uid) {
        Connection conn = DBUtil.getConn();
        String sql = "DELETE FROM cart WHERE uid=?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }
}