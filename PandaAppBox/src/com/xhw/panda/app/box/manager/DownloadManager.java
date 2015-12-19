package com.xhw.panda.app.box.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Intent;
import android.net.Uri;

import com.lidroid.xutils.util.IOUtils;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.domain.DownloadInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.http.HttpHelper;
import com.xhw.panda.app.box.http.HttpHelper.HttpResult;
import com.xhw.panda.app.box.utils.ActivityUtils;



public class DownloadManager {
	/** Ĭ�� */
	public static final int STATE_NONE = 0;
	/** �ȴ� */
	public static final int STATE_WAITING = 1;
	/** ������ */
	public static final int STATE_DOWNLOADING = 2;
	/** ��ͣ */
	public static final int STATE_PAUSE = 3;
	/** ���� */
	public static final int STATE_ERROR = 4;
	/** ������� */
	public static final int STATE_DOWNLOED = 5;

	private static DownloadManager instance;

	private DownloadManager() {
	}

	/** ���ڼ�¼������Ϣ,�������ʽ��Ŀ,��Ҫ�־û����� */
	private Map<Long, DownloadInfo> mDownloadMap = new ConcurrentHashMap<Long, DownloadInfo>();
	/**���ڼ�¼�������ص�����,����ȡ������ʱ,��ͨ��id�ҵ����������ɾ��*/
	private Map<Long,DownloadTask> mTaskMap=new ConcurrentHashMap<Long, DownloadManager.DownloadTask>();
	
	private List<DownloadObserver> mObservers=new ArrayList<DownloadObserver>();
	
	/** ע��۲��� */
	public void registerObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (!mObservers.contains(observer)) {
				mObservers.add(observer);
				//System.out.println(mObservers.size());
			}
		}
	}

	/** ��ע��۲��� */
	public void unRegisterObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				mObservers.remove(observer);
			}
		}
	}
	/** ������״̬���͸ı��ʱ��ص� */
	public void notifyDownloadStateChanged(DownloadInfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadStateChanged(info);
			}
		}
	}

	/** �����ؽ��ȷ��͸ı��ʱ��ص� */
	public void notifyDownloadProgressed(DownloadInfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadProgressed(info);
			}
		}
	}
	// ����
	public static synchronized DownloadManager getInstance() {
		if (instance == null) {
			instance = new DownloadManager();
		}
		return instance;
	}

	public synchronized void download(AppInfo appInfo) {
		DownloadInfo info = mDownloadMap.get(appInfo.id);
		if (info == null) {
			info = DownloadInfo.clone(appInfo);
			mDownloadMap.put(appInfo.id, info);// ���浽������
		}
		if (info.getDownloadState() == STATE_NONE
				|| info.getDownloadState() == STATE_PAUSE
				|| info.getDownloadState() == STATE_ERROR) {
			// ����֮ǰ����״̬����ΪSTATE_WAITING��
			// ��Ϊ��ʱ��û�в���ʼ���أ�ֻ�ǰ�����������̳߳��У�
			// ������������ʼִ��ʱ���Ż��ΪSTATE_DOWNLOADING
			info.setDownloadState(STATE_WAITING);
			// ֪ͨ���½��� 
			notifyDownloadStateChanged(info);
			DownloadTask task = new DownloadTask(info);
			mTaskMap.put(info.getId(), task);
			ThreadManager.createDownLoadPool().execute(task);
		}

	}
	/** ��װӦ�� */
	public synchronized void install(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownloadMap.get(appInfo.id);// �ҳ�������Ϣ
		if (info != null) {// ���Ͱ�װ����ͼ
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.setDataAndType(Uri.parse("file://" + info.getPath()),
					"application/vnd.android.package-archive");
			ActivityUtils.startActivity(installIntent);
		}
		notifyDownloadStateChanged(info);
	}
	
	/**��ͣ����*/
	public synchronized void pause(AppInfo appInfo){
		stopDownload(appInfo);
		DownloadInfo info=mDownloadMap.get(appInfo.id);
		if(info!=null){// �޸�����״̬
			info.setDownloadState(STATE_PAUSE);
			notifyDownloadStateChanged(info);
		}
	}
	/** ȡ�����أ��߼�����ͣ���ƣ�ֻ����Ҫɾ�������ص��ļ� */
	public synchronized void cancel(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownloadMap.get(appInfo.id);// �ҳ�������Ϣ
		if (info != null) {// �޸�����״̬��ɾ���ļ�
			info.setDownloadState(STATE_NONE);
			notifyDownloadStateChanged(info);
			info.setCurrentSize(0);
			File file = new File(info.getPath());
			file.delete();
		}
	}
	private void stopDownload(AppInfo appInfo) {
		DownloadTask task=mTaskMap.remove(appInfo.id);
		if(task!=null){
			ThreadManager.createDownLoadPool().cancel(task);
		}
	}

	public class DownloadTask implements Runnable {
		private DownloadInfo info;
		public DownloadTask(DownloadInfo info) {
			this.info = info;
		}

		@Override
		public void run() {
			info.setDownloadState(STATE_DOWNLOADING);
			notifyDownloadStateChanged(info);
			File file = new File(info.getPath());// ��ȡ�����ļ�
			HttpResult httpResult = null;
			InputStream stream = null;
			// ����ļ�������, ���߽���Ϊ0,���߽��Ⱥ��ļ����Ȳ�һ�� ��������
			if (info.getCurrentSize() == 0 || !file.exists()
					|| file.length() != info.getCurrentSize()) {
				info.setCurrentSize(0);
				file.delete();
				httpResult = HttpHelper.download(GlobalContants.DOWN_SERVLET + info.getUrl());
			} else {
				// ����Ҫ��������
				// �ļ������ҳ��Ⱥͽ�����ȣ����öϵ�����
				httpResult = HttpHelper.download(GlobalContants.DOWN_SERVLET+ info.getUrl() + "&range="
						+ info.getCurrentSize());
			}
			if (httpResult == null
					|| (stream = httpResult.getInputStream()) == null) {
				info.setDownloadState(STATE_ERROR);
				notifyDownloadStateChanged(info);
			} else {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file, true);
					int count = -1;
					byte[] buffer = new byte[1024];
					while (((count = stream.read(buffer)) != -1)
							&& info.getDownloadState() == STATE_DOWNLOADING) {
						fos.write(buffer, 0, count);
						fos.flush();
						info.setCurrentSize(info.getCurrentSize()+count);
						notifyDownloadProgressed(info);
					}
				} catch (Exception e) {
					info.setDownloadState(STATE_ERROR);
					notifyDownloadStateChanged(info);
					info.setCurrentSize(0);
					file.delete(); 
				} finally {
					IOUtils.closeQuietly(fos);
					if (httpResult != null) {
						httpResult.close();
					}
				}
				// �жϽ����Ƿ��App���
				if (info.getCurrentSize() == info.getAppSize()) {
					info.setDownloadState(STATE_DOWNLOED);
					notifyDownloadStateChanged(info);
				} else if (info.getDownloadState() == STATE_PAUSE) {
					notifyDownloadStateChanged(info);
				} else {
					info.setDownloadState(STATE_ERROR);
					notifyDownloadStateChanged(info);
					info.setCurrentSize(0);
					file.delete();
				}
			}
			mTaskMap.remove(info.getId());
		}
	}
	public interface DownloadObserver {

		public void onDownloadStateChanged(DownloadInfo info);

		public void onDownloadProgressed(DownloadInfo info);
	}
	public DownloadInfo getDownloadInfo(long id) {
		// TODO Auto-generated method stub
		return mDownloadMap.get(id);
	}
}

