package com.asking.pad.app.ui.commom;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;


/**
 * Created by Jun on 2016/4/19.
 */
public class PhotoShowActivity extends BaseActivity {

    @BindView(R.id.photo_iv)
    PhotoDraweeView mPhotoIv;

    @BindView(R.id.photo_ly)
    View mPhotoLv;

    @BindView(R.id.iv_img)
    ImageView iv_img;

    private String mImageUrl;

    public static void openActivity(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.WEB_IMAGE_URL, url);
        CommonUtil.openActivity(PhotoShowActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();
        mImageUrl = getIntent().getExtras().getString(Constants.WEB_IMAGE_URL);

        if (!mImageUrl.startsWith("http")) {
            mImageUrl = "file://" + mImageUrl;
        }
    }


    @OnClick({R.id.iv_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                setBtnAnimation(0,90);
                break;
        }
    }


    private void setBtnAnimation(int i, int i2) {
        if (mPhotoIv != null) {
            final int width = mPhotoIv.getWidth();
            final int height = mPhotoIv.getHeight();

            Animation rotateAnimation = new RotateAnimation((float) i, (float) i2, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setInterpolator(new AccelerateInterpolator());
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)mPhotoIv.getLayoutParams();
                    lp.height = width;
                    mPhotoIv.setLayoutParams(lp);
                    //mPhotoIv.update(mPhotoIv.getWidth(), mPhotoIv.getHeight());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mPhotoIv.startAnimation(rotateAnimation);
        }
    }

    @Override
    public void initView() {
        super.initView();
        mPhotoLv.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    public void initListener() {

        mPhotoIv.setOnViewTapListener(new OnViewTapListener() {

            @Override
            public void onViewTap(View view, float v, float v1) {
                PhotoShowActivity.this.finish();
            }

        });
    }

    @Override
    public void initLoad() {
        super.initLoad();

        if (!TextUtils.isEmpty(mImageUrl)) {
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(mImageUrl);
            controller.setOldController(mPhotoIv.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null || mPhotoIv == null) {
                        return;
                    }
                    mPhotoIv.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            mPhotoIv.setController(controller.build());
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }
}

