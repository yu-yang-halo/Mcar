package com.carbeauty;

import android.content.Context;

import com.carbeauty.cache.ContentBox;

import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/6/23.
 */
public class UserAddressManager {
    public static final  String KEY_ADDRESS="GOA_ADDRESS";
    public static final  String KEY_NAME="GOA_NAME";
    public static final  String KEY_PHONE="GOA_PHONE";
    public static void cacheUserInfoToLocal(Context ctx,UserInfo userInfo){

        String receivingInfo=userInfo.getReceivingInfo();
        if(receivingInfo==null){
            return;
        }

        String[] namePhoneAddress=receivingInfo.split(",");

        if(namePhoneAddress.length==3){
            ContentBox.loadString(ctx,KEY_NAME,namePhoneAddress[0]);
            ContentBox.loadString(ctx,KEY_PHONE,namePhoneAddress[1]);
            ContentBox.loadString(ctx,KEY_ADDRESS,namePhoneAddress[2]);
        }





    }
    public static String cacheAddressToLocal(Context ctx,
                                           String address,
                                           String name,String phone){

        ContentBox.loadString(ctx,KEY_ADDRESS,address);
        ContentBox.loadString(ctx,KEY_NAME,name);
        ContentBox.loadString(ctx,KEY_PHONE,phone);


        return getReceivingInfo(ctx);

    }


    public static String getReceivingInfo(Context ctx){
        String address=ContentBox.getValueString(ctx,KEY_ADDRESS,"");
        String name=ContentBox.getValueString(ctx,KEY_NAME,"");
        String phone=ContentBox.getValueString(ctx,KEY_PHONE,"");

        String receivingInfo=name+","+phone+","+address;

        return receivingInfo;
    }


}
