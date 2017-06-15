package com.asking.pad.app.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.RequestEntity;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/19.
 */

public class UserInfoActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.ad_avatar)
    AskSimpleDraweeView ad_avatar;

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.tv_sex)
    TextView tv_sex;

    @BindView(R.id.tv_birthdate)
    TextView tv_birthdate;

    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.tv_school_address)
    TextView tv_school_address;

    @BindView(R.id.tv_school_name)
    TextView tv_school_name;

    @BindView(R.id.tv_grade)
    TextView tv_grade;

    @BindView(R.id.tv_class)
    TextView tv_class;

    @BindView(R.id.et_resume)
    EditText et_resume;

    private TimePickerView pvTime;
    /**
     * 只要县的region_code
     * levelId--年级
     * classId--班级
     */
    private String regionCode;
    /**
     * 年级ID
     */
    String levelId;

    UserEntity userEntity;
    MaterialDialog mLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        mLoadDialog = getLoadingDialog().build();

        userEntity = AppContext.getInstance().getUserEntity();
        ad_avatar.setImageUrl(userEntity.getAvatar());
        et_username.setText(userEntity.getNickName());
        tv_sex.setText(userEntity.getSex() == 0 ? "女" : "男");
        tv_birthdate.setText(userEntity.getBirthdayStr());
        et_address.setText(userEntity.getArea()); // 联系地址（用户自己手动输入，不给选择）
        tv_school_name.setText(userEntity.getSchoolName());
        et_resume.setText(userEntity.getRemark()); // 个人简介
        tv_school_address.setText(userEntity.getRegionName()); // 学校所在 省市县
        levelId = AppContext.getInstance().getUserEntity().getLevelId();
        tv_grade.setText(Constants.getUserGradeName()); // 年级
        tv_class.setText(userEntity.getClassId()); // 班级

        regionCode = userEntity.getRegionCode();
    }

    public void onEventMainThread(AddressEvent event) {
        String address = (String) event.values[0]; // 省市县（区）
        //县（区）的代码
        regionCode = (String) event.values[1];
        if (!TextUtils.isEmpty(address)) {
            tv_school_address.setText(address);
            if (!address.equals(userEntity.getRegionName())) {
                // 清空班级年级
                tv_school_name.setText("");
                tv_grade.setText("");
                tv_class.setText("");
            }
        }
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type){
            case AppEventType.SET_PER_MODI_CAMERA_REQUEST:
                String path  = (String)event.values[0];
                mLoadDialog.show();
                final String qiNiuImgName = DateUtil.currentDateMilltime().replace(":", "-").replace(" ", "_") + "avatar.jpg";
                mPresenter.qiNiuUploadFile(path, qiNiuImgName, new ApiRequestListener<String>() {

                    @Override
                    public void onResultSuccess(String res) {
                        String qiNiuUrl = Constants.getQiNiuFile(qiNiuImgName);
                        userEntity.setAvatar(qiNiuUrl);
                        ad_avatar.setImageUrl(qiNiuUrl);
                        mLoadDialog.dismiss();
                    }

                    @Override
                    public void onResultFail() {
                        mLoadDialog.dismiss();
                    }
                });
                break;
        }
    }

    private void showSchoolName() {
        if (!TextUtils.isEmpty(tv_school_address.getText().toString()) && !TextUtils.isEmpty(regionCode)) {
            mPresenter.presenterGetSchoolInfo(regionCode, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    showSchoolDialog(res);
                }
            }); // 请求学校数据
        } else {
            showShortToast(R.string.choose_address);
        }
    }

    /**
     * 学校名称
     * @param res
     */
    private void showSchoolDialog(String res) {
        try {
            List<UserEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<UserEntity>>() {
            });
            if (list != null && list.size() > 0) {
                final List<LabelEntity> dialogEntities = new ArrayList<>();

                for (UserEntity userEntity : list) {
                    dialogEntities.add(new LabelEntity("", userEntity.getSchoolName()));
                }
                final SelectDialog selectDialog = new SelectDialog(this, "meArrayWheel");
                selectDialog.title(getString(R.string.basepacket_plase_choose))
                        .datas(dialogEntities)
                        .callBackListener(new SelectDialog.DialogCallBackListener() {
                            @Override
                            public void callBack(LabelEntity dialogEntity, int pos, String str) {
                                if (!TextUtils.isEmpty(dialogEntity.getName())) {
                                    tv_school_name.setText(dialogEntity.getName());
                                    if (!tv_school_name.equals(userEntity.getSchoolName())) {
                                        tv_grade.setText("");
                                        tv_class.setText("");
                                    }
                                }
                                selectDialog.dismiss();
                            }
                        }).show();
            }
        } catch (Exception e) {
            JSONObject jsonRes = JSON.parseObject(res);
            if (TextUtils.equals("0", jsonRes.getString("flag")) && !TextUtils.isEmpty(jsonRes.getString("msg"))) {
                showShortToast(jsonRes.getString("msg"));
            }
        }
    }

    /**
     * 年级
     */
    private void showGradeDialog() {
        final SelectDialog selectDialog = new SelectDialog(this, "meNumericWheelGrade");
        selectDialog.title(getString(R.string.basepacket_plase_choose))
                .callBackListener(new SelectDialog.DialogCallBackListener() {
                    @Override
                    public void callBack(LabelEntity dialogEntity, int pos, String str) {
                        if (!Constants.gradeVersionValues[pos].equals(userEntity.getLevelId())) {
                            tv_grade.setText(Constants.gradeVersionValues[pos]);
                            levelId = String.valueOf(pos + 1);
                        } else {
                            tv_class.setText("");
                        }
                        selectDialog.dismiss();
                    }
                }).show();
    }

    /**
     *  班级
      */
    private void showClassDialog() {
        final SelectDialog selectDialog = new SelectDialog(this, "meNumericWheelClass");
        selectDialog.title(getString(R.string.basepacket_plase_choose))
                .callBackListener(new SelectDialog.DialogCallBackListener() {
                    @Override
                    public void callBack(LabelEntity dialogEntity, int pos, String str) {
                        tv_class.setText(String.valueOf(pos + 1));
                        selectDialog.dismiss();
                    }
                }).show();
    }

    /**
     * 性别
     */
    private void showSexDialog() {
        final SelectDialog selectDialog = new SelectDialog(this, "meNumericWheelSex");
        selectDialog.title(getString(R.string.basepacket_plase_choose))
                .callBackListener(new SelectDialog.DialogCallBackListener() {
                    @Override
                    public void callBack(LabelEntity dialogEntity, int pos, String str) {
                        tv_sex.setText(str);
                        selectDialog.dismiss();
                    }
                }).show();
    }

    private void updateUser() {
        String nickName = et_username.getText().toString();
        String birthday = tv_birthdate.getText().toString();
        String schoolAddress = tv_school_address.getText().toString();
        String schoolName = tv_school_name.getText().toString();

        if(TextUtils.isEmpty(nickName)){
            showShortToast("昵称不能为空！");
            return;
        }

        if(TextUtils.isEmpty(birthday)){
            showShortToast("昵出生日期不能为空！");
            return;
        }

        if(TextUtils.isEmpty(schoolAddress)){
            showShortToast("学校地址不能为空！");
            return;
        }

        if(TextUtils.isEmpty(schoolName)){
            showShortToast("学校名称不能为空！");
            return;
        }

        if(TextUtils.isEmpty(levelId)){
            showShortToast("年级名称不能为空！");
            return;
        }

        mPresenter.updateUser(userEntity.getName(), nickName, tv_sex.getText().toString().equals("女") ? "0" : "1",
                birthday, schoolAddress, regionCode, schoolName,
                et_resume.getText().toString(), et_address.getText().toString(), levelId, tv_class.getText().toString()
        ,userEntity.getAvatar(),new ApiRequestListener<String>(){
                    @Override
                    public void onResultSuccess(String res) {
                        onUpdateUserSuccess(res);
                    }
                });
    }

    public void onUpdateUserSuccess(String responseBody) {
        RequestEntity entity = JSON.parseObject(responseBody, RequestEntity.class);
        if (entity.getCode() == 0) {
            showShortToast(R.string.success_change);
            userEntity.setNickName(et_username.getText().toString()); // 昵称
            userEntity.setSex(tv_sex.getText().toString().equals("女") ? 0 : 1);
            userEntity.setBirthdayStr(tv_birthdate.getText().toString()); // 出生日期
            userEntity.setArea(et_address.getText().toString()); // 联系地址
            userEntity.setRegionName(tv_school_address.getText().toString()); // 学校所在
            userEntity.setSchoolName(tv_school_name.getText().toString()); // 学校名称
            userEntity.setLevelId(levelId); // 年级
            userEntity.setClassId(tv_class.getText().toString()); // 班级
            userEntity.setRemark(et_resume.getText().toString()); // 个人简介
            userEntity.setRegionCode(regionCode); // 用于请求学校
            AppContext.getInstance().saveUserData(userEntity);
            EventBus.getDefault().post(new AppEventType(AppEventType.RE_USER_INFO_REQUEST));
            AppContext.getInstance().setIsUserDataPerfect(true);
            finish();
        } else {
            showShortToast(entity.getMsg());
        }
    }

    @OnClick({R.id.tv_sex, R.id.tv_birthdate, R.id.tv_school_name, R.id.tv_school_address
            , R.id.tv_grade, R.id.tv_class, R.id.btn_save, R.id.btn_cancel, R.id.ad_avatar})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_avatar:
                CameraActivity.openActivity(this, AppEventType.SET_PER_MODI_CAMERA_REQUEST, CameraActivity.FROM_NOTE);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                updateUser();
                break;
            case R.id.tv_sex:
                showSexDialog();
                break;
            case R.id.tv_birthdate:
                if (pvTime == null) {
                    //时间选择器
                    pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
                    pvTime.setTime(new Date());
                    pvTime.setCyclic(true);
                    pvTime.setCancelable(true);
                    //时间选择后回调
                    pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                        @Override
                        public void onTimeSelect(Date date) {
                            tv_birthdate.setText(DateUtil.getTime(date));
                        }
                    });
                }
                pvTime.show();
                break;
            case R.id.tv_school_name:
                showSchoolName();
                break;
            case R.id.tv_school_address:
                openActivity(SelectAddressActivity.class);
                break;
            case R.id.tv_grade:
                showGradeDialog();
                break;
            case R.id.tv_class:
                showClassDialog();
                break;
        }
    }
}
