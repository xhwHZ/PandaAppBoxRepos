package com.xhw.panda.app.box.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xhw.panda.app.box.holder.EasyBaseHolder;
import com.xhw.panda.app.box.holder.MoreHolder;
import com.xhw.panda.app.box.manager.ThreadManager;
import com.xhw.panda.app.box.utils.ThreadUtils;

public abstract class MoreAdapter<T> extends EasyBaseAdapter<T> implements OnItemClickListener
{
	// 普通条目
	private final static int TYPE_DEFAULT = 0;

	// 加载更多条目
	private final static int TYPE_MORE = 1;
	
	private ListView listView;

	public MoreAdapter(List<T> dataList,ListView listView)
	{
		super(dataList);
		this.listView=listView;
		this.listView.setOnItemClickListener(this);
	}

	//ListView条目点击回调
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		//Toast.makeText(BaseApplication.getAppliction(), "position:"+position, 0).show();
		onListViewItemClick(parent,view,position);
	}
	
	/**
	 * 在此方法中处理ListView条目点击事件
	 * @param parent 父容器 ListView
	 * @param view 被点击的item
	 * @param position 位置(除去了父容器)
	 */
	protected void onListViewItemClick(AdapterView<?> parent, View view,
			int position)
	{
		// do nothing
	}

	public List<T> getDataList()
	{
		return dataList;
	}

	public void setDataList(List<T> dataList)
	{
		this.dataList = dataList;
	}

	
	@Override
	public int getCount()
	{
		return dataList.size() + 1;// 最后一个条目就是加载更多的条目
	}

	@Override
	public T getItem(int position)
	{
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/** 根据位置，判断当前条目是什么类型 */
	@Override
	public int getItemViewType(int position)
	{
		if (position == dataList.size())
		{
			return TYPE_MORE;
		}
		return TYPE_DEFAULT;
	}

	/**
	 * 当前ListView有多少种不同的条目类型,要使用多类型的ListView，此方法必须复写，系统会为以此为依据，
	 * 准备多少种类型的convertView
	 */
	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		switch (getItemViewType(position))
		{
		case TYPE_MORE:// 这个位置，拿加载更多的convertView(系统缓存)，具体系统拿哪个，我已经重写了getItemViewType和getViewTypeCount方法来指示系统怎么去拿了
			MoreHolder<T> moreHolder = null;
			if (convertView == null)
			{
				moreHolder = getMoreHolder();
			} else
			{
				moreHolder = (MoreHolder<T>) convertView.getTag();
			}
			return moreHolder.getItemRootView();
		case TYPE_DEFAULT:// 这个位置，拿普通条目的convertView
			EasyBaseHolder<T> holder = null;
			if (convertView == null)
			{
				holder = getHolder();
			} else
			{
				holder = (EasyBaseHolder<T>) convertView.getTag();
			}
			T t = getItem(position);
			// 设置数据，并刷新条目
			holder.setData(t);
			return holder.getItemRootView();
		default:
			return null;
		}

	}

	// 当加载更多的条目被显示时(getView方法被调用),MoreHolder的getItemRootView被触发,getItemRootView触发adapter的加载更多的方法
	// 简单的说，就是当加载更多的条目出现时，此方法被调用
	public void loadMore()
	{
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {

			@Override
			public void run()
			{
				final List<T> newDataList = getMoreDataFromServer();
				ThreadUtils.runOnUiThread(new Runnable() {
					
					@Override
					public void run()
					{
						if (newDataList == null)
						{
							// 请求服务器失败
							moreHolder.setData(MoreHolder.STATE_ERROR);
						} else if (newDataList.size() == 0)
						{
							// 服务器没有更多数据了
							moreHolder.setData(MoreHolder.STATE_NO_MORE);
						} else
						{
							// 请求成功
							moreHolder.setData(MoreHolder.STATE_HAS_MORE);
							dataList.addAll(newDataList);
							notifyDataSetChanged();
						}
					}
				});
			}
		});
	}

	private MoreHolder<T> moreHolder;

	private MoreHolder<T> getMoreHolder()
	{
		if (moreHolder == null)
		{
			moreHolder = new MoreHolder<T>(this);
		}
		return moreHolder;
	}

	/**
	 * 从服务器中加载更多数据
	 * 
	 * @return
	 */
	protected abstract List<T> getMoreDataFromServer();
}
