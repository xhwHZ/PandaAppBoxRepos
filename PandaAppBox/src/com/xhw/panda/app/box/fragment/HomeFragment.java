package com.xhw.panda.app.box.fragment;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.activity.DetailActivity;
import com.xhw.panda.app.box.adapter.AppListAdapter;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.domain.HomeJsonInfo;
import com.xhw.panda.app.box.holder.HomePicHolder;
import com.xhw.panda.app.box.net.HomeNetOperation;
import com.xhw.panda.app.box.view.BaseListView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

/**
 * ��ҳFragment
 * 
 * @author admin
 *
 */
public class HomeFragment extends BaseFragment
{

	private List<AppInfo> homeAppList;
	
	private List<String> imgList;

	// ��������������ɹ����������ݵ�ҳ��
	@Override
	protected View createSuccessView()
	{
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_load_img);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_load_img);
		View homeSuccessView = View.inflate(BaseApplication.getAppliction(),
				R.layout.view_success_home, null);
		BaseListView listView = (BaseListView) homeSuccessView
				.findViewById(R.id.listView);
		//�����ֲ�ͼ
		HomePicHolder homePicHolder=new HomePicHolder();
		homePicHolder.setData(imgList);
		listView.addHeaderView(homePicHolder.getItemRootView());
		
		AppListAdapter adapter = new AppListAdapter(homeAppList,listView){

			@Override
			protected List<AppInfo> getMoreDataFromServer()
			{
				HomeNetOperation netOperation=new HomeNetOperation();
				HomeJsonInfo requestData = netOperation.requestData(homeAppList.size());//������������
				if(requestData!=null)
				{
					return requestData.list;
				}
				return null;
			}

		};
		listView.setAdapter(adapter);
		// ListView�����Ż������ٻ���ʱ������ͼƬ
		// �ڶ������� ListView��������ʱ�Ƿ�ֹͣ����ͼƬ
		// ���������� ListView���ٻ���ʱ�Ƿ�ֹͣ����ͼƬ
		listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
		return homeSuccessView;
	}

	@Override
	public RequestState requestServer()
	{
		HomeNetOperation netOperation = new HomeNetOperation();
		HomeJsonInfo requestData = netOperation.requestData(0);
		if (requestData == null)
		{
			return RequestState.ERROR;
		}
		imgList=requestData.picture;
		homeAppList = requestData.list;
		return checkData(homeAppList);
	}


}
