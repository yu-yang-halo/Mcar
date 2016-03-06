package cn.service.bean;

import java.util.List;

public class OilOrderInfo {
	private int id;
	private int type;
	private int state;
	private int payState;
	private int userId;
	private int carId;
	private int shopId;
	private int stationId;
	private float price;
	private int couponId;
	private String createTime;
	private String finishTime;
	private String orderTime;
	private List<OilOrderNumberInfo> oilOrderNumber;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getPayState() {
		return payState;
	}
	public void setPayState(int payState) {
		this.payState = payState;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
	public List<OilOrderNumberInfo> getOilOrderNumber() {
		return oilOrderNumber;
	}
	public void setOilOrderNumber(List<OilOrderNumberInfo> oilOrderNumber) {
		this.oilOrderNumber = oilOrderNumber;
	}
	@Override
	public String toString() {
		return "OilOrderInfo [id=" + id + ", type=" + type + ", state=" + state
				+ ", payState=" + payState + ", userId=" + userId + ", carId="
				+ carId + ", shopId=" + shopId + ", stationId=" + stationId
				+ ", price=" + price + ", couponId=" + couponId
				+ ", createTime=" + createTime + ", finishTime=" + finishTime
				+ ", orderTime=" + orderTime + ", oilOrderNumber="
				+ oilOrderNumber + "]";
	}
	public OilOrderInfo(int id, int type, int state, int payState, int userId,
			int carId, int shopId, int stationId, float price, int couponId,
			String createTime, String finishTime, String orderTime,
			List<OilOrderNumberInfo> oilOrderNumber) {
		super();
		this.id = id;
		this.type = type;
		this.state = state;
		this.payState = payState;
		this.userId = userId;
		this.carId = carId;
		this.shopId = shopId;
		this.stationId = stationId;
		this.price = price;
		this.couponId = couponId;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.orderTime = orderTime;
		this.oilOrderNumber = oilOrderNumber;
	}
	public static class OilOrderNumberInfo{
		private int id;
		private int oilId;
		private int oilOrderId;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getOilId() {
			return oilId;
		}
		public void setOilId(int oilId) {
			this.oilId = oilId;
		}
		public int getOilOrderId() {
			return oilOrderId;
		}
		public void setOilOrderId(int oilOrderId) {
			this.oilOrderId = oilOrderId;
		}
		@Override
		public String toString() {
			return "OilOrderNumberInfo [id=" + id + ", oilId=" + oilId
					+ ", oilOrderId=" + oilOrderId + "]";
		}
		public OilOrderNumberInfo(int id, int oilId, int oilOrderId) {
			super();
			this.id = id;
			this.oilId = oilId;
			this.oilOrderId = oilOrderId;
		}
		
	}

	
}
