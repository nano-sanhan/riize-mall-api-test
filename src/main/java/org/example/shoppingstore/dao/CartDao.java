package org.example.shoppingstore.dao;

import org.apache.commons.dbutils.QueryRunner;
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

    public int addCart(int uid, int gid) {
        Connection conn = DBUtil.getConn();
        PreparedStatement ps = null;
        int row = 0;

        try {
            String sqlGetUser = "SELECT username FROM user WHERE uid=?";
            PreparedStatement psUser = conn.prepareStatement(sqlGetUser);
            psUser.setInt(1, uid);
            ResultSet rsUser = psUser.executeQuery();
            String userName = "";
            if (rsUser.next()) {
                userName = rsUser.getString("username");
            }

            String sqlGetName = "SELECT gname FROM goods WHERE gid=?";
            PreparedStatement psName = conn.prepareStatement(sqlGetName);
            psName.setInt(1, gid);
            ResultSet rs = psName.executeQuery();
            String goodsName = "";
            if (rs.next()) {
                goodsName = rs.getString("gname");
            }

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
                cart.setUserName(rs.getString("userName"));
                cart.setGid(rs.getInt("gid"));
                cart.setGoodsName(rs.getString("goodsName"));
                cart.setNum(rs.getInt("num"));
                cart.setGoods(goodsDao.findById(rs.getInt("gid")));
                list.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

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

    public void updateCartNum(int cid, int num) {
        Connection conn = DBUtil.getConn();
        QueryRunner qr = new QueryRunner();
        String sql = "UPDATE cart SET num = ? WHERE cid = ?";
        try {
            qr.update(conn, sql, num, cid);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, null, conn);
        }
    }

    // 根据cid查询购物车（修复之前报红）
    public Cart findCartByCid(int cid) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM cart WHERE cid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cart cart = null;
        GoodsDao goodsDao = new GoodsDao();

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cid);
            rs = ps.executeQuery();
            if (rs.next()) {
                cart = new Cart();
                cart.setCid(rs.getInt("cid"));
                cart.setUid(rs.getInt("uid"));
                cart.setUserName(rs.getString("userName"));
                cart.setGid(rs.getInt("gid"));
                cart.setGoodsName(rs.getString("goodsName"));
                cart.setNum(rs.getInt("num"));
                cart.setGoods(goodsDao.findById(rs.getInt("gid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return cart;
    }
}