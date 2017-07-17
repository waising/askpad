package com.asking.pad.app.ui.oto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.ScreenUtils;
import com.asking.pad.app.ui.oto.attachment.RTSAttachment;
import com.asking.pad.app.widget.doodle.ActionTypeEnum;
import com.asking.pad.app.widget.doodle.DoodleView;
import com.asking.pad.app.widget.doodle.SupportActionType;
import com.asking.pad.app.widget.doodle.TransactionCenter;
import com.asking.pad.app.widget.doodle.action.MyPath;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.rts.RTSCallback;
import com.netease.nimlib.sdk.rts.RTSChannelStateObserver;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.constant.RTSEventType;
import com.netease.nimlib.sdk.rts.constant.RTSTimeOutEvent;
import com.netease.nimlib.sdk.rts.constant.RTSTunnelType;
import com.netease.nimlib.sdk.rts.model.RTSCalleeAckEvent;
import com.netease.nimlib.sdk.rts.model.RTSCommonEvent;
import com.netease.nimlib.sdk.rts.model.RTSControlEvent;
import com.netease.nimlib.sdk.rts.model.RTSData;
import com.netease.nimlib.sdk.rts.model.RTSNotifyOption;
import com.netease.nimlib.sdk.rts.model.RTSOnlineAckEvent;
import com.netease.nimlib.sdk.rts.model.RTSOptions;
import com.netease.nimlib.sdk.rts.model.RTSTunData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 发起/接受会话界面
 * <p/>
 * Created by huangjun on 2015/7/27.
 */
public class RTSActivity extends BaseFrameActivity<UserPresenter,UserModel> implements View.OnClickListener {
    private String account;      // 对方帐号
    private String sessionId;    // 会话的唯一标识
    private RTSData sessionInfo; // 本次会话的信息
    private boolean audioOpen = false; // 语音默认
    private boolean finishFlag = false; // 结束标记，避免多次回调onFinish
    private static boolean needFinish = true; // Activity销毁后，从最近任务列表恢复，则finish

    private static boolean isBusy = false;

    private DoodleView doodleView;

