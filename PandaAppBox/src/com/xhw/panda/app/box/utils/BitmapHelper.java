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
			// ���һ������������ͼƬ������������ı�Ӧ�õĶ����ڴ�(0.5f��ʾ�ٷ�֮��ʮ)
			bitmapUtils = new BitmapUtils(BaseApplication.getAppliction(),
					FileUtils.getImgCacheDir(), 0.3f);
		}
		return bitmapUtils;
	}

}
