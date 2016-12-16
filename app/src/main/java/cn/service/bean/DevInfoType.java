package cn.service.bean;

/**
 * Created by Administrator on 2016/12/16.
 */

public class DevInfoType {
    private int id;
    private String name;
    private String description;
    private int snId;
    private String ip;
    private String port;
    private int state;
    private String createTime;
    private float longitude;
    private float latitude;

    public DevInfoType(int id, String name, String description, int snId, String ip, String port, int state, String createTime, float longitude, float latitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.snId = snId;
        this.ip = ip;
        this.port = port;
        this.state = state;
        this.createTime = createTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    @Override
    public String toString() {
        return "DevInfoType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", snId=" + snId +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", state=" + state +
                ", createTime='" + createTime + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSnId() {
        return snId;
    }

    public void setSnId(int snId) {
        this.snId = snId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
