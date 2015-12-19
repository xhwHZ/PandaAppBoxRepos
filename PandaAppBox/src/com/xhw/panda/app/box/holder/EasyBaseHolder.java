package com.xhw.panda.app.box.holder;

import android.view.View;

public abstract class EasyBaseHolder<T>
{

	protected View itemRootView;

	public View getItemRootView()
	{
		return this.itemRootView;
	}

	public View findViewById(int resId)
	{
		return itemRootView.findViewById(resId);
	}

	public EasyBaseHolder()
	{
		itemRootView = initItemRootView();
		initChildView();
		itemRootView.setTag(this);
	}

	/**
	 * 在该方法中初始化子控件，该方法在initItemRootView后调用 模版:this.iv_icon = (ImageView)
	 * itemRootView.findViewById(R.id.iv_icon);
	 */
	protected void initChildView()
	{
	}

	/**
	 * 初始化ListView的Item的根布局
	 * 
	 * @return
	 */
	protected abstract View initItemRootView();

	protected T data;

	public T getData()
	{
		return this.data;
	}
	
	public void setData(T data)
	{
		this.data = data;
		refreshItem();
	}

	/**
	 * 刷新条目数据 在此方法中给控件设置数据 setData方法会触发此方法
	 */
	protected abstract void refreshItem();

//	public void recycle()
//	{
//
//	}
}
