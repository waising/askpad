package com.asking.pad.app.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.NetworkUtils;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.classmedia.ClassMediaActivity;
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

public class ClassMainFragment extends BaseEvenFrameFragment<UserPresenter,UserModel> {
    @BindView(R.id.rv_main)
    RecyclerView rv_main;

    CommAdapter mineAdapter;
    List<LabelEntity> mDatas = new ArrayList<>();

    public static ClassMainFragment newInstance() {
        ClassMainFragment fragment = new ClassMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first_main);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        mDatas.clear();
        mDatas.add(new LabelEntity(R.mipmap.backpacket_czsx,"XK01", getString(R.string.ask_czsx)));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_czwl,"XK02", getString(R.string.ask_czwl)));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_gzsx,"XK03", getString(R.string.ask_gzsx)));
        mDatas.add(new LabelEntity(R.mipmap.backpacket_gzwl,"XK04", getString(R.string.ask_gzwl)));

        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
        rv_main.setLayoutManager(mgr);
        mineAdapter = new CommAdapter(getActivity(), mDatas);
        rv_main.setAdapter(mineAdapter);
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassifyActivty.openActivity(labelEntity.getId(),labelEntity.getName(),false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}