package com.xhw.panda.app.box.net;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.xhw.panda.app.box.domain.SubjectJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;

/**
 * 专题界面的网络操作
 * 
 * @author admin
 *
 */
public class SubjectNetOperation extends
		BaseNetOperation<List<SubjectJsonInfo>>
{

	@Override
	protected String getServletUrl()
	{
		return GlobalContants.SUBJECT_SERVLET;
	}

	@Override
	protected String getJsonCacheName()
	{
		return "subject";
	}

	@Override
	protected List<SubjectJsonInfo> parseJson(String json)
	{
		List<SubjectJsonInfo> infoList = mGson.fromJson(json,
				new TypeToken<List<SubjectJsonInfo>>() {
				}.getType());
		return infoList;
	}
}
