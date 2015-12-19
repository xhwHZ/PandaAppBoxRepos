package com.xhw.panda.app.box.net;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.xhw.panda.app.box.domain.AppInfo;
import com.xhw.panda.app.box.global.GlobalContants;

/**
 * AppÍøÂç²Ù×÷Àà
 * @author admin
 *
 */
public class AppNetOperation extends BaseNetOperation<List<AppInfo>>
{

	@Override
	protected String getServletUrl()
	{
		return GlobalContants.APP_SERVLET;
	}

	@Override
	protected String getJsonCacheName()
	{
		return "app";
	}

	@Override
	protected List<AppInfo> parseJson(String json)
	{
		return mGson.fromJson(json,new TypeToken<List<AppInfo>>() {
		}.getType());
	}


}
