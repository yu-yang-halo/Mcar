package cn.service.bean;

public class CityInfo {
	private int cityId;
	private String name;
	private int shopNum;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getShopNum() {
		return shopNum;
	}

	public void setShopNum(int shopNum) {
		this.shopNum = shopNum;
	}

	@Override
	public String toString() {
		return "CityInfo [cityId=" + cityId + ", name=" + name + ", shopNum="
				+ shopNum + "]";
	}

	public CityInfo(int cityId, String name, int shopNum) {
		super();
		this.cityId = cityId;
		this.name = name;
		this.shopNum = shopNum;
	}

}
