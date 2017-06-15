package com.asking.pad.app.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;

/**
 * 收藏的p层
 */

public class ShopCartPresenter extends BasePresenter<ShopCartModel> {


    /**
     * 购物车list
     */
    public void presenterShopCartList(ApiRequestListener mListener) {
        baseReqStr(mModel.modelShopCartList(), mListener);
    }

    /**
     * 删除购物车
     */
    public void presenterDelShopCart(String id, ApiRequestListener mListener) {
        baseReq(mModel.modelDelShopCart(id), "", mListener);
    }


}
