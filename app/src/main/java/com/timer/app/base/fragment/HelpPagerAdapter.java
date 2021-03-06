package com.timer.app.base.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.timer.app.base.R;

/**
 * Created by felias on 7.12.16.
 */

public class HelpPagerAdapter extends FragmentStatePagerAdapter {

public static int COUNT=4;

    public HelpPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment helpScreen=null;
        Bundle args=null;
        switch (position) {
            case 0:
                helpScreen = new HelpFragment();
                args = new Bundle();
                args.putInt(HelpFragment.IMAGE_ID, R.drawable.help1_new);
                args.putInt(HelpFragment.TEXT_ID,R.string.help1_text);
                helpScreen.setArguments(args);
                return helpScreen;
            case 1:
                helpScreen = new HelpFragment();
                args = new Bundle();
                args.putInt(HelpFragment.IMAGE_ID, R.drawable.help2_new);
                args.putInt(HelpFragment.TEXT_ID,R.string.help2_text);
                helpScreen.setArguments(args);
                return helpScreen;
            case 2:
                helpScreen = new HelpFragment();
                args = new Bundle();
                args.putInt(HelpFragment.IMAGE_ID, R.drawable.help3_new);
                args.putInt(HelpFragment.TEXT_ID,R.string.help3_text);
                helpScreen.setArguments(args);
                return helpScreen;
            case 3:
                helpScreen = new HelpFragment();
                args = new Bundle();
                args.putInt(HelpFragment.IMAGE_ID, R.drawable.help4_new);
                args.putInt(HelpFragment.TEXT_ID,R.string.help4_text);
                helpScreen.setArguments(args);
                return helpScreen;
        }
        return helpScreen;
    }

    @Override
    public int getCount() {
        return COUNT;
    }



}
