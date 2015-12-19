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

//六个页面的布局基类
public abstract class BasePageView extends FrameLayout
{
	/**
	 * 请求服务器状态
	 * 
	 * @author admin
	 *
	 */
	public enum RequestState {
		// 数值为了和状态值对应起来
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

	// 四个状态
	// 加载中
	private static final int STATE_LOADING = 0;

	// 加载失败
	private static final int STATE_ERROR = 1;

	// 请求服务器成功，但服务器没有返回数据
	private static final int STATE_EMPTY = 2;

	// 请求服务器成功，服务器有数据
	private static final int STATE_SUCCESS = 3;

	// 当前状态
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
		// 将四种状态对应的布局文件都加载进FrameLayout中
		initRootView();
		// 根据当前状态显示哪一个布局
		showViewByCurrentState();
		// 请求服务器的方法会在BaseFragment中的onActivityCreated方法被执行,因为这个View会在createView中被创建，所以init方法会比请求服务器的方法先调用
	}

	private View loadingView;

	private View errorView;

	private View emptyView;

	private View successView;

	// 将四种状态对应的布局文件都加载进frameLayout中
	private void initRootView()
	{
		// 1、加载中的页面
		loadingView = createLoadingView();
		this.addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// 2、请求服务器失败的页面
		errorView = createErrorView();
		this.addView(errorView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// 3、请求服务器成功，但没有数据的空页面
		emptyView = createEmptyView();
		this.addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// 4、请求服务器成功，且有数据的页面
		// 不能放这里，成功界面的数据要请求服务器后才确定
		// successView = createSuccessView();
		// rootView.addView(successView, new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	// 请求服务器
	public void getDataFromServer()
	{
		// 请求服务器前，将状态设置成加载中
		CURRENT_STATE = STATE_LOADING;
		// 显示加载中的页面
		showViewByCurrentState();

		// 开启子线程请求网络
		//使用线程池
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			
			@Override
			public void run()
			{
				//SystemClock.sleep(2000);
				// 真正请求服务器
				final RequestState result = requestServer();
				// 可能这个Fragment已经被销毁了，但是子线程还在跑，有可能空指针
				ThreadUtils.runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
						// 刷新状态
						CURRENT_STATE = result.getValue();
						showViewByCurrentState();
					}
				});
			}
		});
		
	}

	// 创建请求服务器成功，但没有数据的空页面
	private View createEmptyView()
	{
		return View.inflate(mContext, R.layout.view_empty, null);
	}

	// 创建请求服务器失败的页面
	private View createErrorView()
	{
		View view = View.inflate(mContext, R.layout.view_error, null);
		// 重新加载按钮
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

	// 创建加载中的页面
	private View createLoadingView()
	{
		return View.inflate(mContext, R.layout.view_loading, null);
	}

	// 根据当前状态，决定显示哪个View
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

		// 请求成功界面由服务器返回的数据决定，因为每次请求服务器后，都会刷新状态，所以createSuccess方法的调用可以放在这里
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
	 * 请求服务器后，并且成功返回有数据的页面时，此方法调用
	 * @return
	 */
	public abstract View createSuccessView();

	/**
	 * 请求服务器时，此方法被调用
	 * @return
	 */
	public abstract RequestState requestServer();

}
