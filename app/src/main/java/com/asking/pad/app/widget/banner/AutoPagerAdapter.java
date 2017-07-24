package com.asking.pad.app.widget.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asking.pad.app.entity.BannerInfo;

import java.util.ArrayList;

public abstract class AutoPagerAdapter extends PagerAdapter {
    private final Context context;
    private ArrayList<BannerInfo> list;
    private int statIndex;

    public AutoPagerAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
	public Object instantiateItem(ViewGroup container, int position) {
    	ImageView target = new ImageView(context);
        ArrayList<BannerInfo> arrayList = list;
        if (arrayList == null || arrayList.size() == 0) {
        	 notifyDataSetChanged();
            return target;
        } else {
            int size;
            if (position == 0) {
                size = arrayList.size() - 1;
            } else if (position == getCount() - 1) {
                size = 0;
            } else {
                size = position - 1;
            }
//            BannerInfo aVar = (BannerInfo) arrayList.get(size);
//            target.setScaleType(ScaleType.FIT_XY);
//            target.setImageResource(R.drawable.wallpaper_icon_default);
           // int dimension = (int) this.a.getResources().getDimension(2131427513);
            //DensityUtil.getWindowWidth(context);
           // remoteIconView.b(aVar.a(), 2130838108);
           // remoteIconView.setOnClickListener(new f(this, aVar));
            getView(target,arrayList.get(size));
            container.addView(target, 0);
            return target;
        }
	}
    
    public abstract void getView(ImageView target, BannerInfo aVar);

	public void setStatIndex(int statIndex) {
        this.statIndex = statIndex;
    }


    @SuppressWarnings("unchecked")
	public void setList(ArrayList<?extends BannerInfo> arrayList) {
    	list= (ArrayList<BannerInfo>) arrayList;
    }

    @Override
	public void destroyItem(ViewGroup container, int position, Object object) {
    	container.removeView((View) object);
	}


	public ArrayList<BannerInfo> getList() {
        return list;
    }

	@Override
	public int getCount() {
		return list == null ? 0 : list.size() + 2;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		  return view == ((View) obj);
	}
}