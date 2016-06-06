package cn.service.bean;

public class DecorationInfo extends OilInfo {

	public DecorationInfo(int id, String name, String desc, float price,
			int shopId,String src) {
		super(id, name, desc, price, shopId,src);
	}

	@Override
	public String toString() {
		return "DecorationInfo [getId()=" + getId() + ", getName()="
				+ getName() + ", getDesc()=" + getDesc() + ", getPrice()="
				+ getPrice() + ", getShopId()=" + getShopId() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}
	
}
