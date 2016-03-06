package cn.service.bean;

public class GoodInfo {

	@Override
	public String toString() {
		return "GoodInfo [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", isShow=" + isShow + ", isChange=" + isChange + ", price="
				+ price + ", src=" + src + ", shopId=" + shopId + "]";
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
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getIsChange() {
		return isChange;
	}
	public void setIsChange(int isChange) {
		this.isChange = isChange;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	private int id;
	private String name;
	private String desc;
	private int isShow;
	private int isChange;
	private float price;
	private String src;
	private int shopId;
	public GoodInfo(int id, String name, String desc, int isShow, int isChange,
			float price, String src, int shopId) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.isShow = isShow;
		this.isChange = isChange;
		this.price = price;
		this.src = src;
		this.shopId = shopId;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
}
