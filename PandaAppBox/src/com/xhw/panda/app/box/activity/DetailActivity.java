package com.xhw.panda.app.box.activity;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.holder.DetailBottomHolder;
import com.xhw.panda.app.box.holder.DetailDesHolder;
import com.xhw.panda.app.box.holder.DetailInfoHolder;
import com.xhw.panda.app.box.holder.DetailSafeHolder;
import com.xhw.panda.app.box.holder.DetailScreenHolder;
import com.xhw.panda.app.box.net.DetailNetOperation;
import com.xhw.panda.app.box.view.BasePageView;
import com.xhw.panda.app.box.view.BasePageView.RequestState;

public class DetailActivity extends BaseActivity
{

	private String packageName;
	private BasePageView detailPageView;

	@Override
	protected void initView()
	{
		this.packageName = getIntent().getStringExtra("packageName");
		detailPageView = new BasePageView(this) {

			@Override
			public RequestState requestServer()
			{
				return DetailActivity.this.requestServer();
			}

			@Override
			public View createSuccessView()
			{
				return DetailActivity.this.createSuccessView();
			}
		};

		setContentView(detailPageView);

		// 获取ActionBar
		ActionBar actionBar = getSupportActionBar();
		// 设置显示Action的返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//请求服务器
		detailPageView.getDataFromServer();
	}

	
	private FrameLayout bottom_layout;
	private FrameLayout detail_info;
	private FrameLayout detail_safe;
	private FrameLayout detail_des;
	private HorizontalScrollView detail_screen;
	private DetailJsonInfo detailInfo;
	private DetailBottomHolder bottomHolder;
	
	protected View createSuccessView()
	{
		View view=View.inflate(this, R.layout.view_success_detail, null);
		
		//底部模块
		bottom_layout=(FrameLayout) view.findViewById(R.id.bottom_layout);
		bottomHolder = new DetailBottomHolder();
		bottomHolder.setData(detailInfo);
		bottom_layout.addView(bottomHolder.getItemRootView());
		bottomHolder.startObserver();
		
		//应用信息模块
		detail_info=(FrameLayout) view.findViewById(R.id.detail_info);
		DetailInfoHolder infoHolder=new DetailInfoHolder();
		infoHolder.setData(detailInfo);
		detail_info.addView(infoHolder.getItemRootView());
		
		
		//安全标记模块
		detail_safe=(FrameLayout) view.findViewById(R.id.detail_safe);
		DetailSafeHolder safeHolder=new DetailSafeHolder();
		safeHolder.setData(detailInfo);
		detail_safe.addView(safeHolder.getItemRootView());
		
		//描述模块
		detail_des=(FrameLayout) view.findViewById(R.id.detail_des);
		DetailDesHolder desHolder=new DetailDesHolder();
		desHolder.setData(detailInfo);
		detail_des.addView(desHolder.getItemRootView());
		
		//横向滚动图片模块
		detail_screen=(HorizontalScrollView) view.findViewById(R.id.detail_screen);
		DetailScreenHolder screenHolder=new DetailScreenHolder();
		screenHolder.setData(detailInfo);
		detail_screen.addView(screenHolder.getItemRootView());
		
		return view;
	}

	protected RequestState requestServer()
	{
		DetailNetOperation netOperation = new DetailNetOperation();
		detailInfo = netOperation.requestData(packageName);
		if (detailInfo == null)
		{
			return RequestState.ERROR;
		}
		return RequestState.SUCCESS;
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (bottomHolder != null) {
			bottomHolder.stopObserver();
		}
	}
	
	// 可以用配置文件来替代这段代码，看Manifest.xml
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item)
	// {
	// if(item.getItemId()==android.R.id.home)//返回按钮的id，固定写法
	// {
	// finish();
	// }
	// return super.onOptionsItemSelected(item);
	// }
}
