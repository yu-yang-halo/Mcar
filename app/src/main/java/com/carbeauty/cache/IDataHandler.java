package com.carbeauty.cache;

import java.util.List;

import cn.service.bean.CarInfo;
import cn.service.bean.DecorationInfo;
import cn.service.bean.OilInfo;

/**
 * Created by Administrator on 2016/3/12.
 */
public class IDataHandler {
    private static  IDataHandler instance=new IDataHandler();

    private List<DecorationInfo> decorationInfoSet;
    private List<OilInfo> oilInfoSet;
    private List<CarInfo> carInfos;

    public static IDataHandler getInstance(){
        if(instance==null){
            instance=new IDataHandler();
        }
        return instance;
    }

    public List<DecorationInfo> getDecorationInfoSet() {
        return decorationInfoSet;
    }

    public void setDecorationInfoSet(List<DecorationInfo> decorationInfoSet) {
        this.decorationInfoSet = decorationInfoSet;
    }

    public List<OilInfo> getOilInfoSet() {
        return oilInfoSet;
    }

    public void setOilInfoSet(List<OilInfo> oilInfoSet) {
        this.oilInfoSet = oilInfoSet;
    }

    public List<CarInfo> getCarInfos() {
        return carInfos;
    }

    public void setCarInfos(List<CarInfo> carInfos) {
        this.carInfos = carInfos;
    }
}
