package com.quangannguyen.smartdoor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by quangannguyen on 2/4/2018.
 */

class MainAdminPagerAdapter extends FragmentPagerAdapter {


    public MainAdminPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                HouseFragment houseFragment = new HouseFragment();
                return houseFragment;
            case 1:
                UserFragment userFragment = new UserFragment();
                return userFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Houses";
            case 1:
                return "Users";
            default:
                return null;
        }
    }
}
