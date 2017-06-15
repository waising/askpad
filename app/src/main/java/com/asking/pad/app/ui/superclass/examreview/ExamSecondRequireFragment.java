package com.asking.pad.app.ui.superclass.examreview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.CBaseAdapter;
import com.asking.pad.app.entity.ExamRequireEntity;
import com.asking.pad.app.entity.ExerAskEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MtGridLayout;
import com.asking.pad.app.widget.MultiStateView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asking.pad.app.R.id.recyclerView;

/**
 * Created by jswang on 2017/4/14.
 */

public class ExamSecondRequireFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(recyclerView)
    RecyclerView recycler;
    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;

    private List<ExamRequireEntity> mDataList = new ArrayList<>();
    private CommAdapter mAdapter;

    private String knowledgeId;

    public static ExamSecondRequireFragment newInstance(String knowledgeId) {
        ExamSecondRequireFragment fragment = new ExamSecondRequireFragment();
        Bundle bundle = new Bundle();
        bundle.putString("knowledgeId", knowledgeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exam_second_req);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            knowledgeId = bundle.getString("knowledgeId");
        }
    }

    @Override
    public void initView() {
        super.initView();
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CommAdapter(getContext(), mDataList);
        recycler.setAdapter(mAdapter);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        if(!TextUtils.isEmpty(knowledgeId)){
            initLoadData(knowledgeId);
        }
    }

    public void initLoadData(String knowledgeId) {
        this.knowledgeId = knowledgeId;
        if (multiStateView != null) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            mPresenter.secondreviewzhuant(knowledgeId,"tixzdfxs",new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String res) {
                    String content = JSON.parseObject(res).getString("tixzdfxs");
                    List<ExamRequireEntity> list = JSON.parseArray(content,ExamRequireEntity.class);
                    if (list != null && list.size()>0) {
                        mDataList.clear();
                        mDataList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    } else {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    }
                }
            });
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommAdapter.ViewHolder>{
        private List<ExamRequireEntity> mDatas;
        private Context mContext;

        public CommAdapter(Context context, List<ExamRequireEntity> datas){
            this.mContext = context;
            this.mDatas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_exam_second_req,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ExamRequireEntity item = mDatas.get(position);
            holder.tv_name.setText("类型"+(position+1)+"："+item.name);

            holder.load_view.setVisibility(View.VISIBLE);
            holder.web_zhanycm.formatMath().showWebImage(holder.load_view);
            if(!TextUtils.isEmpty(item.zhanycm)){
                holder.web_zhanycm.setText(item.zhanycm);
            }else{
                holder.load_view.setVisibility(View.GONE);
            }

            if(item.isExpand){
                holder.el_layout.expand(false);
            }else{
                holder.el_layout.collapse(false);
            }
            holder.rl_expand.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    item.isExpand = !item.isExpand;
                    holder.rl_expand.setSelected(item.isExpand);
                    if(item.isExpand){
                        holder.el_layout.expand();
                    }else{
                        holder.el_layout.collapse();
                    }
                }
            });


            holder.rl_expand.setSelected(item.isExpand);

            if(item.diants != null && item.diants.size()>0){
                holder.ll_list.removeAllViews();
                holder.ll_list.setAdapter(new GridAdapter(item));
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.rl_expand)
            RelativeLayout rl_expand;

            @BindView(R.id.tv_name)
            TextView tv_name;

            @BindView(R.id.el_layout)
            ExpandableLayout el_layout;

            @BindView(R.id.load_view)
            MultiStateView load_view;

            @BindView(R.id.web_zhanycm)
            AskMathView web_zhanycm;

            @BindView(R.id.ll_list)
            MtGridLayout ll_list;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                ll_list.setOrientation(LinearLayout.VERTICAL);
                ll_list.setColumnCount(1);
            }
        }
    }

    public class GridAdapter extends CBaseAdapter<ExerAskEntity> {
        ExamRequireEntity mEntity;

        class ViewHolder {
            public View rl_expand;
            public TextView tv_name;
            public ExpandableLayout el_layout;
            public AskMathView web_zhanycm;

            public View btn_detail;
        }

        public GridAdapter(ExamRequireEntity mEntity) {
            super((Activity) getContext(), mEntity.diants);
            this.mEntity = mEntity;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = listContainer.inflate(R.layout.item_exam_second_req_child, null);
                holder.rl_expand = convertView.findViewById(R.id.rl_expand);
                holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                holder.el_layout = (ExpandableLayout)convertView.findViewById(R.id.el_layout);
                holder.web_zhanycm = (AskMathView)convertView.findViewById(R.id.web_zhanycm);
                holder.btn_detail = convertView.findViewById(R.id.btn_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ExerAskEntity item = listItems.get(position);
            holder.tv_name.setText("题型"+(position+1)+"："+item.name);
            holder.rl_expand.setOnClickListener(setOnClick(holder,item));
            holder.rl_expand.setSelected(item.isExpand);

            if(item.isExpand){
                holder.el_layout.expand(false);
            }else{
                holder.el_layout.collapse(false);
            }

            holder.web_zhanycm.formatMath().showWebImage();
            holder.web_zhanycm.setText(item.subject_description_html);

            holder.btn_detail.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("zhanycm", item.silyx);
                    bundle.putSerializable("ExerAskEntity", item);
                    openActivity(ExamTopicDetailsActivity.class, bundle);
                }
            });

            return convertView;
        }

        private View.OnClickListener setOnClick(final ViewHolder holder,final ExerAskEntity item){
            return new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    item.isExpand = !item.isExpand;
                    holder.rl_expand.setSelected(item.isExpand);
                    if(item.isExpand){
                        holder.el_layout.expand();
                    }else{
                        holder.el_layout.collapse();
                    }
                }
            };
        }
    }
}
