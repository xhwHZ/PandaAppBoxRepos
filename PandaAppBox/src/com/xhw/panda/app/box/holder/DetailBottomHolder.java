package com.xhw.panda.app.box.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.domain.DownloadInfo;
import com.xhw.panda.app.box.manager.DownloadManager;
import com.xhw.panda.app.box.manager.DownloadManager.DownloadObserver;
import com.xhw.panda.app.box.utils.ThreadUtils;

public class DetailBottomHolder extends EasyBaseHolder<DetailJsonInfo>
		implements OnClickListener, DownloadObserver
{
	private Button mBtnFavorites, mBtnShare, mBtnProgress;
	private FrameLayout mLayout;
	private int mState; // 下载状态
	private int mProgress; // 当前进度
	private ProgressBar mProgeressView;
	private DownloadManager mDownloadManager2;
	private TextView tv_load_process;


	public void refreshState(int state, int progress)
	{
		mState = state;
		mProgress = progress;
		switch (mState)
		{
		case DownloadManager.STATE_NONE:
			mProgeressView.setVisibility(View.GONE);
			mBtnProgress.setVisibility(View.VISIBLE);
			mBtnProgress
					.setText("下载");
			break;
		case DownloadManager.STATE_PAUSE:
			mProgeressView.setVisibility(View.VISIBLE);
			mProgeressView.setProgress(progress);
			mBtnProgress.setVisibility(View.GONE);
			tv_load_process.setVisibility(View.VISIBLE);
			tv_load_process.setText("暂停");
			break;
		case DownloadManager.STATE_ERROR:
			mProgeressView.setVisibility(View.GONE);
			tv_load_process.setVisibility(View.GONE);
			mBtnProgress.setVisibility(View.VISIBLE);
			mBtnProgress.setText("失败");
			break;
		case DownloadManager.STATE_WAITING:
			mProgeressView.setVisibility(View.VISIBLE);
			mProgeressView.setProgress(progress);
			tv_load_process.setVisibility(View.VISIBLE);
			tv_load_process.setText("等待");
			mBtnProgress.setVisibility(View.GONE);
			break;
		case DownloadManager.STATE_DOWNLOADING:
			mProgeressView.setVisibility(View.VISIBLE);
			mProgeressView.setProgress(progress);
			tv_load_process.setVisibility(View.VISIBLE);
			tv_load_process.setText(progress + "%");
			// mProgeressView.setCenterText("");
			mBtnProgress.setVisibility(View.GONE);
			break;
		case DownloadManager.STATE_DOWNLOED:
			mProgeressView.setVisibility(View.GONE);
			tv_load_process.setVisibility(View.GONE);
			mBtnProgress.setVisibility(View.VISIBLE);
			mBtnProgress.setText("安装");
			break;
		default:
			break;
		}
	}

	@Override
	public void setData(DetailJsonInfo data)
	{

		if (mDownloadManager2 == null)
		{
			mDownloadManager2 = DownloadManager.getInstance();
		}
		DownloadInfo downloadInfo = mDownloadManager2.getDownloadInfo(data
				.id);
		if (downloadInfo != null)
		{
			mState = downloadInfo.getDownloadState();
			mProgress = (int) (downloadInfo.getCurrentSize() * 100 / downloadInfo
					.getAppSize());
		} else
		{
			mState = DownloadManager.STATE_NONE;
			mProgress = 0;
		}
		super.setData(data);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bottom_favorites:
			Toast.makeText(BaseApplication.getAppliction(), "收藏", 0)
					.show();
			break;
		case R.id.bottom_share:
			Toast.makeText(BaseApplication.getAppliction(),"分享", 0)
					.show();
			break;
		case R.id.detail_progress:
		case R.id.progress_btn:
			if (mState == DownloadManager.STATE_NONE
					|| mState == DownloadManager.STATE_PAUSE
					|| mState == DownloadManager.STATE_ERROR)
			{
				mDownloadManager2.download(data);
			} else if (mState == DownloadManager.STATE_WAITING
					|| mState == DownloadManager.STATE_DOWNLOADING)
			{
				mDownloadManager2.pause(data);
			} else if (mState == DownloadManager.STATE_DOWNLOED)
			{
				mDownloadManager2.install(data);
			}
			break;
		default:
			break;
		}
	}

	public void startObserver()
	{
		mDownloadManager2.registerObserver(this);
	}

	public void stopObserver()
	{
		mDownloadManager2.unRegisterObserver(this);
	}

	@Override
	public void onDownloadStateChanged(DownloadInfo info)
	{
		refreshHolder(info);
	}

	@Override
	public void onDownloadProgressed(DownloadInfo info)
	{
		refreshHolder(info);
	}

	private void refreshHolder(final DownloadInfo info)
	{
		AppInfo appInfo = data;
		if (appInfo.id == info.getId())
		{
			ThreadUtils.runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					refreshState(info.getDownloadState(), (int) (info
							.getCurrentSize() * 100 / info.getAppSize()));
				}
			});
		}
	}



	@Override
	protected View initItemRootView()
	{
		View view = View.inflate(BaseApplication.getAppliction(), R.layout.detail_bottom, null);
		mBtnFavorites = (Button) view.findViewById(R.id.bottom_favorites);
		mBtnShare = (Button) view.findViewById(R.id.bottom_share);
		mBtnProgress = (Button) view.findViewById(R.id.progress_btn);
		mBtnFavorites.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		mBtnProgress.setOnClickListener(this);
		mBtnFavorites.setText("收藏");
		mBtnShare.setText("分享");

		mLayout = (FrameLayout) view.findViewById(R.id.progress_layout);
		mProgeressView = (ProgressBar) view.findViewById(R.id.pb_load_process);
		tv_load_process = (TextView) view.findViewById(R.id.tv_load_process);
		mProgeressView.setId(R.id.detail_progress);
		mProgeressView.setOnClickListener(this);
		// mProgeressView.setProgressDrawable(UIUtils.getResources().getDrawable(
		// R.drawable.progress_drawable));
		mProgeressView.setMax(100);
		// mProgeressView.setProgress(0);
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// mLayout.addView(mProgeressView, params);
		return view;
	}

	@Override
	protected void refreshItem()
	{
		refreshState(mState, mProgress);
		
	}

}
