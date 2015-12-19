package com.xhw.panda.app.box.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xhw.panda.app.box.holder.EasyBaseHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.BaseAdapter;

public abstract class EasyBaseAdapter<T> extends BaseAdapter
//implements RecyclerListener
{

	//private List<EasyBaseHolder<T>> mDisplayedHolders;

//	public List<EasyBaseHolder<T>> getDisplayedHolders()
//	{
//		synchronized (mDisplayedHolders)
//		{
//			return new ArrayList<EasyBaseHolder<T>>(mDisplayedHolders);
//		}
//	}
//
//	@Override
//	public void onMovedToScrapHeap(View view)
//	{
//		if (null != view)
//		{
//			Object tag = view.getTag();
//			if (tag instanceof EasyBaseHolder)
//			{
//				EasyBaseHolder<T> holder = (EasyBaseHolder<T>) tag;
//				synchronized (mDisplayedHolders)
//				{
//					mDisplayedHolders.remove(holder);
//				}
//				holder.recycle();
//			}
//		}
//	}

	protected List<T> dataList;

	public EasyBaseAdapter(List<T> dataList)
	{
		this.dataList = dataList;
		//mDisplayedHolders = new ArrayList<EasyBaseHolder<T>>();
	}

	@Override
	public int getCount()
	{
		return dataList.size();
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

//	protected boolean isMore()
//	{
//		return false;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		EasyBaseHolder<T> holder = null;
		if (convertView == null)
		{
			holder = getHolder();
		} else
		{
			holder = (EasyBaseHolder<T>) convertView.getTag();
		}
		T data = getItem(position);
		holder.setData(data);
//		if (!isMore())
//		{
//			mDisplayedHolders.add(holder);
//		}
		return holder.getItemRootView();
	}

	protected abstract EasyBaseHolder<T> getHolder();

}
