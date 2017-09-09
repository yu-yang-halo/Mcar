package com.pay;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AlibabaPay {
//    // 商户PID
//    public static final String PARTNER = "2088221558831706";
//    // 商户收款账号
//    public static final String SELLER = "13395602277@163.com";
//    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "AMITT+B/+nA9sS08qO4s1gERdYyCb4SfB8bL0JPAkEAyP0I27+PLIM7zdzqKy9rAlUe2t3SFqShiOhj2q4HKjfMoFHP/8PvdVcRII361/r/f0g9r/r2JDH1rKCv0anBDwJAVkSfmyyrPJZ7o3zg3nCBWtmiIiRFDuA/FO/Y+QoHNXxjwk1YSxV9JjgUYEwj9iV0Szv3bwoRV4YR32f3DOiVvQJAF4pgw5rAf5FOMQgjVVWMm3DrpCbN1Q8Ak6NQzmhvjWvyAD7JMlTq5oNn9wSsvXnX/Xn83SZj3PkvQ2QfwSV/Ew==";
//    // 支付宝公钥
//    public static final String RSA_PUBLIC = "///PrQEB/+ilIZwBC2AQ2UBVOrFXfFl75p6/";

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017090708595026";
    public static final String RSA2_PRIVATE = "AMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCwo7ZwCTkqR+OlbsVQkVpI3TR8qzIou6jatEZDsNLjpcRhZ+JRU4Z6VZPg68cDMGgv9t6rYtCI+IpdDJN0+Zv8uFHVSFmyaoic3+lDE0S2Oyl1lGByvgM0g7cGT3Z8DYC0gVU7iyw2V9KQhKbTpcnlcj8xh0w3E1CSI20m12l3bF5U3GL2uXVs9wY67dyZJZ7Tu5OM5eB/bxw0jftpnkfksCxUJzUlpw5+JpEPSd6dCLC6fVDw9TxERV/3bfsND4EouufkaYygMSnkOskuCEtIarh6vRiMA+V3h1g4b/B9LtO2pl5k8C+cIHa7GQJHZBcBb+iCb+1RF1532d+oXtc9AgMBAAECggEAcjpch1Ufl50HtPrONMzactZB1AEtnbPFfTEpYyrdJgQT292/OFJoW4It8Dzo5MIQDjx6QKDVNE5tw8CXNwUX/7T+jouMHdnQ4InYId07CRF3PScnu+vuzK4E1NrcR1rACXq9sJTOkZH3hCeUbrf+uRdJkHDrM46VAvxP4Ndax7PA+VrN1gGHdosGhRgOhRFn58h4krU97vWUzpfXizs4CVBl8WCuVOllTysxXqHGjzLMsQ2Ap9v/sYbVhiEzFuY9oKJxrtnGYqIiKNqzM+LVt3IgBknictphRzT3gMRrzQ4yxQ1KwZ5NCZc5CgNRHB/D5r0zYq8ZtyBwxIeWojie7QKBgQD671nNg9sWIXNXhtz0Dfu4/Vo699Fy+Jav+uaMRwWvOSN9uye5uAwPZgbvn6skbTFRklTtFzkzyI3jN289Kf8wSgIiL56sGoDoxCuYSiG5BiPB3WNMyieYN2OT9UFLfr3ETl31ZBP15xjbbR/spr9nXTrbQIc8utPAkm04CyHUBwKBgQC0NHT92+Eo/CPYKgzvo++bkayKlYQNJOuxdCPFD1vT8Rcs/T3MDvWbmUPjXMqE896e82xs4PkZQqx1B8/Dju++HkLAkGPYAj7N1mD9hM5F5Ng3C6lw1QKBu38D7JxtFjRqSlPy1bBZh2L1YNQ/xmo5/qZxNZh4Zbv5Jjrtx/kRmwKBgQDo+zG1zqardYNR0LV1I0aECzaraUveI1BFM/66X3a3qaV6JzpLBMatxVzLPsP3niocQnD3cIh7u8mBHQb7EvMMMqqocu4vfKwTcY/IZYvJ7hlk9dgsg8UfXs3loeBt587pZJl4LOMbfOVnVXo+jtNHdRggWVk22X+hXFJrpmpSIQKBgB+AC/NUPEety5Y9w4UgYwIOtNqEyFCD469hXd9Uj/i8bBCxM9w3wTcx02wrDDuxZXjjlFB0AxcnUH4kn2zPz5isMX73vnxD6DaTAuFOFcCiACmfID9Y/Ncwr0NAWRe4ifEKjSIluG3t/J5oDrIXPPpmSTIA7GYfYnxq8mIeUquBAoGALMXsMMTaUcNH/ldLZ/qQmljE9adykuZgkXamVe0WH7sRiXbs+yTi+HHjjQj11mOzgxw8fFWvhiWQoX2Z6CfX+aJ99ON9+YjAVJ40UsaCMKC6YLTFIUtzYmmfcjGo/ssGLGt5dzU4kVcH8o/bM0F5YbMj1A95xjvNFyR4XfMm34U==";


    private  String PARTNER;
    private  String SELLER;

    public AlibabaPay(String PARTNER,String SELLER){
        this.PARTNER=PARTNER;
        this.SELLER=SELLER;

    }

    public String getPayInfoData(AlipayInfo alipayInfo){
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, true,alipayInfo);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey =  RSA2_PRIVATE ;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, true);
        final String payInfo = orderParam + "&" + sign;


        return payInfo;


    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
//    private String sign(String content) {
//        return SignUtils.sign(content, RSA_PRIVATE);
//    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(AlipayInfo alipayInfo) {
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, true,alipayInfo);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        return orderParam;
    }
    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
//    public String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
//        return key;
//    }
}
