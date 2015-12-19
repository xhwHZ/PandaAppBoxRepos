package com.xhw.panda.app.box.holder;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.utils.BitmapHelper;

import android.view.View;
import android.widget.ImageView;

public class DetailScreenHolder extends EasyBaseHolder<DetailJsonInfo>
{

	private ImageView[] imgViews;
	
	@Override
	protected View initItemRootView()
	{
		View view=View.inflate(BaseApplication.getAppliction(), R.layout.detail_screen, null);
		imgViews=new ImageView[5];
		imgViews[0]=(ImageView) view.findViewById(R.id.screen_1);
		imgViews[1]=(ImageView) view.findViewById(R.id.screen_2);
		imgViews[2]=(ImageView) view.findViewById(R.id.screen_3);
		imgViews[3]=(ImageView) view.findViewById(R.id.screen_4);
		imgViews[4]=(ImageView) view.findViewById(R.id.screen_5);
		return view;
	}

	@Override
	protected void refreshItem()
	{
		BitmapUtils bitmapUtils=BitmapHelper.getBitmapUtils();
		//图片的数量 [3,5]
		List<String> screenList= data.screen;
		for(int i=0;i<5;i++)
		{
			if(i<=screenList.size()-1)
			{
				imgViews[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(imgViews[i], GlobalContants.IMAGE_SERVLET+screenList.get(i));
			}else{
				//如果图片太少，最后的ImageView隐藏
				imgViews[i].setVisibility(View.GONE);
			}
		}
	}

}
