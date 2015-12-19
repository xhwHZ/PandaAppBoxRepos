package com.xhw.panda.app.box.holder;

import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.adapter.MoreAdapter;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.utils.ThreadUtils;

import android.view.View;
import android.widget.RelativeLayout;

public class MoreHolder<T> extends EasyBaseHolder<Integer>
{
	
	private  MoreAdapter<T> adapter;

	public MoreHolder(MoreAdapter<T> adapter)
	{
		super();
		this.adapter=adapter;
	}

	private RelativeLayout rl_more_loading,rl_more_error;

	public static final int STATE_ERROR = 0;

	public static final int STATE_NO_MORE = 1;
	
	public static final int STATE_HAS_MORE = 2;
	
	
	@Override
	public View getItemRootView()
	{
		adapter.loadMore();
		return super.getItemRootView();
	}

	@Override
	protected void initChildView()
	{
		rl_more_loading=(RelativeLayout) this.findViewById(R.id.rl_more_loading);
		rl_more_error=(RelativeLayout) this.findViewById(R.id.rl_more_error);
	}

	@Override
	protected View initItemRootView()
	{
		return View.inflate(BaseApplication.getAppliction(),
				R.layout.item_load_more, null);
	}

	@Override
	protected void refreshItem()
	{
		switch (data)
		{
		case STATE_ERROR:
			rl_more_error.setVisibility(View.VISIBLE);
			rl_more_loading.setVisibility(View.GONE);
			break;
		case STATE_NO_MORE:
			//两个子View都是GONE,这时整个加载更多的View都会隐藏掉
			rl_more_error.setVisibility(View.GONE);
			rl_more_loading.setVisibility(View.GONE);
			break;
		case STATE_HAS_MORE:
			rl_more_error.setVisibility(View.GONE);
			rl_more_loading.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
