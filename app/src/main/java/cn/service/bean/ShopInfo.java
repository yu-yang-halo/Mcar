package cn.service.bean;

public class ShopInfo {
	private int shopId;
	private String name;
	private float longitude;
	private float latitude;
	private int cityId;
	private String desc;
	private String panorama;

	private String openTime;
	private String closeTime;
	private int oilPer;
	private int decoPer;
	private int metaPer;
	private String phone;

	private int kilometerDistance;

	public int getKilometerDistance() {
		return kilometerDistance;
	}

	public void setKilometerDistance(int kilometerDistance) {
		this.kilometerDistance = kilometerDistance;
	}

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
			int cityId, String desc,String panorama,String openTime,String closeTime,
					int oilPer,int decoPer,int metaPer,String phone) {
		super();
		this.shopId = shopId;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cityId = cityId;
		this.desc = desc;
		this.panorama=panorama;
		this.openTime=openTime;
		this.closeTime=closeTime;
		this.oilPer=oilPer;
		this.decoPer=decoPer;
		this.metaPer=metaPer;
		this.phone=phone;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public int getOilPer() {
		return oilPer;
	}

	public void setOilPer(int oilPer) {
		this.oilPer = oilPer;
	}

	public int getDecoPer() {
		return decoPer;
	}

	public void setDecoPer(int decoPer) {
		this.decoPer = decoPer;
	}

	public int getMetaPer() {
		return metaPer;
	}

	public void setMetaPer(int metaPer) {
		this.metaPer = metaPer;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPanorama() {
		return panorama;
	}

	public void setPanorama(String panorama) {
		this.panorama = panorama;
	}


	@Override
	public String toString() {
		return "ShopInfo{" +
				"shopId=" + shopId +
				", name='" + name + '\'' +
				", longitude=" + longitude +
				", latitude=" + latitude +
				", cityId=" + cityId +
				", desc='" + desc + '\'' +
				", panorama='" + panorama + '\'' +
				", openTime='" + openTime + '\'' +
				", closeTime='" + closeTime + '\'' +
				", oilPer=" + oilPer +
				", decoPer=" + decoPer +
				", metaPer=" + metaPer +
				", phone='" + phone + '\'' +
				", kilometerDistance=" + kilometerDistance +
				'}';
	}
}
