package com.xhw.panda.app.box.domain;

import com.xhw.panda.app.box.manager.DownloadManager;
import com.xhw.panda.app.box.utils.FileUtils;

public class DownloadInfo
{
	private long id;//唯一标识
	private String packageName;//包名字
	private long appSize;//软件大小
	private long currentSize;//当前下载大小
	private int downloadState=0;//下载状态
	private String url;//下载地址
	private String path;//保存路径
	
	
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
	
	//利用AppInfo克隆出一个DownloadInfo
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
