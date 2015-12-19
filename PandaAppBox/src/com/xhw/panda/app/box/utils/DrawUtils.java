package com.xhw.panda.app.box.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawUtils
{
	/**
	 * 代码创建Shape
	 * @return
	 */
	public static GradientDrawable createShape()
	{
		GradientDrawable drawable=new GradientDrawable();
		drawable.setCornerRadius(UIUtils.dp2px(5));//设置四个角的圆角半径
		return drawable;
	}
	
	/**
	 * 代码创建状态选择器
	 * @return
	 */
	public static StateListDrawable createSeletor(Drawable normalDrawable,Drawable pressedDrawable)
	{
		StateListDrawable drawable=new StateListDrawable();
		//按下的情况
		drawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
		//其余情况
		drawable.addState(new int[]{}, normalDrawable);
		return drawable;
		
	}
}
