package com.asking.pad.app.ui.classmedia;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenNoPreActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DESHelper;
import com.asking.pad.app.entity.classmedia.ClassMedia;
import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.classmedia.cache.ClassMediaCacheActivity;
import com.asking.pad.app.ui.classmedia.download.ClassDownloadManager;
import com.asking.pad.app.ui.commom.DownloadFile;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.asking.pad.app.widget.MultiStateView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import tcking.github.com.giraffeplayer.GiraffePlayerView;


/**
 * Created by jswang on 2017/6/1.
 */

public class ClassMediaDetailsActivity extends BaseEvenNoPreActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.giraffe_video_view)
    GiraffePlayerView video_view;

    @BindView(R.id.ll_pdf)
    View ll_pdf;

    @BindView(R.id.pdfView)
    PDFView pdfView;

    @BindView(R.id.ll_detail)
    View ll_detail;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tea_avatar)
    AskSimpleDraweeView tea_avatar;

    @BindView(R.id.tv_class_name)
    TextView tv_class_name;

    @BindView(R.id.tv_tea_name)
    TextView tv_tea_name;

    @BindView(R.id.tv_price_count)
    TextView tv_price_count;

    @BindView(R.id.btn_pay)
    TextView btn_pay;

    @BindView(R.id.tv_class_detail)
    WebView tv_class_detail;

    @BindView(R.id.ll_free)
    View ll_free;

    @BindView(R.id.tv_free_time)
    TextView tv_free_time;

    ClassMedia mClassVideo;
    int playProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_media_detail);
        ButterKnife.bind(this);

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mClassVideo = this.getIntent().getParcelableExtra("ClassMedia");
        playProgress = mClassVideo.getPlayProgress();
    }

    private void downMediaMenu() {
        toolBar.inflateMenu(R.menu.menu_down_class_media);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_info:
                        ClassMediaTable e = new ClassMediaTable();
                        e.setUserId(AppContext.getInstance().getUserId());
                        e.setCourseTypeId(mClassVideo.getCourseTypeId());
                        e.setCourseDataId(mClassVideo.getCourseDataId());
                        e.setPdfUrl(mClassVideo.getPdfUrl());
                        e.setVideoUrl(mClassVideo.getVideoUrl());
                        e.setCourseName(mClassVideo.getCourseName());
                        ClassDownloadManager.getInstance().startDown(e);
                        CommonUtil.openActivity(ClassMediaCacheActivity.class);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "课程详情");

        video_view.setShowNavIcon(false);
        video_view.setVideoPath(mClassVideo.getVideoUrl());
        video_view.setOnOrienChangeListener(new GiraffePlayerView.OnOrienChangeListener() {
            @Override
            public void OnPortrait() {
                ll_pdf.setVisibility(View.VISIBLE);
                toolBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnLandscape() {
                ll_pdf.setVisibility(View.GONE);
                toolBar.setVisibility(View.GONE);
            }

            @Override
            public void OnPlay() {
                countDownFree();
            }
        });

        BitmapUtil.displayImage(mClassVideo.getVideoImgUrl(), video_view.iv_tmpimg_video, true);

        try {
            /**
             * 设置加载进来的页面自适应手机屏幕
             */
            tv_class_detail.getSettings().setUseWideViewPort(true);
            tv_class_detail.getSettings().setLoadWithOverviewMode(true);
            tv_class_detail.setVerticalScrollBarEnabled(false);
            tv_class_detail.setVerticalScrollbarOverlay(false);
            tv_class_detail.setHorizontalScrollBarEnabled(false);
            tv_class_detail.setHorizontalScrollbarOverlay(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        load_view.setVisibility(View.GONE);
        ll_detail.setVisibility(View.GONE);
        if (TextUtils.equals(mClassVideo.getPurchaseState(), "0")) {
            ll_detail.setVisibility(View.VISIBLE);
            tv_free_time.setVisibility(View.VISIBLE);
            tv_class_name.setText(mClassVideo.getCourseName());
            tv_tea_name.setText(mClassVideo.getTeacher());
            tea_avatar.setImageUrl(mClassVideo.getTeacherImgUrl());
            tv_price.setText("￥" + mClassVideo.getPrice());
            tv_price_count.setText(String.format("已有%s人购买", mClassVideo.getPurchasedNum()));
            tv_class_detail.loadDataWithBaseURL(null, mClassVideo.getDetail(), "text/html", "utf-8", "about:blank");
            try {
                BigDecimal freeTime = new BigDecimal(mClassVideo.getFreeTime());
                int time = freeTime.divide(new BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP).intValue();
                tv_free_time.setText(String.format("%s分钟免费试看", time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            load_view.setVisibility(View.VISIBLE);
            load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    netPdfData();
                }
            });
            netPdfData();
            video_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (playProgress > 5000 && video_view.isPlayerSupport()) {
                        video_view.start();
                        video_view.seekTo(playProgress, false);
                    }
                }
            }, 300);

            downMediaMenu();
        }

        ll_free.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.PAY_SUCCESSS_REQUEST:
                mClassVideo.setPurchaseState("1");
                ll_free.setVisibility(View.GONE);
                tv_free_time.setVisibility(View.GONE);
                ll_detail.setVisibility(View.GONE);
                load_view.setVisibility(View.VISIBLE);
                load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        netPdfData();
                    }
                });
                netPdfData();
                downMediaMenu();
                break;
        }
    }

    private void countDownFree() {
        if (TextUtils.equals(mClassVideo.getPurchaseState(), "0")) {
            ClassTimerTask.getInstance().startTimer(this, new ClassTimerTask.OnRunListener() {
                @Override
                public void OnRun() {
                    long time = video_view.getCurrentPosition();
                    long freetime = new BigDecimal(mClassVideo.getFreeTime())
                            .multiply(new BigDecimal(1000)).longValue();
                    if (time > freetime) {
                        ClassTimerTask.getInstance().stopTimer();
                        video_view.stop();
                        tv_free_time.setVisibility(View.GONE);
                        ll_free.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void netPdfData() {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        String pdfUrl = mClassVideo.getPdfUrl();
        final String pdfPath = Constants.getPdfPath(pdfUrl);
        DownloadFile.downloadUpdateFile(pdfUrl, pdfPath, new ApiRequestListener() {
            @Override
            public void onResultSuccess(Object res) {
                displayFromUri(pdfPath);
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_ERROR);
            }
        });
    }

    private void displayFromUri(final String filePath) {
        DESHelper.decryptFile(this, filePath, new ApiRequestListener<byte[]>() {
            @Override
            public void onResultSuccess(byte[] res) {
                pdfView.fromBytes(res)   //设置pdf文件地址
                        .swipeHorizontal(false)  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                        .enableSwipe(true)   //是否允许翻页，默认是允许翻
                        .onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                pdfView.fitToWidth(); // optionally pass page number
                            }
                        })
                        .load();
                load_view.setViewState(load_view.VIEW_STATE_CONTENT);
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_ERROR);
            }
        });
    }

    @OnClick({R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                PayClassMediaActivity.openActivity(ClassMediaDetailsActivity.this, mClassVideo);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video_view != null) {
            video_view.onPause();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (video_view != null) {
//            video_view.onResume();
//        }
//    }

    @Override
    protected void onDestroy() {
        setStudyRecord();
        super.onDestroy();
        ClassTimerTask.getInstance().stopTimer();
        if (video_view != null) {
            video_view.onDestroy();
        }
    }

    private void setStudyRecord() {
        int progress = video_view.getCurrentPosition();
        if (!TextUtils.equals(mClassVideo.getPurchaseState(), "0") && progress > 0) {
            int max = video_view.getDuration();
            EventBus.getDefault().post(new AppEventType(AppEventType.RE_STU_PROGRESSS_SUCCESSS_REQUEST
                    , mClassVideo.getCourseTypeId(), mClassVideo.getCourseDataId(), max, progress));
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
