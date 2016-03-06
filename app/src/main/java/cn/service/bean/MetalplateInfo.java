package cn.service.bean;

public class MetalplateInfo extends OilInfo {
    private int number;
	public MetalplateInfo(int id, String name, String desc, float price,
			int shopId,int number) {
		super(id, name, desc, price, shopId);
		this.number=number;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "MetalplateInfo [number=" + number + "]  ***"+super.toString();
	}


}
