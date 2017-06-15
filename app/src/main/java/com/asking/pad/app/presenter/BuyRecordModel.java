package com.asking.pad.app.presenter;

import com.asking.pad.app.api.Networks;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.RxSchedulers;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 笔记请求model
 * create by linbin
 */

public class BuyRecordModel extends BaseModel {


    // 获取购买记录
    public Observable<ResponseBody> modelMyBuyRecords(String ticket, int start, int limit) {
        return Networks.getInstance().getUserApi()
                .getMyBuyRecords(ticket, start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }
    // 删除购买记录
    public Observable<ResponseBody> modelDelMyBuyRecords(String orderId) {
        return Networks.getInstance().getUserApi()
                .delMyBuyRecords(orderId).compose(RxSchedulers.<ResponseBody>io_main());
    }

}
