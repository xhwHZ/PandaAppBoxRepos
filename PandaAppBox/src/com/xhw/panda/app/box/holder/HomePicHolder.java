package com.xhw.panda.app.box.holder;

import java.util.LinkedList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.utils.BitmapHelper;
import com.xhw.panda.app.box.utils.ThreadUtils;

import android.net.VpnService;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class HomePicHolder extends EasyBaseHolder<List<String>>
{

	private ViewPager viewPager;

	@Override
	protected View initItemRootView()
	{
		viewPager = new ViewPager(BaseApplication.getAppliction());
		viewPager.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, BaseApplication
						.getAppliction().getResources()
						.getDimensionPixelSize(R.dimen.home_img_height)));
		return viewPager;
	}

	@Override
	protected void refreshItem()
	{
		viewPager.setAdapter(new HomePageAdapter());
		// 不要从第0页开始，从第0页开始不能往左边滑动了，初始从一半的位置开始，具体位置微调
		int midPosition = Integer.MAX_VALUE / 2;
		// 调整到从第零张开始
		int startMidPosition = midPosition - (midPosition % data.size());
		viewPager.setCurrentItem(startMidPosition);// 会导致onPageSelected被调用

		final AutoTask autoTask = new AutoTask();
		autoTask.start();

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					autoTask.stop();
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					autoTask.start();
					break;
				}
				return false;
			}
		});
	}

	// 是否开始轮播的标记
	private boolean flag=false;

	/**
	 * 自动轮播任务类
	 * 
	 * @author admin
	 *
	 */
	private class AutoTask implements Runnable
	{

		@Override
		public void run()
		{
			if (flag)
			{
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
				ThreadUtils.postDelayed(this, 2000);
			}
		}

		//外部调用这个方法，启动run方法
		public void start()
		{
			if (!flag)
			{
				flag = true;
				ThreadUtils.postDelayed(this, 2000);
			}
		}

		public void stop()
		{
			if (flag)
			{
				flag = false;
				ThreadUtils.cancel(this);
			}
		}
	}

	private class HomePageAdapter extends PagerAdapter
	{

		private LinkedList<ImageView> imageViewList = new LinkedList<ImageView>();

		@Override
		public int getCount()
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			ImageView imageView = null;
			if (imageViewList.size() > 0)
			{
				imageView = imageViewList.remove(0);
			} else
			{
				imageView = new ImageView(BaseApplication.getAppliction());
			}
			imageView.setScaleType(ScaleType.FIT_XY);
			BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils();
			bitmapUtils.display(
					imageView,
					GlobalContants.IMAGE_SERVLET
							+ data.get(position % data.size()));
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			imageViewList.add((ImageView) object);
			container.removeView((View) object);
		}

	}
}
