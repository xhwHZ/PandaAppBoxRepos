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
		// 内容默认缩起来
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
		tv_author.setText("作者:" + data.author);
	}

	// 获取全部内容展开时的高度
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

	// 获取展开7行时的高度
	private int getShortMeasureHeight()
	{
		// 复制一个新的TextView进行测量，最好不要在原来的TextView上测量，有可能影响其它代码
		TextView copyTextView = new TextView(BaseApplication.getAppliction());
		copyTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// 字体14dp
		copyTextView.setLines(7);// 强制占7行s
		copyTextView.setMaxLines(7);// 最多7行

		// 开始测量
		int width = copyTextView.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		copyTextView.measure(widthMeasureSpec, heightMeasureSpec);
		return copyTextView.getMeasuredHeight();

	}

	private boolean isExpand = false;// 默认不展开

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
		{// 所有层级的布局中都找不到ScrollView
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
			if (!isExpand)// 展开
			{
				startHeight = getShortMeasureHeight();
				endHeight = getLongMeasureHeight();
				isExpand = true;
			} else
			{// 缩起
				startHeight = getLongMeasureHeight();
				endHeight = getShortMeasureHeight();
				isExpand = false;
			}

			ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight,
					endHeight);

			// 值变化
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animator)
				{
					int value = (Integer) animator.getAnimatedValue();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_content
							.getLayoutParams();
					layoutParams.height = value;
					tv_content.setLayoutParams(layoutParams);
					//让ScrollView移动到最下面
					scrollView.scrollTo(0, scrollView.getMeasuredHeight());
				}
			});

			// 状态变化
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
