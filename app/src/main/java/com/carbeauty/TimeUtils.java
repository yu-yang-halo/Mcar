package com.carbeauty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtils {
    public static void main(String[] args) {
		System.out.println(createDateFormat("8:30",0));
	 }
    
    public static String createDateFormat(String hhmm,int incre){
    	Date date=new Date();
    	date.setDate(date.getDate()+incre);
    	date.setHours(Integer.parseInt(hhmm.split(":")[0]));
    	date.setMinutes(Integer.parseInt(hhmm.split(":")[1]));
    	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd+HH+mm+ss");
    	
    	return simpleDateFormat.format(date);
    }
	public static String createDateFormat2(String hhmm,int incre){
		Date date=new Date();
		date.setDate(date.getDate()+incre);
		date.setHours(Integer.parseInt(hhmm.split(":")[0]));
		date.setMinutes(Integer.parseInt(hhmm.split(":")[1]));
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

		return simpleDateFormat.format(date);
	}

	public static String getTime(int incre){
		Date date=new Date();
		date.setDate(date.getDate() + incre);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd+HH+mm+ss");

		return simpleDateFormat.format(date);
	}

	public static Date formatString(String timeStr){
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
	public static String getShowTime(String timeStr){
		Date date=formatString(timeStr);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}
	public static String getShowTime(String timeStr,String format){
		Date date=formatString(timeStr);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	public static boolean isOverTime(String endTime){
		Date date=formatString(endTime);
		Date nowTime=new Date();

		int retVal=nowTime.compareTo(date);

		if(retVal>0){
			return true;
		}else{
			return false;
		}
	}
}
