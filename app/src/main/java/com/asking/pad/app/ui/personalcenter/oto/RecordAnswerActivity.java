package com.asking.pad.app.ui.personalcenter.oto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.RecordAnswer;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
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
 * 答疑记录界面
 * create by linbin
 */

public class RecordAnswerActivity extends BaseFrameActivity<UserPresenter, UserModel> implements AnswerRecordDelDialog.DelListner {

    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    /**
     * 下拉刷新控件
     */
    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;


    @BindView(R.id.tv_right)
    TextView tvRight;//右边按钮

    /**
     * 数据
     */
    private ArrayList<RecordAnswer> dataList = new ArrayList<>();

    /**
     * Recyview的adapter
     */
    CommAdapter mAdapter;
    /**
     * 开始页数，一页加载几个
     */
    int start = 0, limit = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_record);
        ButterKnife.bind(this);
    }


    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "答疑记录");
        GridLayoutManager mgr = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(mgr);
        mAdapter = new CommAdapter(this);
        recycler.setAdapter(mAdapter);

        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//加载更多
                start += limit;
                loadData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//下拉刷新
                start = 0;
                dataList.clear();
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                loadData();
            }
        });
        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        loadData();
    }

    /**
     * 请求数据
     */
    private void loadData() {
        mPresenter.orderhistory(start + "", limit + "", AppContext.getInstance().getUserName(), "s", new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String orders = resobj.getString("Orders");
                    List<RecordAnswer> list = JSON.parseArray(orders, RecordAnswer.class);
                    if (list != null && list.size() > 0) {
                        dataList.addAll(list);//解析Orders数组
                        mAdapter.notifyDataSetChanged();
                    }
                }
                if(dataList.size()>0){
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                }else{
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
                swipeLayout.refreshComplete();
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                swipeLayout.refreshComplete();
            }
        });
    }

    /**
     * 删除答疑记录
     */
    private void delData(final int position, String id) {
        mPresenter.orderhistoryDel(id, "s", new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//成功保存笔记内容
                showShortToast(R.string.success_del);
                dataList.remove(position);
                mAdapter.notifyDataSetChanged();
                if (dataList.size() == 0) {
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
            }

        });//删除请求

    }

    /**
     * 图片资源
     */

    int[] resIconId = {R.mipmap.ic_pending_evaluation, R.mipmap.ic_already_evaluated, R.mipmap.ic_already_complain};

    @Override
    public void del(int position, String id,AnswerRecordDelDialog delDialog) {
        requestDel(position, id,delDialog);
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_oto_bg)//背景图片
                ImageView iv_oto_bg;
        @BindView(R.id.iv_oto_tip)//标签图标
                ImageView iv_oto_tip;
        @BindView(R.id.tv_class)//年级，科目
                TextView tv_class;
        @BindView(R.id.tv_tea_name)//教师名字
                TextView tv_tea_name;
        @BindView(R.id.tv_tea_code)//教师code
                TextView tv_tea_code;
        @BindView(R.id.tv_time)//时间
                TextView tv_time;
        @BindView(R.id.tv_oto_tip)//标签
                TextView tv_oto_tip;
        @BindView(R.id.close)//删除按钮
                ImageView close;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * recyclew中的adapter
     */
    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        /**
         * 是否删除按钮可见
         */
        private boolean mIsDelVisible;

        private Context mContext;

        public CommAdapter(Context context) {
            this.mContext = context;
        }

        /**
         * 是否删除按钮可见
         */
        public void setDelVisible(boolean isDelVisible) {
            mIsDelVisible = isDelVisible;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_record_answer_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final RecordAnswer e = dataList.get(position);
            switch (e.state) {//少个状态
                case 4://待评价
                    holder.iv_oto_tip.setVisibility(View.VISIBLE);
                    holder.tv_oto_tip.setVisibility(View.VISIBLE);
                    holder.iv_oto_tip.setImageResource(resIconId[0]);
                    holder.tv_oto_tip.setText("待评价");
                    holder.tv_oto_tip.setTextColor(getResources().getColor(R.color.organge));
                    break;
                case 5://已评价
                    holder.iv_oto_tip.setVisibility(View.VISIBLE);
                    holder.tv_oto_tip.setVisibility(View.VISIBLE);
                    holder.iv_oto_tip.setImageResource(resIconId[1]);
                    holder.tv_oto_tip.setText("已评价");
                    holder.tv_oto_tip.setTextColor(getResources().getColor(R.color.deep_blue));
                    break;
                case 11://已投诉
                    holder.iv_oto_tip.setVisibility(View.VISIBLE);
                    holder.tv_oto_tip.setVisibility(View.VISIBLE);
                    holder.iv_oto_tip.setImageResource(resIconId[2]);
                    holder.tv_oto_tip.setText("已投诉");
                    holder.tv_oto_tip.setTextColor(getResources().getColor(R.color.deep_red));
                    break;
                default:
                    holder.iv_oto_tip.setVisibility(View.INVISIBLE);
                    holder.tv_oto_tip.setVisibility(View.INVISIBLE);
                    break;
            }
            if (e.teacher != null) {
                holder.tv_tea_name.setText(e.teacher.name);
                holder.tv_tea_code.setText(e.teacher.code);
            }
            if (e.time != null) {
                holder.tv_time.setText(DateUtil.stampToDate(e.time.uploadTime));
            }
            if (e.subject != null) {
                String subjectClass;
                if (TextUtils.equals(e.subject.subject, Constants.subjectValues[0])) {
                    subjectClass = getString(R.string.online_dialog1_t1);
                } else {
                    subjectClass = getString(R.string.online_dialog1_t2);
                }
                switch (e.subject.grade) {
                    case 7:
                        subjectClass = subjectClass + "-" + getString(R.string.online_dialog1_t3);
                        break;
                    case 8:
                        subjectClass = subjectClass + "-" + getString(R.string.online_dialog1_t4);
                        break;
                    case 9:
                        subjectClass = subjectClass + "-" + getString(R.string.online_dialog1_t5);
                        break;
                    case 10:
                        subjectClass = subjectClass + "-" + getString(R.string.online_dialog1_t6);
                        break;
                    case 11:
                        subjectClass = subjectClass + "-" + getString(R.string.online_dialog1_t7);
                        break;
                    case 12:
                        subjectClass = subjectClass + "-" + getString(R.string.online_dialog1_t8);
                        break;
                }
                holder.tv_class.setText(subjectClass);

                String imgUrl = e.getImgUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    BitmapUtil.displayImage(imgUrl, holder.iv_oto_bg, true);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {//点击每项的结果，跳转到短视频播放界面
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(e.getVideoUrl())){
                            Bundle parameter = new Bundle();
                            parameter.putSerializable("OtoRecord",e);
                            openActivity(OtoRecordDetailActivity.class,parameter);
                        }else{
                            Toast.makeText(RecordAnswerActivity.this,"没有答疑记录视频地址！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.close.setOnClickListener(new View.OnClickListener() {//删除按钮
                    @Override
                    public void onClick(View v) {
                        showDelDialog(position, e.orderId);
                    }
                });

                if (mIsDelVisible) {
                    holder.close.setVisibility(View.VISIBLE);
                } else {
                    holder.close.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private void showDelDialog(int position, String id) {
        AnswerRecordDelDialog answerRecordDelDialog = new AnswerRecordDelDialog();
        answerRecordDelDialog.setDelListner(this);
        answerRecordDelDialog.setPosition(position);
        answerRecordDelDialog.setId(id);
        answerRecordDelDialog.show(this.getSupportFragmentManager(), "");
    }

    /**
     * 请求删除接口
     */
    private void requestDel(int position, String id,AnswerRecordDelDialog delDialog) {
        delDialog.dismiss();
        delData(position, id);
    }

    @OnClick({R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right://右边编辑按钮
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_answer_complte), null,
                            null, null);
                    ((TextView) view).setText("完成");
                    mAdapter.setDelVisible(true);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_answer_edit), null,
                            null, null);
                    ((TextView) view).setText("编辑");
                    mAdapter.setDelVisible(false);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
