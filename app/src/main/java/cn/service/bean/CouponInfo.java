package cn.service.bean;

public class CouponInfo {
	private int id;
	private String number;
	private String name;
	private String desc;
	private int shopId;

	public CouponInfo(int id, String number, String name, String desc,
			int shopId, float price, float discount, int type,
			String createTime, String endTime, int createUserId, int useUserId,
			int isUsed, int orderType) {
		super();
		this.id = id;
		this.number = number;
		this.name = name;
		this.desc = desc;
		this.shopId = shopId;
		this.price = price;
		this.discount = discount;
		this.type = type;
		this.createTime = createTime;
		this.endTime = endTime;
		this.createUserId = createUserId;
		this.useUserId = useUserId;
		this.isUsed = isUsed;
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "CouponInfo [id=" + id + ", number=" + number + ", name=" + name
				+ ", desc=" + desc + ", shopId=" + shopId + ", price=" + price
				+ ", discount=" + discount + ", type=" + type + ", createTime="
				+ createTime + ", endTime=" + endTime + ", createUserId="
				+ createUserId + ", useUserId=" + useUserId + ", isUsed="
				+ isUsed + ", orderType=" + orderType + "]";
	}

	private float price;
	private float discount;
	private int type;
	private String createTime;
	private String endTime;
	private int createUserId;
	private int useUserId;
	private int isUsed;
	private int orderType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public float getPrice() {
		return (int)price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public int getUseUserId() {
		return useUserId;
	}

	public void setUseUserId(int useUserId) {
		this.useUserId = useUserId;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
}
