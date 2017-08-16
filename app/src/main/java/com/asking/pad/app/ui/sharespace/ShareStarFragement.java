package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.entity.sharespace.ShareStarHistory;
import com.asking.pad.app.entity.sharespace.ShareStarRank;
import com.asking.pad.app.presenter.ShareModel;
import com.asking.pad.app.presenter.SharePresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.dialog.ShareRuleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 共享之星的界面
 * linbin
 */
public class ShareStarFragement extends BaseEvenFrameFragment<SharePresenter, ShareModel> {

    @BindView(R.id.ad_avatar)
    ImageView ad_avatar;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_answer_num)
    TextView tvAnswerNum;

    @BindView(R.id.tv_accept_num)
    TextView tvAcceptNum;

    List<ShareStarRank> dataList = new ArrayList<>();
    ShareStarGridAdapter mAdapter;

    public static ShareStarFragement newInstance() {
        ShareStarFragement fragment = new ShareStarFragement();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_star);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new ShareStarGridAdapter(getActivity(), dataList);
        recyclerView.setAdapter(mAdapter);

        requestRank();
    }

    public void onEventMainThread(AppLoginEvent event) {
        switch (event.type) {
            case AppLoginEvent.LOGIN_SUCCESS:
                requestRank();
                break;
        }
    }

    private void requestRank() {
        /**
         * 请求排名，显示前八名
         */
        mPresenter.presenterShareRank(8, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                dataList.clear();
                dataList.addAll(JSON.parseArray(resStr, ShareStarRank.class));
                mAdapter.notifyDataSetChanged();
            }
        });

        /**
         * 取第一名
         */
        mPresenter.presenterShareHistory(0, 1, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("list");
                    List<ShareStarHistory> entity = JSON.parseArray(resList, ShareStarHistory.class);
                    if (entity != null && entity.size() > 0) {
                        ShareStarHistory shareStarHistory = entity.get(0);
                        if (shareStarHistory != null) {
                            BitmapUtil.displayCirImage(shareStarHistory.getUserAvatar(),R.dimen.space_160, ad_avatar);
                            if (!TextUtils.isEmpty(shareStarHistory.getNickName())) {//昵称为空，显示手机号
                                tvName.setText(shareStarHistory.getNickName());
                            } else {
                                tvName.setText(shareStarHistory.getMobile());
                            }
                            tvAnswerNum.setText(shareStarHistory.getAnswerNum() + "");
                            tvAcceptNum.setText(shareStarHistory.getAdoptNum() + "");
                        }
                    }
                }
            }

        });
    }

    @OnClick({R.id.tv_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rule://共享之星的规则
                ShareRuleDialog shareRuleDialog = new ShareRuleDialog();
                shareRuleDialog.show(getChildFragmentManager(), "");
                break;

        }
    }
}
