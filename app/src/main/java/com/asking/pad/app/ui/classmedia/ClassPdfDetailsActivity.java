package com.asking.pad.app.ui.classmedia;

import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DESHelper;
import com.asking.pad.app.entity.classmedia.ClassMedia;
import com.asking.pad.app.ui.commom.DownloadFile;
import com.asking.pad.app.widget.MultiStateView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jswang on 2017/6/1.
 */

public class ClassPdfDetailsActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.pdfView)
    PDFView pdfView;

    ClassMedia mClassVideo;

    public static void openActivity(ClassMedia e){
        Bundle bundle = new Bundle();
        bundle.putParcelable("ClassMedia", e);
        CommonUtil.openActivity(ClassPdfDetailsActivity.class,bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_pdf_detail);
        ButterKnife.bind(this);

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mClassVideo = this.getIntent().getParcelableExtra("ClassMedia");
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, mClassVideo.getCourseName());

        load_view.setVisibility(View.VISIBLE);
        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netPdfData();
            }
        });
        netPdfData();
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

    private void displayFromUri(String filePath) {
        DESHelper.decryptFile(this,filePath,new ApiRequestListener<byte[]>(){
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
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }
}
