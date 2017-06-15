package com.asking.pad.app.ui.camera.views;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.asking.pad.app.AppContext;

import java.io.IOException;

/**
 * Created by jswang on 2017/4/17.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{
    private SurfaceHolder mSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
    private Camera mCamera = null; // Camera对象，相机预览

    private int mRotation;

    private int degrees;

    private int mOrientation;

    private boolean isTorchOpen;

    private OnCameraActionCallback mCallback;

    public void setCameraCallback(OnCameraActionCallback mCallback) {
        this.mCallback = mCallback;
    }

    public CameraView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public CameraView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        mSurfaceHolder = getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
        mSurfaceHolder.addCallback(this); // SurfaceHolder加入回调接口
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 設置顯示器類型，setType必须设置
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            for(int i = 0;i < Camera.getNumberOfCameras();i++){
                CameraInfo cameraInfo = new CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        mCamera = Camera.open(i);
                        setCameraDisplayOrientation(mRotation, i, mCamera);
                    } catch (RuntimeException e) {
                        if (mCamera == null) {
                            Toast.makeText(getContext(), "请去‘设置’-‘权限管理’中打开相机权限", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        if (mCamera == null) {
                            Toast.makeText(getContext(), "相机无法打开，重启阿思可在线试试看", Toast.LENGTH_SHORT).show();
                        }
                    }
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "请去‘设置’-‘权限管理’中打开相机权限", Toast.LENGTH_SHORT).show();
                        mCamera.release();
                        mCamera = null;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.setPreviewCallback(this);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        release();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(mCallback != null){
            mCallback.onPreviewChanged(data, camera);
        }
    }

    public void setCameraDisplayOrientation(int rotation, int cameraId, Camera camera) {
        int degrees = 0;
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        switch (rotation) {
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        degrees = cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT ? (360 - ((degrees + cameraInfo.orientation) % 360)) % 360 : ((cameraInfo.orientation - degrees) + 360) % 360;
        mOrientation = cameraInfo.orientation;
        this.degrees = degrees;
        camera.setDisplayOrientation(degrees);
    }

    public int getCameraDisplayOrientation() {
        return degrees;
    }

    public int getInfoOrientation() {
        return mOrientation;
    }

    public void setCameraRotation(int mRotation) {
        this.mRotation = mRotation;
    }

    public void setCameraType(int i) {

    }

    public void setAttrNeedPreviewData(boolean z) {

    }

    private PictureCallback picCallback= new PictureCallback() {
        public void onPictureTaken(byte[] bArr, Camera camera) {

        }
    };

    public void takePicture() {
        ShutterCallback shutterCallback = null;
        if ( AppContext.getInstance().getPreferencesBoolean("CameraShutter")) {
            shutterCallback = new ShutterCallback() {
                public void onShutter() {
                }
            };
        }
        try {
            mCamera.takePicture(shutterCallback, null, picCallback);
        } catch (Exception e) {
            Toast.makeText(getContext(), "相机拍照异常，请重新尝试一下", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetCamera() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException e) {
            }
            try {
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();
            } catch (Exception e2) {
            }
        }
    }

    public void stopCamera() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
            }
        }
    }

    public boolean isTorchOpen() {
        return this.isTorchOpen;
    }

    public void toggleTorchOpen() {
        setTorchOpen(!isTorchOpen);
    }

    private boolean setTorchOpen(boolean bool) {
        isTorchOpen = bool;
        try {
            Parameters parameters = mCamera.getParameters();
            if (isTorchOpen) {
                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            } else {
                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            }
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            isTorchOpen = false;
        }
        return isTorchOpen;
    }

    public void release() {
        try {
            if(isTorchOpen){
                setTorchOpen(false);
            }
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
        } catch (Exception e) {
        }
    }


}
