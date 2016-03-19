package com.carbeauty;

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
	public static String getTime(int incre){
		Date date=new Date();
		date.setDate(date.getDate() + incre);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd+HH+mm+ss");

		return simpleDateFormat.format(date);
	}
}
