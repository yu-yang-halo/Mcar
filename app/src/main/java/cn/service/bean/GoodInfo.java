package cn.service.bean;

import android.graphics.Bitmap;

public class GoodInfo {

	@Override
	public String toString() {
		return "GoodInfo [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", isShow=" + isShow + ", isChange=" + isChange + ", price="
				+ price + ", src=" + src + ", shopId=" + shopId + ",type="+type+" ]";
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
	private boolean isTop;
	private float price;
	private String colors;
	private String tags;


	public int getBuyNumber() {
		return buyNumber;
	}

	public void setBuyNumber(int buyNumber) {
		this.buyNumber = buyNumber;
	}

	private String src;

	private int buyNumber;


	public boolean isTop() {
		return isTop;
	}

	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}

	private int shopId;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	private int type;
	private String href;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public GoodInfo(int id, String name, String desc, int isShow, int isChange,
			float price, String src, int shopId,int type,String href,boolean isTop) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.isShow = isShow;
		this.isChange = isChange;
		this.price = price;

		this.src = src;
		this.shopId = shopId;
		this.type=type;
		this.href=href;
		this.isTop=isTop;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	private Bitmap bitmap;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
