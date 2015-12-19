package com.xhw.panda.app.box.fragment;

import java.util.List;

import android.view.View;

import com.xhw.panda.app.box.adapter.AppListAdapter;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.net.AppNetOperation;
import com.xhw.panda.app.box.view.BaseListView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

/**
 * ”¶”√Fragment
 * @author admin
 *
 */
public class AppFragment extends BaseFragment
{

	private List<AppInfo> appInfoList;
	private AppListAdapter adapter;

	@Override
	public RequestState requestServer()
	{
		AppNetOperation netOperation=new AppNetOperation();
		appInfoList = netOperation.requestData(0);
		return checkData(appInfoList);
	}

	@Override
	protected View createSuccessView()
	{
		BaseListView listView=new BaseListView(BaseApplication.getAppliction());
		adapter = new AppListAdapter(appInfoList,listView){

			@Override
			protected List<AppInfo> getMoreDataFromServer()
			{
				AppNetOperation netOperation=new AppNetOperation();
				return netOperation.requestData(appInfoList.size());
			}
			
		};
		listView.setAdapter(adapter);
		return listView;
	}
	
}
