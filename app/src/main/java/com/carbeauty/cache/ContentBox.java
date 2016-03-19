package com.carbeauty.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ContentBox{
	private static final String SharePreference_name="car_android";
	private static SharedPreferences sp;
	private static Editor editor;
	public static final String KEY_CAR_ID="key_carId";
	public static final String KEY_SHOP_ID="key_shopId";
    public static final String KEY_ORDER_TIME="key_order_time";
	public static final String KEY_WAHT_DAY="key_what_day";


	
	public static void loadString(Context ctx, String key,String value) {
         sp=ctx.getSharedPreferences(SharePreference_name, Context.MODE_PRIVATE);
         editor=sp.edit();
         editor.putString(key, value);
         editor.commit();
	}
	
	public static String getValueString(Context ctx,String key,String default_value){
		sp=ctx.getSharedPreferences(SharePreference_name, Context.MODE_PRIVATE);
		String value=sp.getString(key, default_value);
		return value;
	}
	
	public static void loadInt(Context ctx, String key,int value) {
        sp=ctx.getSharedPreferences(SharePreference_name, Context.MODE_PRIVATE);
        editor=sp.edit();
        editor.putInt(key, value);
        editor.commit();
	}
	
	public static int getValueInt(Context ctx,String key,int default_value){
		sp=ctx.getSharedPreferences(SharePreference_name, Context.MODE_PRIVATE);
		int value=sp.getInt(key, default_value);
		return value;
	}
}
