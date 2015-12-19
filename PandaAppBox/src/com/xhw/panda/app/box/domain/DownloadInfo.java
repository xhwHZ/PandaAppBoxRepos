package com.xhw.panda.app.box.domain;

import com.xhw.panda.app.box.manager.DownloadManager;
import com.xhw.panda.app.box.utils.FileUtils;

public class DownloadInfo
{
	private long id;//Ψһ��ʶ
	private String packageName;//������
	private long appSize;//�����С
	private long currentSize;//��ǰ���ش�С
	private int downloadState=0;//����״̬
	private String url;//���ص�ַ
	private String path;//����·��
	
	
	public String getPackageName()
	{
		return packageName;
	}
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public long getAppSize()
	{
		return appSize;
	}
	public void setAppSize(long appSize)
	{
		this.appSize = appSize;
	}
	public long getCurrentSize()
	{
		return currentSize;
	}
	public void setCurrentSize(long currentSize)
	{
		this.currentSize = currentSize;
	}
	public int getDownloadState()
	{
		return downloadState;
	}
	public void setDownloadState(int downloadState)
	{
		this.downloadState = downloadState;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getPath()
	{
		return path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	
	//����AppInfo��¡��һ��DownloadInfo
	public static DownloadInfo clone(AppInfo appInfo)
	{
		DownloadInfo downloadInfo=new DownloadInfo();
		downloadInfo.id=appInfo.id;
		downloadInfo.packageName=appInfo.packageName;
		downloadInfo.appSize=appInfo.size;
		downloadInfo.currentSize=0;
		downloadInfo.downloadState=DownloadManager.STATE_NONE;
		downloadInfo.url=appInfo.downloadUrl;
		downloadInfo.path=FileUtils.getDownloadDir()+"/"+downloadInfo.packageName+".apk";
		return downloadInfo;
	}
	
	
}
