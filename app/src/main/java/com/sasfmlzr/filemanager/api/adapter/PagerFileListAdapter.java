package com.sasfmlzr.filemanager.api.adapter;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.fragment.FileViewFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PagerFileListAdapter extends FragmentPagerAdapter
        implements FileViewFragment.OnFragmentInteractionListener{

    private final List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Context context;
    private boolean firstFragment = true;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onDirectorySelected(File currentFile);
    }
    public PagerFileListAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

    public void callBackStackFragments() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            System.exit(0);
        }
    }

    public void createFileViewFragment(File currentFile) {
        String absolutePath = currentFile.getAbsolutePath();
        FileViewFragment fragment = FileViewFragment.newInstance(currentFile);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (firstFragment) {
            //transaction.add(fragment, absolutePath);
            transaction.add(R.id.pager, fragment, absolutePath);
            firstFragment = false;
        } else {
            //transaction.re
            transaction.replace(R.id.pager, fragment, absolutePath);
            //transaction.replace(g, fragment, absolutePath);
            transaction.addToBackStack(absolutePath);
        }
        transaction.commit();
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
    public void onDirectorySelected(File currentFile) {
        createFileViewFragment(currentFile);
    }


}
