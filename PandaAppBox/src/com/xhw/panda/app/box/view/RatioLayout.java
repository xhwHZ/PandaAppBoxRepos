package com.xhw.panda.app.box.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout
{

	private float ratio=2.43f;// 宽和高的比例
	
	

	public void setRatio(float ratio)
	{
		this.ratio = ratio;
	}

	public RatioLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		float value=attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/com.xhw.panda.app.box", "ratio", 2.43f);
		setRatio(value);
	}

	public RatioLayout(Context context, AttributeSet attrs)
	{
		this(context,attrs,0);//本类处理
	}

	public RatioLayout(Context context)
	{
		super(context);
	}

	//测量当前布局
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		//MeasureSpec 测量规则包含两部分 模式和值
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);//模式
		int widthSize=MeasureSpec.getSize(widthMeasureSpec);//宽度大小
		int width=widthSize-getPaddingLeft()-getPaddingRight();//去掉两边Padding大小
		
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);//模式
		int heightSize=MeasureSpec.getSize(heightMeasureSpec);//高度大小
		int height=heightSize-getPaddingTop()-getPaddingBottom();//去掉上下Padding大小
		
		//一般来说，布局文件中的match_parent会对应Exactly,而wrap_content会对应At_most
		
		//如果宽度固定(Exactly)
		if(widthMode==MeasureSpec.EXACTLY&&heightMode!=MeasureSpec.EXACTLY)
		{
			//修正一下高度的值，让高度按照宽度值计算
			height=(int) (width/ratio+0.5f);
		}else if(widthMode!=MeasureSpec.EXACTLY&&heightMode==MeasureSpec.EXACTLY)
		{
			//如果高度固定
			width=(int) (height*ratio+0.5f);
		}
		widthMeasureSpec=MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, width+getPaddingLeft()+getPaddingRight());
		heightMeasureSpec=MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, height+getPaddingTop()+getPaddingBottom());
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
