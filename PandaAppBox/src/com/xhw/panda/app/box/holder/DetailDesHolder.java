package com.xhw.panda.app.box.holder;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.listener.DefaultAnimatorListener;

import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailDesHolder extends EasyBaseHolder<DetailJsonInfo> implements
		OnClickListener
{
	@ViewInject(R.id.des_content)
	private TextView tv_content;

	@ViewInject(R.id.des_author)
	private TextView tv_author;

	@ViewInject(R.id.des_arrow)
	private ImageView iv_arrow;

	@ViewInject(R.id.des_layout)
	private RelativeLayout rl_root;

	private ScrollView scrollView;
	
	@Override
	protected View initItemRootView()
	{
		View view = View.inflate(BaseApplication.getAppliction(),
				R.layout.detail_des, null);
		ViewUtils.inject(this, view);
		// ����Ĭ��������
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_content
				.getLayoutParams();
		layoutParams.height = getShortMeasureHeight();
		tv_content.setLayoutParams(layoutParams);
		return view;
	}

	@Override
	protected void refreshItem()
	{
		rl_root.setOnClickListener(this);
		tv_content.setText(data.des);
		tv_author.setText("����:" + data.author);
	}

	// ��ȡȫ������չ��ʱ�ĸ߶�
	private int getLongMeasureHeight()
	{
		int width = tv_content.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);

		tv_content.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		tv_content.measure(widthMeasureSpec, heightMeasureSpec);
		return tv_content.getMeasuredHeight();
	}

	// ��ȡչ��7��ʱ�ĸ߶�
	private int getShortMeasureHeight()
	{
		// ����һ���µ�TextView���в�������ò�Ҫ��ԭ����TextView�ϲ������п���Ӱ����������
		TextView copyTextView = new TextView(BaseApplication.getAppliction());
		copyTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// ����14dp
		copyTextView.setLines(7);// ǿ��ռ7��s
		copyTextView.setMaxLines(7);// ���7��

		// ��ʼ����
		int width = copyTextView.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		copyTextView.measure(widthMeasureSpec, heightMeasureSpec);
		return copyTextView.getMeasuredHeight();

	}

	private boolean isExpand = false;// Ĭ�ϲ�չ��

	public ScrollView findScrollView(View v)
	{
		ViewParent parent = v.getParent();
		if (parent instanceof ViewGroup)
		{
			ViewGroup group=(ViewGroup) parent;
			
			if (parent instanceof ScrollView)
			{
				return (ScrollView) group;
			} else
			{
				return findScrollView(group);
			}

		} else
		{// ���в㼶�Ĳ����ж��Ҳ���ScrollView
			return null;
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.des_layout:
			scrollView=findScrollView(tv_content);
			int startHeight;
			int endHeight;
			if (!isExpand)// չ��
			{
				startHeight = getShortMeasureHeight();
				endHeight = getLongMeasureHeight();
				isExpand = true;
			} else
			{// ����
				startHeight = getLongMeasureHeight();
				endHeight = getShortMeasureHeight();
				isExpand = false;
			}

			ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight,
					endHeight);

			// ֵ�仯
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animator)
				{
					int value = (Integer) animator.getAnimatedValue();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_content
							.getLayoutParams();
					layoutParams.height = value;
					tv_content.setLayoutParams(layoutParams);
					//��ScrollView�ƶ���������
					scrollView.scrollTo(0, scrollView.getMeasuredHeight());
				}
			});

			// ״̬�仯
			valueAnimator.addListener(new DefaultAnimatorListener() {

				@Override
				public void onAnimationEnd(Animator animator)
				{
					iv_arrow.setImageResource(isExpand ? R.drawable.arrow_up
							: R.drawable.arrow_down);
				}
			});

			valueAnimator.setDuration(500);
			valueAnimator.start();

			break;
		}
	}
}
