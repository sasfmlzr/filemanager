package com.sasfmlzr.filemanager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sasfmlzr.filemanager.api.fragment.FragmentFileView;

public class MainActivity extends AppCompatActivity implements FragmentFileView.OnArticleSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        createFragment(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onClickBackHome();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickBackHome() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            this.finish();
        }
    }

    public void createFragment(String currentPath) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentFileView fragment;
        if (fragmentManager.getBackStackEntryCount() == 0) {
            fragment = new FragmentFileView();
            fragmentManager.beginTransaction()
                    .add(R.id.fileviewonactivity, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragment = FragmentFileView.newInstance(currentPath);
            fragmentManager.beginTransaction()
                    .replace(R.id.fileviewonactivity, fragment)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public void onArticleSelected(String currentPath) {
        createFragment(currentPath);
    }
}
