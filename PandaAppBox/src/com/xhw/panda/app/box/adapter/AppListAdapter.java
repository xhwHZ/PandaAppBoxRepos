package com.xhw.panda.app.box.adapter;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xhw.panda.app.box.activity.DetailActivity;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.domain.DownloadInfo;
import com.xhw.panda.app.box.holder.AppListHoder;
import com.xhw.panda.app.box.holder.EasyBaseHolder;
import com.xhw.panda.app.box.manager.DownloadManager;
import com.xhw.panda.app.box.manager.DownloadManager.DownloadObserver;
import com.xhw.panda.app.box.utils.ActivityUtils;

public abstract class AppListAdapter extends MoreAdapter<AppInfo>
//implements DownloadObserver
{

	public AppListAdapter(List<AppInfo> dataList, ListView listView)
	{
		super(dataList, listView);
	}

	@Override
	protected EasyBaseHolder<AppInfo> getHolder()
	{
		AppListHoder appListHoder=new AppListHoder();
		DownloadManager.getInstance().registerObserver(appListHoder);
		return appListHoder;
	}

	@Override
	protected abstract List<AppInfo> getMoreDataFromServer();

	@Override
	protected void onListViewItemClick(AdapterView<?> parent, View view,
			int position)
	{
		Intent intent = new Intent(BaseApplication.getAppliction(),
				DetailActivity.class);
		intent.putExtra("packageName", dataList.get(position).packageName);
		ActivityUtils.startActivity(intent);
	}

//	public void startObserver() {
//		DownloadManager.getInstance().registerObserver(this);
//	}
//
//	public void stopObserver() {
//		DownloadManager.getInstance().unRegisterObserver(this);
//	}
//	
//	@Override
//	public void onDownloadStateChanged(DownloadInfo info)
//	{
//		refreshHolder(info);
//	}
//
//	@Override
//	public void onDownloadProgressed(DownloadInfo info)
//	{
//		refreshHolder(info);
//	}
	
//	private void refreshHolder(final DownloadInfo info) {
//		System.out.println("refresh");
//		List<EasyBaseHolder<AppInfo>> displayedHolders = getDisplayedHolders();
//		for (int i = 0; i < displayedHolders.size(); i++) {
//			EasyBaseHolder<AppInfo> baseHolder = displayedHolders.get(i);
//			if (baseHolder instanceof AppListHoder2) {
//				final AppListHoder2 holder = (AppListHoder2) baseHolder;
//				AppInfo appInfo = holder.getData();
//				if (appInfo.id == info.getId()) {
//					ThreadUtils.post(new Runnable() {
//						@Override
//						public void run() {
//							System.out.println("coming???");
//							holder.refreshState(info.getDownloadState(),
//									(int) (info.getCurrentSize() * 100 / info
//											.getAppSize()));
//						}
//					});
//				}
//			}
//		}
//	}
	
}
