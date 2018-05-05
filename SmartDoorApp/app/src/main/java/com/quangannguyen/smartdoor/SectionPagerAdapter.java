package com.quangannguyen.smartdoor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by quangannguyen on 2/4/2018.
 */

class SectionPagerAdapter extends FragmentPagerAdapter {


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                LichSuFragment lichSuFragment = new LichSuFragment();
                return lichSuFragment;
            case 1:
                ThanhVienFragment thanhVienFragment = new ThanhVienFragment();
                return thanhVienFragment;
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
                return "Lịch Sử";
            case 1:
                return "Thành Viên";
            default:
                return null;
        }
    }
}
