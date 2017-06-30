package com.asking.pad.app.ui.classmedia.cache;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.AESHelper;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.commom.DownloadFile;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.widget.MultiStateView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import tcking.github.com.giraffeplayer.GiraffePlayerView;

/**
 * Created by jswang on 2017/6/9.
 */

public class ClassMediaCacheDetailActivity extends BaseActivity {
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

    ClassMediaTable mClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_media_cache_detail);
        ButterKnife.bind(this);

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mClass = this.getIntent().getParcelableExtra("ClassMediaTable");
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, mClass.getCourseName());

        video_view.setShowNavIcon(false);
        video_view.setVideoPath(Constants.getVideoPath(mClass.getVideoUrl()));
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
            }
        });

        BitmapUtil.displayImage(mClass.getVideoImgUrl(), video_view.iv_tmpimg_video, true);

        load_view.setVisibility(View.VISIBLE);
        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netPdfData();
            }
        });
        netPdfData();
    }

    @Override
    public void initLoad() {
        super.initLoad();
        video_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                int playProgress = DbHelper.getInstance().getPlayProgress(mClass.getCourseDataId());
                if (playProgress > 5000 && video_view.isPlayerSupport()) {
                    video_view.start();
                    video_view.seekTo(playProgress, false);
                }
            }
        }, 300);
    }

    private void netPdfData() {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        String pdfUrl = mClass.getPdfUrl();
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

    private void displayFromUri(String filePath) {
        AESHelper.decryptFile(this,filePath,new ApiRequestListener<byte[]>(){
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
        setStudyRecord();
        super.onDestroy();
        if (video_view != null) {
            video_view.onDestroy();
        }
    }

    private void setStudyRecord() {
        int progress = video_view.getCurrentPosition();
        if (progress > 0) {
            int max = video_view.getDuration();
            DbHelper.getInstance().insertOrReplaceStudyRecord(mClass.getCourseDataId(),max,progress);
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
