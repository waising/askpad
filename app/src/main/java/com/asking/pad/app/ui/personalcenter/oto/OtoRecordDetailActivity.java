package com.asking.pad.app.ui.personalcenter.oto;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.entity.RecordAnswer;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tcking.github.com.giraffeplayer.GiraffePlayerView;

/**
 * Created by jswang on 2017/6/9.
 */

public class OtoRecordDetailActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.iv_img)
    SimpleDraweeView iv_img;

    @BindView(R.id.giraffe_video_view)
    GiraffePlayerView video_view;

    RecordAnswer mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oto_record_detail);
        ButterKnife.bind(this);

        mRecord = (RecordAnswer)this.getIntent().getSerializableExtra("OtoRecord");
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar,"答疑记录");

        if (!TextUtils.isEmpty(mRecord.getImgUrl())) {
            iv_img.setImageURI(mRecord.getImgUrl());
        }
        video_view.liveBox.setBackgroundResource(R.color.white);
        video_view.setShowNavIcon(false);
        video_view.setVideoPath(mRecord.getVideoUrl()+"?avvod/m3u8");
        BitmapUtil.displayImage(String.format("%s?vframe/jpg/offset/10/",mRecord.getVideoUrl()), video_view.iv_tmpimg_video, true);
        video_view.setOnOrienChangeListener(new GiraffePlayerView.OnOrienChangeListener() {
            @Override
            public void OnPortrait() {

            }

            @Override
            public void OnLandscape() {

            }

            @Override
            public void OnPlay() {

            }
        });

        switch (mRecord.state) {//少个状态
            case 4://待评价
                toolBar.inflateMenu(R.menu.menu_oto_record4);
                break;
            case 5://已评价
                toolBar.inflateMenu(R.menu.menu_oto_record5);
                break;
            case 11://已投诉
                toolBar.inflateMenu(R.menu.menu_oto_record11);
                break;
        }
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_info:
                        Bundle parameter = new Bundle();
                        parameter.putSerializable("OtoRecord",mRecord);

                        switch (mRecord.state) {
                            case 4://待评价
                                openActivity(OtoCommentActivity.class,parameter);
                                break;
                            case 5://已评价
                                openActivity(OtoCommentDetailActivity.class,parameter);
                                break;
                            case 11://已投诉
                                openActivity(OtoFeedBackActivity.class,parameter);
                                break;
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video_view != null) {
            video_view.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (video_view != null) {
            video_view.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (video_view != null) {
            video_view.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (video_view != null) {
            video_view.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (video_view != null && video_view.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
