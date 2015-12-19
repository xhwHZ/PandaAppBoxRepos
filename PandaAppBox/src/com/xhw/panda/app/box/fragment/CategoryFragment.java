package com.xhw.panda.app.box.fragment;

import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.adapter.EasyBaseAdapter;
import com.xhw.panda.app.box.adapter.MoreAdapter;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.CategoryJsonInfo;
import com.xhw.panda.app.box.domain.CategoryJsonInfo.CategoryInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.holder.EasyBaseHolder;
import com.xhw.panda.app.box.net.CategoryNetOperation;
import com.xhw.panda.app.box.utils.BitmapHelper;
import com.xhw.panda.app.box.view.BaseListView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

/**
 * 分类Fragment
 * 
 * @author admin
 *
 */
public class CategoryFragment extends BaseFragment
{

	private List<CategoryJsonInfo> categoryJsonList;

	@Override
	public RequestState requestServer()
	{
		CategoryNetOperation netOperation = new CategoryNetOperation();
		categoryJsonList = netOperation.requestData();
		return checkData(categoryJsonList);
	}

	@Override
	protected View createSuccessView()
	{
		BaseListView listView = new BaseListView(
				BaseApplication.getAppliction());
		CategorySuccessAdapter adapter = new CategorySuccessAdapter(
				categoryJsonList);
		listView.setAdapter(adapter);
		return listView;
	}

	private class CategorySuccessAdapter extends
			EasyBaseAdapter<CategoryJsonInfo>
	{

		public CategorySuccessAdapter(List<CategoryJsonInfo> dataList)
		{
			super(dataList);
		}

		@Override
		protected EasyBaseHolder<CategoryJsonInfo> getHolder()
		{
			return new CategoryHolder();
		}

	}

	private class CategoryHolder extends EasyBaseHolder<CategoryJsonInfo>
	{

		@ViewInject(R.id.ll_root)
		private LinearLayout ll_root;

		@ViewInject(R.id.tv_title)
		private TextView tv_title;

		@Override
		protected View initItemRootView()
		{
			View view = View.inflate(BaseApplication.getAppliction(),
					R.layout.item_category, null);
			ViewUtils.inject(this, view);
			return view;
		}

		@Override
		protected void refreshItem()
		{
			BitmapUtils bitmapUtils=BitmapHelper.getBitmapUtils();
			// 标题
			tv_title.setText(data.title);
			//因为getView方法会触发此方法，将会导致多次添加，所以每次进来，除了tv_title,其余全部移除掉
			ll_root.removeViews(1, ll_root.getChildCount()-1);
			List<CategoryInfo> categoryInfoList = data.infos;
			// 没有一个CategoryInfo对象，就创建一个item_category_content
			for (CategoryInfo categoryInfo : categoryInfoList)
			{
				View childView = View.inflate(BaseApplication.getAppliction(),
						R.layout.item_category_content, null);

				RelativeLayout rl_1 = (RelativeLayout) childView
						.findViewById(R.id.rl_1);
				RelativeLayout rl_2 = (RelativeLayout) childView
						.findViewById(R.id.rl_2);
				RelativeLayout rl_3 = (RelativeLayout) childView
						.findViewById(R.id.rl_3);
				ImageView iv_1 = (ImageView) childView.findViewById(R.id.iv_1);
				ImageView iv_2 = (ImageView) childView.findViewById(R.id.iv_2);
				ImageView iv_3 = (ImageView) childView.findViewById(R.id.iv_3);
				TextView tv_1 = (TextView) childView.findViewById(R.id.tv_1);
				TextView tv_2 = (TextView) childView.findViewById(R.id.tv_2);
				TextView tv_3 = (TextView) childView.findViewById(R.id.tv_3);
				
				if(!TextUtils.isEmpty(categoryInfo.name1)&&!TextUtils.isEmpty(categoryInfo.url1))
				{
					rl_1.setVisibility(View.VISIBLE);
					bitmapUtils.display(iv_1, GlobalContants.IMAGE_SERVLET+categoryInfo.url1);
					tv_1.setText(categoryInfo.name1);
				}else{
					rl_1.setVisibility(View.INVISIBLE);
				}
				
				
				if(!TextUtils.isEmpty(categoryInfo.name2)&&!TextUtils.isEmpty(categoryInfo.url2))
				{
					rl_2.setVisibility(View.VISIBLE);
					bitmapUtils.display(iv_2, GlobalContants.IMAGE_SERVLET+categoryInfo.url2);
					tv_2.setText(categoryInfo.name2);
				}else{
					rl_2.setVisibility(View.INVISIBLE);
				}
				
				if(!TextUtils.isEmpty(categoryInfo.name3)&&!TextUtils.isEmpty(categoryInfo.url3))
				{
					rl_3.setVisibility(View.VISIBLE);
					bitmapUtils.display(iv_3, GlobalContants.IMAGE_SERVLET+categoryInfo.url3);
					tv_3.setText(categoryInfo.name3);
				}else{
					rl_3.setVisibility(View.INVISIBLE);
				}
				
				ll_root.addView(childView);
			}
		}

	}

}
