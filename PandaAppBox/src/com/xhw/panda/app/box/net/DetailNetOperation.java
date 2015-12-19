package com.xhw.panda.app.box.net;

import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.utils.FileUtils;
import com.xhw.panda.app.box.utils.MD5Utils;


public class DetailNetOperation 
{

	public DetailJsonInfo requestData(String packageName)
	{
		SystemClock.sleep(500);
		// 获取本地缓存
		String jsonData = getCacheFromLocal(packageName);
		// 如果本地缓存没有，读取网络
		if (TextUtils.isEmpty(jsonData))
		{
			// 请求服务器获取数据
			jsonData = requestServer(packageName);
			if(jsonData==null)
			{
				return null;
			}
			// 本地缓存也存储一份
			setCacheToLocal(packageName,jsonData);
		}
		return parseJson(jsonData);
	}

	
	private String getCacheFromLocal(String packageName)
	{
		//包名作md5加密
		String md5=MD5Utils.getTextMd5(packageName);
		return FileUtils.readJsonFromCache(md5);
	}
	
	private void setCacheToLocal(String packageName,String data)
	{
		String md5=MD5Utils.getTextMd5(packageName);
		FileUtils.writeJsonToCache(md5, data);
	}
	
	private String requestServer(String packageName)
	{
		HttpUtils httpUtils = new HttpUtils();
		ResponseStream rs = null;
		try
		{
			rs = httpUtils.sendSync(HttpMethod.GET,
					GlobalContants.DETAIL_SERVLET + packageName);
			String json= FileUtils.in2String(rs);
			return json;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private DetailJsonInfo parseJson(String json)
	{
		Gson gson=new Gson();
		return gson.fromJson(json, DetailJsonInfo.class);
	}

}
