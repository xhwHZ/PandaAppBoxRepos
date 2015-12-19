package com.xhw.panda.app.box.net;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.global.GlobalContants;

public class RankNetOperation extends NoParamsNetOperation<List<String>>
{

	@Override
	protected String getUrl()
	{
		return GlobalContants.RANK_SERVLET;
	}

	@Override
	protected List<String> parseJson(String json)
	{
		Gson gson=new Gson();
		return gson.fromJson(json, new TypeToken<List<String>>() {
		}.getType());
	}

}
