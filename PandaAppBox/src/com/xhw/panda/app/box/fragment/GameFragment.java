package com.xhw.panda.app.box.fragment;

import java.util.List;

import android.view.View;

import com.xhw.panda.app.box.adapter.AppListAdapter;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.net.GameNetOperation;
import com.xhw.panda.app.box.view.BaseListView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

/**
 * ”Œœ∑Fragment
 * @author admin
 *
 */
public class GameFragment extends BaseFragment
{

	private List<AppInfo> gameAppList;
	private AppListAdapter adapter;

	@Override
	public RequestState requestServer()
	{
		GameNetOperation netOperation=new GameNetOperation();
		gameAppList = netOperation.requestData(0);
		return checkData(gameAppList);
	}

	@Override
	protected View createSuccessView()
	{
		BaseListView listView=new BaseListView(BaseApplication.getAppliction());
		adapter = new AppListAdapter(gameAppList,listView){
			@Override
			protected List<AppInfo> getMoreDataFromServer()
			{
				GameNetOperation netOperation=new GameNetOperation();
				return netOperation.requestData(gameAppList.size());
			}
			
		};
		listView.setAdapter(adapter);
		return listView;
	}
	
}
