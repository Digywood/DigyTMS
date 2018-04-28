package com.digywood.tms.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.digywood.tms.Fragments.FlashAttemptFragment;
import com.digywood.tms.Fragments.TestAttemptFragment;

/**
 * Created by Shashank on 26-04-2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TestAttemptFragment tab1 = new TestAttemptFragment();
                return tab1;
            case 1:
                FlashAttemptFragment tab2 = new FlashAttemptFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
