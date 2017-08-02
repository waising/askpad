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
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.sharespace.ShareStarHistory;
import com.asking.pad.app.entity.sharespace.ShareStarRank;
import com.asking.pad.app.presenter.ShareModel;
import com.asking.pad.app.presenter.SharePresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.dialog.ShareRuleDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 共享之星的界面
 * linbin
 */
public class ShareStarFragement extends BaseFrameFragment<SharePresenter, ShareModel> {

    @BindView(R.id.ad_avatar)
    ImageView ad_avatar;


    ShareStarGridAdapter shareStarGridAdapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.tv_name)
    TextView tvName;


    @BindView(R.id.tv_answer_num)
    TextView tvAnswerNum;

    @BindView(R.id.tv_accept_num)
    TextView tvAcceptNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_star);
        ButterKnife.bind(this, getContentView());
    }


    @Override
    public void initLoad() {
        super.initLoad();
        requestHistory();
    }

    /**
     * 请求排名，显示前八名
     */
    private void requestRank() {
        mPresenter.presenterShareRank(8, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {
                    List<ShareStarRank> entity = JSON.parseArray(resStr, ShareStarRank.class);
                    if (entity != null && entity.size() > 0) {
                        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
                        recyclerView.setLayoutManager(mgr);
                        shareStarGridAdapter = new ShareStarGridAdapter(getActivity(), entity);
                        recyclerView.setAdapter(shareStarGridAdapter);

                    } else {

                    }

                }

            }

        });
    }

    /**
     * 取第一名
     */
    private void requestHistory() {

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
                        requestRank();
                    } else {

                    }

                }

            }

        });
    }


    public static ShareStarFragement newInstance() {
        ShareStarFragement fragment = new ShareStarFragement();
        return fragment;
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
