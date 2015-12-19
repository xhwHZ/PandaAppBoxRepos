package com.xhw.panda.app.box.holder;

import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.domain.DownloadInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.manager.DownloadManager;
import com.xhw.panda.app.box.manager.DownloadManager.DownloadObserver;
import com.xhw.panda.app.box.utils.BitmapHelper;
import com.xhw.panda.app.box.utils.ThreadUtils;
import com.xhw.panda.app.box.utils.UIUtils;
import com.xhw.panda.app.box.view.ProgressArc;

public class AppListHoder extends EasyBaseHolder<AppInfo> implements OnClickListener,DownloadObserver
{

	@Override
	protected View initItemRootView()
	{
		View view = View.inflate(BaseApplication.getAppliction(), R.layout.list_item, null);
		icon = (ImageView) view.findViewById(R.id.item_icon);
		tvTitle = (TextView) view.findViewById(R.id.item_title);
		tvSize = (TextView) view.findViewById(R.id.item_size);
		tvDes = (TextView) view.findViewById(R.id.item_bottom);
		rb = (RatingBar) view.findViewById(R.id.item_rating);
		mActionLayout = (RelativeLayout) view.findViewById(R.id.item_action);
		mActionLayout.setBackgroundResource(R.drawable.list_item_action_bg);
		mActionLayout.setOnClickListener(this);

		mProgressLayout = (FrameLayout) view.findViewById(R.id.action_progress);
		mProgressArc = new ProgressArc(BaseApplication.getAppliction());
		int arcDiameter = UIUtils.dp2px(26);
		// 设置圆的直径
		mProgressArc.setArcDiameter(arcDiameter);
		// 设置进度条的颜色
		mProgressArc.setProgressColor(UIUtils.getColor(R.color.progress));
		int size = UIUtils.dp2px(27);
		mProgressLayout.addView(mProgressArc, new ViewGroup.LayoutParams(size,
				size));

		mActionText = (TextView) view.findViewById(R.id.action_txt);

		return view;
	}

	@Override
	protected void refreshItem()
	{
		initDownloadState();
		
		BitmapUtils bitmapUtils=BitmapHelper.getBitmapUtils();
		AppInfo appInfo = getData();
		tvTitle.setText(appInfo.name);
		tvSize.setText(Formatter.formatFileSize(BaseApplication.getAppliction(),
				appInfo.size));
		tvDes.setText(appInfo.des);
		rb.setRating(appInfo.stars);

		String url = appInfo.iconUrl;
		bitmapUtils.display(icon, GlobalContants.IMAGE_SERVLET + url);
		refreshState(mState, mProgress);
	}

	//初始化下载状态
	private void initDownloadState()
	{
		if (downloadManager == null)
		{
			downloadManager = DownloadManager.getInstance();
		}
		DownloadInfo downloadInfo = downloadManager.getDownloadInfo(data.id);
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
	}
	
	
	private ImageView icon;
	private TextView tvTitle, tvSize, tvDes, mActionText;
	private RatingBar rb;
	private RelativeLayout mActionLayout;
	private FrameLayout mProgressLayout;
	private DownloadManager downloadManager;
	private int mState;
	private float mProgress;
	private ProgressArc mProgressArc;



//	@Override
//	public void setData(AppInfo data) {
//		if (downloadManager == null) {
//			downloadManager = DownloadManager.getInstance();
//		}
//		DownloadInfo downloadInfo = downloadManager.getDownloadInfo(data
//				.id);
//		if (downloadInfo != null) {
//			mState = downloadInfo.getDownloadState();
//			mProgress = (int) (downloadInfo.getCurrentSize() * 100 / downloadInfo
//					.getAppSize());
//		} else {
//			mState = DownloadManager.STATE_NONE;
//			mProgress = 0;
//		}
//		super.setData(data);
//	}


	public void refreshState(int state, float progress) {
		mState = state;
		mProgress = progress;
		switch (mState) {
		case DownloadManager.STATE_NONE:
			mProgressArc.seForegroundResource(R.drawable.ic_download);
			// 是否画进度条，不画进度条
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText("下载");
			break;
		case DownloadManager.STATE_PAUSE:
			mProgressArc.seForegroundResource(R.drawable.ic_resume);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText("暂停");
			break;
		case DownloadManager.STATE_ERROR:
			mProgressArc.seForegroundResource(R.drawable.ic_redownload);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText("失败");
			break;
		case DownloadManager.STATE_WAITING:
			mProgressArc.seForegroundResource(R.drawable.ic_pause);
			// 是否画进度条
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
			mProgressArc.setProgress(progress / 100, false);
			mActionText.setText("等待");
			break;
		case DownloadManager.STATE_DOWNLOADING:
			mProgressArc.seForegroundResource(R.drawable.ic_pause);
			// 画进度条
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
			mProgressArc.setProgress(progress / 100, true);
			mActionText.setText(mProgress + "%");
			break;
		case DownloadManager.STATE_DOWNLOED:
			mProgressArc.seForegroundResource(R.drawable.ic_install);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText("安装");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.item_action) {
			if (mState == DownloadManager.STATE_NONE
					|| mState == DownloadManager.STATE_PAUSE
					|| mState == DownloadManager.STATE_ERROR) {
				downloadManager.download(getData());
			} else if (mState == DownloadManager.STATE_WAITING
					|| mState == DownloadManager.STATE_DOWNLOADING) {
				downloadManager.pause(getData());
			} else if (mState == DownloadManager.STATE_DOWNLOED) {
				downloadManager.install(getData());
			}
		}
	}

	private void refreshHolder(final DownloadInfo downInfo)
	{
		if(downInfo.getId()==data.id)
		{
			ThreadUtils.post(new Runnable() {
				@Override
				public void run() {
				   refreshState(downInfo.getDownloadState(),
							(int) (downInfo.getCurrentSize() * 100 / downInfo
									.getAppSize()));
				}
			});
		}
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


}
