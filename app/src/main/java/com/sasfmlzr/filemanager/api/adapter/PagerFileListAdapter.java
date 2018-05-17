package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sasfmlzr.filemanager.api.fragment.ContainerPagerFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PagerFileListAdapter extends FragmentPagerAdapter {
    private static final String KEY_FRAGMENT = "fragment";

    private List<File> fileList = new ArrayList<>();
    private Context context;

    public PagerFileListAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ContainerPagerFragment.newInstance(fileList.get(position));
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
    public Parcelable saveState() {
        Bundle state = new Bundle();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File file : fileList) {
            arrayList.add(file.getAbsolutePath());
        }
        state.putStringArrayList(KEY_FRAGMENT, arrayList);
        return state;

    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            ArrayList<String> arrayList = ((Bundle) state).getStringArrayList(KEY_FRAGMENT);

            List<File> files = new ArrayList<>();
            assert arrayList != null;
            for (String path : arrayList) {
                files.add(new File(path));
            }
            setFiles(files);
        }
    }

    public void setFiles(List<File> files) {
        fileList = files;
        notifyDataSetChanged();
    }
}
