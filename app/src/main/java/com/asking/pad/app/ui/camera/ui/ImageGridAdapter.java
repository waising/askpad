package com.asking.pad.app.ui.camera.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asking.pad.app.R;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.camera.utils.ImageService;

import java.io.File;


public class ImageGridAdapter extends  RecyclerView.Adapter<ImageGridAdapter.ViewHolder> {
    private Activity mActivity;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        public String mURL;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView =  (ImageView) itemView.findViewById(R.id.id_item_image);
            this.mURL = "";
        }
    }

    public ImageGridAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onBindViewHolder(ImageGridAdapter.ViewHolder holder,final int position) {
        if (!holder.mURL.equals(getItem(position))) {
            holder.mURL = (String) getItem(position);
            String thumb = ImageService.getInstance().getImageThumbnail(holder.mURL);
            String url = holder.mURL;
            if (!(thumb == null || thumb.isEmpty() || !new File(thumb).exists())) {
                url = thumb;
            }
            BitmapUtil.displayImage(url, holder.mImageView,false);
            holder.mImageView.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.putExtra("imgPath",(String)getItem(position));
                    mActivity.setResult(1001, i);
                    mActivity.finish();
                }

            });
        }
    }

    @Override
    public ImageGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageGridAdapter.ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_image_grid,parent,false));
    }

    @Override
    public int getItemCount() {
        return ImageService.getInstance().getCurrentDirImages().size();
    }

    public Object getItem(int position) {
        return ImageService.getInstance().getCurrentDirImages().get(position);
    }
}