package com.sasfmlzr.filemanager.api.adapter;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerFileListAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Context context;

    public PagerFileListAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public float getPageWidth(int position) {
        if (context.getResources().getConfiguration().orientation == 1) {
            return 1f;
        } else {
            return 0.5f;
        }
    }

}
