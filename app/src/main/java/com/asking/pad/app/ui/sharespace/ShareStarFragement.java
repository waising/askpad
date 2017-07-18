package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.sharespace.ShareStarHistory;
import com.asking.pad.app.presenter.ShareModel;
import com.asking.pad.app.presenter.SharePresenter;
import com.asking.pad.app.widget.AskSimpleDraweeView;
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
    AskSimpleDraweeView ad_avatar;


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
        requestHistory();

//        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
//        recyclerView.setLayoutManager(mgr);
//        shareStarGridAdapter = new ShareStarGridAdapter(getActivity(), initList());
//        recyclerView.setAdapter(shareStarGridAdapter);
    }

    /**
     * 取第一名
     */
    private void requestHistory() {

        mPresenter.presenterShareHistory(0, 1, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {
                    List<ShareStarHistory> entity = JSON.parseArray(resStr, ShareStarHistory.class);
                    if (entity != null && entity.size() > 0) {
                      //  ad_avatar.setImageUrl(entity.get(0).);
                        tvName.setText(entity.get(0).getNickName());
                        tvAnswerNum.setText(entity.get(0).getAnswerNum());
                        tvAcceptNum.setText(entity.get(0).getAdoptNum());
                        requestUserAvator(entity.get(0).getUserId());
                    } else {

                    }

                }

            }

        });
    }

    private void requestUserAvator(String userId) {


        mPresenter.presenterUserAvator(userId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {


                    List<ShareStarHistory> entity = JSON.parseArray(resStr, ShareStarHistory.class);
                    if (entity != null && entity.size() > 0) {

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
            case R.id.tv_rule:
                ShareRuleDialog shareRuleDialog = new ShareRuleDialog();
                shareRuleDialog.show(getChildFragmentManager(), "");
                break;

        }
    }


}
