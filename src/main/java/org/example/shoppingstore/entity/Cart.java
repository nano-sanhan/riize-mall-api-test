package org.example.shoppingstore.entity;

public class Cart {
    private Integer cid;
    private Integer uid;
    private Integer gid;
    private Integer num;
    private Goods goods; // 保留原有商品关联对象
    private String goodsName; // ✅ 新增：商品名称
    private String userName;  // ✅ 新增：用户名

    public Cart() {}

    // Getter & Setter 全部保留 + 新增两个字段的get/set方法
    public Integer getCid() { return cid; }
    public void setCid(Integer cid) { this.cid = cid; }
    public Integer getUid() { return uid; }
    public void setUid(Integer uid) { this.uid = uid; }
    public Integer getGid() { return gid; }
    public void setGid(Integer gid) { this.gid = gid; }
    public Integer getNum() { return num; }
    public void setNum(Integer num) { this.num = num; }
    public Goods getGoods() { return goods; }
    public void setGoods(Goods goods) { this.goods = goods; }
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}