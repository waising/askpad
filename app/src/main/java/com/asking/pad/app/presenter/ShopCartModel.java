package com.asking.pad.app.presenter;

import com.asking.pad.app.api.Networks;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.RxSchedulers;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 收藏请求model
 * create by linbin
 */

public class ShopCartModel extends BaseModel {


    /**
     * 获取购物车list
     */
    public Observable<ResponseBody> modelShopCartList() {
        return Networks.getInstance().getUserApi()
                .ShopCartList().compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 删除购物车
     */
    public Observable<ResponseBody> modelDelShopCart(String id) {
        return Networks.getInstance().getUserApi()
                .DelShopCart(id).compose(RxSchedulers.<ResponseBody>io_main());
    }


}
