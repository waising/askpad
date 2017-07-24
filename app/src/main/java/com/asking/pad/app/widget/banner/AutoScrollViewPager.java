package com.asking.pad.app.widget.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.BannerInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AutoScrollViewPager extends LinearLayout {
	private UninterceptableViewPager mViewPager;
	private AutoPagerAdapter mAdapter;
	private PagerControl mPagerControl;
	private View mView;
	private Handler mHandler;
	private AutoRunnable autoRunnable;
	private int g;
	private boolean mIsToDrag;
	private int i;
	private final PageChangeListener mPageChangeListener;
	private boolean mCycling;
	private boolean mAutoCycle;

	public AutoScrollViewPager(Context context) {
		super(context);
		autoRunnable = new AutoRunnable(this);
		mPageChangeListener = new PageChangeListener(this);
		mAutoCycle = false;
		init(context);
	}

	public AutoScrollViewPager(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		autoRunnable = new AutoRunnable(this);
		mPageChangeListener = new PageChangeListener(this);
		mAutoCycle = false;
		init(context);
	}
	
	class AutoRunnable implements Runnable {
	    final  AutoScrollViewPager a;

	    AutoRunnable(AutoScrollViewPager autoScrollViewPager) {
	        this.a = autoScrollViewPager;
	    }

	    public void run() {
	        if (this.a.mViewPager != null) {
	            this.a.mViewPager.setCurrentItem(this.a.mViewPager.getCurrentItem() + 1, true);
	        }
	    }
	}

	public static int a(Context context, int i) {
		return (int) (((float) (i >= 0 ? 1 : -1)) * 0.5f + ((float) i)
				* context.getResources().getDisplayMetrics().density);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.horizontal_pager_view2, this);
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setOrientation(LinearLayout.VERTICAL);
		setLayoutParams(layoutParams);
		mView = findViewById(R.id.framelayout_pager);
		mViewPager = (UninterceptableViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setOnPageChangeListener(mPageChangeListener);
		setViewPagerScrollSpeed(mViewPager);
		mPagerControl = (PagerControl) findViewById(R.id.pager_indicator);
		mHandler = new Handler();
		this.g = (int) (((double) ViewConfiguration.get(this.getContext())
				.getScaledTouchSlop()) * 0.5d);
	}
	
	public void setAdapter(AutoPagerAdapter mAdapter){
		this.mAdapter = mAdapter;
		mViewPager.setAdapter(mAdapter);
	}

	private void setViewPagerScrollSpeed(
			UninterceptableViewPager uninterceptableViewPager) {
		try {
			Field declaredField = ViewPager.class.getDeclaredField("mScroller");
			declaredField.setAccessible(true);
			Field declaredField2 = ViewPager.class.getDeclaredField("sInterpolator");
			declaredField2.setAccessible(true);
			FixedSpeedScroller dVar = new FixedSpeedScroller(uninterceptableViewPager.getContext(),
					(Interpolator) declaredField2.get(null));
			dVar.a(1000);
			declaredField.set(uninterceptableViewPager, dVar);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e2) {
		} catch (IllegalAccessException e3) {
		}
	}

	public void removeAuto() {
		mHandler.removeCallbacks(autoRunnable);
	}

	public void autoCycle() {
		if (mCycling) {
			mHandler.removeCallbacks(autoRunnable);
			mHandler.postDelayed(autoRunnable, 5000);
		}
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void onDestroy() {
		if (!(mHandler == null || autoRunnable == null)) {
			mHandler.removeCallbacks(autoRunnable);
			mHandler = null;
			autoRunnable = null;
		}
		if (mViewPager != null) {
			mViewPager.removeAllViews();
			mViewPager = null;
		}
		if (mPagerControl != null) {
			mPagerControl.removeAllViews();
			mPagerControl = null;
		}
		removeAllViews();
		if (mAdapter != null) {
			ArrayList<BannerInfo> list = mAdapter.getList();
			if (list != null) {
				list.clear();
			}
			mAdapter = null;
		}
	}

	public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		int action = motionEvent.getAction();
		if (action == 2 && mIsToDrag) {
			return super.onInterceptTouchEvent(motionEvent);
		}
		if (action == 0 && mAutoCycle) {
			ViewParent parent = getParent();
			if (parent != null) {
				parent.requestDisallowInterceptTouchEvent(true);
			}
			return super.onInterceptTouchEvent(motionEvent);
		} else {
			switch (action & 255) {
			case 0:
				this.i = (int) motionEvent.getX();
				break;
			case 1:
			case 3:
				mIsToDrag = false;
				break;
			case 2:
				action = (int) motionEvent.getX();
				int abs = Math.abs(action - this.i);
				ViewParent parent2 = getParent();
				if (abs > this.g) {
					mIsToDrag = true;
					this.i = action;
					if (parent2 != null) {
						parent2.requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
			}
			return super.onInterceptTouchEvent(motionEvent);
		}
	}

	public void setLayoutTabVisibility(boolean z) {
		if (z) {
			int a = a(getContext(), (int) 3);
			mView.setPadding(a, 0, a, 0);
		} else {
			mView.setPadding(3,3, 3, 0);
		}
	}

	public void setPagerData(ArrayList<?extends BannerInfo> arrayList) {
		if (arrayList != null && arrayList.size() != 0) {
			if (mAdapter != null) {
				mAdapter.setList(arrayList);
				mAdapter.notifyDataSetChanged();
			}
			if (mPagerControl != null) {
				mPagerControl.removeAllViews();
				mPagerControl.a(1);
				mPagerControl.setNumPages(arrayList.size());
			}
			if (mViewPager != null) {
				mViewPager.setCurrentItem(1, false);
			}
			mCycling = true;
			autoCycle();
		}
	}

	public void setStatIndex(int i) {
		mAdapter.setStatIndex(i);
	}

	public class PageChangeListener implements OnPageChangeListener {
		public int a;
		public AutoScrollViewPager b;

		public PageChangeListener(AutoScrollViewPager autoScrollViewPager) {
			this.b = autoScrollViewPager;
		}

		@Override
		public void onPageScrollStateChanged(int i) {
			if (i == 0) {
				this.b.mAutoCycle = false;
				this.b.autoCycle();
			} else {
				this.b.mAutoCycle = true;
				this.b.removeAuto();
			}
		}

		@Override
		public void onPageScrolled(int i, float f, int i2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int i) {
			this.a = i;
			if (i == 0) {
				this.a = b.mAdapter.getCount() - 2;
			} else if (i == this.b.mAdapter.getCount() - 1) {
				this.a = 1;
			}
			if (i != this.a) {
				new Handler().postDelayed(new PageChangeRunnable(this), 150);
			} else {
				int i2 = i - 1;
				if (i == 0) {
					i2 = this.b.mAdapter.getCount() - 3;
				} else if (i == this.b.mAdapter.getCount() - 1) {
					i2 = 0;
				}
				if (i2 != this.b.mPagerControl.getCurrentPage()) {
					this.b.mPagerControl.setCurrentPager(i2);
				}
			}
		}
	}
	
	class PageChangeRunnable implements Runnable {
	    final  PageChangeListener a;

	    PageChangeRunnable(PageChangeListener bVar) {
	        this.a = bVar;
	    }

	    public void run() {
	        if (this.a.b.mViewPager != null) {
	            this.a.b.mViewPager.setCurrentItem(this.a.a, false);
	        }
	    }
	}
}