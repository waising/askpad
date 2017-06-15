package com.asking.pad.app.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by jswang on 2017/4/20.
 */

public class SelectAddressActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.re_View)
    RecyclerView re_View;

    List<UserEntity> dataList = new ArrayList<>();

    SelectAddressAdapter mAdapter;
    String strAddress = "";
    String strRegionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, getString(R.string.basepacket_plase_choose));

        re_View.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectAddressAdapter(mActivity,dataList);
        mAdapter.getClickItem(clickItem);
        re_View.setAdapter(mAdapter);

        mPresenter.getRegionInfo(0+"",buy1Listener);
    }

    SelectAddressAdapter.ClickItem clickItem = new SelectAddressAdapter.ClickItem() {
        @Override
        public void setClickItem(String regionCode, String regionName) {
            mPresenter.getRegionInfo(regionCode,buy1Listener);
            strAddress+=(regionName+" - ");
            strRegionCode = regionCode;
        }
    };

    ApiRequestListener buy1Listener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String res) {
            dataList.clear();
            dataList.addAll(CommonUtil.parseDataToList(res, new TypeToken<List<UserEntity>>() {
            }));

            if(dataList!=null && dataList.size()>0){
                mAdapter.notifyDataSetChanged();
            }else{
                if(strAddress!=null && strAddress.length()>1){
                    String address = strAddress.substring(0, strAddress.length() - 3);
                    EventBus.getDefault().post(new AddressEvent(address,strRegionCode));
                    finish();
                }
            }
        }
    };
}
