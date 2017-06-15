package com.asking.pad.app.ui.camera.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.camera.utils.OnCameraListener;
import com.asking.pad.app.ui.camera.utils.ScreenUtils;
import com.asking.pad.app.ui.camera.views.CameraBottomPanelLayout;
import com.asking.pad.app.ui.camera.views.CameraView;
import com.asking.pad.app.ui.camera.views.FocusImageView;
import com.asking.pad.app.ui.camera.views.OnCameraActionCallback;

import java.io.ByteArrayOutputStream;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jswang on 2017/2/16.
 */

@SuppressWarnings("deprecation")
public class CameraMultiFragment extends Fragment implements OnCameraActionCallback,
        CameraBottomPanelLayout.OnCameraBottomPanelListener, View.OnClickListener {
    protected View rootView;
    private CameraView cameraView;
    private CameraBottomPanelLayout footLayout;
    private FocusImageView focusImageView;
    private View cameraLight;
    private View ll_tip;
    private boolean isCanTake = true;
    private boolean isRunPreview = false;

    private int evenType;

    private int fromWhere;

    private int pic_orient = 1;

    public static CameraMultiFragment newInstance(int evenType, int fromWhere) {
        CameraMultiFragment fragment = new CameraMultiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("evenType", evenType);
        bundle.putInt("fromWhere", fromWhere);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            evenType = bundle.getInt("evenType");
            fromWhere = bundle.getInt("fromWhere");
        }
    }

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        rootView = layoutInflater.inflate(R.layout.camera_multi_camera_layout, null);
        cameraView = (CameraView) rootView.findViewById(R.id.camera_search_preview);
        cameraView.setCameraRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation());
        cameraView.setCameraCallback(this);
        cameraView.setCameraType(1);

        footLayout = (CameraBottomPanelLayout) rootView.findViewById(R.id.camera_bottom_panel_layout);
        footLayout.setOnCameraBottomPanelListener(this);
        cameraLight = rootView.findViewById(R.id.camera_control_light);
        cameraLight.setOnClickListener(this);

        focusImageView = (FocusImageView) rootView.findViewById(R.id.focusImageView);

        ll_tip = rootView.findViewById(R.id.ll_tip);
        setBtnAnimation(ll_tip, 0, 90);
        if (fromWhere == CameraActivity.FROM_NOTE) {
            ll_tip.setVisibility(View.GONE);
        } else {
            ll_tip.setVisibility(View.VISIBLE);
        }
        return this.rootView;
    }

    private void setBtnAnimation(View view, int i, int i2) {
        if (view != null) {
            Animation rotateAnimation = new RotateAnimation((float) i, (float) i2, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setInterpolator(new AccelerateInterpolator());
            rotateAnimation.setFillAfter(true);
            view.startAnimation(rotateAnimation);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        cameraView.setAttrNeedPreviewData(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private OnCameraListener.c Q = new OnCameraListener.c() {
        public void a(int i) {
            if (pic_orient != i) {
                pic_orient = i;
            }
        }
    };

    public void onResume() {
        super.onResume();
        if (cameraView != null) {
            cameraView.resetCamera();
        }
        OnCameraListener.a(getActivity()).a(this.Q);
    }

    public void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.stopCamera();
        }
        OnCameraListener.a(getActivity()).b(this.Q);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_control_light:
                onTorchOpen();
                break;
        }
    }

    private void onTorchOpen() {
        cameraView.toggleTorchOpen();
        if (cameraView.isTorchOpen()) {
            cameraLight.setBackgroundResource(R.mipmap.camera_search_light_on);
            return;
        }
        cameraLight.setBackgroundResource(R.mipmap.camera_search_light_off);
    }

    @Override
    public void onCameraMoving() {

    }

    @Override
    public void onCameraOpenFailed() {
    }

    @Override
    public void onFocusEnd(boolean success) {
        if (!success) {
            //Toast.makeText(getActivity(),"对焦失败，请调整一下！",Toast.LENGTH_SHORT).show();
            focusImageView.onFocusFailed();
        } else {
            focusImageView.onFocusSuccess();
        }
    }

    @Override
    public void onFocusStarted() {

        focusImageView.startFocus();
    }

    @Override
    public void onFocusStarted(int i, int i2) {

        focusImageView.startFocus();
    }

    @Override
    public void onPreviewChanged(final byte[] bArr, final Camera camera) {
        if (isRunPreview) {
            isRunPreview = false;
            try {
                final ByteArrayOutputStream stream = BitmapUtil.yuvImage(bArr, camera);
                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = optionsBitmap(stream.toByteArray());
                            stream.close();
                        } catch (OutOfMemoryError e) {
                            subscriber.onError(e);
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new rx.Observer<Bitmap>() {
                            @Override
                            public void onNext(Bitmap bitmap) {
                                resultBitmap(bitmap);
                                cameraComplete();
                            }

                            @Override
                            public void onCompleted() {
                                cameraComplete();
                            }

                            @Override
                            public void onError(Throwable e) {
                                cameraError();
                            }
                        });
            } catch (Exception e) {
                cameraError();
            } catch (OutOfMemoryError e) {
                cameraError();
            }
        }
    }

    private void cameraComplete() {
        isCanTake = true;
        footLayout.c_take.setEnabled(true);
    }

    private void cameraError() {
        isCanTake = true;
        Toast.makeText(getActivity(), "哎呀，相机不给力呀，调整一下再试试呗！", Toast.LENGTH_SHORT).show();
        footLayout.c_take.setEnabled(true);
    }

    protected Bitmap optionsBitmap(byte[]... bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        byte[] bArr2 = bArr[0];
        if (bArr2 == null) {
            return null;
        }
        int orient = cameraView != null ? getCameraDisplayOrientation() : 0;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr2, 0, bArr2.length, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Config.RGB_565;
        options.inSampleSize = BitmapUtil.inSampleSize(options, -1, ScreenUtils.getScreenWidth() * ScreenUtils.getScreenHeight());
        try {
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr2, 0, bArr2.length, options);
            try {
                if (orient == 0) {
                    return decodeByteArray;
                }
                Matrix matrix = new Matrix();
                matrix.setRotate((float) orient);
                decodeByteArray = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, false);
                return decodeByteArray;
            } catch (OutOfMemoryError e) {
                return decodeByteArray;
            } catch (Exception e2) {
                return decodeByteArray;
            }
        } catch (OutOfMemoryError e3) {
            return null;
        } catch (Exception e4) {
            return null;
        }
    }

    protected void resultBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(getActivity(), "哎呀，相机不给力呀，调整一下再试试呗！", Toast.LENGTH_SHORT).show();
            cameraView.resetCamera();
            return;
        }
        goToPage(bitmap, null, 1);
    }


    private void goToPage(Bitmap bitmap, String str, int i) {
        if (cameraLight != null) {
            cameraLight.setBackgroundResource(R.mipmap.camera_search_light_off);
        }
        ParamHelper.acquireParamsReceiver(CameraPublishFragment.class.getName()).put("pic_bitmap", bitmap);
        int infoOrientation = cameraView == null ? 0 : cameraView.getInfoOrientation();
        Bundle bundle = new Bundle();
        bundle.putInt("evenType", evenType);
        bundle.putString("pic_url", str);
        bundle.putInt("pic_orient", pic_orient);
        bundle.putInt("pic_info_orient", infoOrientation);
        bundle.putInt("pic_res", i);
        bundle.putInt("fromWhere", fromWhere);
        Fragment f = CameraPublishFragment.newInstance(bundle);
        ((CameraActivity) getActivity()).goToPage(f);
        relCameraView();
    }


    @Override
    public void OnCameraTake() {
        if (isCanTake && cameraView != null) {
            isRunPreview = true;
            isCanTake = false;
            footLayout.c_take.setEnabled(false);
        }
    }

    @Override
    public void onTakePictureStarted() {
    }

    @Override
    public void OnCameraCancel() {
        getActivity().finish();
    }

    @Override
    public void OnCameraAlbum() {
        Intent intent = new Intent(getContext(), ImageSelectActivity.class);
        getActivity().startActivityForResult(intent, 4);
    }

    public int getCameraDisplayOrientation() {
        return cameraView.getCameraDisplayOrientation();
    }

    public void onDestroyView() {
        super.onDestroyView();
        relCameraView();
    }

    private void relCameraView() {
        if (cameraView != null) {
            cameraView.release();
            cameraView = null;
        }
    }
}
