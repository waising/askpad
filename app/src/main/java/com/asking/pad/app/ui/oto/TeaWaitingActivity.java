package com.asking.pad.app.ui.oto;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.AppManager;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.NimTokenEntity;
import com.asking.pad.app.mvp.OnCountDownListener;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.model.RTSData;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/17.
 */

public class TeaWaitingActivity  extends BaseFrameActivity<UserPresenter,UserModel> {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.title_more_menu)
    TextView title_more_menu;

    @BindView(R.id.root_tea_waiting)
    View root_tea_waiting;

    @BindView(R.id.tv_img)
    ImageView tv_img;

    @BindView(R.id.tv_tea_name)
    TextView tv_tea_name;

    @BindView(R.id.tv_time)
    Chronometer tv_time;

    @BindView(R.id.ll_out_time)
    View ll_out_time;

    int askMoney;
    String subject;
    String grade;
    String picTakePath;
    String picName;
    String qiNiuUrl;
    String picHwyResult;

    String orderId;
    String teaAvatar;
    String teaName;
    String userName;
    String userAvatar;

    OtoWaitingLogoutDialog outDialog;
    OtoWaitingDialog waitingDialog;

    /**
     * 为0时4.学生首单退款接口
     */
    int askTimes;

    /**
     * 0-等待接单   1-正在审题
     */
    int orderState = 0;

    boolean isActivityDestory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oto_tea_analyze_waiting);
        ButterKnife.bind(this);

        askMoney = getIntent().getIntExtra("askMoney", 0);
        subject = getIntent().getStringExtra("subjectId");
        grade = getIntent().getStringExtra("gradeId");
        picTakePath = getIntent().getStringExtra("picTakePath");
        picName = getIntent().getStringExtra("picName");
        qiNiuUrl = getIntent().getStringExtra("qiNiuUrl");
        picHwyResult = getIntent().getStringExtra("picHwyResult");

        askTimes = getIntent().getIntExtra("askTimes", 0);
        userAvatar = getIntent().getStringExtra("userAvatar");
        userName = getIntent().getStringExtra("userName");

    }

    @Override
    public void initView() {
        super.initView();
        tv_title.setText("等待接单");
        root_tea_waiting.setVisibility(View.GONE);
        //倒计时60秒显示等待提示
        mPresenter.countDownPresenter(60, new OnCountDownListener() {
            @Override
            public void onComplete() {
                if (waitingDialog == null) {
                    waitingDialog = new OtoWaitingDialog(mActivity);
                } else {
                    waitingDialog.show();
                }
            }
        });

        ll_out_time.setVisibility(View.GONE);
        mPresenter.countDownPresenter(60 * 3, new OnCountDownListener() {
            @Override
            public void onComplete() {
                ll_out_time.setVisibility(View.VISIBLE);
            }
        });
        orderbulid();
    }

    private void orderbulid() {
        mPresenter.orderbuild(subject, grade, picHwyResult, qiNiuUrl, askMoney + "", userName, userName, userAvatar, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                ArrayMap<String, String> resMap = JSON.parseObject(resStr, new TypeReference<ArrayMap<String, String>>() {
                });
                orderId = resMap.get("orderId");
                loginNIMClient();
                orderstate(orderId);
            }
        });
    }

    private void orderstate(final String orderId) {
        mPresenter.orderstate(orderId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject resObject = JSON.parseObject(res);
                Double state = resObject.getDouble("state");
                String teacher = resObject.getString("teacher");
                if (state < 0) {
                    isActivityDestory = true;
                    showShortToast("老师已取消答疑");
                    finish();
                } else if (state == 2) {
                    try {
                        if (root_tea_waiting.getVisibility() == View.GONE) {
                            tv_title.setText("正在审题");
                            tv_time.setVisibility(View.GONE);
                            // 将计时器清零
                            tv_time.setBase(SystemClock.elapsedRealtime());
                            //开始计时
                            tv_time.start();

                            if (!TextUtils.isEmpty(teacher)) {
                                JSONObject teaObject = JSON.parseObject(teacher);
                                teaAvatar = teaObject.getString("avatar");
                                if (!TextUtils.isEmpty(teaAvatar)) {
                                    BitmapUtil.displayUserImage(TeaWaitingActivity.this, teaAvatar, tv_img);
                                }
                                teaName = teaObject.getString("name");
                                tv_tea_name.setText(teaName);
                            }
                        }
                    } catch (Exception e) {
                    }
                    orderState = 2;
                    root_tea_waiting.setVisibility(View.VISIBLE);
                    title_more_menu.setVisibility(View.GONE);
                }
                //倒计时10秒显示等待提示
                mPresenter.countDownPresenter(3, new OnCountDownListener() {
                    @Override
                    public void onComplete() {
                        if (!isActivityDestory) {
                            orderstate(orderId);
                        }
                    }
                });
            }
        });
    }

    private void loginNIMClient() {
        StatusCode status = NIMClient.getStatus();
        //如果是登陆状态
        if (status.getValue() == StatusCode.LOGINED.getValue() && !TextUtils.isEmpty(AppContext.getInstance().getWYToken())) {
            //监听白板
            openLearningActivity();
        } else {
            //获取网易im登陆token
            String userName = AppContext.getInstance().getUserName();
            mPresenter.nimtoken(userName, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String resStr) {
                    reNimTokenEntity(resStr);
                }
            });
        }
    }

    private void reNimTokenEntity(String res) {
        //获取网易云im token
        final NimTokenEntity nimToken = JSON.parseObject(res, NimTokenEntity.class);
        LoginInfo info = new LoginInfo(AppContext.getInstance().getUserName().toLowerCase(), nimToken.getToken()); // config...
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        //登录成功
                        //缓存网易token
                        // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                        AppContext.getInstance().saveWYToken(nimToken.getToken());
                        openLearningActivity();
                    }

                    @Override
                    public void onFailed(int code) {
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                };
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }

    private Observer<RTSData> mObserver;

    public void openLearningActivity() {
        mObserver = new Observer<RTSData>() {
            @Override
            public void onEvent(RTSData rtsData) {
                if (!isActivityDestory) {
                    HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(TeaWaitingActivity.class.getName());
                    mParams.put("rtsData", rtsData);
                    mParams.put("orderId", orderId);
                    mParams.put("teaName", teaName);
                    mParams.put("teaAvatar", teaAvatar);
                    mParams.put("qiNiuUrl", qiNiuUrl);
                    mParams.put("askTimes", askTimes);
                    mParams.put("grade", grade);
                    openActivity(OtoLearningActivity.class);

                    AppManager.getAppManager().finishActivity(OtoAskActivity.class);

                    //RTSActivity.incomingSession(TeaWaitingActivity.this,rtsData);

                    finish();
                }
                try {
                    FileUtils.deleteFile(new File(picTakePath));
                } catch (Exception e) {
                }
            }
        };
        RTSManager.getInstance().observeIncomingSession(mObserver, true);
    }

    @OnClick({R.id.tv_tea_c_analyze, R.id.title_more_menu, R.id.tv_camera, R.id.tv_qustion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tea_c_analyze:
            case R.id.title_more_menu:
                openActivity(OtoDetailActivity.class, getIntent().getExtras());
                break;
            case R.id.tv_camera:
                backActivity(1);
                break;
            case R.id.tv_qustion:
                backActivity(0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backActivity(0);
    }

    private void backActivity(final int type) {
        if (outDialog == null) {
            outDialog = new OtoWaitingLogoutDialog(mActivity);
            outDialog.setLogoutListener(new OtoWaitingLogoutDialog.LououtListener() {
                @Override
                public void click() {
                    try {
                        if (mObserver != null) {
                            RTSManager.getInstance().observeIncomingSession(mObserver, false);
                        }
                    } catch (Exception e) {
                    }
                    mPresenter.ordercancel(orderId, new ApiRequestListener<String>() {
                        @Override
                        public void onResultSuccess(String resStr) {
                            backOP(type);
                        }

                        @Override
                        public void onResultFail() {
                            backOP(type);
                        }
                    });
                }
            });
        }
        outDialog.show();
    }

    private void backOP(final int type) {
        if (type == 1) {
            EventBus.getDefault().post(new AppEventType(AppEventType.RE_CAMERA_QUS_REQUEST));
            AppManager.getAppManager().finishActivity(OtoAskActivity.class);
        }
        isActivityDestory = true;
        finish();
    }

    @Override
    protected void onDestroy() {
        isActivityDestory = true;
        super.onDestroy();
    }
}
