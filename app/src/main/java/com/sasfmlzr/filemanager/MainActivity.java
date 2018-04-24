package com.sasfmlzr.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sasfmlzr.filemanager.api.fragment.FragmentFileView;

public class MainActivity extends AppCompatActivity implements FragmentFileView.OnArticleSelectedListener {
    protected static final String DEFAULT_PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();
    private String currentPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        createFragment(DEFAULT_PATH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            callBackHome();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void callBackHome() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(DEFAULT_PATH, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            finish();
        }
    }



    public void createFragment(String currentPath) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentFileView fragment;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = FragmentFileView.newInstance(currentPath);
        if (fragmentManager.getBackStackEntryCount() == 0) {
            transaction.add(R.id.fileviewonactivity, fragment);
        } else {
            transaction.replace(R.id.fileviewonactivity, fragment);
        }
        transaction.addToBackStack(currentPath).commit();
    }

    @Override
    public void onArticleSelected(String currentPath) {
        createFragment(currentPath);
        this.currentPath=currentPath;
    }
}
