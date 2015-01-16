package com.fsz570.easyaccounting.adapter;

import com.fsz570.easyaccounting.ChartFragment;
import com.fsz570.easyaccounting.InputFragment;
import com.fsz570.easyaccounting.ListFragment;
import com.fsz570.easyaccounting.SettingFragment;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	private InputFragment inputFragment;
	private ListFragment listFragment;
	private ChartFragment chartFragment;
	private SettingFragment settingFragment;

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			if(inputFragment==null){
				inputFragment = new InputFragment();
			}
			return inputFragment;
		case 1:
			if(listFragment==null){
				listFragment = new ListFragment();
			}
			return listFragment;
		case 2:
			if(chartFragment==null){
				chartFragment = new ChartFragment();
			}
			return chartFragment;
		case 3:
			if(settingFragment==null){
				settingFragment = new SettingFragment();
			}
			return settingFragment;
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
