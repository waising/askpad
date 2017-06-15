package com.asking.pad.app.ui.classmedia;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jswang on 2017/6/7.
 */

public class ClassTimerTask {
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    public static ClassTimerTask mInstance;

    public static ClassTimerTask getInstance() {
        if (mInstance == null) {
            mInstance = new ClassTimerTask();
        }
        return mInstance;
    }

    public void startTimer(final Activity mActivity,final OnRunListener mListener) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimerTask = new TimerTask() {
            public void run() {
                if(mListener != null){
                    (mActivity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.OnRun();
                        }
                    });
                }
            }
        };
        mTimer.schedule(mTimerTask, 0,10);
    }

    public void stopTimer() {
        try {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
        } catch (Exception e) {
        }
    }

    public  interface OnRunListener{
        void OnRun();
    }
}
