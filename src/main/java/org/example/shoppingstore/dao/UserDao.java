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
    // 用户登录
    public User login(String username, String password) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(username);
                user.setType(rs.getInt("type"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setRegTime(rs.getString("regTime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }

    // 用户注册
    public int register(User user) {
        Connection conn = DBUtil.getConn();
        String sql = "INSERT INTO user(username,password,phone,regTime) VALUES(?,?,?,?)";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            ps.setString(4, new java.sql.Timestamp(System.currentTimeMillis()).toString()); // 自动写入当前时间
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    // 查询所有普通用户
    public List<User> findAllUser() {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM user WHERE type=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
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
    // 只新增这一个方法：根据用户名搜索普通用户
    public List<User> findUserByUsername(String username) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM user WHERE type=0 AND username LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
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
    // 在UserDao中新增
    public int countAllUser() {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT COUNT(*) FROM user WHERE type=0";
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
    //✅ ==========新增：根据用户ID查询单个用户的完整信息==========
    public User findUserById(Integer uid) {
        Connection conn = DBUtil.getConn();
        String sql = "SELECT * FROM user WHERE uid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("username"));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setType(rs.getInt("type"));
                user.setRegTime(rs.getString("regTime") == null ? "暂无数据" : rs.getString("regTime"));//查询注册时间
                user.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }
    public int updatePhoneAndAddress(Integer uid, String phone, String address) {
        Connection conn = DBUtil.getConn();
        String sql = "UPDATE user SET phone=?, address=? WHERE uid=?";
        PreparedStatement ps = null;
        int row = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setInt(3, uid);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }
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
}


