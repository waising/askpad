package com.asking.pad.app.ui.oto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.mvp.OnCountDownListener;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.ScreenUtils;
import com.asking.pad.app.ui.oto.attachment.RTSAttachment;
import com.asking.pad.app.widget.doodle.ActionTypeEnum;
import com.asking.pad.app.widget.doodle.DoodleView;
import com.asking.pad.app.widget.doodle.SupportActionType;
import com.asking.pad.app.widget.doodle.Transaction;
import com.asking.pad.app.widget.doodle.TransactionCenter;
import com.asking.pad.app.widget.doodle.action.MyPath;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.rts.RTSCallback;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.constant.RTSEventType;
import com.netease.nimlib.sdk.rts.constant.RTSTunnelType;
import com.netease.nimlib.sdk.rts.model.RTSCommonEvent;
import com.netease.nimlib.sdk.rts.model.RTSControlEvent;
import com.netease.nimlib.sdk.rts.model.RTSData;
import com.netease.nimlib.sdk.rts.model.RTSOnlineAckEvent;
import com.netease.nimlib.sdk.rts.model.RTSOptions;
import com.netease.nimlib.sdk.rts.model.RTSTunData;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jswang on 2017/4/18.
 */

public class OtoLearningActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.iv_img)
    SimpleDraweeView iv_img;

    @BindView(R.id.again)
    TextView again;

    @BindView(R.id.tv_time)
    Chronometer tv_time;

    @BindView(R.id.end)
    TextView end;

    @BindView(R.id.scroll_view)
    ScrollView scroll_view;

    @BindView(R.id.doodle_view)
    DoodleView mDoodleView;

    private String account;      // 对方帐号
    private String sessionId;    // 会话的唯一标识
    private RTSData sessionInfo; // 本次会话的信息
    private boolean finishFlag = false; // 结束标记，避免多次回调onFinish

    private MaterialDialog materialDialog;

    /**
     * 订单的ID
     */
    private String orderId;
    private String qiNiuUrl;
    /**
     * 为0时4.学生首单退款接口
     */
    int askTimes;

    private String teaName;
    private String teaAvatar;
    private String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_oto_learning);
        ButterKnife.bind(this);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(TeaWaitingActivity.class.getName());
        orderId = (String) mParams.get("orderId");
        askTimes = (int) mParams.get("askTimes");
        qiNiuUrl = (String) mParams.get("qiNiuUrl");
        teaName = (String) mParams.get("teaName");
        teaAvatar = (String) mParams.get("teaAvatar");
        grade = (String) mParams.get("grade");

        sessionInfo = (RTSData) mParams.get("rtsData");
        account = sessionInfo.getAccount();
        sessionId = sessionInfo.getLocalSessionId();
    }

    @Override
    public void initData() {
        super.initData();
        try {
            String extraStr = sessionInfo.getExtra();
            BigDecimal scale = new BigDecimal(extraStr);
            BigDecimal screenWidth = new BigDecimal(ScreenUtils.getScreenWidth());
            int height = screenWidth.divide(scale, 2, BigDecimal.ROUND_HALF_UP).intValue();
            mDoodleView.getLayoutParams().height = height;
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerInComingObserver(true);
        registerCommonObserver(true);

        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);

        if (!TextUtils.isEmpty(qiNiuUrl)) {
            iv_img.setImageURI(qiNiuUrl);
        }

        mDoodleView.setOnScrollToListener(new DoodleView.OnScrollToListener() {
            @Override
            public void OnScrollTo(final Transaction t) {
                scroll_view.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll_view.scrollTo((int) (t.getX() * mDoodleView.getWidth()), (int) (t.getY() * mDoodleView.getHeight()));
                    }
                });
            }

            @Override
            public void OnStartTime() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 将计时器清零
                        tv_time.setBase(SystemClock.elapsedRealtime());
                    }
                });
            }
        });
    }

    private void registerCommonObserver(boolean register) {
        RTSManager.getInstance().observeChannelState(sessionId, channelStateObserver, register);
        RTSManager.getInstance().observeHangUpNotification(sessionId, endSessionObserver, register);
        RTSManager.getInstance().observeReceiveData(sessionId, receiveDataObserver, register);
        //RTSManager.getInstance().observeTimeoutNotification(sessionId, timeoutObserver, register);
        RTSManager.getInstance().observeControlNotification(sessionId, controlObserver, register);
    }

    /**
     * 监听控制消息
     */
    private com.netease.nimlib.sdk.Observer<RTSControlEvent> controlObserver = new com.netease.nimlib.sdk.Observer<RTSControlEvent>() {
        @Override
        public void onEvent(RTSControlEvent rtsControlEvent) {
            //Toast.makeText(mthis, rtsControlEvent.getCommandInfo(), Toast.LENGTH_SHORT).show();
            Log.d("11111111111111", rtsControlEvent.getCommand() + rtsControlEvent.getCommandInfo());
        }
    };

    /**
     * 监听对方挂断
     */
    private com.netease.nimlib.sdk.Observer<RTSCommonEvent> endSessionObserver = new com.netease.nimlib.sdk.Observer<RTSCommonEvent>() {
        @Override
        public void onEvent(RTSCommonEvent rtsCommonEvent) {
            onFinish();
        }
    };

    /**
     * 监听当前会话的状态
     */
    private com.netease.nimlib.sdk.rts.RTSChannelStateObserver channelStateObserver = new com.netease.nimlib.sdk.rts.RTSChannelStateObserver() {

        @Override
        public void onConnectResult(String sessionId, RTSTunnelType tunType, long channelId, int code, String file) {
        }


        @Override
        public void onChannelEstablished(String sessionId, RTSTunnelType tunType) {
            if (tunType == RTSTunnelType.AUDIO) {
                RTSManager.getInstance().setSpeaker(sessionId, true); // 默认开启扬声器
            }
        }

        @Override
        public void onUserJoin(String sessionId, RTSTunnelType tunType, String account) {

        }

        @Override
        public void onUserLeave(String sessionId, RTSTunnelType tunType, String account, int event) {

        }

        @Override
        public void onDisconnectServer(String sessionId, RTSTunnelType tunType) {
            Toast.makeText(mActivity, "onDisconnectServer, tunType=" + tunType.toString(), Toast
                    .LENGTH_SHORT).show();
            if (tunType == RTSTunnelType.DATA) {
                // 如果数据通道断了，那么关闭会话
                Toast.makeText(mActivity, "TCP通道断开，自动结束会话", Toast.LENGTH_SHORT).show();
                endSession();
            } else if (tunType == RTSTunnelType.AUDIO) {
                // 如果音频通道断了，那么UI变换
                Toast.makeText(mActivity, "音频通道断开，可能无法时时通话", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String sessionId, RTSTunnelType tunType, int code) {
            Toast.makeText(mActivity, "onError, tunType=" + tunType.toString() + ", error=" + code,
                    Toast.LENGTH_LONG).show();
            endSession();
        }

        @Override
        public void onNetworkStatusChange(String sessionId, RTSTunnelType tunType, int value) {
            // 网络信号强弱
        }
    };

    private void acceptSession() {
        RTSOptions options = new RTSOptions().setRecordAudioTun(true).setRecordDataTun(true);
        RTSManager.getInstance().accept(sessionId, options, new RTSCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                // 判断开启通道是否成功
                if (success) {
                    //acceptView();
                } else {
                    Toast.makeText(mActivity, "通道开启失败!请查看LOG", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    Toast.makeText(mActivity, "接受会话失败,音频通道同时只能有一个会话开启", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "接受会话失败, code=" + code, Toast.LENGTH_SHORT).show();
                }
                onFinish();
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(mActivity, "接受会话异常, e=" + exception.toString(), Toast.LENGTH_SHORT).show();
                onFinish();
            }
        });
    }

    /**
     * 监听收到对方发送的通道数据
     */
    private com.netease.nimlib.sdk.Observer<RTSTunData> receiveDataObserver = new com.netease.nimlib.sdk.Observer<RTSTunData>() {
        @Override
        public void onEvent(RTSTunData rtsTunData) {
            String data = "[parse bytes error]";
            try {
                data = new String(rtsTunData.getData(), 0, rtsTunData.getLength(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("2222222222", data);
            TransactionCenter.getInstance().onReceive(sessionId, data);
        }
    };

    private void openOtoEndActivity() {
        HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(OtoLearningActivity.class.getName());
        mParams.put("orderId", orderId);
        mParams.put("teaName", teaName);
        mParams.put("teaAvatar", teaAvatar);
        mParams.put("askTimes", askTimes);
        openActivity(OtoEndActivity.class);
        materialDialog.dismiss();
        finish();
    }

    private void onFinish() {
        if (!finishFlag) {
            finishFlag = true;
            RTSAttachment attachment = new RTSAttachment((byte) 1);
            IMMessage msg = MessageBuilder.createCustomMessage(account, SessionTypeEnum.P2P, attachment.getContent(), attachment);
            msg.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
        }
        openOtoEndActivity();
    }

    @Override
    public void initView() {
        super.initView();
        materialDialog = getLoadingDialog().build();
        materialDialog.setContent("提交中...");
        again.setVisibility(View.GONE);
    }

    private OtoWaitingLogoutDialog otoWaitingLogoutDialog;

    @OnClick({R.id.end, R.id.again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.end:
                showEndDialog();
                break;
            case R.id.again://再来一题

                break;
        }
    }

    private void endSession() {
        RTSManager.getInstance().close(sessionId, new RTSCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(mActivity, "挂断请求错误，code：" + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
        AVChatManager.getInstance().hangUp2(0,new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
        onFinish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            materialDialog.show();
        }
    }

    @Override
    public void initLoad() {
        super.initLoad();
        countTime();//开始计时
        acceptSession();
        initDoodleView();
        openAudio();

    }

    private void countTime() {
        // 将计时器清零
        tv_time.setBase(SystemClock.elapsedRealtime());
        //开始计时
        tv_time.start();

        try{
            int gradeId = Integer.valueOf(grade);
            BigDecimal askcoin;
            if(gradeId<10){
                //初中科目 6阿思可币/60秒
                askcoin = new BigDecimal(6);
            }else{
                //高中科目 8阿思可币/60秒
                askcoin = new BigDecimal(8);
            }
            double userIntegral = AppContext.getInstance().getUserEntity().getIntegral();
            BigDecimal integral = new BigDecimal(userIntegral);
            BigDecimal totalTime = new BigDecimal(0);
            if(integral.doubleValue()>0){
                totalTime = integral.multiply(new BigDecimal(60)).divide(askcoin,2,BigDecimal.ROUND_HALF_UP);
            }
            if(askTimes == 0){
                //首单免费体验时长20分钟
                totalTime = totalTime.add(new BigDecimal(120));
            }
            int takeTime = totalTime.intValue();
            //倒计时60秒显示等待提示
            mPresenter.countDownPresenter(takeTime, new OnCountDownListener() {
                @Override
                public void onComplete() {
                    Toast.makeText(OtoLearningActivity.this,"阿思可币不足，3分钟后将终止答疑",Toast.LENGTH_SHORT).show();
                    mPresenter.countDownPresenter(180, new OnCountDownListener() {
                        @Override
                        public void onComplete() {
                            endSession();
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        showEndDialog();
    }

    private void showEndDialog() {
        if (otoWaitingLogoutDialog == null) {
            otoWaitingLogoutDialog = new OtoWaitingLogoutDialog(mActivity, "是否要结束这次辅导？", null, View.GONE, "继续学习", "确定结束");
            otoWaitingLogoutDialog.setLogoutListener(new OtoWaitingLogoutDialog.LououtListener() {
                @Override
                public void click() {
                    endSession();
                }
            });
        }
        otoWaitingLogoutDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // 这里需要重绘
        mDoodleView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mDoodleView != null) {
            mDoodleView.end();
        }
        super.onDestroy();
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        registerInComingObserver(false);
        registerCommonObserver(false);
    }

    com.netease.nimlib.sdk.Observer<StatusCode> userStatusObserver = new com.netease.nimlib.sdk.Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                finish();
            }
        }
    };

    private void registerInComingObserver(boolean register) {
        RTSManager.getInstance().observeOnlineAckNotification(sessionId, onlineAckObserver, register);
    }

    /**
     * 被叫方监听在线其他端的接听响应
     */
    private com.netease.nimlib.sdk.Observer<RTSOnlineAckEvent> onlineAckObserver = new com.netease.nimlib.sdk.Observer<RTSOnlineAckEvent>() {
        @Override
        public void onEvent(RTSOnlineAckEvent rtsOnlineAckEvent) {
            if (rtsOnlineAckEvent.getClientType() != ClientType.Android) {
                String client = null;
                switch (rtsOnlineAckEvent.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    default:
                        break;
                }
                if (client != null) {
                    String option = rtsOnlineAckEvent.getEvent() == RTSEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ?
                            "接受" : "拒绝";
                    Toast.makeText(mActivity, "白板演示已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "白板演示已在其他端处理", Toast.LENGTH_SHORT).show();
                }
                onFinish();
            }
        }
    };


    //开启语音
    private void openAudio() {
        //true ：静音
        RTSManager.getInstance().setMute(sessionId, false);
    }


    /**
     * ***************************** 画板 ***********************************
     */
    private void initDoodleView() {
        // add support ActionType
        SupportActionType.getInstance().addSupportActionType(ActionTypeEnum.Path.getValue(), MyPath.class);

        mDoodleView.init(sessionId, account, DoodleView.Mode.BOTH, Color.WHITE, this);

        mDoodleView.setPaintSize(10);
        mDoodleView.setPaintType(ActionTypeEnum.Path.getValue());

        //guanbi 硬件加速
        //mDoodleView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // adjust paint offset
    }

    /**
     * 撤销一步
     */
    private void doodleBack() {
        mDoodleView.paintBack();
    }

    /**
     * 清屏
     */
    private void clear() {
        mDoodleView.clear();
    }
}
