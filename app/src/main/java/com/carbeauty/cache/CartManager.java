package com.carbeauty.cache;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/3/29.
 */
public class CartManager {
    private List<MyCartClass> myCartClassList=new ArrayList<MyCartClass>();


    public void clearCartList(){
        myCartClassList.clear();
    }

    public boolean addToCart(int id,int count,Bitmap bm,GoodInfo goodInfo){

        int pos=-1;

        for (int i=0;i<myCartClassList.size();i++){
              if(id==myCartClassList.get(i).getId()){
                  pos=i;
                  break;
              }
        }
        if(pos>=0){
            if(count!=myCartClassList.get(pos).getCount()){
                myCartClassList.get(pos).setCount(count);
            }else{
                return false;
            }
        }else{
            MyCartClass mClass=new MyCartClass(id,count,bm);
            mClass.setGoodInfo(goodInfo);
            myCartClassList.add(mClass);

        }

        return true;

    }

    public List<MyCartClass> getMyCartClassList() {
        return myCartClassList;
    }

    private static CartManager instance=new CartManager();
    private CartManager(){

    }
    public static CartManager getInstance(){
        return instance;
    }

    public class MyCartClass{
        int id;
        int count;
        Bitmap bitmap;
        GoodInfo goodInfo;
        boolean checkYN;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isCheckYN() {
            return checkYN;
        }

        public void setCheckYN(boolean checkYN) {
            this.checkYN = checkYN;
        }

        public MyCartClass(int id, int count, Bitmap bitmap) {
            this.id = id;
            this.count = count;
            this.bitmap = bitmap;
        }

        public GoodInfo getGoodInfo() {
            return goodInfo;
        }

        public void setGoodInfo(GoodInfo goodInfo) {
            this.goodInfo = goodInfo;
        }

        @Override
        public String toString() {
            return "MyCartClass{" +
                    "id=" + id +
                    ", count=" + count +
                    '}';
        }
    }
}
