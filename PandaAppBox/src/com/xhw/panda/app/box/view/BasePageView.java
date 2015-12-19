package com.xhw.panda.app.box.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.manager.ThreadManager;
import com.xhw.panda.app.box.utils.ThreadUtils;

//����ҳ��Ĳ��ֻ���
public abstract class BasePageView extends FrameLayout
{
	/**
	 * ���������״̬
	 * 
	 * @author admin
	 *
	 */
	public enum RequestState {
		// ��ֵΪ�˺�״ֵ̬��Ӧ����
		ERROR(1), EMPTY(2), SUCCESS(3);
		private RequestState(int value)
		{
			this.value = value;
		}

		int value;

		public int getValue()
		{
			return this.value;
		}
	}

	// �ĸ�״̬
	// ������
	private static final int STATE_LOADING = 0;

	// ����ʧ��
	private static final int STATE_ERROR = 1;

	// ����������ɹ�����������û�з�������
	private static final int STATE_EMPTY = 2;

	// ����������ɹ���������������
	private static final int STATE_SUCCESS = 3;

	// ��ǰ״̬
	private int CURRENT_STATE = STATE_LOADING;

	public BasePageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public BasePageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public BasePageView(Context context)
	{
		super(context);
		init();
	}

	private Context mContext;

	private void init()
	{
		mContext = BaseApplication.getAppliction();
		// ������״̬��Ӧ�Ĳ����ļ������ؽ�FrameLayout��
		initRootView();
		// ���ݵ�ǰ״̬��ʾ��һ������
		showViewByCurrentState();
		// ����������ķ�������BaseFragment�е�onActivityCreated������ִ��,��Ϊ���View����createView�б�����������init�����������������ķ����ȵ���
	}

	private View loadingView;

	private View errorView;

	private View emptyView;

	private View successView;

	// ������״̬��Ӧ�Ĳ����ļ������ؽ�frameLayout��
	private void initRootView()
	{
		// 1�������е�ҳ��
		loadingView = createLoadingView();
		this.addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// 2�����������ʧ�ܵ�ҳ��
		errorView = createErrorView();
		this.addView(errorView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// 3������������ɹ�����û�����ݵĿ�ҳ��
		emptyView = createEmptyView();
		this.addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// 4������������ɹ����������ݵ�ҳ��
		// ���ܷ�����ɹ����������Ҫ������������ȷ��
		// successView = createSuccessView();
		// rootView.addView(successView, new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	// ���������
	public void getDataFromServer()
	{
		// ���������ǰ����״̬���óɼ�����
		CURRENT_STATE = STATE_LOADING;
		// ��ʾ�����е�ҳ��
		showViewByCurrentState();

		// �������߳���������
		//ʹ���̳߳�
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			
			@Override
			public void run()
			{
				//SystemClock.sleep(2000);
				// �������������
				final RequestState result = requestServer();
				// �������Fragment�Ѿ��������ˣ��������̻߳����ܣ��п��ܿ�ָ��
				ThreadUtils.runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
						// ˢ��״̬
						CURRENT_STATE = result.getValue();
						showViewByCurrentState();
					}
				});
			}
		});
		
	}

	// ��������������ɹ�����û�����ݵĿ�ҳ��
	private View createEmptyView()
	{
		return View.inflate(mContext, R.layout.view_empty, null);
	}

	// �������������ʧ�ܵ�ҳ��
	private View createErrorView()
	{
		View view = View.inflate(mContext, R.layout.view_error, null);
		// ���¼��ذ�ť
		Button btnRetry = (Button) view.findViewById(R.id.btn_retry);
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				getDataFromServer();
			}
		});
		return view;
	}

	// ���������е�ҳ��
	private View createLoadingView()
	{
		return View.inflate(mContext, R.layout.view_loading, null);
	}

	// ���ݵ�ǰ״̬��������ʾ�ĸ�View
	private void showViewByCurrentState()
	{
		if (loadingView != null)
		{
			loadingView
					.setVisibility(CURRENT_STATE == STATE_LOADING ? View.VISIBLE
							: View.INVISIBLE);
		}
		if (errorView != null)
		{
			errorView.setVisibility(CURRENT_STATE == STATE_ERROR ? View.VISIBLE
					: View.INVISIBLE);
		}
		if (emptyView != null)
		{
			emptyView.setVisibility(CURRENT_STATE == STATE_EMPTY ? View.VISIBLE
					: View.INVISIBLE);
		}

		// ����ɹ������ɷ��������ص����ݾ�������Ϊÿ������������󣬶���ˢ��״̬������createSuccess�����ĵ��ÿ��Է�������
		if (CURRENT_STATE == STATE_SUCCESS)
		{
			if (successView == null)
			{
				successView = createSuccessView();
				this.addView(successView, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			successView.setVisibility(View.VISIBLE);
		} else
		{
			if (successView != null)
				successView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * ����������󣬲��ҳɹ����������ݵ�ҳ��ʱ���˷�������
	 * @return
	 */
	public abstract View createSuccessView();

	/**
	 * ���������ʱ���˷���������
	 * @return
	 */
	public abstract RequestState requestServer();

}
