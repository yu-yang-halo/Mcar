package cn.service.bean;

public class MetalplateInfo extends OilInfo {
    private String number;
	public MetalplateInfo(int id, String name, String desc, float price,
			int shopId,String number,String src) {
		super(id, name, desc, price, shopId,src);
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

	private int count=0;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
