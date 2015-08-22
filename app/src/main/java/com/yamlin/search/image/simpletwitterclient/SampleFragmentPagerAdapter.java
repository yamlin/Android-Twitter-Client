package com.yamlin.search.image.simpletwitterclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yamlin.search.image.simpletwitterclient.Views.MentionFragment;
import com.yamlin.search.image.simpletwitterclient.Views.PostFragment;
import com.yamlin.search.image.simpletwitterclient.Views.TimelineFragment;

/**
 * Created by yamlin on 8/18/15.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Timeline", "Mentions"};

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return TimelineFragment.newInstance();
            case 1:
                return MentionFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}