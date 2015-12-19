package com.xhw.panda.app.box.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawUtils
{
	/**
	 * ���봴��Shape
	 * @return
	 */
	public static GradientDrawable createShape()
	{
		GradientDrawable drawable=new GradientDrawable();
		drawable.setCornerRadius(UIUtils.dp2px(5));//�����ĸ��ǵ�Բ�ǰ뾶
		return drawable;
	}
	
	/**
	 * ���봴��״̬ѡ����
	 * @return
	 */
	public static StateListDrawable createSeletor(Drawable normalDrawable,Drawable pressedDrawable)
	{
		StateListDrawable drawable=new StateListDrawable();
		//���µ����
		drawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
		//�������
		drawable.addState(new int[]{}, normalDrawable);
		return drawable;
		
	}
}
