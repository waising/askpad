package com.asking.pad.app.ui.pay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.CourseEntity;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wxwang on 2016/10/27.
 */

public class PayTextBookAdapter extends RecyclerView.Adapter<PayTextBookAdapter.ViewHolder> {

    private List<CourseEntity> mDatas;
    private Context mContext;

    OnCommItemListener mListener;

    public PayTextBookAdapter(Context context, List<CourseEntity> datas, OnCommItemListener mListener){
        this.mContext = context;
        this.mDatas = datas;
        this.mListener =mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pay_version_list,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CourseEntity courseEntity = mDatas.get(position);

        String imageName = Constants.getVersionImages(courseEntity.getSubjectImgKey());
        if(!TextUtils.isEmpty(imageName)){
            InputStream in;
            try {
                in = mContext.getAssets().open("images/km/"+imageName);
                Bitmap bmp= BitmapFactory.decodeStream(in);
                holder.mVersionIv.setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }else{
            holder.mVersionIv.setImageResource(R.mipmap.class_img);
        }
        holder.mVersionTv.setText(courseEntity.getVersionName());
        if (courseEntity.isSelect){
            holder.mClassLy.setBackgroundResource(R.mipmap.pay_jc_border_bg);
        }else{
            holder.mClassLy.setBackgroundResource(0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CourseEntity ii : mDatas) {
                    ii.isSelect = false;
                }
                courseEntity.isSelect = true;
                notifyDataSetChanged();
                mListener.OnCommItem(courseEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.version_iv)
        ImageView mVersionIv;
        @BindView(R.id.version_tv)
        TextView mVersionTv;
        @BindView(R.id.class_ly)
        LinearLayout mClassLy;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

