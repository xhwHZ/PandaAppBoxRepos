package com.xhw.panda.app.box.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.activity.BaseActivity;
import com.xhw.panda.app.box.application.BaseApplication;
import com.xhw.panda.app.box.domain.UserInfo;
import com.xhw.panda.app.box.global.GlobalContants;
import com.xhw.panda.app.box.utils.BitmapHelper;

public class MenuHolder extends EasyBaseHolder<UserInfo> implements OnClickListener
{

	@ViewInject(R.id.photo_layout)
	private RelativeLayout rl_login;
	
	@ViewInject(R.id.image_photo)
	private ImageView iv_icon;
	
	@ViewInject(R.id.user_name)
	private TextView tv_name;
	
	@ViewInject(R.id.user_email)
	private TextView tv_email;
	
	@ViewInject(R.id.exit_layout)
	private RelativeLayout rl_exit;
	
	@Override
	protected View initItemRootView()
	{
		View view= View.inflate(BaseApplication.getAppliction(), R.layout.menu_left, null);
		ViewUtils.inject(this,view);
		rl_login.setOnClickListener(this);
		rl_exit.setOnClickListener(this);
		return view;
	}

	@Override
	protected void refreshItem()
	{
		
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.photo_layout://µÇÂ¼
			HttpUtils httpUtils=new HttpUtils();
			httpUtils.send(HttpMethod.GET, GlobalContants.USER_SERVLET, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo)
				{
					String result=responseInfo.result;
					Gson gson=new Gson();
					UserInfo userInfo = gson.fromJson(result, UserInfo.class);
					BitmapUtils bitmapUtils=BitmapHelper.getBitmapUtils();
					bitmapUtils.display(iv_icon, GlobalContants.IMAGE_SERVLET+userInfo.icon);
					tv_name.setText(userInfo.name);
					tv_email.setText(userInfo.email);
				}

				@Override
				public void onFailure(HttpException error, String msg)
				{
					Toast.makeText(BaseApplication.getAppliction(), "ÍøÂçÁ¬½ÓÊ§°Ü", 0).show();
				}
			});
			break;
			
		case R.id.exit_layout:
			BaseActivity.closeAllActivity();
			break;
			
		}
	}

}
