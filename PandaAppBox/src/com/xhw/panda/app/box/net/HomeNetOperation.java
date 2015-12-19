package com.xhw.panda.app.box.net;

import com.xhw.panda.app.box.domain.HomeJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;

/**
 * 主界面的网络操作
 * @author admin
 *
 */
public class HomeNetOperation extends BaseNetOperation<HomeJsonInfo>
{

	@Override
	protected String getServletUrl()
	{
		return GlobalContants.HOME_SERVLET;
	}

	@Override
	protected String getJsonCacheName()
	{
		return "home";
	}

	@Override
	protected HomeJsonInfo parseJson(String json)
	{
		return mGson.fromJson(json, HomeJsonInfo.class);
	}
}
