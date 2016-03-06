package cn.service.bean;

public class AssessInfoType {
	private int id;
	private String  assess;
	private int shopId;
	private int level;
	private int userId;
	private String createTime;
	private int orderId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAssess() {
		return assess;
	}
	public void setAssess(String assess) {
		this.assess = assess;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "AssessInfoType [id=" + id + ", assess=" + assess + ", shopId="
				+ shopId + ", level=" + level + ", userId=" + userId
				+ ", createTime=" + createTime + ", orderId=" + orderId + "]";
	}
	public AssessInfoType(int id, String assess, int shopId, int level,
			int userId, String createTime, int orderId) {
		super();
		this.id = id;
		this.assess = assess;
		this.shopId = shopId;
		this.level = level;
		this.userId = userId;
		this.createTime = createTime;
		this.orderId = orderId;
	}
	
}
