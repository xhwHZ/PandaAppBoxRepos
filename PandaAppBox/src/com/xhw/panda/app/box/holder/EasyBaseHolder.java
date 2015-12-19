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
	 * �ڸ÷����г�ʼ���ӿؼ����÷�����initItemRootView����� ģ��:this.iv_icon = (ImageView)
	 * itemRootView.findViewById(R.id.iv_icon);
	 */
	protected void initChildView()
	{
	}

	/**
	 * ��ʼ��ListView��Item�ĸ�����
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
	 * ˢ����Ŀ���� �ڴ˷����и��ؼ��������� setData�����ᴥ���˷���
	 */
	protected abstract void refreshItem();

//	public void recycle()
//	{
//
//	}
}
