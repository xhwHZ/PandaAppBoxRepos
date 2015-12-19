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
	// ��ͨ��Ŀ
	private final static int TYPE_DEFAULT = 0;

	// ���ظ�����Ŀ
	private final static int TYPE_MORE = 1;
	
	private ListView listView;

	public MoreAdapter(List<T> dataList,ListView listView)
	{
		super(dataList);
		this.listView=listView;
		this.listView.setOnItemClickListener(this);
	}

	//ListView��Ŀ����ص�
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		//Toast.makeText(BaseApplication.getAppliction(), "position:"+position, 0).show();
		onListViewItemClick(parent,view,position);
	}
	
	/**
	 * �ڴ˷����д���ListView��Ŀ����¼�
	 * @param parent ������ ListView
	 * @param view �������item
	 * @param position λ��(��ȥ�˸�����)
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
		return dataList.size() + 1;// ���һ����Ŀ���Ǽ��ظ������Ŀ
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

	/** ����λ�ã��жϵ�ǰ��Ŀ��ʲô���� */
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
	 * ��ǰListView�ж����ֲ�ͬ����Ŀ����,Ҫʹ�ö����͵�ListView���˷������븴д��ϵͳ��Ϊ�Դ�Ϊ���ݣ�
	 * ׼�����������͵�convertView
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
		case TYPE_MORE:// ���λ�ã��ü��ظ����convertView(ϵͳ����)������ϵͳ���ĸ������Ѿ���д��getItemViewType��getViewTypeCount������ָʾϵͳ��ôȥ����
			MoreHolder<T> moreHolder = null;
			if (convertView == null)
			{
				moreHolder = getMoreHolder();
			} else
			{
				moreHolder = (MoreHolder<T>) convertView.getTag();
			}
			return moreHolder.getItemRootView();
		case TYPE_DEFAULT:// ���λ�ã�����ͨ��Ŀ��convertView
			EasyBaseHolder<T> holder = null;
			if (convertView == null)
			{
				holder = getHolder();
			} else
			{
				holder = (EasyBaseHolder<T>) convertView.getTag();
			}
			T t = getItem(position);
			// �������ݣ���ˢ����Ŀ
			holder.setData(t);
			return holder.getItemRootView();
		default:
			return null;
		}

	}

	// �����ظ������Ŀ����ʾʱ(getView����������),MoreHolder��getItemRootView������,getItemRootView����adapter�ļ��ظ���ķ���
	// �򵥵�˵�����ǵ����ظ������Ŀ����ʱ���˷���������
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
							// ���������ʧ��
							moreHolder.setData(MoreHolder.STATE_ERROR);
						} else if (newDataList.size() == 0)
						{
							// ������û�и���������
							moreHolder.setData(MoreHolder.STATE_NO_MORE);
						} else
						{
							// ����ɹ�
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
	 * �ӷ������м��ظ�������
	 * 
	 * @return
	 */
	protected abstract List<T> getMoreDataFromServer();
}
