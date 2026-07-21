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

/**
 * 商品数据访问对象（GoodsDao）
 * 核心职责：封装商品相关的所有数据库操作（CRUD），是业务层与数据库之间的桥梁
 * 操作表：goods（商品表）
 * 核心字段：gid(商品ID)、gname(商品名称)、price(价格)、stock(库存)、gdesc(描述)、gimg(商品图片路径)
 * 设计说明：所有方法均使用PreparedStatement防止SQL注入，统一通过DBUtil管理数据库连接
 */
public class GoodsDao {

    /**
     * 查询所有商品信息
     * @return List<Goods> 商品列表：包含所有商品记录，若无数据则返回空列表（非null）
     * 应用场景：商品列表页展示、后台商品管理页查询全部商品
     */
    public List<Goods> findAll() {
        // 1. 获取数据库连接（通过自定义工具类DBUtil）
        Connection conn = DBUtil.getConn();
        // 2. 定义查询SQL：查询goods表所有字段
        String sql = "SELECT * FROM goods";
        // 声明预编译Statement对象（防止SQL注入）和结果集对象
        PreparedStatement ps = null;
        ResultSet rs = null;
        // 初始化商品列表，避免返回null导致空指针异常
        List<Goods> list = new ArrayList<>();

        try {
            // 3. 创建预编译Statement对象
            ps = conn.prepareStatement(sql);
            // 4. 执行查询，返回结果集
            rs = ps.executeQuery();
            // 5. 遍历结果集，封装Goods实体对象
            while (rs.next()) {
                Goods goods = new Goods();
                goods.setGid(rs.getInt("gid"));          // 商品ID（主键）
                goods.setGname(rs.getString("gname"));   // 商品名称
                goods.setPrice(rs.getBigDecimal("price"));// 商品价格（BigDecimal避免浮点精度丢失）
                goods.setStock(rs.getInt("stock"));      // 商品库存
                goods.setGdesc(rs.getString("gdesc"));   // 商品描述
                goods.setGimg(rs.getString("gimg"));     // 商品图片路径
                // 将封装好的商品对象添加到列表
                list.add(goods);
            }
        } catch (SQLException e) {
            // 捕获SQL异常并打印堆栈信息（实际项目建议替换为日志记录）
            e.printStackTrace();
        } finally {
            // 6. 释放数据库资源（关闭顺序：ResultSet → PreparedStatement → Connection）
            DBUtil.close(rs, ps, conn);
        }
        // 返回商品列表
        return list;
    }

    /**
     * 根据商品ID查询单个商品详情
     * @param gid 商品ID（goods表主键）
     * @return Goods 商品对象：存在则返回封装好的对象，不存在则返回null
     * 应用场景：商品详情页、购物车关联商品信息、订单创建时校验商品
     */
    public Goods findById(int gid) {
        Connection conn = DBUtil.getConn();
        // SQL：根据主键gid精准查询单条商品记录
        String sql = "SELECT * FROM goods WHERE gid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        // 初始化商品对象为null（无数据时直接返回null）
        Goods goods = null;

        try {
            ps = conn.prepareStatement(sql);
            // 为SQL占位符赋值：第1个?替换为商品ID
            ps.setInt(1, gid);
            rs = ps.executeQuery();
            // 若结果集有数据（仅一条，主键唯一），封装商品对象
            if (rs.next()) {
                goods = new Goods();
                goods.setGid(gid);                       // 商品ID（直接使用入参，无需从结果集获取）
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

    /**
     * 添加新商品到数据库
     * 【核心修改1】：SQL新增gimg字段，代码中添加gimg参数赋值，支持商品图片路径入库
     * @param goods 封装了商品信息的Goods实体对象（包含gname/price/stock/gdesc/gimg）
     * @return int 受影响行数：1表示添加成功，0表示失败
     * 应用场景：后台商品管理-新增商品
     */
    public int addGoods(Goods goods) {
        Connection conn = DBUtil.getConn();
        // SQL：插入商品信息，包含新增的gimg（商品图片路径）字段
        String sql = "INSERT INTO goods(gname,price,stock,gdesc,gimg) VALUES(?,?,?,?,?)";
        PreparedStatement ps = null;
        // 记录数据库操作受影响的行数（INSERT操作成功则返回1）
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            // 为占位符赋值，顺序与SQL字段顺序一致
            ps.setString(1, goods.getGname());    // 商品名称
            ps.setBigDecimal(2, goods.getPrice());// 商品价格
            ps.setInt(3, goods.getStock());       // 商品库存
            ps.setString(4, goods.getGdesc());    // 商品描述
            ps.setString(5, goods.getGimg());     // 【核心修改】商品图片路径赋值
            // 执行插入操作，返回受影响行数
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源（ResultSet为null，仅关闭Statement和Connection）
            DBUtil.close(ps, conn);
        }
        // 返回受影响行数，供业务层判断是否添加成功
        return row;
    }

    /**
     * 修改商品信息
     * 【核心修改2】：SQL新增gimg字段赋值，支持商品图片路径修改
     * @param goods 封装了修改后商品信息的Goods实体对象（必须包含gid主键）
     * @return int 受影响行数：1表示修改成功，0表示无此商品（gid不存在）
     * 应用场景：后台商品管理-编辑商品信息
     */
    public int updateGoods(Goods goods) {
        Connection conn = DBUtil.getConn();
        // SQL：更新商品信息，包含gimg字段，通过gid主键定位要修改的记录
        String sql = "UPDATE goods SET gname=?,price=?,stock=?,gdesc=?,gimg=? WHERE gid=?";
        PreparedStatement ps = null;
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            // 为占位符赋值（顺序与SQL字段顺序一致）
            ps.setString(1, goods.getGname());
            ps.setBigDecimal(2, goods.getPrice());
            ps.setInt(3, goods.getStock());
            ps.setString(4, goods.getGdesc());
            ps.setString(5, goods.getGimg());     // 【核心修改】商品图片路径赋值
            ps.setInt(6, goods.getGid());         // 商品主键（定位要修改的记录）
            // 执行更新操作，返回受影响行数
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 根据商品ID删除商品
     * @param gid 商品ID（goods表主键）
     * @return int 受影响行数：1表示删除成功，0表示无此商品
     * 应用场景：后台商品管理-删除商品（实际项目建议做逻辑删除，而非物理删除）
     */
    public int deleteGoods(int gid) {
        Connection conn = DBUtil.getConn();
        // SQL：根据主键删除指定商品记录
        String sql = "DELETE FROM goods WHERE gid=?";
        PreparedStatement ps = null;
        int row = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, gid);
            // 执行删除操作，返回受影响行数
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return row;
    }

    /**
     * 统计商品总数
     * @return int 商品总数量：0表示无商品，大于0表示对应数量
     * 应用场景：后台商品管理页分页、首页展示商品总数、数据统计报表
     */
    public int countAllGoods() {
        Connection conn = DBUtil.getConn();
        // SQL：使用COUNT(*)统计表中所有记录数（高效，不返回具体数据）
        String sql = "SELECT COUNT(*) FROM goods";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0; // 初始化统计数为0

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
}