package com.xhw.panda.app.box.holder;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.domain.DetailJsonInfo.SafeInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.listener.DefaultAnimatorListener;
import com.xhw.panda.app.box.utils.BitmapHelper;

public class DetailSafeHolder extends EasyBaseHolder<DetailJsonInfo> implements
		OnClickListener
{

	@ViewInject(R.id.safe_layout)
	private RelativeLayout rl_root;

	@ViewInject(R.id.safe_content)
	private LinearLayout contentGroup;

	@ViewInject(R.id.safe_arrow)
	private ImageView iv_arrow;

	private ImageView[] title_imgs;

	private LinearLayout[] des_layouts;

	private ImageView[] des_imgs;

	private TextView[] des_tvs;

	@Override
	protected View initItemRootView()
	{
		View view = View.inflate(BaseApplication.getAppliction(),
				R.layout.detail_safe, null);

		// ��ʼ������ͼƬ
		title_imgs = new ImageView[4];
		title_imgs[0] = (ImageView) view.findViewById(R.id.iv_1);
		title_imgs[1] = (ImageView) view.findViewById(R.id.iv_2);
		title_imgs[2] = (ImageView) view.findViewById(R.id.iv_3);
		title_imgs[3] = (ImageView) view.findViewById(R.id.iv_4);

		// ��ʼ��������ͼƬ
		des_imgs = new ImageView[4];
		des_imgs[0] = (ImageView) view.findViewById(R.id.des_iv_1);
		des_imgs[1] = (ImageView) view.findViewById(R.id.des_iv_2);
		des_imgs[2] = (ImageView) view.findViewById(R.id.des_iv_3);
		des_imgs[3] = (ImageView) view.findViewById(R.id.des_iv_4);

		// ��ʼ���������ı�
		des_tvs = new TextView[4];
		des_tvs[0] = (TextView) view.findViewById(R.id.des_tv_1);
		des_tvs[1] = (TextView) view.findViewById(R.id.des_tv_2);
		des_tvs[2] = (TextView) view.findViewById(R.id.des_tv_3);
		des_tvs[3] = (TextView) view.findViewById(R.id.des_tv_4);

		// ��ʼ�����������Բ���
		des_layouts = new LinearLayout[4];
		des_layouts[0] = (LinearLayout) view.findViewById(R.id.des_layout_1);
		des_layouts[1] = (LinearLayout) view.findViewById(R.id.des_layout_2);
		des_layouts[2] = (LinearLayout) view.findViewById(R.id.des_layout_3);
		des_layouts[3] = (LinearLayout) view.findViewById(R.id.des_layout_4);

		ViewUtils.inject(this, view);

		// ����Ĭ�����أ��߶�������Ϊ0
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentGroup
				.getLayoutParams();
		layoutParams.height=0;
		contentGroup.setLayoutParams(layoutParams);
		
		rl_root.setOnClickListener(this);
		return view;
	}

	@Override
	protected void refreshItem()
	{
		BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils();
		List<SafeInfo> safeList = data.safe;
		for (int i = 0; i < 4; i++)
		{
			if (i <= safeList.size() - 1)
			{
				SafeInfo safeInfo = safeList.get(i);
				title_imgs[i].setVisibility(View.VISIBLE);
				des_layouts[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(title_imgs[i], GlobalContants.IMAGE_SERVLET
						+ safeInfo.safeUrl);
				bitmapUtils.display(des_imgs[i], GlobalContants.IMAGE_SERVLET
						+ safeInfo.safeDesUrl);
				des_tvs[i].setText(safeInfo.safeDes);
				// ��ɫ����
				int color = 0;
				int colorType = safeInfo.safeDesColor;
				if (colorType >= 1 && colorType <= 3)
				{
					color = Color.rgb(255, 153, 0);
				} else if (colorType == 4)
				{
					color = Color.rgb(0, 177, 62);
				} else
				{
					color = Color.rgb(122, 122, 122);
				}
				des_tvs[i].setTextColor(color);
			} else
			{
				title_imgs[i].setVisibility(View.GONE);
				des_layouts[i].setVisibility(View.GONE);
			}
		}
	}

	private boolean isShow = false;

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.safe_layout:
			int startHeight;
			int endHeight;
			if (!isShow)// չ������
			{
				isShow = true;
				// contentGroup.setVisibility(View.VISIBLE);
				startHeight = 0;
				endHeight = getContentHeight();
			} else
			{// �رն���
				isShow = false;
				// contentGroup.setVisibility(View.GONE);
				startHeight = getContentHeight();
				endHeight = 0;
			}
			final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentGroup
					.getLayoutParams();
			ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight,
					endHeight);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animator)
				{
					int value = (Integer) animator.getAnimatedValue();// �õ���ǰʱ���ֵ
					layoutParams.height = value;
					contentGroup.setLayoutParams(layoutParams);

				}
			});

			// ����״̬�ı�
			valueAnimator.addListener(new DefaultAnimatorListener() {

				@Override
				public void onAnimationEnd(Animator animator)
				{
					// �л���ͷͼƬ
					iv_arrow.setImageResource(isShow ? R.drawable.arrow_up
							: R.drawable.arrow_down);
				}
			});
			valueAnimator.setDuration(500);
			valueAnimator.start();
			break;
		}
	}

	public int getContentHeight()
	{
		// ��Ȳ��ᷢ���ı䣬���Կ�ȵ�ֱֵ��ȡ����
		int width = contentGroup.getMeasuredWidth();
		// �ø߶Ȱ������ݣ�������Ҫ��Ҫ����
		contentGroup.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// ģʽ ��С
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);
		// ���߶���1000
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		// ��������: ����Ǿ�ȷֵwidth,�߶����1000����ʵ��Ϊ׼
		contentGroup.measure(widthMeasureSpec, heightMeasureSpec);
		return contentGroup.getMeasuredHeight();
	}

}
