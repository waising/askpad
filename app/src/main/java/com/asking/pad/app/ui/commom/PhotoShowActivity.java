package com.asking.pad.app.ui.commom;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Jun on 2016/4/19.
 */
public class PhotoShowActivity extends BaseActivity {

    public  DisplayImageOptions itemOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.no_pic)
            .showImageForEmptyUri(R.mipmap.no_pic)
            .showImageOnFail(R.mipmap.no_pic)
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.NONE)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true).build();

    @BindView(R.id.photo_iv)
    PhotoView mPhotoIv;

    @BindView(R.id.photo_ly)
    View photo_ly;

    @BindView(R.id.iv_img)
    ImageView iv_img;

    private String mImageUrl;

    int toDegrees = 0;

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
    public void initView() {
        super.initView();
        mImageUrl = getIntent().getExtras().getString(Constants.WEB_IMAGE_URL);

        if (!mImageUrl.startsWith("http")) {
            mImageUrl = "file://" + mImageUrl;
        }

        ImageLoader.getInstance().displayImage(mImageUrl, mPhotoIv, itemOptions);

        photo_ly.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        mPhotoIv.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener(){
            @Override
            public void onViewTap(View view, float v, float v1) {
                finish();
            }
        });
    }

    @OnClick({R.id.iv_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                toDegrees = toDegrees + 90;
                mPhotoIv.setRotationTo(toDegrees);
                break;
        }
    }
}

