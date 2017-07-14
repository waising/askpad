package com.asking.pad.app.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.classmedia.cache.ClassMediaCacheActivity;
import com.asking.pad.app.ui.pay.PayAskActivity;
import com.asking.pad.app.ui.personalcenter.CollectionActivity;
import com.asking.pad.app.ui.personalcenter.NoteActivity;
import com.asking.pad.app.ui.personalcenter.SettingActivity;
import com.asking.pad.app.ui.personalcenter.oto.RecordAnswerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/10.
 */

public class MineAcivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.mine_rv)
    RecyclerView mine_rv;

    CommAdapter mineAdapter;
    List<LabelEntity> mDatas = new ArrayList<>();

    public static void openActivity(Activity activity){
        Intent intent = new Intent(activity, MineAcivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second_main);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        mDatas = new ArrayList<>();
        mDatas.add(new LabelEntity(R.mipmap.mine_oto_record, getString(R.string.myOtoRecord)));
        mDatas.add(new LabelEntity(R.mipmap.mine_pay, getString(R.string.pay)));
        mDatas.add(new LabelEntity(R.mipmap.mine_note, getString(R.string.my_note)));
        mDatas.add(new LabelEntity(R.mipmap.mine_class_media_cache, getString(R.string.app_class_media_cache)));
        mDatas.add(new LabelEntity(R.mipmap.mine_shopping_record, getString(R.string.shopping_record)));
        mDatas.add(new LabelEntity(R.mipmap.mine_set, getString(R.string.set)));


        GridLayoutManager mgr = new GridLayoutManager(this, 3);
        mine_rv.setLayoutManager(mgr);
        mineAdapter = new CommAdapter(this, mDatas);
        mine_rv.setAdapter(mineAdapter);
    }

    class CommViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_img)
        ImageView item_img;

        @BindView(R.id.item_text)
        TextView item_text;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        private List<LabelEntity> mDatas;
        private Context mContext;

        public CommAdapter(Context context, List<LabelEntity> datas) {
            this.mContext = context;
            this.mDatas = datas;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_second_main, parent, false));
        }

        @Override
        public void onBindViewHolder(CommViewHolder holder, int position) {
            final LabelEntity labelEntity = mDatas.get(position);

            holder.item_img.setImageResource(labelEntity.getIcon());
            holder.item_text.setText(labelEntity.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (labelEntity.getIcon()) {
                        case R.mipmap.mine_oto_record://答疑记录
                            CommonUtil.openActivity(RecordAnswerActivity.class);
                            break;
                        case R.mipmap.mine_pay://充值
                            CommonUtil.openAuthActivity(PayAskActivity.class);
                            break;
                        case R.mipmap.mine_collect://收藏夹
                            CommonUtil.openAuthActivity(CollectionActivity.class);
                            break;
                        case R.mipmap.mine_class_media_cache://我的缓存
                            CommonUtil.openActivity(ClassMediaCacheActivity.class);
                            break;
                        case R.mipmap.mine_note://笔记本
                            CommonUtil.openActivity(NoteActivity.class);
                            break;
                        case R.mipmap.mine_shopping_record://我的课程
                            CommonUtil.openAuthActivity(MineStudyRecordActivity.class);
                            break;
                        case R.mipmap.mine_set://设置
                            CommonUtil.openAuthActivity(SettingActivity.class);
                            break;
                        case R.mipmap.mine_stu_record:
                            // CommonUtil.openAuthActivity(mContext, MineStuRecordActivity.class);
                            break;
                        case R.mipmap.mine_recommend:
                            //CommonUtil.openAuthActivity(mContext, ShareAwardActivity.class);
                            break;
                        case R.mipmap.mine_sign:
                            //CommonUtil.openAuthActivity(mContext, FeedBackDialog.class);
                            break;

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}
