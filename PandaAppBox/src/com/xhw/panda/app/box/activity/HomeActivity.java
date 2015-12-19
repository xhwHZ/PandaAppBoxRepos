package com.xhw.panda.app.box.activity;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.panda.app.box.R;
import com.xhw.panda.app.box.factory.FragmentFactory;
import com.xhw.panda.app.box.holder.MenuHolder;
import com.xhw.panda.app.box.utils.UIUtils;

public class HomeActivity extends BaseActivity implements
		OnQueryTextListener
{

	// 主页根布局，抽屉根布局
	@ViewInject(R.id.drawerLayout)
	private DrawerLayout mDrawerLayout;

	@SuppressWarnings("deprecation")
	private ActionBarDrawerToggle drawerToggle;

	@ViewInject(R.id.vp_home)
	private ViewPager vp_home;

	// 依附在ViewPager上方的指示器控件(Tab长条)
	@ViewInject(R.id.pager_tab_strip)
	private PagerTabStrip pagerTabStrip;

	@ViewInject(R.id.fl_menu)
	private FrameLayout fl_menu;

	@Override
	protected void initView()
	{
		setContentView(R.layout.activity_home);
		ViewUtils.inject(this);
		//初始化Menu
		MenuHolder menuHolder=new MenuHolder();
		//menuHolder.setData(data);
		fl_menu.addView(menuHolder.getItemRootView());
		
		
		// 设置tab长条下划线的颜色
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(
				R.color.indicatorcolor));
		// 获取ActionBar
		ActionBar actionBar = getSupportActionBar();

		// 显示抽屉开关(固定写法)
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		// 获取抽屉开关(显示在ActionBar上的)
		// p1 : ActionBar所在的Activity
		// p2 : 抽屉根布局
		// p3 : 抽屉开关图片
		// p4、p5 ： 抽屉开和关的描述
		drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_am, R.string.drawer_open,
				R.string.drawer_close) {
			// 抽屉打开
			@Override
			public void onDrawerOpened(View drawerView)
			{
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				//Toast.makeText(HomeActivity.this, "抽屉打开了...", 0).show();
			}

			// 抽屉关闭
			@Override
			public void onDrawerClosed(View drawerView)
			{
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				//Toast.makeText(HomeActivity.this, "抽屉关闭了...", 0).show();
			}
		};
		mDrawerLayout.setDrawerListener(drawerToggle);// 设置抽屉状态的监听
		// 开关与ActionBar建立联系，同步一下
		drawerToggle.syncState();
	}

	private String[] tab_names;
	
	@Override
	protected void initData()
	{
		//初始化Tab标题数据
		tab_names = UIUtils.getStringArray(R.array.tab_names);
		FragmentManager fm = getSupportFragmentManager();
		HomeAdapter adapter = new HomeAdapter(fm);
		vp_home.setAdapter(adapter);
		
	}

	@Override
	protected void initListener()
	{
		//页面发生改变，调用请求服务器，刷新数据的方法
		vp_home.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position)
			{
				FragmentFactory.getFragment(position).getDataFromServer();
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if (android.os.Build.VERSION.SDK_INT >= 11)
		{
			SearchView searchView = (SearchView) menu.findItem(
					R.id.action_search).getActionView();
			searchView.setOnQueryTextListener(this);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_search)
		{
			Toast.makeText(this, "搜索", 0).show();
		}
		// 如果抽屉开关不处理，就交给父控件处理
		return drawerToggle.onOptionsItemSelected(item)
				| super.onOptionsItemSelected(item);
	}

	// 当搜索文本发生变化时，被调用
	@Override
	public boolean onQueryTextChange(String newText)
	{
		Toast.makeText(this, newText, 0).show();
		return false;
	}

	// 当搜索提交时，被调用
	@Override
	public boolean onQueryTextSubmit(String query)
	{
		Toast.makeText(this, query, 0).show();
		return false;
	}

	private class HomeAdapter extends FragmentStatePagerAdapter
	{

		public HomeAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			return FragmentFactory.getFragment(position);
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return tab_names.length;
		}

		// 设置PagerTabStrip中的标题需要重写此方法
		@Override
		public CharSequence getPageTitle(int position)
		{
			return tab_names[position];
		}

	}

	//数组长度表示要点击的次数
			long[] hits=new long[2];
			
			@Override
			public void onBackPressed()
			{
				System.arraycopy(hits, 1, hits, 0, hits.length-1);
				hits[hits.length-1]=SystemClock.uptimeMillis();//开机后开始计算的时间
				if(hits[0]>=(SystemClock.uptimeMillis()-2000)){//2秒连击时间
					  android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
					  System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
				}else{
					Toast.makeText(this, "连续双击两次退出应用", 0).show();
				}
			}
	
}
