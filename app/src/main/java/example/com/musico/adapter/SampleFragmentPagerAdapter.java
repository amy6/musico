package example.com.musico.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import example.com.musico.fragment.SampleFragment;

import static example.com.musico.activity.MainActivity.ALBUMS_TAG;
import static example.com.musico.activity.MainActivity.ARTISTS_TAG;
import static example.com.musico.activity.MainActivity.FRAGMENT_TAG;
import static example.com.musico.activity.MainActivity.SONGS_TAG;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private String[] pageTitle = {"Songs", "Artists", "Albums"};

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        SampleFragment fragment = new SampleFragment();
        //set bundle data to identify the current fragment
        Bundle args = new Bundle();
        String tag = SONGS_TAG;
        switch (position) {
            case 1:
                tag = ARTISTS_TAG;
                break;
            case 2:
                tag = ALBUMS_TAG;
                break;
        }
        args.putString(FRAGMENT_TAG, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }
}
