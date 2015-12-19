package com.xhw.panda.app.box.net;

import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhw.panda.app.box.http.HttpHelper;
import com.xhw.panda.app.box.http.HttpHelper.HttpResult;
import com.xhw.panda.app.box.utils.FileUtils;

public abstract class BaseNetOperation<T>
{
	protected Gson mGson;

	public BaseNetOperation()
	{
		mGson = new Gson();
	}

	/**
	 * 请求数据，如果有缓存就用缓存，没缓存就请求网络
	 * 
	 * @param index
	 */
	public T requestData(final int index)
	{
		if (index != 0)
		{
			SystemClock.sleep(1000);
		}
		// 获取本地缓存
		String jsonData = getCacheFromLocal(index);
		// 如果本地缓存没有，读取网络
		if (TextUtils.isEmpty(jsonData))
		{
			// 请求服务器获取数据
			jsonData = requestServer(index);
			if (jsonData == null)
			{
				return null;
			}
			// 本地缓存也存储一份
			setCacheToLocal(jsonData, index);
		}
		return parseJson(jsonData);

	}

	// 将数据保存到本地
	private void setCacheToLocal(String cache, int index)
	{
		FileUtils.writeJsonToCache(getJsonCacheName() + "_" + index, cache);
	}

	// 从本地读取缓存
	private String getCacheFromLocal(int index)
	{
		return FileUtils.readJsonFromCache(getJsonCacheName() + "_" + index);
	}

	// 请求网络获取数据
	private String requestServer(int index)
	{
		HttpResult httpResult = HttpHelper.get(getServletUrl() + index);
		if (httpResult == null)
		{
			return null;
		}
		String result = httpResult.getString();
		return result;
	}

	/**
	 * 请求服务器的Servlet地址
	 * 
	 * @return
	 */
	protected abstract String getServletUrl();

	/**
	 * 保存json缓存文件的文件名
	 * 
	 * @return
	 */
	protected abstract String getJsonCacheName();

	/**
	 * 解析json数据
	 * 
	 * @param json
	 */
	protected abstract T parseJson(String json);
}
