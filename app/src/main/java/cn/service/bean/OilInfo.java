package cn.service.bean;

public class OilInfo {

	private int id;
	private String name;
	private String desc;
	private float price;
	private int shopId;
	private String src;

	private boolean isExpand;

	public boolean isExpand() {
		return isExpand;
	}

	public void setIsExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "OilInfo [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", price=" + price + ", shopId=" + shopId + "]";
	}

	public OilInfo(int id, String name, String desc, float price, int shopId,String src) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.shopId = shopId;
		this.src=src;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}
