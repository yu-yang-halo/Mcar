package com.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AlibabaPay {
//    // 商户PID
//    public static final String PARTNER = "2088221558831706";
//    // 商户收款账号
//    public static final String SELLER = "13395602277@163.com";
//    // 商户私钥，pkcs8格式
      public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMjAuBP4DoulGyYfLOT+ZuRtPY5v75bIQSqTkxynvXyopaxisBnS87KBAHTM6Zv7waW5T5miTPMzJbERd+Lo3eTY9k9GOTcF6dNOnyMi82LQ26uedwmN2g8b6rRRvVui2IV6q2yHFMOn9ag1SXZfMnfgtz6WrGfW3t4CbmJpy+UZAgMBAAECgYAYIPm/BLCS3jxeH58gwucRC4Q7i36ri9QxTt1+szaWNhr7ljz+TZmW2eR5JWvqoJ7M0XIc0CIZlQsxthzHvB93PDplgnzhRRhzNJHfvJQm2H9b2ueyFkrDI32sT8b3WLO5OF/uQ298LRyJxPpgPOU4fHzbe4SxV1hvTXKiCbD8hQJBAP2FUCxs3XI87kNCWCClMEovD2W0ZIQjz7i0KP1DzUBlzxf7jCQRATQd+B/iVejcf08iOU3JwzRkEd5mCyMYEBcCQQDKt01EKkgoZyxQx3rAqOPjY6MSEVvyUSjbZyuSP2kFTT4qfXs0D5HQR+nA9sS08qO4s1gERdYyCb4SfB8bL0JPAkEAyP0I27+PLIM7zdzqKy9rAlUe2t3SFqShiOhj2q4HKjfMoFHP/8PvdVcRII361/r/f0g9r/r2JDH1rKCv0anBDwJAVkSfmyyrPJZ7o3zg3nCBWtmiIiRFDuA/FO/Y+QoHNXxjwk1YSxV9JjgUYEwj9iV0Szv3bwoRV4YR32f3DOiVvQJAF4pgw5rAf5FOMQgjVVWMm3DrpCbN1Q8Ak6NQzmhvjWvyAD7JMlTq5oNn9wSsvXnX/Xn83SZj3PkvQ2QfwSV/Ew==";
//    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private  String PARTNER;
    private  String SELLER;

    public AlibabaPay(String PARTNER,String SELLER){
        this.PARTNER=PARTNER;
        this.SELLER=SELLER;

    }



    public String getPayInfoData(AlipayInfo alipayInfo){
        String orderInfo = getOrderInfo(alipayInfo);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = alipayInfo.getSign();

        System.out.println("sign is same:::"+sign(orderInfo).equals(sign));


        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();


        return payInfo;


    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

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

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + alipayInfo.getOut_trade_no()+ "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + alipayInfo.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + alipayInfo.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + alipayInfo.getPrice() + "\"";
        String notify_url=WSConnector.getInstance().getWsUrl()+"alipayNotify";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
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
