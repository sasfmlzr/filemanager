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

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements FileViewFragment.OnFragmentInteractionListener,
        DirectoryNavigationFragment.OnFragmentInteractionListener {

    protected static final File DEFAULT_PATH = new File(Environment
            .getExternalStorageDirectory()
            .getAbsolutePath());

    private File currentFile;
    private boolean firstFragment = true;

    public void callBackStackFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    public void createFileViewFragment(File currentFile) {
        String absolutePath = currentFile.getAbsolutePath();
        FileViewFragment fragment = FileViewFragment.newInstance(currentFile);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (firstFragment) {
            transaction.add(R.id.fileviewonactivity, fragment, absolutePath);
            firstFragment = false;
        } else {
            transaction.replace(R.id.fileviewonactivity, fragment, absolutePath);
            transaction.addToBackStack(absolutePath);
        }
        transaction.commit();
    }

    public void createDirectoryNavigationFragment(File currentFile) {
        String absolutePath = currentFile.getAbsolutePath();
        DirectoryNavigationFragment fragment = DirectoryNavigationFragment.newInstance(currentFile);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (firstFragment) {
            transaction.add(R.id.directory_navigation, fragment);
        } else {
            transaction.replace(R.id.directory_navigation, fragment);
            transaction.addToBackStack(absolutePath);
        }
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        createFileViewFragment(DEFAULT_PATH);
        createDirectoryNavigationFragment(DEFAULT_PATH);
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

    @Override
    public void onDirectorySelected(File currentFile) {
        createFileViewFragment(this.currentFile);
        this.currentFile = currentFile;
        createDirectoryNavigationFragment(this.currentFile);
    }
}
