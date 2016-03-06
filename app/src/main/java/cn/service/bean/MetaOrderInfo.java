package cn.service.bean;

import java.util.List;

public class MetaOrderInfo extends OilOrderInfo {

	public MetaOrderInfo(int id, int type, int state, int payState, int userId,
			int carId, int shopId, int stationId, float price, int couponId,
			String createTime, String finishTime, String orderTime,
			List<MetaOrderNumber> metaOrderNumbers,
			List<MetaOrderImg> metaOrderImgs) {
		super(id, type, state, payState, userId, carId, shopId, stationId,
				price, couponId, createTime, finishTime, orderTime, null);
		this.metaOrderNumbers = metaOrderNumbers;
		this.metaOrderImgs = metaOrderImgs;
	}

	private List<MetaOrderNumber> metaOrderNumbers;
	private List<MetaOrderImg> metaOrderImgs;

	public List<MetaOrderNumber> getMetaOrderNumbers() {
		return metaOrderNumbers;
	}

	public void setMetaOrderNumbers(List<MetaOrderNumber> metaOrderNumbers) {
		this.metaOrderNumbers = metaOrderNumbers;
	}

	public List<MetaOrderImg> getMetaOrderImgs() {
		return metaOrderImgs;
	}

	public void setMetaOrderImgs(List<MetaOrderImg> metaOrderImgs) {
		this.metaOrderImgs = metaOrderImgs;
	}
	
	

public static class MetaOrderNumber {
	private int id;
	private int metaId;
	private int metaOrderId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMetaId() {
		return metaId;
	}

	public void setMetaId(int metaId) {
		this.metaId = metaId;
	}

	public int getMetaOrderId() {
		return metaOrderId;
	}

	public void setMetaOrderId(int metaOrderId) {
		this.metaOrderId = metaOrderId;
	}

	@Override
	public String toString() {
		return "MetaOrderNumber [id=" + id + ", metaId=" + metaId
				+ ", metaOrderId=" + metaOrderId + "]";
	}

	public MetaOrderNumber(int id, int metaId, int metaOrderId) {
		super();
		this.id = id;
		this.metaId = metaId;
		this.metaOrderId = metaOrderId;
	}

}

public static class MetaOrderImg {
	private int id;
	private int metaOrderId;
	private String imgName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMetaOrderId() {
		return metaOrderId;
	}

	public void setMetaOrderId(int metaOrderId) {
		this.metaOrderId = metaOrderId;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	@Override
	public String toString() {
		return "MetaOrderImg [id=" + id + ", metaOrderId=" + metaOrderId
				+ ", imgName=" + imgName + "]";
	}

	public MetaOrderImg(int id, int metaOrderId, String imgName) {
		super();
		this.id = id;
		this.metaOrderId = metaOrderId;
		this.imgName = imgName;
	}

}

}
