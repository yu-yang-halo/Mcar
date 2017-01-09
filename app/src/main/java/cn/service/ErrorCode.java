/**
 * <p>Package: cn.lztech.elcomm.message</p>
 * <p>File: ErrorCode.java</p>
  *
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>Created at 2010-11-4</p>
 *
 * @author zjiang
 * @version 1.0
 */
package cn.service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Administrator
 * ErrorCode 错误编码及信息
 */
public enum ErrorCode {
	ACCEPT( 0, "成功。"),
	REJECT(1, "服务器错误，请稍后再试！"),
	LOGIN_FAILED(2, "登录失败，请再试！"),
	CONN_TO_WS_ERR(3, "不能连接到服务器，请检查手机通信状态，稍后再试！"),
	BALANCE_NOT_ENOUGH(24,"余额不足"),
	
	INVALID_LOGIN_NAME_PWD(1001, "用户名或密码输入错误。"),
	LOGIN_NAME_NOT_EXIST(1002, "用户名或密码输入错误。"),
	DUP_NAME(1008, "用户名已经被使用。"),
	DEVICE_DOES_NOT_EXISTS(1009, "设备不存在。"),
	DEVICE_BELONG_TO_OTHER(1010, "设备已经属于别的账户。"),
	USER_DOES_NOT_EXIST(1011, "用户不存在。"),
	DEVICE_TYPE_IN_USE(1012, "The device type is used by attribute definition. It could not been deleted"),
	ALERT_NOT_EXISTS(1013, "预警还没有设置。请先设置预警。"),

	DEVICE_TYPE_NOT_EXISTS(1014, "设备类型不存在。"),
	FK_VIOLATION(1015, "数据库外键错误。 "),
	INVALID_USER_ID(1016, "无效用户ID。"),
	NAME_IS_REQUIRED(1017, "请输入名称。"),
	INTERNAL_DB_ERR(1018, "系统错误。"),
	INVALID_SYS_USER(1019, "用户不是系统用户。"),
	CAN_NOT_DEL(1020, "名称被使用，不能删除。"),
	SERVICE_CLIENT_LOGOFF(1021, "The client for the services is not logged in."),
	EMPTY_DATA(1022, "设备属性已经被使用。"),
	PERMISSION_DENY(1023, "登录失效,请重新登录"),
	INVALID_OBJECT_ID(1024, "设备ID错误。"),
	INVALID_FIELD_ID(1025, "字段ID错误。"),
	INVALID_FIELD_NAME(1026, "字段名称错误。"),
	OBJECT_NOT_EXIST(1027, "设备不存在 。"),
	OBJECT_NOT_FOUND(1028, "设备未找到。"),
	CLASS_NOT_FOUND(1029, "类未找到。"),
	OBJECT_NOT_ADEVICE(1030, "此对象不是设备类型。"),
	FIELD_NOT_FOUND(1031, "字段未找到。"),
	ALERT_NOT_FOUND(1032, "预警还未设置。请先设置预警。"),
	INVALID_HIST_SRC_DATA(1033, "数据库错误。请稍后再试！ "),
	CONVERSION_IS_NOT_SUPPORTED(1034, "不支持此变换。 "),
	OBJID_OR_ALERTID_REQ(1035, "请提供设备ID"),
	INVALID_DATA_FMT(1036, "数据格式错误。 "),
	DEVICE_NOT_CONNECTED(1037, "设备不在线。"),
	DEVICE_REJECT(1038, "网络拥堵，请稍后重试！"),
	INVALID_SEC_TOKEN(1039, "超时。请重新登录！"),
	AC_TYPE_UNKNOWN(1040, "请首先进行对码学习。"),
	DEVICE_NOT_REG(1041, "The device is not registered in core server"),
	TASK_NOT_EXIST(1042, "The scheduled task 不存在，请设置!"),//The scheduled task does not exist
	OBJECT_ALREADY_EXIST(1043, "设备已经注册，不能再注册。"),
	
	DEVICE_NOT_ASSIGN_CLASS(1051,"车牌已存在"),
	CAN_NOT_FIND_STATION(1052,"can't find the station"),
	COUPUN_EXIST(1053,"优惠券不可重复领取"),
	COUPUN_HAS_EMPTY(1054,"优惠券已全部被领取"),
	USER_ALREADY_EXIST(1055, "用户已存在"),
	REQ_TIME_OUT(1100, "网络慢。请稍后再试！"),
	INTERNAL_CONN_ERROR(1101, "e联WS接口服务器连接错误"),
	CODE_CACHE_NULL(1048, "Verification code cache is null"),
	DB_QUERY_ERROR(1900, "数据库查询错误"),
	INTERFACE_NOT_SUPPORTED(1901, "不支持此接口"),
	INTERNAL_ERROR(1902, "系统错误。请重新登录再试！"),
	RELOGIN(2012,"密码修改成功，请重新登录！"),
	DATAFORMAT_ERROR(2002,"时间数据格式化错误，请输入正确的时间格式"),
	VEDIO_LOAD_FAIL(2003,"视频加载错误"),
	DEVICE_LOCKED(2004,"设备已锁定，请按设备解锁"),


	UNKNOWERRORCODE(10000,"未解析的errcode"),

	NET_WORK_TIME_OUT(10001,"服务请求超时，请检查网络状态");
	
	
	private static final Map<Integer, ErrorCode> lookup = new HashMap<Integer, ErrorCode>();

	static {
		for (ErrorCode s : EnumSet.allOf(ErrorCode.class))
			lookup.put(s.getCode(), s);
	}

	private int code;
	private String desc;

	private ErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public boolean equals(ErrorCode value) {
		if (this.code == value.getCode())
			return true;
		else
			return false;
	}
	
	public String toString() {
		return this.desc;
	}

	public static ErrorCode get(int code) {
		ErrorCode errCode=lookup.get(code);
		if(errCode==null){
			errCode=ErrorCode.UNKNOWERRORCODE;
		}
		return errCode;
	}
}
