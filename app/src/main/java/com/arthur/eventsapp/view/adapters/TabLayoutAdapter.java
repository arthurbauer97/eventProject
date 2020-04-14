package com.arthur.eventsapp.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.arthur.eventsapp.view.fragments.EventsFragment;
import com.arthur.eventsapp.view.fragments.ProfileFragment;

public class TabLayoutAdapter extends FragmentStatePagerAdapter {

    int nOfTabs;

    public TabLayoutAdapter(FragmentManager fm, int nOfTabs) {
        super(fm);
        this.nOfTabs = nOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EventsFragment();
            case 1:
                return new ProfileFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return nOfTabs;
    }
}
