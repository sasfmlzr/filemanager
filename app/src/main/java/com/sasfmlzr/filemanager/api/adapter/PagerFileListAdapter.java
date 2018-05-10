package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.sasfmlzr.filemanager.api.fragment.EmptyPagerFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PagerFileListAdapter extends FragmentPagerAdapter {

    private List<File> fileList = new ArrayList<>();
    private Context context;
    private static final String TAG = "PagerFileListAdapter";

    public PagerFileListAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    public void addFragment(List<File> files) {
        fileList = files;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return EmptyPagerFragment.newInstance(fileList.get(position));
    }

    @Override
    public int getCount() {
        return fileList.size();
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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "position = [" + position + "], object = [" + object + "]");
        super.setPrimaryItem(container, position, object);
    }
}
