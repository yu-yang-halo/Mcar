package cn.service.bean;

/**
 * Created by Administrator on 2017/11/4 0004.
 */

public class ExpressInfo {
    private String expressName;
    private String expressNum;

    public ExpressInfo(String expressName, String expressNum) {
        this.expressName = expressName;
        this.expressNum = expressNum;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }
}
