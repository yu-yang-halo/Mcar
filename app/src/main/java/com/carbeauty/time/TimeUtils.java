package com.carbeauty.time;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtils {
     public static void main(String[] args) {

	 }
	public static String formatDueDate(String timeStr, String format2){

		String format = "yyyy-MM-dd'+'HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
		try {
			Date time = sdf.parse(timeStr);

			return sdf2.format(time);


		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
	public static String formatString(String timeStr, String format2){
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS+08:00";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
		try {
			Date time = sdf.parse(timeStr);

			return sdf2.format(time);


		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
	public static String formatString(String timeStr){
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		try {
			Date time = sdf.parse(timeStr);

			return sdf2.format(time);


		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
    public static String createDateFormat(String hhmm, int incre){
    	Date date=new Date();
    	date.setDate(date.getDate()+incre);
    	date.setHours(Integer.parseInt(hhmm.split(":")[0]));
    	date.setMinutes(Integer.parseInt(hhmm.split(":")[1]));
    	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	return simpleDateFormat.format(date);
    }
	public static String createDateFormat(String year, String month, String day, String hh, String mm,
                                          String formatType){
		Date date=new Date();
		date.setYear(Integer.parseInt(year)-1900);
		date.setMonth(Integer.parseInt(month)-1);
		date.setDate(Integer.parseInt(day));
		date.setHours(Integer.parseInt(hh));
		date.setMinutes(Integer.parseInt(mm));
		date.setSeconds(0);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(formatType);

		return simpleDateFormat.format(date);
	}

	public static String createNowDate(String formatType){
		Date date=new Date();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(formatType);
		return simpleDateFormat.format(date);
	}
	public static Date createNewDate(Date date, int addDays, int hour, int minute){
		if(date==null){
			date=new Date();
		}

		Calendar calendar= Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+addDays);
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE,minute);
		calendar.set(Calendar.MILLISECOND,0);


		return calendar.getTime();
	}


	public static int getThisYear(){
		Date date=new Date();
		return date.getYear()+1900;
	}
	public static int getThisMonth(){
		Date date=new Date();
		return date.getMonth()+1;
	}
	public static int getThisDay(){
		Date date=new Date();
		return date.getDay();
	}

	public static int[] getThisYearMonthDay(Date date){

		int ymd[]=new int[]{date.getYear()+1900,date.getMonth()+1,date.getDate()};


		return ymd;
	}
	public static int[] getThisYearMonthDay(){
		Date date=new Date();
	    int ymd[]=getThisYearMonthDay(date);
		return ymd;
	}
	public static int[] getThisHourMinute(){
		Date date=new Date();
		int hourMin[]=new int[]{date.getHours(),date.getMinutes()};

		return hourMin;
	}

	public static Date getDate(String time, String formatType){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(formatType);
		Date date= null;
		try {
			date = simpleDateFormat.parse(time);
		} catch (ParseException e) {
			date=new Date();
		}
		return date;
	}
	public static Date getServerTime() {
		Date serverDate = null;
		URL url = null;// 取得资源对象
		try {
			url = new URL("http://www.baidu.com");
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect(); // 发出连接
			long ld = uc.getDate(); // 取得网站日期时间
			serverDate = new Date(ld); // 转换为标准时间对象
			System.err.println("服务器时间=================" + serverDate);
		} catch (Exception e) {
			serverDate = new Date();
			System.err.println("本地时间=================" + serverDate);
		}
		return serverDate;
	}
	public static boolean compareToAMoreThanB(String time0, String time1){

		if(time0==null||time1==null){
			return false;
		}

		Date dateA=getDate(time0,"yyyy-MM-dd HH:mm:ss");
		Date dateB=getDate(time1,"yyyy-MM-dd HH:mm:ss");

		if(dateA.compareTo(dateB)>=0){

			return true;
		}else{
			return false;
		}


	}

}
