package cn.service.bean;

public class PromotionInfoType extends BannerInfoType {

	public PromotionInfoType(int id, String imgName, String src) {
		super(id, imgName, src);
	}

	@Override
	public String toString() {
		return "PromotionInfoType [getId()=" + getId() + ", getImgName()="
				+ getImgName() + ", getSrc()=" + getSrc() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}
    
}
