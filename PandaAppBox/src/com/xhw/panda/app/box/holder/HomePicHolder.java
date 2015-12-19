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
		// ��Ҫ�ӵ�0ҳ��ʼ���ӵ�0ҳ��ʼ��������߻����ˣ���ʼ��һ���λ�ÿ�ʼ������λ��΢��
		int midPosition = Integer.MAX_VALUE / 2;
		// �������ӵ����ſ�ʼ
		int startMidPosition = midPosition - (midPosition % data.size());
		viewPager.setCurrentItem(startMidPosition);// �ᵼ��onPageSelected������

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

	// �Ƿ�ʼ�ֲ��ı��
	private boolean flag=false;

	/**
	 * �Զ��ֲ�������
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

		//�ⲿ�����������������run����
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
