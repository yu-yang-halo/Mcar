package com.carbeauty;

/**
 * Created by Administrator on 2016/3/11.
 */
public class Constants {
    public static final int SEARCH_TYPE_DECO=5;
    public static final int SEARCH_TYPE_OIL=6;
    public static final int SEARCH_TYPE_META=7;

    public final static String AC_TYPE="activity_task_type";
    public final static String ORDER_RESULT_IS_OK="order_result_is_ok";
    public final static String OFFER_PRICE="offer_price";

    public final static int AC_TYPE_WASH=1000;
    public final static int AC_TYPE_OIL=1001;
    public final static int AC_TYPE_META=1002;

    public final static int AC_TYPE_META2=1003;

    public final static int AC_TYPE_GOOD=1004;



    public final static int AC_TYPE_ORDER_BEFORE=1004;
    public final static int AC_TYPE_ORDER_AFTER=1005;

    public final static int PAY_STATE_UNFINISHED=0;//支付状态未完成
    public final static int PAY_STATE_FINISHED=1;//支付状态已完成


    public final static int TYPE_PAY_ONLINE=0;//在线支付
    public final static int TYPE_PAY_TOSHOP=1;//到店支付
    public final static int STATE_ORDER_UNFINISHED=0;//订单状态未完成
    public final static int STATE_ORDER_FINISHED=1;//已经完成
    public final static int STATE_ORDER_WAIT=2;//等待客服确认
    public final static int STATE_ORDER_CANCEL=3;//取消


    public final static int GOOD_STATE_ORDER_NO_DELIVE=0;//0未发货
    public final static int GOOD_STATE_ORDER_DELIVE=1;//1已发货
    public final static int GOOD_STATE_ORDER_NO_PAY=2;// 2 未支付
    public final static int GOOD_STATE_ORDER_PAY=3;//3已支付
    public final static int GOOD_STATE_ORDER_CANCEL=4;//取消

    public final static int PAY_TYPE_ALIPAY=1;//支付宝
    public final static int PAY_TYPE_WECHAT=2;//微信
    public final static int PAY_TYPE_COUPON=3;//优惠券



    public final static int SEARCH_SHOP=1;//搜索类型 按店搜索
    public final static int SEARCH_USER=2;//搜索类型 按用户搜索


    /*
        优惠券 type 和orderType
     */
    public static final int COUPON_TYPE_DISCOUNT=0;//折扣
    public static final int COUPON_TYPE_PRICE=1;//价格


    public static final int ORDER_TYPE_ALL=0;
    public static final int ORDER_TYPE_DECO=1;
    public static final int ORDER_TYPE_OIL=2;
    public static final int ORDER_TYPE_META=3;
    public static final int ORDER_TYPE_AUTO=4;


    public static final int USER_TYPE_ADMIN=2;
    public static final int USER_TYPE_NOMAL=3;
    public static final int USER_TYPE_VIP=5;

}
