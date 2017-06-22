package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.commom.ToastUtil;
import com.asking.pad.app.entity.NoteEntity;
import com.asking.pad.app.presenter.NoteModel;
import com.asking.pad.app.presenter.NotePresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.commom.PhotoShowActivity;
import com.asking.pad.app.ui.login.LoginActivity;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 笔记界面
 * create by linbin
 */

public class NoteActivity extends BaseFrameActivity<NotePresenter, NoteModel> implements NoteAddEditDialog.NoteListner, MyNoteAdapter.CallDelNote, DeleteDialog.DeleteListner {

    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_right)
    TextView tvRight;//新增笔记按钮

    @BindView(R.id.load_view)
    MultiStateView load_view;

    String token;
    int start = 0;
    int limit = 6;
    MyNoteAdapter myNoteAdapter;

    private MaterialDialog mLoadDialog;

    /**
     * 删除弹窗
     */
    private DeleteDialog deleteDialog;
    /**
     * list
     */
    private List<NoteEntity.ListBean> dataList = new ArrayList<>();
    private NoteAddEditDialog mNoteAddEditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynote);
        ButterKnife.bind(this);
    }


    @Override
    public void initView() {
        super.initView();
        setToolbar(mToolbar, getResources().getString(R.string.my_note));//标题栏

        mLoadDialog = getLoadingDialog().build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        token = AppContext.getInstance().getToken();//获取token
        boolean isLogin = AppContext.getInstance().isLogin();//未登陆，跳转到登陆页面
        if (!isLogin) {
            CommonUtil.openActivity(LoginActivity.class);
        }

        myNoteAdapter = new MyNoteAdapter(this, dataList, this);
        myNoteAdapter.startDelNote(this);
        recyclerView.setAdapter(myNoteAdapter);

        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {//刷新控件
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                //　上拉加载更多
                start += limit;
                getDataNow();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                // 上拉刷新
                requstData();
            }
        });
        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_view.setViewState(load_view.VIEW_STATE_LOADING);
                requstData();
            }
        });
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        requstData();
    }

    public void requstData() {
        start = 0;
        dataList.clear();
        getDataNow();
    }


    /**
     * 页面数据请求
     */
    private void getDataNow() {
        mPresenter.presenterMyNote(token, start, limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("list");

                    List<NoteEntity.ListBean> list = JSON.parseArray(resList, NoteEntity.ListBean.class);
                    if (list != null && list.size() > 0) {
                        dataList.addAll(list);
                        myNoteAdapter.notifyDataSetChanged();
                    }

                    swipeLayout.refreshComplete();
                    if (dataList.size() == 0) {
                        load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                    } else {
                        load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                    }

                }
            }

            @Override
            public void onResultFail() {
                swipeLayout.refreshComplete();
                if (dataList.size() == 0) {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            }
        });
    }

    @OnClick({R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right://右边新增笔记编辑按钮
                qiNiuImgName = "";
                mNoteAddEditDialog = new NoteAddEditDialog();
                mNoteAddEditDialog.setFromWhere(NoteAddEditDialog.ADD_NEW_NOTE);
                mNoteAddEditDialog.setNoteListner(this);
                mNoteAddEditDialog.show(this.getSupportFragmentManager(), "");
                break;
        }
    }

    /**
     * 点击保存笔记按钮
     *
     * @param content
     */
    @Override
    public void saveNote(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            ToastUtil.showMessage("请输入笔记标题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showMessage("请输入笔记内容");
            return;
        }
        String qiNiuUrl = "";
        if(!TextUtils.isEmpty(qiNiuImgName)){
            qiNiuUrl = Constants.QiNiuHead + qiNiuImgName;
        }
        mPresenter.saveNode(title, content, qiNiuUrl, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//成功保存笔记内容
                showShortToast(getString(R.string.success_save_note));
                mNoteAddEditDialog.dismiss();
                swipeLayout.autoRefresh();
            }
        });
    }

    /**
     * 修改笔记请求
     *
     * @param content
     * @param id
     */
    @Override
    public void alterNote(String content, String id, final NoteAddEditDialog noteAddEditDialog) {
        mPresenter.presenterAlterMyNote(content, id, new ApiRequestListener<JSONObject>() {
            @Override
            public void onResultSuccess(JSONObject object) {//成功修改笔记内容
                showShortToast(getString(R.string.success_alter_note));
                noteAddEditDialog.dismiss();
                swipeLayout.autoRefresh();
            }
        });
    }

    /**
     * 加载拍照后图片存储路径
     *
     * @param ivPhotoView
     * @param ivTakePhoto
     */
    String qiNiuImgName;
    @Override
    public void loadImage(String filePath, final ImageView ivPhotoView, ImageView ivTakePhoto) {
        mLoadDialog.show();
        qiNiuImgName = DateUtil.currentDateMilltime().replace(":", "-").replace(" ", "_") + "note.jpg";
        mPresenter.qiNiuUploadFile(filePath, qiNiuImgName, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                String qiNiuUrl = Constants.QiNiuHead + qiNiuImgName;
                BitmapUtil.displayImage(qiNiuUrl, ivPhotoView, true);
                ivPhotoView.setVisibility(View.VISIBLE);
                mLoadDialog.dismiss();
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    @Override
    public void jumpToZoom(String imgUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.WEB_IMAGE_URL, imgUrl);
        CommonUtil.openActivity(PhotoShowActivity.class, bundle);//跳转到图片放大界面
    }


    /**
     * 显示删除弹窗
     */
    @Override
    public void delNote(int currentPosition, String id) {
        deleteDialog = new DeleteDialog();
        deleteDialog.setDeleteListner(this);
        deleteDialog.setPosition(currentPosition);
        deleteDialog.setId(id);
        deleteDialog.show(getSupportFragmentManager(), "");
    }


    /**
     * 删除笔记请求
     *
     * @param
     */
    @Override
    public void ok(final int position, String id, DeleteDialog deleteDialog) {
        deleteDialog.dismiss();
        mPresenter.presenterDelMyNote(token, id, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//成功保存笔记内容
                showShortToast(R.string.success_del);
                dataList.remove(position);
                myNoteAdapter.notifyDataSetChanged();

                if (dataList.size() == 0) {
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                } else {
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                }
            }
        });
    }
}
