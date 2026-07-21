package org.example.shoppingstore.entity;

import java.math.BigDecimal;

public class Goods {
    private Integer gid;
    private String gname;
    private BigDecimal price;
    private Integer stock;
    private String gdesc;
    private String gimg;
    public Goods() {}

    // Getter & Setter
    public Integer getGid() { return gid; }
    public void setGid(Integer gid) { this.gid = gid; }
    public String getGname() { return gname; }
    public void setGname(String gname) { this.gname = gname; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getGdesc() { return gdesc; }
    public void setGdesc(String gdesc) { this.gdesc = gdesc; }
    public String getGimg() { return gimg; }
    public void setGimg(String gimg) { this.gimg = gimg; }
}