    public static void incomingSession(Context context, RTSData rtsData) {
        if (isBusy) {
            RTSManager.getInstance().close(rtsData.getLocalSessionId(), null);
            Toast.makeText(context, "close session", Toast.LENGTH_SHORT).show();
            return;
        }
        needFinish = false;
        HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(TeaWaitingActivity.class.getName());
        mParams.put("rtsData", rtsData);

        Intent intent = new Intent();
        intent.setClass(context, RTSActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needFinish) {
            finish();
            return;
        }

        isBusy = true;

        setContentView(R.layout.activity_oto_learning);

        findViews();

        HashMap<String, Object> mParams = ParamHelper.acceptParams(TeaWaitingActivity.class.getName());
        sessionInfo = (RTSData) mParams.get("rtsData");
        account = sessionInfo.getAccount();
        sessionId = sessionInfo.getLocalSessionId();
        registerInComingObserver(true);
//
        initAudioSwitch();
        registerCommonObserver(true);

        //放到所有UI的基类里面注册，所有的UI实现onKickOut接口
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                acceptSession();
            }
        },3000);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // 这里需要重绘
        doodleView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (doodleView != null) {
            doodleView.end();
        }

        super.onDestroy();
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        registerInComingObserver(false);
        registerOutgoingObserver(false);
        registerCommonObserver(false);

        needFinish = true;

        isBusy = false;
    }

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                finish();
            }
        }
    };

    private void findViews() {
        doodleView = (DoodleView) findViewById(R.id.doodle_view);


        float screenWidth = ScreenUtils.getScreenWidth() * 1.0f;
        ViewGroup.LayoutParams params = doodleView.getLayoutParams();
        params.width = ((int) (screenWidth / 100)) * 100; // 比屏幕小的100的整数
        params.height = params.width; // 保证宽高比为1:1
        doodleView.setLayoutParams(params);
    }

    private void registerOutgoingObserver(boolean register) {
        RTSManager.getInstance().observeCalleeAckNotification(sessionId, calleeAckEventObserver, register);
    }

    private void registerInComingObserver(boolean register) {
        RTSManager.getInstance().observeOnlineAckNotification(sessionId, onlineAckObserver, register);
    }

    private void registerCommonObserver(boolean register) {
        RTSManager.getInstance().observeChannelState(sessionId, channelStateObserver, register);
        RTSManager.getInstance().observeHangUpNotification(sessionId, endSessionObserver, register);
        RTSManager.getInstance().observeReceiveData(sessionId, receiveDataObserver, register);
        RTSManager.getInstance().observeTimeoutNotification(sessionId, timeoutObserver, register);
        RTSManager.getInstance().observeControlNotification(sessionId, controlObserver, register);
    }

    /**
     * 主叫方监听被叫方的接受or拒绝会话的响应
     */
    private Observer<RTSCalleeAckEvent> calleeAckEventObserver = new Observer<RTSCalleeAckEvent>() {
        @Override
        public void onEvent(RTSCalleeAckEvent rtsCalleeAckEvent) {
            if (rtsCalleeAckEvent.getEvent() == RTSEventType.CALLEE_ACK_AGREE) {
                // 判断SDK自动开启通道是否成功
                if (!rtsCalleeAckEvent.isTunReady()) {
                    Toast.makeText(RTSActivity.this, "通道开启失败!请查看LOG", Toast.LENGTH_SHORT).show();
                    return;
                }
                acceptView(); // 进入会话界面
            } else if (rtsCalleeAckEvent.getEvent() == RTSEventType.CALLEE_ACK_REJECT) {
                Toast.makeText(RTSActivity.this, "对方拒绝了本次请求", Toast.LENGTH_SHORT).show();
                onFinish(false);
            }
        }
    };

    /**
     * 监听对方挂断
     */
    private Observer<RTSCommonEvent> endSessionObserver = new Observer<RTSCommonEvent>() {
        @Override
        public void onEvent(RTSCommonEvent rtsCommonEvent) {
            Toast.makeText(RTSActivity.this, "对方结束白板演示", Toast.LENGTH_SHORT).show();
            onFinish(false);
        }
    };

    /**
     * 监听收到对方发送的通道数据
     */
    private Observer<RTSTunData> receiveDataObserver = new Observer<RTSTunData>() {
        @Override
        public void onEvent(RTSTunData rtsTunData) {
            String data = "[parse bytes error]";
            try {
                data = new String(rtsTunData.getData(), 0, rtsTunData.getLength(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            TransactionCenter.getInstance().onReceive(sessionId, data);
        }
    };

    /**
     * 被叫方监听在线其他端的接听响应
     */
    private Observer<RTSOnlineAckEvent> onlineAckObserver = new Observer<RTSOnlineAckEvent>() {
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
                    Toast.makeText(RTSActivity.this, "白板演示已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RTSActivity.this, "白板演示已在其他端处理", Toast.LENGTH_SHORT).show();
                }
                onFinish();
            }
        }
    };

    /**
     * 监听控制消息
     */
    private Observer<RTSControlEvent> controlObserver = new Observer<RTSControlEvent>() {
        @Override
        public void onEvent(RTSControlEvent rtsControlEvent) {
            Toast.makeText(RTSActivity.this, rtsControlEvent.getCommandInfo(), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 监听当前会话的状态
     */
    private RTSChannelStateObserver channelStateObserver = new RTSChannelStateObserver() {

        @Override
        public void onConnectResult(String sessionId, RTSTunnelType tunType, long channelId, int code, String file) {
            Toast.makeText(RTSActivity.this, "onConnectResult, tunType=" + tunType.toString() +
                    ", channelId=" + channelId +
                    ", code=" + code + ", file=" + file, Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onChannelEstablished(String sessionId, RTSTunnelType tunType) {
            Toast.makeText(RTSActivity.this, "onCallEstablished,tunType=" + tunType.toString(), Toast
                    .LENGTH_SHORT).show();

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
            Toast.makeText(RTSActivity.this, "onDisconnectServer, tunType=" + tunType.toString(), Toast
                    .LENGTH_SHORT).show();
            if (tunType == RTSTunnelType.DATA) {
                // 如果数据通道断了，那么关闭会话
                Toast.makeText(RTSActivity.this, "TCP通道断开，自动结束会话", Toast.LENGTH_SHORT).show();
                endSession();
            } else if (tunType == RTSTunnelType.AUDIO) {
                // 如果音频通道断了，那么UI变换
                if (audioOpen) {
                    audioSwitch();
                }
            }
        }

        @Override
        public void onError(String sessionId, RTSTunnelType tunType, int code) {
            Toast.makeText(RTSActivity.this, "onError, tunType=" + tunType.toString() + ", error=" + code,
                    Toast.LENGTH_LONG).show();
            endSession();
        }

        @Override
        public void onNetworkStatusChange(String sessionId, RTSTunnelType tunType, int value) {
            // 网络信号强弱
        }
    };

    private Observer<RTSTimeOutEvent> timeoutObserver = new Observer<RTSTimeOutEvent>() {
        @Override
        public void onEvent(RTSTimeOutEvent rtsTimeOutEvent) {
            Toast.makeText(RTSActivity.this,
                    (rtsTimeOutEvent == RTSTimeOutEvent.OUTGOING_TIMEOUT) ? "对方未接受请求" :
                            "超时未处理，自动结束", Toast.LENGTH_SHORT).show();
            onFinish();
        }
    };

    private void startSession() {
        List<RTSTunnelType> types = new ArrayList<>(1);
        types.add(RTSTunnelType.AUDIO);
        types.add(RTSTunnelType.DATA);

        String pushContent = account + "发起一个会话";
        String extra = "extra_data";
        RTSOptions options = new RTSOptions().setRecordAudioTun(false)
                .setRecordDataTun(true);
        RTSNotifyOption notifyOption = new RTSNotifyOption();
        notifyOption.apnsContent = pushContent;
        notifyOption.extendMessage = extra;
        sessionId = RTSManager.getInstance().start(account, types, options, notifyOption, new RTSCallback<RTSData>() {
            @Override
            public void onSuccess(RTSData rtsData) {
                RTSAttachment attachment = new RTSAttachment((byte) 0);
                IMMessage msg = MessageBuilder.createCustomMessage(account, SessionTypeEnum.P2P, attachment.getContent(), attachment);

                NIMClient.getService(MsgService.class).sendMessage(msg, false); // 发送给对方
            }

            @Override
            public void onFailed(int code) {
                if (code == 11001) {
                    Toast.makeText(RTSActivity.this, "无可送达的被叫方", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RTSActivity.this, "发起会话失败,code=" + code, Toast.LENGTH_SHORT).show();
                }
                onFinish();
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(RTSActivity.this, "发起会话异常,e=" + exception.toString(), Toast.LENGTH_SHORT).show();
                onFinish();
            }
        });

        if (sessionId == null) {
            Toast.makeText(RTSActivity.this, "发起会话失败!", Toast.LENGTH_SHORT).show();
            onFinish();
        }
    }

    @Override
    public void onClick(View v) {
    }

    private void acceptSession() {
        RTSOptions options = new RTSOptions().setRecordAudioTun(false).setRecordDataTun(true);
        RTSManager.getInstance().accept(sessionId, options, new RTSCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                // 判断开启通道是否成功
                if (success) {
                    acceptView();
                } else {
                    Toast.makeText(RTSActivity.this, "通道开启失败!请查看LOG", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    Toast.makeText(RTSActivity.this, "接受会话失败,音频通道同时只能有一个会话开启", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RTSActivity.this, "接受会话失败, code=" + code, Toast.LENGTH_SHORT).show();
                }
                onFinish();
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(RTSActivity.this, "接受会话异常, e=" + exception.toString(), Toast.LENGTH_SHORT).show();
                onFinish();
            }
        });
    }

    private void acceptView() {
        initDoodleView();
    }

    private void endSession() {
        RTSManager.getInstance().close(sessionId, new RTSCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(RTSActivity.this, "挂断请求错误，code：" + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {

            }
        });

        onFinish();
    }

    private void onFinish() {
        onFinish(true);
    }

    private void onFinish(boolean selfFinish) {
        if (finishFlag) {
            return;
        }
        finishFlag = true;

        RTSAttachment attachment = new RTSAttachment((byte) 1);

        IMMessage msg = MessageBuilder.createCustomMessage(account, SessionTypeEnum.P2P, attachment.getContent(), attachment);
        if (!selfFinish) {
            // 被结束会话，在这里模拟一条接收的消息
            msg.setFromAccount(account);
            msg.setDirect(MsgDirectionEnum.In);
        }

        msg.setStatus(MsgStatusEnum.success);

        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);

        finish();
    }

    /**
     * ***************************** 画板 ***********************************
     */
    private void initDoodleView() {
        // add support ActionType
        SupportActionType.getInstance().addSupportActionType(ActionTypeEnum.Path.getValue(), MyPath.class);

        doodleView.init(sessionId, account, DoodleView.Mode.BOTH, Color.WHITE, this);

        doodleView.setPaintSize(10);
        doodleView.setPaintType(ActionTypeEnum.Path.getValue());

        // adjust paint offset
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                Log.i("Doodle", "statusBarHeight =" + statusBarHeight);

                int marginTop = doodleView.getTop();
                Log.i("Doodle", "doodleView marginTop =" + marginTop);

                int marginLeft = doodleView.getLeft();
                Log.i("Doodle", "doodleView marginLeft =" + marginLeft);

                float offsetX = marginLeft;
                float offsetY = statusBarHeight + marginTop;

                doodleView.setPaintOffset(offsetX, offsetY);
                Log.i("Doodle", "client1 offsetX = " + offsetX + ", offsetY = " + offsetY);
            }
        }, 50);
    }

    /**
     * 撤销一步
     */
    private void doodleBack() {
        doodleView.paintBack();
    }

    /**
     * 清屏
     */
    private void clear() {
        doodleView.clear();
    }

    /**
     * 语音开关
     */
    private void audioSwitch() {
        audioOpen = !audioOpen;
        RTSManager.getInstance().setMute(sessionId, !audioOpen);
        // 通过控制协议通知对方
        String content = "对方静音" + (audioOpen ? "关闭" : "开启");
        RTSManager.getInstance().sendControlCommand(sessionId, content, new RTSCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String tip = "静音" + (audioOpen ? "关闭" : "开启");
                Toast.makeText(RTSActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(RTSActivity.this, "控制协议发送失败, code =" + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(RTSActivity.this, "控制协议发送异常, e=" + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化语音开关(默认关闭)
     */
    private void initAudioSwitch() {
        RTSManager.getInstance().setMute(sessionId, true);
    }
}