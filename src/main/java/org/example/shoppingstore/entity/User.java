package org.example.shoppingstore.entity;

public class User {
    private Integer uid;
    private String username;
    private String password;
    private String address;
    private String phone;
    private Integer type; // 0=普通用户，1=管理员
    private String regTime;
    private Integer status;
    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter & Setter
    public Integer getUid() { return uid; }
    public void setUid(Integer uid) { this.uid = uid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getRegTime() { return regTime; }
    public void setRegTime(String regTime) { this.regTime = regTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}