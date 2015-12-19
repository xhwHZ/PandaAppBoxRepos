package com.xhw.panda.app.box.factory;

import java.util.HashMap;
import java.util.Map;

import com.xhw.panda.app.box.fragment.AppFragment;
import com.xhw.panda.app.box.fragment.BaseFragment;
import com.xhw.panda.app.box.fragment.CategoryFragment;
import com.xhw.panda.app.box.fragment.GameFragment;
import com.xhw.panda.app.box.fragment.HomeFragment;
import com.xhw.panda.app.box.fragment.SubjectFragment;

public class FragmentFactory
{

	private static Map<Integer, BaseFragment> cacheFragment = new HashMap<Integer, BaseFragment>();

	/**
	 * 根据索引创建Fragment,具有缓存功能
	 * 
	 * @param position
	 * @return
	 */
	public static BaseFragment getFragment(int position)
	{
		// 有缓存，就返回缓存
		BaseFragment cache = cacheFragment.get(position);
		if (cache != null)
		{
			return cache;
		}

		BaseFragment fragment = null;
		switch (position)
		{
		case 0:
			// 首页
			fragment = new HomeFragment();
			break;
		case 1:
			// 应用
			fragment = new AppFragment();
			break;
		case 2:
			// 游戏
			fragment = new GameFragment();
			break;
		case 3:
			// 专题
			fragment = new SubjectFragment();
			break;
		case 4:
			// 分类
			fragment = new CategoryFragment();
			break;
		default:
			fragment = null;
			break;
		}
		// 设置缓存
		if (fragment != null)
		{
			cacheFragment.put(position, fragment);
		}
		return fragment;
	}
}
