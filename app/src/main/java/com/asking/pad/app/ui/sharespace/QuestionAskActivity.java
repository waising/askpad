package com.asking.pad.app.ui.sharespace;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.MultipartBody;

/**
 * Created by jswang on 2017/7/17.
 */

public class QuestionAskActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {

    @BindView(R.id.toolBar)//标题
            Toolbar mToolbar;
    @BindView(R.id.camera_iv)
    ImageView cameraIv;
    @BindView(R.id.question_caifu_et)
    EditText caifuEt;
    @BindView(R.id.caifu_info_tv)
    TextView caifuInfoTv;

    @BindView(R.id.question_title_et)
    EditText titleEt;
    @BindView(R.id.question_content_et)
            EditText contentEt;


    RecyclerView rv_grade;
    ArrayList<LabelEntity> gradeList = new ArrayList<>();

    RecyclerView rv_subject;
    ArrayList<LabelEntity> subjectList = new ArrayList<>();

    String km = "M";
    String levelId = "7";
    String caifu= "0";
    String picTakePath = "";
    private MaterialDialog mLoadDialog;

    Bitmap bitmap;
    //-----------拍照End------------------
    public void onEventMainThread(AppEventType event) {
        switch (event.type){
            case AppEventType.NOTE_CAMERA_REQUEST:
                picTakePath = (String)event.values[0];
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(picTakePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap= BitmapFactory.decodeStream(fis);

                cameraIv.setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_ask);
        ButterKnife.bind(this);
    }

    @Override
    public void initData(){
        super.initData();

        gradeList.add(new LabelEntity("7", "七年级", true));
        gradeList.add(new LabelEntity("8", "八年级", false));
        gradeList.add(new LabelEntity("9", "九年级", false));
        gradeList.add(new LabelEntity("10", "高一", false));
        gradeList.add(new LabelEntity("11", "高二", false));
        gradeList.add(new LabelEntity("12", "高三", false));

        subjectList.add(new LabelEntity("M", "数学", true));
        subjectList.add(new LabelEntity("P", "物理", false));
    }

    @Override
    public void initView(){
        super.initView();
        setToolbar(mToolbar, "我要提问");

        mLoadDialog = getLoadingDialog().build();

        rv_grade = (RecyclerView) findViewById(R.id.rv_grade);
        rv_subject = (RecyclerView) findViewById(R.id.rv_subject);

        rv_grade.setLayoutManager(new GridLayoutManager(mActivity, 6));
        rv_grade.setAdapter(new CommAdapter(mActivity, gradeList, new OnItemListener() {
            @Override
            public void OnItem(LabelEntity e) {
                levelId = e.getId();
            }
        }));

        rv_subject.setLayoutManager(new GridLayoutManager(mActivity, 6));
        rv_subject.setAdapter(new CommAdapter(mActivity, subjectList, new OnItemListener() {
            @Override
            public void OnItem(LabelEntity e) {
                km = e.getId();
            }
        }));

        caifuInfoTv.setText(String.format(String.valueOf(caifuInfoTv.getText()), Double.toString(AppContext.getInstance().getUserEntity().getIntegral())));
    }


    @OnClick({R.id.camera_iv,R.id.question_ask_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.camera_iv:
                CameraActivity.openActivity(this, AppEventType.NOTE_CAMERA_REQUEST, CameraActivity.FROM_OTHER);
                break;
            case R.id.question_ask_btn:
                submitQuestion();
                break;
        }
    }

    private void submitQuestion(){
        if(TextUtils.isEmpty(picTakePath)){
            showShortToast("请先拍取题目");
            return;
        }else if(TextUtils.isEmpty(titleEt.getText())){
            showShortToast("请输入问题标题");
            return;
        }else if(!TextUtils.isEmpty(caifuEt.getText())){
            caifu = caifuEt.getText().toString();
        }

        //上传图片
        upLoadImage(picTakePath);
    }

    private void submit(String content){
        try {

            String c = "";
            if(!TextUtils.isEmpty(contentEt.getText()))
                c = contentEt.getText().toString();

            mPresenter.submitQuestion(km,levelId,caifu,titleEt.getText().toString(),c + "</br>"+content,new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String resStr) {//数据返回成功
                    showShortToast("提交成功！");
                    mLoadDialog.dismiss();
                    //刷新列表
                    EventBus.getDefault().post(new AppEventType(AppEventType.QUESTION_ASK,caifu));
                    //刷新本地信息 ask币
                    EventBus.getDefault().post(new AppEventType(AppEventType.RE_USER_INFO_REQUEST));

                    QuestionAskActivity.this.finish();
                    Log.i(QuestionAskActivity.class.getSimpleName(),"提交成功");
                }

                @Override
                public void onResultFail(){
                    showShortToast("提问失败");
                    mLoadDialog.dismiss();
                }
            });
        }catch (Exception e){
            mLoadDialog.dismiss();
        }
    }

    /**
     * 加载拍照后图片存储路径
     *
     */
    public void upLoadImage(String filePath) {
        mLoadDialog.show();

        MultipartBody.Part body = CommonUtil.getMultipartBodyPart(this, bitmap, filePath);

        mPresenter.sendSubmitPic(body, new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                //mLoadDialog.dismiss();
                Log.i(QuestionAskActivity.class.getSimpleName(),"上传成功");

                //上传题目
                submit(resStr);
            }

            @Override
            public void onResultFail(){
                showShortToast("提问失败");
                mLoadDialog.dismiss();
            }
        });//提交图片，获取地址，在上传问题
    }

    class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {
        ArrayList<LabelEntity> dataList = new ArrayList<>();
        Activity mActivity;
        OnItemListener mListener;

        public CommAdapter(Activity mActivity, ArrayList<LabelEntity> dataList, OnItemListener mListener) {
            this.mActivity = mActivity;
            this.dataList = dataList;
            this.mListener = mListener;
        }

        @Override
        public CommAdapter.CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommAdapter.CommViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_grade_popup, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommAdapter.CommViewHolder holder, final int position) {
            final LabelEntity e = dataList.get(position);
            holder.item_name.setSelected(e.getSelect());
            holder.item_name.setText(e.getName());
            holder.item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (LabelEntity ii : dataList) {
                        ii.setSelect(false);
                    }

                    e.setSelect(true);
                    notifyDataSetChanged();

                    mListener.OnItem(e);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class CommViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_name)
            TextView item_name;

            public CommViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private interface OnItemListener {
        void OnItem(LabelEntity e);
    }

}
