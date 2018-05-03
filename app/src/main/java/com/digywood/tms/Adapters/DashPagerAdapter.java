package com.digywood.tms.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.digywood.tms.Fragments.AssessmentFragment;
import com.digywood.tms.Fragments.FlashFragment;
import com.digywood.tms.Fragments.PracticeFragment;

/**
 * Created by Shashank on 26-04-2018.
 */

public class DashPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DashPagerAdapter(FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PracticeFragment tab1 = new PracticeFragment();
                return tab1;
            case 1:
                FlashFragment tab2 = new FlashFragment();
                return tab2;
            case 2:
                AssessmentFragment tab3 = new AssessmentFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
