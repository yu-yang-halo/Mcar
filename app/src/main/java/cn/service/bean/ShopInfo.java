package cn.service.bean;

public class ShopInfo {
	private int shopId;
	private String name;
	private float longitude;
	private float latitude;
	private int cityId;
	private String desc;

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ShopInfo(int shopId, String name, float longitude, float latitude,
			int cityId, String desc) {
		super();
		this.shopId = shopId;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cityId = cityId;
		this.desc = desc;
	}



	@Override
	public String toString() {
		return "ShopInfo [shopId=" + shopId + ", name=" + name + ", longitude="
				+ longitude + ", latitude=" + latitude + ", cityId=" + cityId
				+ ", desc=" + desc + "]";
	}

}
