package com.asking.pad.app.ui.commom;

import android.os.Handler;
import android.os.Message;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.commom.AESHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jswang on 2017/6/2.
 */

public class DownloadFile {
    public static void downloadUpdateFile(final String downloadUrl,final String savePath,final ApiRequestListener mListener){
        if(new File(savePath).exists()){
            mListener.onResultSuccess("");
            return;
        }
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        mListener.onResultSuccess("");
                        break;
                    case 1:
                        break;
                    case -1:
                        mListener.onResultFail();
                        break;
                }

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int downloadCount = 0;
                    long totalSize = 0;
                    int updateTotalSize;

                    HttpURLConnection httpConnection = null;
                    InputStream is = null;
                    FileOutputStream fos = null;

                    try {
                        URL url = new URL(downloadUrl);
                        httpConnection = (HttpURLConnection) url.openConnection();
                        httpConnection.setConnectTimeout(10000);
                        httpConnection.setReadTimeout(20000);
                        updateTotalSize = httpConnection.getContentLength();
                        if (httpConnection.getResponseCode() == 404) {
                            mHandler.sendEmptyMessage(-1);
                            throw new Exception("fail!");
                        }
                        is = httpConnection.getInputStream();
                        fos = new FileOutputStream(new File(savePath), false);

                        AESHelper.encrypt(is, fos);

//                        byte buffer[] = new byte[2048];
//                        int readSize = 0;
//                        while ((readSize = is.read(buffer)) > 0) {
//                            fos.write(buffer, 0, readSize);
//                            totalSize += readSize;
//                            if ((downloadCount == 0)
//                                    || (int) (totalSize * 100 / updateTotalSize) - 4 > downloadCount) {
//                                downloadCount += 4;
//                                Message msg = mHandler.obtainMessage();
//                                msg.what = 1;
//                                msg.arg1 = downloadCount;
//                                mHandler.sendMessage(msg);
//                            }
//                        }

                        mHandler.sendEmptyMessage(0);

                    } finally {
                        if (httpConnection != null) {
                            httpConnection.disconnect();
                        }
                        is.close();
                        fos.close();
                    }
                }catch (Exception e){
                    mHandler.sendEmptyMessage(-1);
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
