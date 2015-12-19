package com.xhw.panda.app.box.utils;

import com.xhw.panda.app.box.activity.BaseActivity;
import com.xhw.panda.app.box.application.BaseApplication;

import android.content.Intent;

public class ActivityUtils
{
	public static void startActivity(Intent intent)
	{
		//如果当前没有Activity，设置任务栈
		if(BaseActivity.currentActivity==null)
		{
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BaseApplication.getAppliction().startActivity(intent);
		}else{
			BaseActivity.currentActivity.startActivity(intent);
		}
	}
}
