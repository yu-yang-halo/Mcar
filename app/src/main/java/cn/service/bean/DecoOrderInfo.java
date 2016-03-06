package cn.service.bean;

import java.util.List;

public class DecoOrderInfo extends OilOrderInfo {

	public DecoOrderInfo(int id, int type, int state, int payState, int userId,
			int carId, int shopId, int stationId, float price, int couponId,
			String createTime, String finishTime, String orderTime,
			List<DecoOrderNumber> decoOrderNumbers) {
		super(id, type, state, payState, userId, carId, shopId, stationId, price,
				couponId, createTime, finishTime, orderTime, null);
		this.decoOrderNumbers=decoOrderNumbers;
	}
    private List<DecoOrderNumber> decoOrderNumbers;
	public List<DecoOrderNumber> getDecoOrderNumbers() {
		return decoOrderNumbers;
	}
	public void setDecoOrderNumbers(List<DecoOrderNumber> decoOrderNumbers) {
		this.decoOrderNumbers = decoOrderNumbers;
	}
	@Override
	public String toString() {
		return "DecoOrderInfo [decoOrderNumbers=" + decoOrderNumbers + "] "+super.toString();
	}
	
	public static class DecoOrderNumber{
		private int id;
		private int decoOrderId;
		private int decoId;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getDecoOrderId() {
			return decoOrderId;
		}
		public void setDecoOrderId(int decoOrderId) {
			this.decoOrderId = decoOrderId;
		}
		public int getDecoId() {
			return decoId;
		}
		public void setDecoId(int decoId) {
			this.decoId = decoId;
		}
		@Override
		public String toString() {
			return "DecoOrderNumber [id=" + id + ", decoOrderId=" + decoOrderId
					+ ", decoId=" + decoId + "]";
		}
		public DecoOrderNumber(int id, int decoOrderId, int decoId) {
			super();
			this.id = id;
			this.decoOrderId = decoOrderId;
			this.decoId = decoId;
		}
		
	}
}
