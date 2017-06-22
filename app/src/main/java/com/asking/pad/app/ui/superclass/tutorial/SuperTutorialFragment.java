package com.asking.pad.app.ui.superclass.tutorial;

/**
 * Created by jswang on 2017/4/19.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.MusicPlayer;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.tutorial.adapter.SuperQueTimeAdapter;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/10.
 */

public class SuperTutorialFragment extends BaseFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.rl_tip)
    View rl_tip;

    @BindView(R.id.tv_tip)
    TextView tv_tip;

    @BindView(R.id.ad_voice)
    AskSimpleDraweeView ad_voice;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    boolean isBuy;
    String gradeId;
    String knowledgeId;
    String classType;

    private MusicPlayer musicPlayer;
    private String musicUrl;

    int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    /**
     * 判断是否重新选择过章节内容
     */
    private boolean isChange = true;

    String[] tits;

    public static SuperTutorialFragment newInstance( Bundle bundle) {
        SuperTutorialFragment fragment = new SuperTutorialFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_super_tutorial);
        ButterKnife.bind(this, getContentView());

        Bundle bundle = getArguments();
        if (bundle != null) {
            gradeId = bundle.getString("gradeId");
            knowledgeId = bundle.getString("knowledgeId");
            classType = bundle.getString("classType");
            isBuy = bundle.getBoolean("isBuy");
        }
    }

    @Override
    public void initView() {
        super.initView();
        tits = getResources().getStringArray(R.array.super_free_menu);
        musicPlayer = MusicPlayer.getPlayer().bindVoice(ad_voice);
        ad_voice.setImageUrl(Constants.GifHeard + "voice.gif");

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pauseMusicPlayer();
                switch (checkedId) {
                    case R.id.rb_tab1:
                        setFragmentPage(0);
                        break;
                    case R.id.rb_tab2:
                        setFragmentPage(1);
                        break;
                    case R.id.rb_tab3:
                        setFragmentPage(2);
                        break;
                    case R.id.rb_tab4:
                        setFragmentPage(3);
                        break;
                }
            }
        });

        fragments.add(SuperSayFragment.newInstance(gradeId,knowledgeId,isBuy));
        fragments.add(SuperQueTimeFragment.newInstance(gradeId,knowledgeId,isBuy,musicPlayer,new SuperQueTimeAdapter.OnCommItemListener() {
            @Override
            public void onPlayVoice(int e) {
                playVoice(e);
            }
        }));
        fragments.add(SuperSpeakerFragment.newInstance(gradeId,knowledgeId,classType,isBuy));
        fragments.add(SuperSumaryFragment.newInstance(gradeId,knowledgeId,isBuy));

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment,fragments.get(0))
                .add(R.id.fragment,fragments.get(1))
                .add(R.id.fragment,fragments.get(2))
                .add(R.id.fragment,fragments.get(3))
                .hide(fragments.get(1))
                .hide(fragments.get(2))
                .hide(fragments.get(3))
                .show(fragments.get(0)).commit();

        setTabView(mCurTabIndex);
    }

    private void setTabView(int index){
        mCurTabIndex = index;
        switch (index) {
            case 0:
                ad_voice.setVisibility(View.VISIBLE);
                tv_tip.setText(tits[index]);
                break;
            case 1:
                ad_voice.setVisibility(View.GONE);
                tv_tip.setText(tits[index]);
                break;
            case 2:
                ad_voice.setVisibility(View.GONE);
                tv_tip.setText(tits[index]);
                break;
            case 3:
                ad_voice.setVisibility(View.GONE);
                tv_tip.setText(tits[index]);
                break;
        }
    }

    private void setFragmentPage(int index) {
        FragmentTransaction trx = getChildFragmentManager().beginTransaction();
        trx.hide(fragments.get(mCurTabIndex));
        if (!fragments.get(index).isAdded()) {
            trx.add(R.id.fragment, fragments.get(index));
        }
        trx.show(fragments.get(index)).commit();

        setTabView(index);
    }

    public void refreshData(String gradeId, String knowledgeId, String classType, boolean isBuy) {
        this.gradeId = gradeId;
        this.knowledgeId = knowledgeId;
        this.classType = classType;
        this.isBuy = isBuy;

        isChange = true;

        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }

        ((SuperSayFragment)fragments.get(0)).refreshData(gradeId,knowledgeId,isBuy);
        ((SuperQueTimeFragment)fragments.get(1)).refreshData(gradeId,knowledgeId,isBuy);
        ((SuperSpeakerFragment)fragments.get(2)).refreshData(gradeId,knowledgeId,classType,isBuy);
        ((SuperSumaryFragment)fragments.get(3)).refreshData(gradeId,knowledgeId,isBuy);
    }

    private void adVoice() {
        if (isChange) {//有重新选择章节
            if (!musicPlayer.isPlaying()) {
                mPresenter.getVoicePath(isBuy,gradeId, knowledgeId, 1, 0, new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        playMusic(res, -1);
                    }
                });
            } else {
                musicPlayer.pause();
                ad_voice.getController().getAnimatable().stop();
            }
        } else {
            if (!musicPlayer.isPlaying()) {
                musicPlayer.play(musicUrl);
                ad_voice.getController().getAnimatable().start();
            } else {
                pauseMusicPlayer();
            }
        }
    }

    private void pauseMusicPlayer() {
        try {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
                ad_voice.getController().getAnimatable().stop();
            }
        } catch (Exception e) {
        }
    }

    private void playMusic(String res, int position) {
        try {
            if(isBuy){
                res = CommonUtil.changeNetFile(res);
            }

            isChange = false;
            musicUrl = res;
            musicPlayer.play(musicUrl);

            prePosition = position;
        } catch (Exception e) {
            e.printStackTrace();
            showShortToast("暂无音频信息");
        }
    }

    private int prePosition = -1;

    private void playVoice(final int position) {
        if (isChange) {//有重新选择章节
            mPresenter.getVoicePath(isBuy,gradeId, knowledgeId, 2, position + 1, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    playMusic(res, position);
                }
            });
        } else {
            if (musicPlayer != null) {
                if (prePosition != position) {
                    mPresenter.getVoicePath(isBuy,gradeId, knowledgeId, 2, position + 1, new ApiRequestListener<String>() {
                        @Override
                        public void onResultSuccess(String res) {
                            playMusic(res, position);
                        }
                    });
                } else if (musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                } else {
                    if (musicUrl != null) {
                        musicPlayer.play(musicUrl);
                    }
                }
            }
        }
    }

    @OnClick({R.id.ad_voice})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_voice:
                adVoice();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        pauseMusicPlayer();
        super.onDestroyView();
    }
}
