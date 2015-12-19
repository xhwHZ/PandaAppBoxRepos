package com.xhw.panda.app.box.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.lidroid.xutils.BitmapUtils;
import com.xhw.panda.app.box.domain.HomeJsonInfo;
import com.xhw.panda.app.box.utils.BitmapHelper;
import com.xhw.panda.app.box.view.BasePageView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

public abstract class BaseFragment extends Fragment
{

	private BasePageView rootView;
	
	protected BitmapUtils bitmapUtils;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		bitmapUtils = BitmapHelper.getBitmapUtils();
		// rootView重用,rootView返回作为ViewPage的一个页面，当ViewPage翻了过远，与该rootView绑定的ViewPage的页面会被销毁
		// 但是该rootView已经绑定了过去被销毁的页面，要想绑定新的页面，必须跟过去的页面断开练习(注意:虽然显示的内容都是一样，但是过去的页面已经被销毁，新的页面是刚创建出来的，所以不是同一个对象)
		if (rootView == null)
		{
			rootView = new BasePageView(getActivity()){

				//这里依旧不实现，交给子类实现
				@Override
				public View createSuccessView()
				{
					return BaseFragment.this.createSuccessView();
				}

				@Override
				public RequestState requestServer()
				{
					return BaseFragment.this.requestServer();
				}
				
			};
		} else
		{
			// 切断与过去页面的联系
			removeParent(rootView);
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getDataFromServer();
	}
	
	public void getDataFromServer()
	{
		rootView.getDataFromServer();
	}
	
	// 切断与父控件的关联
	private void removeParent(FrameLayout rootView)
	{
		ViewParent viewParent = rootView.getParent();
		if (viewParent instanceof ViewGroup)
		{
			ViewGroup viewGroup = (ViewGroup) viewParent;
			viewGroup.removeView(rootView);
		}
	}

	/**
	 * 校验网络数据，确定返回值
	 * 
	 * @param list
	 * @return
	 */
	protected RequestState checkData(List dataList)
	{
		if (dataList == null)
		{
			return RequestState.ERROR;
		} else if (dataList.size() == 0)
		{
			return RequestState.EMPTY;
		} else
		{
			return RequestState.SUCCESS;
		}
	}
	

	/**
	 * 每次请求网络，这个方法会调用
	 * 留给外部调用的请求服务器方法
	 * 
	 * @return 请求结果
	 */
	public abstract RequestState requestServer();


	/**
	 * 这个方法只有请求网络后，并且成功返回数据，才有可能被调用
	 * 请求服务器成功并返回数据的方法
	 * 
	 * @return 请求成功显示的视图
	 */
	protected abstract View createSuccessView();


}
