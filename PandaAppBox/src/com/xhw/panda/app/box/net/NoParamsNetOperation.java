package com.xhw.panda.app.box.net;

import android.os.SystemClock;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xhw.panda.app.box.utils.FileUtils;
import com.xhw.panda.app.box.utils.MD5Utils;

/**
 * 无请求参数的网络连接基类
 * @author admin
 *
 * @param <T>
 */
public abstract class NoParamsNetOperation<T>
{

	protected abstract String getUrl();
	
	public T requestData()
	{
		//SystemClock.sleep(1000);
		// 获取本地缓存
		String jsonData = getCacheFromLocal(getUrl());
		// 如果本地缓存没有，读取网络
		if (TextUtils.isEmpty(jsonData))
		{
			// 请求服务器获取数据
			jsonData = requestServer();
			if (jsonData == null)
			{
				return null;
			}
			// 本地缓存也存储一份
			setCacheToLocal(getUrl(), jsonData);
		}
		return parseJson(jsonData);
	}

	private String getCacheFromLocal(String url)
	{
		// 包名作md5加密
		String md5 = MD5Utils.getTextMd5(url);
		return FileUtils.readJsonFromCache(md5);
	}

	private void setCacheToLocal(String url, String data)
	{
		String md5 = MD5Utils.getTextMd5(url);
		FileUtils.writeJsonToCache(md5, data);
	}

	private String requestServer()
	{
		HttpUtils httpUtils = new HttpUtils();
		ResponseStream rs = null;
		try
		{
			rs = httpUtils.sendSync(HttpMethod.GET, getUrl());
			String json = FileUtils.in2String(rs);
			return json;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	protected abstract  T parseJson(String json);
}
