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
	 * ������������Fragment,���л��湦��
	 * 
	 * @param position
	 * @return
	 */
	public static BaseFragment getFragment(int position)
	{
		// �л��棬�ͷ��ػ���
		BaseFragment cache = cacheFragment.get(position);
		if (cache != null)
		{
			return cache;
		}

		BaseFragment fragment = null;
		switch (position)
		{
		case 0:
			// ��ҳ
			fragment = new HomeFragment();
			break;
		case 1:
			// Ӧ��
			fragment = new AppFragment();
			break;
		case 2:
			// ��Ϸ
			fragment = new GameFragment();
			break;
		case 3:
			// ר��
			fragment = new SubjectFragment();
			break;
		case 4:
			// ����
			fragment = new CategoryFragment();
			break;
		default:
			fragment = null;
			break;
		}
		// ���û���
		if (fragment != null)
		{
			cacheFragment.put(position, fragment);
		}
		return fragment;
	}
}
