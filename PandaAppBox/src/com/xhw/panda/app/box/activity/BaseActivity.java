package com.xhw.panda.app.box.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public abstract class BaseActivity extends ActionBarActivity
{
	public static List<BaseActivity> activityList = new LinkedList<BaseActivity>();

	public static Activity currentActivity;
	
	@Override
	protected void onPause()
	{
		currentActivity=null;
		super.onPause();
	}
	
	@Override
	protected void onResume()
	{
		currentActivity=this;
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		synchronized (activityList)
		{
			activityList.add(this);
		}
		initView();
		initData();
		initListener();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		synchronized (activityList)
		{
			activityList.remove(this);
		}
	}

	protected abstract void initView();

	protected  void initData(){}

	protected void initListener()
	{

	}

	// �˳�Ӧ�ó���
	public static void closeAllActivity()
	{
		synchronized (activityList)
		{
			// ��Ϊfinish�ᴥ��destory�����������ڱ���ʱ�޸ļ��ϣ����Դ���һ������Ʒ
			List<BaseActivity> copyActivities = new LinkedList<BaseActivity>(
					activityList);
			for (BaseActivity activity : copyActivities)
			{
				activity.finish();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	
}
