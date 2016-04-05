package cn.service.bean;

public class UserInfo {

	private int userId;
	private String loginName;
	private String realName;
	private String password;

	public UserInfo(int userId, String loginName, String realName,
			String password, String email, String phone, String wechatId,
			String regTime, int type, int shopId) {
		super();
		this.userId = userId;
		this.loginName = loginName;
		this.realName = realName;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.wechatId = wechatId;
		this.regTime = regTime;
		this.type = type;
		this.shopId = shopId;
	}

	private String email;
	private String phone;
	private String wechatId;
	private String regTime;
	private int type;
	private int shopId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", loginName=" + loginName
				+ ", realName=" + realName + ", password=" + password
				+ ", email=" + email + ", phone=" + phone + ", wechatId="
				+ wechatId + ", regTime=" + regTime + ", type=" + type
				+ ", shopId=" + shopId + "]";
	}

}
