package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.CandidateEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.asking.pad.app.widget.dialog.ShareRuleDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 共享之星的界面
 * linbin
 */
public class ShareStarFragement extends BaseFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.ad_avatar)
    AskSimpleDraweeView ad_avatar;


    ShareStarGridAdapter shareStarGridAdapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_star);
        ButterKnife.bind(this, getContentView());
        //   ad_avatar.setImageUrl(userEntity.getAvatar());
        initList();
        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
//        LinearLayoutManager mgr=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mgr);
        shareStarGridAdapter = new ShareStarGridAdapter(getActivity(), initList());
        recyclerView.setAdapter(shareStarGridAdapter);
    }

    private ArrayList<CandidateEntity> initList() {
        ArrayList<CandidateEntity> list = new ArrayList<CandidateEntity>();
        CandidateEntity candidateEntity = new CandidateEntity();
        candidateEntity.setAcceptNum("1");
        candidateEntity.setRankName("fdf");
        list.add(candidateEntity);
        CandidateEntity candidateEntityTwo = new CandidateEntity();
        candidateEntityTwo.setAcceptNum("2");
        candidateEntityTwo.setRankName("ffdf");
        list.add(candidateEntityTwo);
        return list;
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
