package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PagerFileListAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Context context;
    private static final String KEY_FRAGMENT = "fragment";
    private static final String TAG = "PagerFileListAdapter";

    public PagerFileListAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
        notifyDataSetChanged();
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

    @Override
    public Parcelable saveState() {
        Bundle state = new Bundle();
        int i = 0;
        for (Fragment fragment : fragmentManager.getFragments()) {
            int itemId = i++;
            String bundleKey = KEY_FRAGMENT + itemId;
            if (fragment.isAdded())
                fragmentManager.putFragment(state, bundleKey, fragment);
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);

            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith(KEY_FRAGMENT)) {
                    Fragment f = fragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        //f.setMenuVisibility(false);
                        addFragment(f);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}
