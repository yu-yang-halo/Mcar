package cn.service.bean;

/**
 * Created by Administrator on 2016/3/31.
 */
public class CameraListType {
    private int id;
    private String name;
    private String uid;
    private String account;
    private String password;
    private int shopId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "CameraListType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", shopId=" + shopId +
                '}';
    }

    public CameraListType(int id, String name, String uid, String account, String password, int shopId) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.account = account;
        this.password = password;
        this.shopId = shopId;
    }
}
