package cn.service.bean;

public class BannerInfoType {
	private int id;
	private String imgName;
	private String src;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	@Override
	public String toString() {
		return "BannerInfoType [id=" + id + ", imgName=" + imgName + ", src="
				+ src + "]";
	}
	public BannerInfoType(int id, String imgName, String src) {
		super();
		this.id = id;
		this.imgName = imgName;
		this.src = src;
	}
	/*


	http://ip:80/upload/banner/图片名
    http://ip:80/upload/goods/店铺ID/图片名
    http://ip:80/upload/promotion/图片名



	 */
	
}
