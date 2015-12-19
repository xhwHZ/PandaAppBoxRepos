package com.xhw.panda.app.box.fragment;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.adapter.MoreAdapter;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.SubjectJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.holder.EasyBaseHolder;
import com.xhw.panda.app.box.net.SubjectNetOperation;
import com.xhw.panda.app.box.view.BaseListView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

/**
 * ×¨ÌâFragment
 * 
 * @author admin
 *
 */
public class SubjectFragment extends BaseFragment
{

	private List<SubjectJsonInfo> subjectInfoList;
	private BaseListView listView;

	@Override
	public RequestState requestServer()
	{
		SubjectNetOperation netOperation = new SubjectNetOperation();
		subjectInfoList = netOperation.requestData(0);
		return checkData(subjectInfoList);
	}

	@Override
	protected View createSuccessView()
	{
		listView = new BaseListView(
				BaseApplication.getAppliction());
		SubjectSuccessAdapter adapter = new SubjectSuccessAdapter();
		listView.setAdapter(adapter);
		return listView;
	}

	private class SubjectSuccessAdapter extends
			MoreAdapter<SubjectJsonInfo>
	{

		public SubjectSuccessAdapter()
		{
			super(subjectInfoList,listView);
		}

		@Override
		protected EasyBaseHolder<SubjectJsonInfo> getHolder()
		{
			return new SubjectHolder();
		}

		@Override
		protected List<SubjectJsonInfo> getMoreDataFromServer()
		{
			SubjectNetOperation netOperation=new SubjectNetOperation();
			return netOperation.requestData(subjectInfoList.size());
		}

		@Override
		protected void onListViewItemClick(AdapterView<?> parent, View view,
				int position)
		{
			
		}
		
	}

	class SubjectHolder extends EasyBaseHolder<SubjectJsonInfo>
	{
		ImageView iv_icon;
		TextView tv_des;

		@Override
		protected View initItemRootView()
		{
			return View.inflate(BaseApplication.getAppliction(),
					R.layout.item_subject, null);
		}

		@Override
		protected void initChildView()
		{
			this.iv_icon = (ImageView) this.findViewById(R.id.iv_icon);
			this.tv_des = (TextView) this.findViewById(R.id.tv_des);
		}

		@Override
		protected void refreshItem()
		{
			bitmapUtils.display(this.iv_icon, GlobalContants.IMAGE_SERVLET
					+ data.url);
			this.tv_des.setText(data.des);
		}
	}
}
