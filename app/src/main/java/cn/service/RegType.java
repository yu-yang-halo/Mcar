package cn.service;

public enum RegType {
	REGULAR_USER_TYPE(3);
	RegType(int val) {
		this.val=val;
	}
	private int val;
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	
}
