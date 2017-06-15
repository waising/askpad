package com.asking.pad.app.ui.oto;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.CourseViewHelper;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.commom.OnItemLabelEntityListener;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.hanvon.HWCloudManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

import static com.asking.pad.app.commom.Constants.APP_CAMERA_PATH_KEY;

/**
 * 一对一答疑页面
 * Created by jswang on 2017/4/17.
 */

public class OtoAskActivity extends BaseEvenActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.iv_img)
    ImageView iv_img;

    @BindView(R.id.ll_subject)
    LinearLayout ll_subject;

    @BindView(R.id.ll_grade)
    LinearLayout ll_grade;

    @BindView(R.id.ll_askcoin)
    LinearLayout ll_askcoin;

    ArrayList<LabelEntity> subjectList = new ArrayList<>();
    ArrayList<LabelEntity> gradeList = new ArrayList<>();
    ArrayList<LabelEntity> askcoinList = new ArrayList<>();

    private String subjectId = Constants.subjectValues[0];
    private String gradeId = Constants.versionTvValues[0];
    private int askMoney = 0;

    String picTakePath;

    private MaterialDialog loadDialog;

    private HWCloudManager hwManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_oto_ask, null);
        setContentView(contentView);
        ButterKnife.bind(this);

        //取消全屏滑动
        getSwipeBackLayout().setSwipeMode(SwipeBackLayout.ORIGINAL);

        hwManager = new HWCloudManager(this, Constants.HWY_KEY);

        picTakePath = this.getIntent().getStringExtra(APP_CAMERA_PATH_KEY);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "提交问题");
        loadDialog = getLoadingDialog().content("提交中...").build();

        subjectList.add(new LabelEntity(Constants.subjectValues[0], getString(R.string.online_dialog1_t1), true));
        subjectList.add(new LabelEntity(Constants.subjectValues[1], getString(R.string.online_dialog1_t2)));
        CourseViewHelper.getView2(this, ll_subject, getString(R.string.oto_ask_t1), CourseViewHelper.getCourseViewAdapter(this, subjectList
                , new OnItemLabelEntityListener() {
                    @Override
                    public void OnItemLabelEntity(LabelEntity e) {
                        subjectId = e.getId();
                    }
                }));

        gradeList.add(new LabelEntity(Constants.versionTvValues[0], getString(R.string.online_dialog1_t3), true));
        gradeList.add(new LabelEntity(Constants.versionTvValues[1], getString(R.string.online_dialog1_t4)));
        gradeList.add(new LabelEntity(Constants.versionTvValues[2], getString(R.string.online_dialog1_t5)));
        gradeList.add(new LabelEntity(Constants.versionTvValues[3], getString(R.string.online_dialog1_t6)));
        gradeList.add(new LabelEntity(Constants.versionTvValues[4], getString(R.string.online_dialog1_t7)));
        gradeList.add(new LabelEntity(Constants.versionTvValues[5], getString(R.string.online_dialog1_t8)));
        CourseViewHelper.getView2(this, ll_grade, getString(R.string.oto_ask_t2), CourseViewHelper.getCourseViewAdapter(this, gradeList
                , new OnItemLabelEntityListener() {
                    @Override
                    public void OnItemLabelEntity(LabelEntity e) {
                        gradeId = e.getId();
                    }
                }));

        askcoinList.add(new LabelEntity(0, "", "0 枚", true));
        askcoinList.add(new LabelEntity(1, "", "1 枚"));
        askcoinList.add(new LabelEntity(2, "", "2 枚"));
        askcoinList.add(new LabelEntity(3, "", "3 枚"));
        askcoinList.add(new LabelEntity(4, "", "4 枚"));
        askcoinList.add(new LabelEntity(5, "", "5 枚"));
        CourseViewHelper.getView2(this, ll_askcoin, getString(R.string.oto_t3), CourseViewHelper.getCourseViewAdapter(this, askcoinList
                , new OnItemLabelEntityListener() {
                    @Override
                    public void OnItemLabelEntity(LabelEntity e) {
                        askMoney = e.getIcon();
                    }
                }));

        loadCameraPic(picTakePath);
    }

    /**
     * 显示图片
     */
    private void loadCameraPic(String filePath) {
        mPresenter.loadCameraPic(filePath,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                picTakePath = res;
                BitmapUtil.displayNoCacheImage(res, iv_img, false);
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.OTO_QA_AS_CAMERA_REQUEST:
                loadCameraPic((String)event.values[0]);
                break;
        }
    }

    String qiNiuToken;

    /**
     * 获取七牛云token
     */
    private void qiniutoken() {
        loadDialog.show();
        mPresenter.qiniutoken(new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject jsonRes = JSON.parseObject(res);
                qiNiuToken = jsonRes.getString("token");
                getHwyQuestion();
            }

            @Override
            public void onResultFail() {
                loadDialog.dismiss();
            }
        });
    }

    String picHwyResult = "";

    /**
     *
     */
    private void getHwyQuestion() {
        mPresenter.getHwyQuestion(picTakePath,hwManager, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                if (!TextUtils.isEmpty(res)) {
                    picHwyResult = CommonUtil.getReplaceStr(res);
                }
                if (TextUtils.isEmpty(picHwyResult)) {
                    //showShortToast("未找到匹配题目");
                }
                qiNiuUpload();
            }

            @Override
            public void onResultFail() {
                loadDialog.dismiss();
            }
        });
    }

    String qiNiuImgName;

    /**
     * 上传七牛云
     */
    private void qiNiuUpload() {
        qiNiuImgName = DateUtil.currentDateMilltime().replace(":", "-").replace(" ", "_") + "oto_ask.jpg";
        mPresenter.qiNiuUpload(picTakePath, qiNiuImgName, qiNiuToken, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                openTeaWaitingActivity();
            }

            @Override
            public void onResultFail() {
                loadDialog.dismiss();
            }
        });
    }

    /**
     * 打开老师正在审题中页面
     */
    private void openTeaWaitingActivity() {
        final UserEntity mUser = AppContext.getInstance().getUserEntity();
        mPresenter.studentinfo(new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                loadDialog.dismiss();

                JSONObject resObject = JSON.parseObject(resStr);
                int askTimes = resObject.getInteger("askTimes");
                String userAvatar = resObject.getString("avatar");
                double integral = resObject.getDouble("integral");
                String qiNiuUrl = Constants.QiNiuHead + qiNiuImgName;

                if (integral != AppContext.getInstance().getUserEntity().getIntegral()) {
                    AppContext.getInstance().getUserEntity().setIntegral(integral);
                    AppContext.getInstance().saveUserData(AppContext.getInstance().getUserEntity());
                }

                Bundle bundle = new Bundle();
                bundle.putInt("askTimes", askTimes);
                bundle.putInt("askMoney", askMoney);
                bundle.putString("userAvatar", userAvatar);
                bundle.putString("userName", mUser.getUserName());
                bundle.putString("subjectId", subjectId);
                bundle.putString("gradeId", gradeId);
                bundle.putString("picTakePath", picTakePath);
                bundle.putString("picName", qiNiuImgName);
                bundle.putString("qiNiuUrl", qiNiuUrl);
                bundle.putString("picHwyResult", picHwyResult);
                openActivity(TeaWaitingActivity.class, bundle);
            }

            @Override
            public void onResultFail() {
                loadDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_img, R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_img:
                CameraActivity.openActivity(this, AppEventType.OTO_QA_AS_CAMERA_REQUEST, CameraActivity.FROM_OTHER);
                break;
            case R.id.btn_submit://提交问题
                if (TextUtils.isEmpty(picTakePath)) {
                    showShortToast("请拍下题目哦~");
                } else {
                    qiniutoken();
                }
                break;
        }
    }
}






















































