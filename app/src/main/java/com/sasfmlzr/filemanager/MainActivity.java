package com.sasfmlzr.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sasfmlzr.filemanager.api.fragment.DirectoryNavigationFragment;
import com.sasfmlzr.filemanager.api.fragment.FileViewFragment;

public class MainActivity extends AppCompatActivity implements FileViewFragment.OnArticleSelectedListener {
    protected static final String DEFAULT_PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();
    private String currentPath;
    private boolean firstFragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        createFileViewFragment(DEFAULT_PATH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            callBackStackFragments();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void callBackStackFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    public void createFileViewFragment(String currentPath) {
        FileViewFragment fragment = FileViewFragment.newInstance(currentPath);
        ;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (firstFragment) {
            transaction.add(R.id.fileviewonactivity, fragment, currentPath);
            firstFragment = false;
        } else {
            transaction.replace(R.id.fileviewonactivity, fragment, currentPath);
            transaction.addToBackStack(currentPath);
        }
        transaction.commit();
    }

    public void createDirectoryNavigationFragment() {
        DirectoryNavigationFragment fragment = new DirectoryNavigationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_directory_navigation, fragment);
        transaction.commit();
    }

    @Override
    public void onArticleSelected(String currentPath) {
        createFileViewFragment(currentPath);
        this.currentPath = currentPath;
    }
}
