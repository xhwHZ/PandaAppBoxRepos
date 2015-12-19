package com.xhw.panda.app.box.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout
{

	private float ratio=2.43f;// ��͸ߵı���
	
	

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
		this(context,attrs,0);//���ദ��
	}

	public RatioLayout(Context context)
	{
		super(context);
	}

	//������ǰ����
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		//MeasureSpec ����������������� ģʽ��ֵ
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);//ģʽ
		int widthSize=MeasureSpec.getSize(widthMeasureSpec);//��ȴ�С
		int width=widthSize-getPaddingLeft()-getPaddingRight();//ȥ������Padding��С
		
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);//ģʽ
		int heightSize=MeasureSpec.getSize(heightMeasureSpec);//�߶ȴ�С
		int height=heightSize-getPaddingTop()-getPaddingBottom();//ȥ������Padding��С
		
		//һ����˵�������ļ��е�match_parent���ӦExactly,��wrap_content���ӦAt_most
		
		//�����ȹ̶�(Exactly)
		if(widthMode==MeasureSpec.EXACTLY&&heightMode!=MeasureSpec.EXACTLY)
		{
			//����һ�¸߶ȵ�ֵ���ø߶Ȱ��տ��ֵ����
			height=(int) (width/ratio+0.5f);
		}else if(widthMode!=MeasureSpec.EXACTLY&&heightMode==MeasureSpec.EXACTLY)
		{
			//����߶ȹ̶�
			width=(int) (height*ratio+0.5f);
		}
		widthMeasureSpec=MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, width+getPaddingLeft()+getPaddingRight());
		heightMeasureSpec=MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, height+getPaddingTop()+getPaddingBottom());
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
