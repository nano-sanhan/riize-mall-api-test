package org.example.shoppingstore.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer oid;
    private Integer uid;
    private String username;  //用户名称
    private BigDecimal totalPrice;
    private Date orderTime;
    private Integer status;   //0=已支付 1=待发货 2=已完成
    private String address;   //收货地址
    private String goodsName; //商品名称

    // 无参构造
    public Order() {}

    // 全字段getter和setter 【全部补全，无遗漏，无报红】
    public Integer getOid() {
        return oid;
    }
    public void setOid(Integer oid) {
        this.oid = oid;
    }
    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Date getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getGoodsName() {
        return goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}