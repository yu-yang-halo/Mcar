package cn.service.bean;

/**
 * Created by Administrator on 2016/3/24.
 */
public class GoodsOrderListType {
    private int id;
    private String goodsInfo;
    private String createTime;
    private int    userId;
    private int shopId;
    private float    price;
    private String address;
    private String   name;
    private String phone;
    private int  state;
    private String processTime;
    private String tradeNo;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public GoodsOrderListType(int id, String goodsInfo, String createTime, int userId, int shopId, float price, String address, String name, String phone, int state, String processTime) {
        this.id = id;
        this.goodsInfo = goodsInfo;
        this.createTime = createTime;
        this.userId = userId;
        this.shopId = shopId;
        this.price = price;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.processTime = processTime;
    }

    @Override
    public String toString() {
        return "GoodsOrderListType{" +
                "id=" + id +
                ", goodsInfo='" + goodsInfo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userId=" + userId +
                ", shopId=" + shopId +
                ", price=" + price +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", state=" + state +
                ", processTime='" + processTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
}
