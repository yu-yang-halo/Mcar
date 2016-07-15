package cn.service.bean;

/**
 * Created by Administrator on 2016/7/14.
 */
public class AlipayInfoType {
    private  int id;
    private String aliPid;
    private String aliKey;
    private String sellerEmail;

    public int getId() {
        return id;
    }

    public AlipayInfoType(int id, String aliPid, String aliKey, String sellerEmail) {
        this.id = id;
        this.aliPid = aliPid;
        this.aliKey = aliKey;
        this.sellerEmail = sellerEmail;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getAliKey() {
        return aliKey;
    }

    public void setAliKey(String aliKey) {
        this.aliKey = aliKey;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
}
