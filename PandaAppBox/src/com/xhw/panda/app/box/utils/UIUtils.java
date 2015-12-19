package com.xhw.panda.app.box.utils;

import com.xhw.panda.app.box.application.BaseApplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class UIUtils
{
	public static Resources getResources()
	{
		return BaseApplication.getAppliction().getResources();
	}
	
	//返回string.xml的数组
	public static String[] getStringArray(int id)
	{
		return getResources().getStringArray(id);
	}
	
	/** 获取颜色id */
	public static int getColor(int colorId) {
		return getResources().getColor(colorId);
	}
	
	/**
	 * dp转成px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(int dp)
	{
		float density=getResources().getDisplayMetrics().density;
		int px=(int) (dp*density+0.5f);//四舍五入
		return px;
	}
	
	/**
	 * px转dp
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dp(int px)
	{
		float density=getResources().getDisplayMetrics().density;
		int dp=(int) (px/density+0.5f);
		return dp;
		
	}
	
	public static Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}
}
