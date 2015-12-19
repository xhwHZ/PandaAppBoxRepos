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

	// ��ҳ�����֣����������
	@ViewInject(R.id.drawerLayout)
	private DrawerLayout mDrawerLayout;

	@SuppressWarnings("deprecation")
	private ActionBarDrawerToggle drawerToggle;

	@ViewInject(R.id.vp_home)
	private ViewPager vp_home;

	// ������ViewPager�Ϸ���ָʾ���ؼ�(Tab����)
	@ViewInject(R.id.pager_tab_strip)
	private PagerTabStrip pagerTabStrip;

	@ViewInject(R.id.fl_menu)
	private FrameLayout fl_menu;

	@Override
	protected void initView()
	{
		setContentView(R.layout.activity_home);
		ViewUtils.inject(this);
		//��ʼ��Menu
		MenuHolder menuHolder=new MenuHolder();
		//menuHolder.setData(data);
		fl_menu.addView(menuHolder.getItemRootView());
		
		
		// ����tab�����»��ߵ���ɫ
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(
				R.color.indicatorcolor));
		// ��ȡActionBar
		ActionBar actionBar = getSupportActionBar();

		// ��ʾ���뿪��(�̶�д��)
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		// ��ȡ���뿪��(��ʾ��ActionBar�ϵ�)
		// p1 : ActionBar���ڵ�Activity
		// p2 : ���������
		// p3 : ���뿪��ͼƬ
		// p4��p5 �� ���뿪�͹ص�����
		drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_am, R.string.drawer_open,
				R.string.drawer_close) {
			// �����
			@Override
			public void onDrawerOpened(View drawerView)
			{
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				//Toast.makeText(HomeActivity.this, "�������...", 0).show();
			}

			// ����ر�
			@Override
			public void onDrawerClosed(View drawerView)
			{
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				//Toast.makeText(HomeActivity.this, "����ر���...", 0).show();
			}
		};
		mDrawerLayout.setDrawerListener(drawerToggle);// ���ó���״̬�ļ���
		// ������ActionBar������ϵ��ͬ��һ��
		drawerToggle.syncState();
	}

	private String[] tab_names;
	
	@Override
	protected void initData()
	{
		//��ʼ��Tab��������
		tab_names = UIUtils.getStringArray(R.array.tab_names);
		FragmentManager fm = getSupportFragmentManager();
		HomeAdapter adapter = new HomeAdapter(fm);
		vp_home.setAdapter(adapter);
		
	}

	@Override
	protected void initListener()
	{
		//ҳ�淢���ı䣬���������������ˢ�����ݵķ���
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
			Toast.makeText(this, "����", 0).show();
		}
		// ������뿪�ز������ͽ������ؼ�����
		return drawerToggle.onOptionsItemSelected(item)
				| super.onOptionsItemSelected(item);
	}

	// �������ı������仯ʱ��������
	@Override
	public boolean onQueryTextChange(String newText)
	{
		Toast.makeText(this, newText, 0).show();
		return false;
	}

	// �������ύʱ��������
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

		// ����PagerTabStrip�еı�����Ҫ��д�˷���
		@Override
		public CharSequence getPageTitle(int position)
		{
			return tab_names[position];
		}

	}

	//���鳤�ȱ�ʾҪ����Ĵ���
			long[] hits=new long[2];
			
			@Override
			public void onBackPressed()
			{
				System.arraycopy(hits, 1, hits, 0, hits.length-1);
				hits[hits.length-1]=SystemClock.uptimeMillis();//������ʼ�����ʱ��
				if(hits[0]>=(SystemClock.uptimeMillis()-2000)){//2������ʱ��
					  android.os.Process.killProcess(android.os.Process.myPid());    //��ȡPID 
					  System.exit(0);   //����java��c#�ı�׼�˳���������ֵΪ0���������˳�
				}else{
					Toast.makeText(this, "����˫�������˳�Ӧ��", 0).show();
				}
			}
	
}
