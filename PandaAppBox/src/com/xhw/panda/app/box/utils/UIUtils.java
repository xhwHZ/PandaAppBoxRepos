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
	
	//����string.xml������
	public static String[] getStringArray(int id)
	{
		return getResources().getStringArray(id);
	}
	
	/** ��ȡ��ɫid */
	public static int getColor(int colorId) {
		return getResources().getColor(colorId);
	}
	
	/**
	 * dpת��px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(int dp)
	{
		float density=getResources().getDisplayMetrics().density;
		int px=(int) (dp*density+0.5f);//��������
		return px;
	}
	
	/**
	 * pxתdp
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
