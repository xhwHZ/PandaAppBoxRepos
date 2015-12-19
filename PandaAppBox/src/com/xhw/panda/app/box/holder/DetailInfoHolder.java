package com.xhw.panda.app.box.holder;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.DetailJsonInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.utils.BitmapHelper;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailInfoHolder extends EasyBaseHolder<DetailJsonInfo>
{

	@ViewInject(R.id.item_icon)
	private ImageView item_icon;

	@ViewInject(R.id.item_title)
	private TextView item_title;

	@ViewInject(R.id.item_rating)
	private RatingBar item_rating;

	@ViewInject(R.id.item_download)
	private TextView item_download;

	@ViewInject(R.id.item_version)
	private TextView item_version;

	@ViewInject(R.id.item_date)
	private TextView item_date;

	@ViewInject(R.id.item_size)
	private TextView item_size;

	@Override
	protected View initItemRootView()
	{
		View view = View.inflate(BaseApplication.getAppliction(),
				R.layout.detail_app_info, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void refreshItem()
	{
		BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils();
		bitmapUtils.display(item_icon, GlobalContants.IMAGE_SERVLET
				+ data.iconUrl);
		item_title.setText(data.name);
		item_rating.setRating(data.stars);
		item_download.setText("下载量:"+data.downloadNum);
		item_version.setText("版本:"+data.version);
		item_date.setText("时间:"+data.date);
		item_size.setText("大小:"+Formatter.formatFileSize(
				BaseApplication.getAppliction(), data.size));
	}

}
