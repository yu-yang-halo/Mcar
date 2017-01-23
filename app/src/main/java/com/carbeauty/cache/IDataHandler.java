package com.carbeauty.cache;

import java.util.List;

import cn.service.bean.CarInfo;
import cn.service.bean.DecorationInfo;
import cn.service.bean.MetalplateInfo;
import cn.service.bean.OilInfo;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/12.
 */
public class IDataHandler {
    private static  IDataHandler instance=new IDataHandler();

    private List<DecorationInfo> decorationInfoSet;
    private List<OilInfo> oilInfoSet;
    private List<CarInfo> carInfos;
    private List<MetalplateInfo> metalplateInfoSet;
    private List<ShopInfo> shopInfos;


    public ShopInfo getShopInfo(int shopId){
        ShopInfo m_shopIndo=null;

        List<ShopInfo> shopInfos=this.getShopInfos();
        if(shopInfos==null){
            return null;
        }

        for (ShopInfo shopInfo:shopInfos) {
           if(shopInfo.getShopId() == shopId) {
               m_shopIndo=shopInfo;
           }
        }

        return m_shopIndo;

    }

    public static IDataHandler getInstance(){
        if(instance==null){
            instance=new IDataHandler();
        }
        return instance;
    }

    public List<DecorationInfo> getDecorationInfoSet() {
        return decorationInfoSet;
    }

    public List<ShopInfo> getShopInfos() {
        return shopInfos;
    }

    public void setShopInfos(List<ShopInfo> shopInfos) {
        this.shopInfos = shopInfos;
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

    public List<MetalplateInfo> getMetalplateInfoSet() {
        return metalplateInfoSet;
    }

    public void setMetalplateInfoSet(List<MetalplateInfo> metalplateInfoSet) {
        this.metalplateInfoSet = metalplateInfoSet;
    }

    public void setCarInfos(List<CarInfo> carInfos) {
        this.carInfos = carInfos;
    }

}
