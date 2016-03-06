package com.carbeauty.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class ContentCacheUtils {
   private static final String preference_key="cn.carbeauty.key";
   private static final String KEY_USERNAME="key_user_name";
   private static final String KEY_PASSWORD="key_password";
   public static void cacheUsernamePass(Context ctx,String username,String password){
	   SharedPreferences sharedPref = ctx.getSharedPreferences(preference_key, Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPref.edit();
       editor.putString(KEY_USERNAME,username);
       editor.putString(KEY_PASSWORD,password);
	   editor.commit();
   }
   public static String[] getUsernamePass(Context ctx){
	   SharedPreferences sharedPref = ctx.getSharedPreferences(preference_key, Context.MODE_PRIVATE);
	   String username=sharedPref.getString(KEY_USERNAME, null);
       String password=sharedPref.getString(KEY_PASSWORD,null);
       return new String[]{username,password};
   }
   public static void clearUsernamePass(Context ctx){
	   SharedPreferences sharedPref = ctx.getSharedPreferences(preference_key, Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPref.edit();
	   editor.putString(KEY_USERNAME,null);
       editor.putString(KEY_PASSWORD,null);
	   editor.commit();
   }
   
}
