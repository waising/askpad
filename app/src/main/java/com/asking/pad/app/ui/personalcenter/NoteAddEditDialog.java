package com.asking.pad.app.ui.personalcenter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.ui.camera.ui.CameraActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

import static com.asking.pad.app.R.id.iv_take_photo;

/**
 * 修改笔记和增加笔记的弹窗
 * create by linbin
 */

public class NoteAddEditDialog extends DialogFragment {

    @BindView(R.id.tv_save)
    TextView vSave;

    @BindView(R.id.iv_close)
    ImageView tvClose;

    @BindView(R.id.tv_note_name)
    TextView tvNoteName;

    @BindView(R.id.tv_note_time)
    TextView tvNoteTime;

    @BindView(R.id.view_edit_line)
    View viewEditLine;
    Unbinder unbinder;

    @BindView(R.id.iv_photo_view)
    ImageView ivPhotoView;

    @BindView(iv_take_photo)
    ImageView ivTakePhoto;

    @BindView(R.id.edt_note_title)
    EditText edtNoteTitle;//笔记内容

    @BindView(R.id.edt_note_content)
    EditText edtNoteContent;//笔记内容

    /**
     * 新增笔记弹窗
     */
    public static final int ADD_NEW_NOTE = 0;
    /**
     * 编辑笔记弹窗
     */
    public static final int EDIT_NOTE = 1;


    private NoteListner mListner;


    private int mFromWhere;

    private String mId;
    private String mContent;
    private String mTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        View view = inflater.inflate(R.layout.layout_dialog_note_add_edit, null);
        unbinder = ButterKnife.bind(this, view);
        initData(view);
        return view;
    }

    /**
     * @param view
     */
    private void initData(View view) {
        if (mFromWhere == ADD_NEW_NOTE) {
            tvNoteName.setText("新增笔记");
            vSave.setText("保存笔记");
            ivPhotoView.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.VISIBLE);
            viewEditLine.setVisibility(View.GONE);
            tvNoteTime.setVisibility(View.INVISIBLE);
        } else {//编辑笔记

            tvNoteName.setText("笔记");
            vSave.setText("编辑笔记");
            ivPhotoView.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            viewEditLine.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mContent)){
                edtNoteContent.setText(mContent);
            }
            tvNoteTime.setText(getString(R.string.note_time_format, mTime));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_save, R.id.iv_close, R.id.iv_take_photo, R.id.iv_photo_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save://保存
                String title = edtNoteTitle.getText().toString();
                String edtContent = edtNoteContent.getText().toString();
                if (mFromWhere == ADD_NEW_NOTE) {//新增笔记
                    if (mListner != null) {
                        mListner.saveNote(title,edtContent);
                    }
                } else {
                    if (mListner != null) {//修改笔记
                        mListner.alterNote(edtContent, mId, this);
                    }
                }
                break;
            case R.id.iv_close://关闭
                dismiss();
                break;
            case R.id.iv_take_photo://调用系统摄像头
                CameraActivity.openActivity(getActivity(), AppEventType.NOTE_CAMERA_REQUEST, CameraActivity.FROM_NOTE);
                break;
            case R.id.iv_photo_view://查看图片
                if (mListner != null) {
                    mListner.jumpToZoom();
                }
                break;
        }
    }


    /**
     * 笔记回调监听
     */
    interface NoteListner {
        void saveNote(String title,String content);//保存笔记

        void alterNote(String content, String id, NoteAddEditDialog noteAddEditDialog);//修改笔记

        void loadImage(String filePath,ImageView ivPhotoView, ImageView ivTakePhoto);

        void jumpToZoom();//跳转到图片放大

    }

    public void setNoteListner(NoteListner noteListner) {
        mListner = noteListner;
    }

    /**
     * 判断是新增笔记界面还是编辑笔记界面
     *
     * @param fromWhere
     */
    public void setFromWhere(int fromWhere) {
        mFromWhere = fromWhere;
    }


    public void setId(String id) {
        mId = id;
    }

    public void setContent(String context) {
        mContent = context;
    }

    public void setTime(String time) {
        mTime = time;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 系统摄像头调用完成回调
     *
     * @param event
     */

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.NOTE_CAMERA_REQUEST:
                if (mListner != null) {//修改笔记
                    mListner.loadImage((String)event.values[0],ivPhotoView, ivTakePhoto);
                }
                break;
        }
    }

}
