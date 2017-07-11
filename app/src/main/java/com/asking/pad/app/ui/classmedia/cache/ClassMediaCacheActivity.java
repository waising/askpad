package com.asking.pad.app.ui.classmedia.cache;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.classmedia.download.ClassDownloadManager;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.personalcenter.oto.AnswerRecordDelDialog;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jswang on 2017/6/9.
 */

public class ClassMediaCacheActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.rec_view)
    RecyclerView rec_view;

    CommAdapter mAdapter;

    private ArrayList<ClassMediaTable> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_class_media);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, getTitle().toString());

        GridLayoutManager mgr = new GridLayoutManager(this, 2);
        rec_view.setLayoutManager(mgr);
        mAdapter = new CommAdapter();
        rec_view.setAdapter(mAdapter);
    }

    @Override
    public void initLoad() {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        Observable<Object> mObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                dataList.clear();
                List<ClassMediaTable> list = DbHelper.getInstance().getAllClassMedia();
                if (list != null) {
                    dataList.addAll(list);
                }
                for (ClassMediaTable e : dataList) {
                    if (e.getDownState() < DownState.START) {
                        e.setDownState(DownState.PAUSE);
                    }
                }
                subscriber.onCompleted();
            }
        });
        mPresenter.newThread(mObservable, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                if (dataList.size() > 0) {
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                    mAdapter.notifyDataSetChanged();
                } else {
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_EMPTY);
            }
        });
    }

    public void onEventMainThread(ClassMediaTable event) {
        try {
            for (int i = 0; i < dataList.size(); i++) {
                if (TextUtils.equals(event.getCourseDataId(), dataList.get(i).getCourseDataId())) {
                    CommViewHolder mHolder = (CommViewHolder) rec_view.findViewHolderForAdapterPosition(i);
                    refreshViewHolder(event, mHolder);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_temp)
        ImageView iv_temp;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.iv_delect)
        ImageView iv_delect;

        @BindView(R.id.tv_down)
        TextView tv_down;

        @BindView(R.id.bar_progress)
        ProgressBar bar_progress;

        @BindView(R.id.tv_progress)
        TextView tv_progress;

        @BindView(R.id.fl_progress)
        View fl_progress;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void refreshViewHolder(ClassMediaTable e, CommViewHolder holder) {
        try {
            switch (e.getDownState()) {
                case DownState.START:
                    holder.tv_down.setText("点击下载");
                    holder.tv_down.setBackgroundResource(R.drawable.bg_corners_green);
                    break;
                case DownState.STOP:
                case DownState.PAUSE:
                case DownState.ERROR:
                    holder.tv_down.setText("暂停");
                    holder.tv_down.setBackgroundResource(R.drawable.bg_corners_or);
                    break;
                case DownState.DOWN:
                    holder.tv_down.setText("下载中...");
                    holder.tv_down.setBackgroundResource(R.drawable.bg_corners_stroke_blue2);
                    break;
                case DownState.FINISH:
                    holder.fl_progress.setVisibility(e.getDownState() == DownState.FINISH ? View.GONE : View.VISIBLE);
                    break;
            }
            int i2 = Math.round(100.0f * e.getReadLength() / e.getCountLength());
            holder.bar_progress.setProgress(i2);
            holder.tv_progress.setText(String.format("%s / %s", FileUtils.formatFileSize(e.getReadLength())
            ,FileUtils.formatFileSize(e.getCountLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {
        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(ClassMediaCacheActivity.this).inflate(R.layout.item_class_media_cache_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final ClassMediaTable e = dataList.get(position);
            holder.tv_name.setText(e.getCourseName());
            if(TextUtils.equals(Constants.CLASS_MEDIA_TYPE_ID[0],e.getCourseTypeId())){
                holder.tv_name.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_class_media_math,0,0,0);
            }else{
                holder.tv_name.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_class_media_physics,0,0,0);
            }
            BitmapUtil.displayImage(e.getVideoImgUrl(), holder.iv_temp, true);
            holder.fl_progress.setVisibility(e.getDownState() == DownState.FINISH ? View.GONE : View.VISIBLE);
            refreshViewHolder(e, holder);
            holder.tv_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (e.getDownState()) {
                        case DownState.START:
                        case DownState.STOP:
                        case DownState.PAUSE:
                        case DownState.ERROR:
                            ClassDownloadManager.getInstance().startDown(e);
                            holder.tv_down.setText("下载中...");
                            holder.tv_down.setBackgroundResource(R.drawable.bg_corners_stroke_blue2);
                            break;
                        case DownState.DOWN:
                            ClassDownloadManager.getInstance().pause(e);
                            holder.tv_down.setText("暂停");
                            holder.tv_down.setBackgroundResource(R.drawable.bg_corners_or);
                            break;
                    }

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (e.getDownState() == DownState.FINISH ) {
                        ClassMediaCacheDetailActivity.openActivity(e);
                    }

                }
            });
            holder.iv_delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDelDialog(e);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private void showDelDialog(final ClassMediaTable e) {
        AnswerRecordDelDialog answerRecordDelDialog = AnswerRecordDelDialog.newInstance("是否删除课程！");
        answerRecordDelDialog.setDelListner(new AnswerRecordDelDialog.DelListner(){
            @Override
            public void del(int position, String id, AnswerRecordDelDialog delDialog) {
                dataList.remove(e);
                ClassDownloadManager.getInstance().deleteDown(e);
                initLoad();
                delDialog.dismiss();
            }
        });
        answerRecordDelDialog.show(this.getSupportFragmentManager(), "AnswerRecordDelDialog");
    }

}
