package cn.service.bean;

public class MetalplateInfo extends OilInfo {
    private String number;
	public MetalplateInfo(int id, String name, String desc, float price,
			int shopId,String number) {
		super(id, name, desc, price, shopId);
		this.number=number;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "MetalplateInfo [number=" + number + "]  ***"+super.toString();
	}


}
