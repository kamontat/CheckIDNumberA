package com.kamontat.checkidnumber.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 11/May/2017 - 10:19 PM
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private final List<Fragment> fragmentList;
	
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragmentList = new ArrayList<>();
	}
	
	public void addFragment(Fragment fragment) {
		fragmentList.add(fragment);
	}
	
	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}
	
	@Override
	public int getCount() {
		return fragmentList.size();
	}
}
