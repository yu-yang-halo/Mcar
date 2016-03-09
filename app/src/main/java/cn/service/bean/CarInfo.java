package cn.service.bean;

public class CarInfo {

	private int id;
	private int type;//
	private String number;
	private int userId;

	public CarInfo(int id, int type, String number, int userId) {
		super();
		this.id = id;
		this.type = type;
		this.number = number;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CarInfo [id=" + id + ", type=" + type + ", number=" + number
				+ ", userId=" + userId + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
