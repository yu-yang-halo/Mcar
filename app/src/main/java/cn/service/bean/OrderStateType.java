package cn.service.bean;

public class OrderStateType {
	private String orderTime;
	private boolean  isFull;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	private boolean  checked;

	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	public OrderStateType(String orderTime, boolean isFull) {
		super();
		this.orderTime = orderTime;
		this.isFull = isFull;
	}
	@Override
	public String toString() {
		return "OrderStateType [orderTime=" + orderTime + ", isFull=" + isFull
				+ "]";
	}
	
}
