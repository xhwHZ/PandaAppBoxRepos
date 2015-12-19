package com.xhw.panda.app.box.net;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xhw.panda.app.box.domain.CategoryJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;

public class CategoryNetOperation extends NoParamsNetOperation<List<CategoryJsonInfo>>
{

	@Override
	protected List<CategoryJsonInfo> parseJson(String json)
	{
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<List<CategoryJsonInfo>>() {
		}.getType());

	}

	@Override
	protected String getUrl()
	{
		return GlobalContants.CATEGORY_SERVLET;
	}

}
