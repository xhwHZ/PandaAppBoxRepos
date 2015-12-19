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
		// rootView����,rootView������ΪViewPage��һ��ҳ�棬��ViewPage���˹�Զ�����rootView�󶨵�ViewPage��ҳ��ᱻ����
		// ���Ǹ�rootView�Ѿ����˹�ȥ�����ٵ�ҳ�棬Ҫ����µ�ҳ�棬�������ȥ��ҳ��Ͽ���ϰ(ע��:��Ȼ��ʾ�����ݶ���һ�������ǹ�ȥ��ҳ���Ѿ������٣��µ�ҳ���Ǹմ��������ģ����Բ���ͬһ������)
		if (rootView == null)
		{
			rootView = new BasePageView(getActivity()){

				//�������ɲ�ʵ�֣���������ʵ��
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
			// �ж����ȥҳ�����ϵ
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
	
	// �ж��븸�ؼ��Ĺ���
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
	 * У���������ݣ�ȷ������ֵ
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
	 * ÿ���������磬������������
	 * �����ⲿ���õ��������������
	 * 
	 * @return ������
	 */
	public abstract RequestState requestServer();


	/**
	 * �������ֻ����������󣬲��ҳɹ��������ݣ����п��ܱ�����
	 * ����������ɹ����������ݵķ���
	 * 
	 * @return ����ɹ���ʾ����ͼ
	 */
	protected abstract View createSuccessView();


}
