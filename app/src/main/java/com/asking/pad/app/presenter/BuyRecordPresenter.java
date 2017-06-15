package com.asking.pad.app.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;

/**
 * 购买记录p层
 */

public class BuyRecordPresenter extends BasePresenter<BuyRecordModel> {

    // 获取购买记录

    public void presenterMyBuyRecords(String ticket, int start, int limit, ApiRequestListener mListener) {
        baseReqStr(mModel.modelMyBuyRecords(ticket, start, limit), mListener);
    }

    // 删除购买记录
    public void presenterDelMyBuyRecords(String orderId, ApiRequestListener mListener) {
        baseReq(mModel.modelDelMyBuyRecords(orderId), "", mListener);
    }

}
