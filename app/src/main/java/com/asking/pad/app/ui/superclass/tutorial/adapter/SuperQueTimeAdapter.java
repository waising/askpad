package com.asking.pad.app.ui.superclass.tutorial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.MusicPlayer;
import com.asking.pad.app.entity.SuperSuperClassQuestionTimeEntity;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.asking.pad.app.widget.MultiStateView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/12.
 */

public class SuperQueTimeAdapter extends RecyclerView.Adapter<SuperQueTimeAdapter.CommViewHolder>{

    int curExpandIndex = -1;

    private List<SuperSuperClassQuestionTimeEntity.AttrListBean> mDatas;
    private Context mContext;
    private RecyclerView recyclerView;
    private MusicPlayer musicPlayer;

    public SuperQueTimeAdapter(Context context, List<SuperSuperClassQuestionTimeEntity.AttrListBean> datas,
                                         RecyclerView recyclerView, MusicPlayer musicPlayer,OnCommItemListener mListener){
        this.mContext = context;
        this.mDatas = datas;
        this.recyclerView = recyclerView;
        this.musicPlayer = musicPlayer;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_quetime,parent,false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final SuperSuperClassQuestionTimeEntity.AttrListBean item = mDatas.get(position);

        holder.titleMathView.setText("问题"+(position+1)+"："+item.getTitleName());
        holder.mathView.setText(item.getContentHtml());

        holder.ad_voice.setTag(R.id.key_position, position);
        holder.ad_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.bindVoice((AskSimpleDraweeView) v);
                mListener.onPlayVoice(Integer.parseInt(v.getTag(R.id.key_position)+""));
            }
        });

        holder.rl_tit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (curExpandIndex != position) {
                    holder.el_layout.expand();
                    holder.iv_expand.setSelected(true);
                    if(curExpandIndex != -1){
                        CommViewHolder h = (CommViewHolder) recyclerView.findViewHolderForAdapterPosition(curExpandIndex);
                        if (h != null) {
                            h.iv_expand.setSelected(false);
                            h.el_layout.collapse();
                        }
                    }
                    curExpandIndex = position;
                } else {
                    holder.el_layout.collapse();
                    holder.iv_expand.setSelected(false);
                    curExpandIndex = -1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.el_layout)
        ExpandableLayout el_layout;

        @BindView(R.id.titleMathView)
        AskMathView titleMathView;

        @BindView(R.id.multiStateView)
        MultiStateView multiStateView;

        @BindView(R.id.mathView)
        AskMathView mathView;

        @BindView(R.id.iv_expand)
        ImageView iv_expand;

        @BindView(R.id.rl_tit)
        RelativeLayout rl_tit;

        @BindView(R.id.ad_voice)
        AskSimpleDraweeView ad_voice;

        boolean isLoading = true;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mathView.formatMath().showWebImage(multiStateView);
            titleMathView.formatMath();
            ad_voice.setImageUrl(Constants.GifHeard+"voice.gif");
            titleMathView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress >= 100) {
                        isLoading = false;
                    } else{
                        isLoading = true;
                    }
                }
            });
        }
    }

    public interface OnCommItemListener{
        void onPlayVoice(int e);
    }

    OnCommItemListener mListener;


}
