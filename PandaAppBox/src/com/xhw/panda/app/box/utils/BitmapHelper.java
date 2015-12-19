package com.xhw.panda.app.box.utils;

import com.lidroid.xutils.BitmapUtils;
import com.xhw.panda.app.box.application.BaseApplication;

public class BitmapHelper
{
	private BitmapHelper()
	{
	}

	private static BitmapUtils bitmapUtils;

	public static BitmapUtils getBitmapUtils()
	{
		if (bitmapUtils == null)
		{
			// 最后一个参数：加载图片，最多允许消耗本应用的多少内存(0.5f表示百分之五十)
			bitmapUtils = new BitmapUtils(BaseApplication.getAppliction(),
					FileUtils.getImgCacheDir(), 0.3f);
		}
		return bitmapUtils;
	}

}
