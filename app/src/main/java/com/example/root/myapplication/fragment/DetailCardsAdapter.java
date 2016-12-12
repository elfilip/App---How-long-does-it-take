package com.example.root.myapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;


import com.example.root.myapplication.R;
import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Measurement;

import java.util.List;

/**
 * Created by felias on 7.12.16.
 */

public class DetailCardsAdapter extends FragmentPagerAdapter {

    public static final String ADAPTER_PARAM = "adapter";
    public static final String MEASUREMENT_PARAM = "measurement";
    public static final String ACTION_NAME = "actionName";

    private List<Measurement> measurementList;
    private Action currentAction;
    private long idCounter=0;


    public DetailCardsAdapter(FragmentManager fm, Action action) {
        super(fm);
        this.currentAction=action;
        this.measurementList=action.getMeasurement();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment cards = null;
        cards=new DetailCardsFragment();

        Bundle args = new Bundle();

        args.putString(ACTION_NAME, currentAction.getName());
        args.putInt(MEASUREMENT_PARAM, position);
        cards.setArguments(args);
        return cards;
    }

    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        idCounter += getCount() + n;
    }

    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return idCounter + position;
    }

    @Override
    public int getCount() {
        return measurementList.size();
    }

    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public void notifyDataSetChanged() {
        notifyChangeInPosition(measurementList.size());
        super.notifyDataSetChanged();
    }
}
