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
	 * �������ݣ�����л�����û��棬û�������������
	 * 
	 * @param index
	 */
	public T requestData(final int index)
	{
		if (index != 0)
		{
			SystemClock.sleep(1000);
		}
		// ��ȡ���ػ���
		String jsonData = getCacheFromLocal(index);
		// ������ػ���û�У���ȡ����
		if (TextUtils.isEmpty(jsonData))
		{
			// �����������ȡ����
			jsonData = requestServer(index);
			if (jsonData == null)
			{
				return null;
			}
			// ���ػ���Ҳ�洢һ��
			setCacheToLocal(jsonData, index);
		}
		return parseJson(jsonData);

	}

	// �����ݱ��浽����
	private void setCacheToLocal(String cache, int index)
	{
		FileUtils.writeJsonToCache(getJsonCacheName() + "_" + index, cache);
	}

	// �ӱ��ض�ȡ����
	private String getCacheFromLocal(int index)
	{
		return FileUtils.readJsonFromCache(getJsonCacheName() + "_" + index);
	}

	// ���������ȡ����
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
	 * �����������Servlet��ַ
	 * 
	 * @return
	 */
	protected abstract String getServletUrl();

	/**
	 * ����json�����ļ����ļ���
	 * 
	 * @return
	 */
	protected abstract String getJsonCacheName();

	/**
	 * ����json����
	 * 
	 * @param json
	 */
	protected abstract T parseJson(String json);
}
