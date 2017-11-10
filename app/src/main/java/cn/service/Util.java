/**
 * 
 * @Title: Util.java
 * @Package cn.lztech.elwsapi
 * @Description:
 *
 * <p>Copyright: Copyright (c) 2011 Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>Developed By: Hefei Lianzheng Electronic Technology, Inc</p>
 *
 * @Author Kai
 * @Date 2011-11-7 下午04:20:17
 * @Version 1.0 
 *
 */
package cn.service;

import android.graphics.Color;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	public static final int REQ_TIME_OUT = 6 * 1000;
	public static final int READ_TIME_OUT = 11 * 1000;

	public static String formatSpeDate(Date date, int hour, int min) {
		date.setHours(hour);
		date.setMinutes(min);
		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'+08:00'";
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static Date formatString(String timeStr) {
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date time = sdf.parse(timeStr);
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isHttpsRequest(String url) {
		int index = url.indexOf("https://");
		if (index < 0) {
			return false;
		} else {
			return true;
		}
	}
	public static String formatHtml(String str){
		int start=str.indexOf("<");
		int end=str.indexOf(">");
		while (start>=0&&end>=0){

			str=str.replace(str.substring(start,end+1),"");
			start=str.indexOf("<");
			end=str.indexOf(">");
		}
		return str;
	}

	public  static int stringToInt(String val){
		int value=Integer.MIN_VALUE;
		try{
			value=Integer.parseInt(val.trim());
		}catch (NumberFormatException e){

		}
		return value;
	}

	public static int rgbArrsToInt(String str){

		int[] arrs=rgbArrs(str);
		return Color.argb(255,arrs[0],arrs[1],arrs[2]);
	}

	public static int[] rgbArrs(String str){
		str=str.replace("+","");
		//rgb(0, 0, 0);
		int[] rgbArrrs=new int[]{0,0,0};
		int start=str.indexOf("(");
		int end=str.indexOf(")");
		String subStr= str.substring(start+1,end);
		String[] subArrs=subStr.split(",");

		if(subArrs==null||subArrs.length!=3){
			return rgbArrrs;
		}
		rgbArrrs[0]=stringToInt(subArrs[0]);
		rgbArrrs[1]=stringToInt(subArrs[1]);
		rgbArrrs[2]=stringToInt(subArrs[2]);
		return rgbArrrs;
	}
	public static boolean isEmpty(String val) {
		if (val == null) {
			return true;
		}
		if (val.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isPhoneNumber(String mobiles){
		Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);  
		System.out.println(m.matches()+"---");  
		return m.matches();  
	}
	public static boolean isOverMinSize(String value,int minLen){
		if(value==null){
			return false;
		}
		if(value.trim().length()>=minLen){
			return true;
		}
		return false;
	}
}
