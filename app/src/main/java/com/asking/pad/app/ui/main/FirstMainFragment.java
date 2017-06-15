package com.asking.pad.app.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.NetworkUtils;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.superclass.StudyClassSubject;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.classmedia.ClassMediaActivity;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.oto.NetUnbleDialog;
import com.asking.pad.app.ui.oto.OtoAskActivity;
import com.asking.pad.app.ui.superclass.classify.ClassifyActivty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.asking.pad.app.commom.Constants.APP_CAMERA_PATH_KEY;

/**
 * Created by jswang on 2017/4/10.
 */

public class FirstMainFragment extends BaseEvenFrameFragment<UserPresenter,UserModel> {
    @BindView(R.id.rv_main)
    RecyclerView rv_main;

    CommAdapter mineAdapter;
    List<LabelEntity> mDatas = new ArrayList<>();


    List<StudyClassSubject> dataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first_main);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
        rv_main.setLayoutManager(mgr);
        mineAdapter = new CommAdapter(getActivity(), mDatas);
        rv_main.setAdapter(mineAdapter);

        initNetData();
    }

    public void clearData() {
        mDatas.clear();
        mDatas.add(new LabelEntity(R.mipmap.backpacket_czsx,"M2", 0+""));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_czwl,"P2", 0+""));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_gzsx,"M3", 0+""));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_gzwl,"P3", 0+""));
        mineAdapter.notifyDataSetChanged();
    }

    public void initNetData() {
        mDatas.clear();
        mDatas.add(new LabelEntity(R.mipmap.backpacket_czsx,"M2", 0+""));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_czwl,"P2", 0+""));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_gzsx,"M3", 0+""));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_gzwl,"P3", 0+""));

        mPresenter.geteStudyClassicTree(new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                List<StudyClassSubject> list = JSON.parseArray(res,StudyClassSubject.class);
                dataList.clear();
                dataList.addAll(list);

                for (int i = 0; i < dataList.size(); i++) {
                    StudyClassSubject e = list.get(i);
                    for (int j = 0; j < mDatas.size(); j++) {
                        LabelEntity e1 = mDatas.get(j);
                        if(TextUtils.equals(e.getSubjectCatalog(),e1.getId())&&
                                TextUtils.equals(e.getPurchased(),"1") ){
                            e1.setName(""+1);
                        }
                    }
                }

                mineAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.iv_oto,R.id.iv_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_oto://一对一答疑
                if (NetworkUtils.isNetworkAvailable()) {
                    CameraActivity.openActivity(getActivity(), AppEventType.HOME_CAMERA_REQUEST,CameraActivity.FROM_OTHER);
                }else{
                    NetUnbleDialog mDialog = new NetUnbleDialog(getActivity());
                    mDialog.show();
                }
                break;
            case R.id.iv_video:
                CommonUtil.openActivity(ClassMediaActivity.class);
                break;
        }
    }

    public void onEventMainThread(AppLoginEvent event) {
        switch (event.type){
            case AppLoginEvent.LOGIN_SUCCESS:
                initNetData();
                break;
            case AppLoginEvent.LOGIN_OUT:
                clearData();
                break;

        }
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type){
            case AppEventType.HOME_CAMERA_REQUEST://一对一答疑页面
                Bundle bundle = new Bundle();
                bundle.putString(APP_CAMERA_PATH_KEY,(String)event.values[0]);
                CommonUtil.openActivity(OtoAskActivity.class,bundle);
                break;
        }
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_img)
        ImageView item_img;

        @BindView(R.id.iv_free_flag)
        View iv_free_flag;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        private List<LabelEntity> mDatas;
        private Context mContext;

        public CommAdapter(Context context, List<LabelEntity> datas){
            this.mContext = context;
            this.mDatas = datas;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_first_main,parent,false));
        }

        @Override
        public void onBindViewHolder(CommViewHolder holder, int position) {
            final LabelEntity labelEntity = mDatas.get(position);

            holder.item_img.setImageResource(labelEntity.getIcon());

            if(TextUtils.equals(labelEntity.getName(),"1")){
                holder.iv_free_flag.setVisibility(View.GONE);
            }else{
                holder.iv_free_flag.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if(TextUtils.equals(labelEntity.getName(),"1")){
                        bundle.putBoolean("isBuy",true);
                    }else{
                        bundle.putBoolean("isBuy",false);
                    }
                    bundle.putString("classType",labelEntity.getId());
                    bundle.putString("className", Constants.getClassName(getContext(),labelEntity.getId()));
                    CommonUtil.openActivity(ClassifyActivty.class,bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}
