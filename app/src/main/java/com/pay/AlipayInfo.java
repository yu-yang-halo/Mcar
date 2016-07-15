package com.pay;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AlipayInfo {
    private String subject;
    private String body;
    private String price;
    private String out_trade_no;
    private String sign;//签名数据

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public AlipayInfo(String subject, String body, String price, String out_trade_no) {
        this.subject = subject;
        this.body = body;
        this.price = price;
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
}
