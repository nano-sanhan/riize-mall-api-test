package org.example.shoppingstore.dao;

import org.example.shoppingstore.entity.Goods;
import org.example.shoppingstore.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsDao {
    // 查询所有商品
    public List<Goods> findAll() {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM goods";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Goods> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Goods goods = new Goods();
                goods.setGid(rs.getInt("gid"));
                goods.setGname(rs.getString("gname"));
                goods.setPrice(rs.getBigDecimal("price"));
                goods.setStock(rs.getInt("stock"));
                goods.setGdesc(rs.getString("gdesc"));
                goods.setGimg(rs.getString("gimg"));
                list.add(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    // 根据ID查询商品
    public Goods findById(int gid) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM goods WHERE gid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Goods goods = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, gid);
            rs = ps.executeQuery();
            if (rs.next()) {
                goods = new Goods();
                goods.setGid(gid);
                goods.setGname(rs.getString("gname"));
                goods.setPrice(rs.getBigDecimal("price"));
                goods.setStock(rs.getInt("stock"));
                goods.setGdesc(rs.getString("gdesc"));
                goods.setGimg(rs.getString("gimg"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return goods;
    }

    // 添加商品 ✅【核心修改1】SQL加gimg字段 + 赋值gimg参数
    public int addGoods(Goods goods) {
        Connection conn = DBUtil.getConn();
        // sql新增 gimg 字段
        String sql = "INSERT INTO goods(gname,price,stock,gdesc,gimg) VALUES(?,?,?,?,?)";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, goods.getGname());
            ps.setBigDecimal(2, goods.getPrice());
            ps.setInt(3, goods.getStock());
            ps.setString(4, goods.getGdesc());
            ps.setString(5, goods.getGimg()); // ✅ 新增：给图片字段赋值
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    // 修改商品 ✅【核心修改2】SQL加gimg字段 + 赋值gimg参数
    public int updateGoods(Goods goods) {
        Connection conn = DBUtil.getConn();
        // sql新增 gimg=? 字段
        String sql = "UPDATE goods SET gname=?,price=?,stock=?,gdesc=?,gimg=? WHERE gid=?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, goods.getGname());
            ps.setBigDecimal(2, goods.getPrice());
            ps.setInt(3, goods.getStock());
            ps.setString(4, goods.getGdesc());
            ps.setString(5, goods.getGimg()); // ✅ 新增：给图片字段赋值
            ps.setInt(6, goods.getGid());
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    // 删除商品
    public int deleteGoods(int gid) {
        Connection conn = DBUtil.getConn();
        String sql = "DELETE FROM goods WHERE gid=?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, gid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    public int countAllGoods() {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT COUNT(*) FROM goods";
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
}