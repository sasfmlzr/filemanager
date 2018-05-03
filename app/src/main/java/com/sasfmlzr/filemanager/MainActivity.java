package com.sasfmlzr.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.sasfmlzr.filemanager.api.adapter.PagerFileListAdapter;
import com.sasfmlzr.filemanager.api.fragment.EmptyPagerFragment;
import com.sasfmlzr.filemanager.api.fragment.FileViewFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity implements
        FileViewFragment.OnFragmentInteractionListener,
        EmptyPagerFragment.OnFragmentInteractionListener {

    protected static final File DEFAULT_PATH = Environment
            .getExternalStorageDirectory();

    private ViewPager viewPager;

    public void addTabs(ViewPager viewPager) {
        PagerFileListAdapter adapter = new PagerFileListAdapter(getSupportFragmentManager(), getApplicationContext());
        adapter.addFragment(EmptyPagerFragment.newInstance(DEFAULT_PATH));
        adapter.addFragment(EmptyPagerFragment.newInstance(DEFAULT_PATH));
        viewPager.setAdapter(adapter);
    }

    public void callBackStackFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.pager);
        addTabs(viewPager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //callBackStackFragments();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    public void replaceChildFragment(File currentFile) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String absolutePath = currentFile.getAbsolutePath();
        EmptyPagerFragment fragment = EmptyPagerFragment.newInstance(currentFile);
        ;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.child_fragment_container, fragment, absolutePath);
        transaction.addToBackStack(absolutePath);
        transaction.commit();
    }

    String TAG = "DEBUGTAG";

    @Override
    public void onDirectorySelected(File currentFile) {
        Log.d(TAG, "onDirectorySelected() called with: currentFile = [" + currentFile + "]");
        replaceChildFragment(currentFile);
    }

    @Override
    public void onEmptyFragmentSelected(File currentFile) {
        Log.d(TAG, "onEmptyFragmentSelected() called with: currentFile = [" + currentFile + "]");
    }
}
