package com.carbeauty.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2016/7/6.
 */
public class MessageManager {
    private static MessageManager instance=new MessageManager();
    private Stack<JPMessage> messageStack=new Stack<JPMessage>();
    private static  final  String MESSAGE_CACHE_KEY="message_cahce_key";
    private MessageManager(){

    }
    public static MessageManager getInstance(){
        if(instance==null){
            instance=new MessageManager();
        }
        return instance;
    }

    public void addJPMessage(Context ctx,JPMessage jpMessage){

        if(jpMessage==null||jpMessage.getMsgContent()==null||jpMessage.getMsgContent().equals("")){
            return;
        }

       getMessageStack(ctx);

       messageStack.add(jpMessage);
       Gson gson=new Gson();
       String messageJSON=gson.toJson(messageStack,new TypeToken<Stack<JPMessage>>(){}.getType());

       ContentBox.loadString(ctx,MESSAGE_CACHE_KEY,messageJSON);


    }

    public void emptyMessageStack(Context ctx){
        getMessageStack(ctx);
        messageStack.removeAllElements();
        ContentBox.loadString(ctx,MESSAGE_CACHE_KEY,null);
    }

    public Stack<JPMessage> getMessageStack(Context ctx){
        String mValue=ContentBox.getValueString(ctx,MESSAGE_CACHE_KEY,null);
        Gson gson=new Gson();
        if(mValue!=null){
            messageStack=gson.fromJson(mValue,new TypeToken<Stack<JPMessage>>(){}.getType());
        }
        if(messageStack==null){
            messageStack=new Stack<JPMessage>();
        }
        return messageStack;
    }
    public static class JPMessage{

        /**
         *消息类型 洗车，养护，钣金喷漆，保养，耗材
         */
        private  int orderId;
        private  int orderType;

        private  String msgContent;

        public JPMessage(String msgContent) {
            this.msgContent = msgContent;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }
    }

}

