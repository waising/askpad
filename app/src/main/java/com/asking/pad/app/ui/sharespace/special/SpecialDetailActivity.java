package com.asking.pad.app.ui.sharespace.special;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.sharespace.ShareSpecial;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/7/17.
 */

public class SpecialDetailActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.edt_note_content)
    EditText edt_note_content;//新增笔记按钮

    @BindView(R.id.recycler)
    RecyclerView recycler;

    ArrayList<ShareSpecial> dataList = new ArrayList<>();
    CommentAdapter mAdapter;

    private MaterialDialog mLoadDialog;

    public static void openActivity(String courseTypeId){
        Bundle bundle = new Bundle();
        bundle.putString("courseTypeId", courseTypeId);
        CommonUtil.openActivity(SpecialDetailActivity.class,bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specical_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        mLoadDialog = getLoadingDialog().build();

        for(int i =0;i<10;i++){
            dataList.add(new ShareSpecial());
        }

        recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentAdapter(this,dataList);
        recycler.setAdapter(mAdapter);
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.NOTE_CAMERA_REQUEST:
                loadImage((String)event.values[0]);
                break;
        }
    }

    /**
     * 加载拍照后图片存储路径
     *
     * @param ivPhotoView
     * @param ivTakePhoto
     */
    String qiNiuImgName;
    public void loadImage(String filePath) {
        mLoadDialog.show();
        qiNiuImgName = DateUtil.currentDateMilltime().replace(":", "-").replace(" ", "_") + "note.jpg";
        mPresenter.qiNiuUploadFile(filePath, qiNiuImgName, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                String qiNiuUrl = Constants.QiNiuHead + qiNiuImgName;
                submit("5965d99d5905eec328694d0d","",qiNiuUrl);
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    private void submit(String communionTopicId,String content,String imgUrl){
        mPresenter.topicmsg(communionTopicId,content,imgUrl,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                mLoadDialog.dismiss();
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_photo_view,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photo_view:
                CameraActivity.openActivity(SpecialDetailActivity.this, AppEventType.NOTE_CAMERA_REQUEST, CameraActivity.FROM_NOTE);
                break;
            case R.id.btn_submit:
                String content = edt_note_content.getText().toString();
                if(!TextUtils.isEmpty(content)){
                    Toast.makeText(SpecialDetailActivity.this,"评价不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoadDialog.show();
                submit("5965d99d5905eec328694d0d",content,"");
                break;
        }
    }

}
