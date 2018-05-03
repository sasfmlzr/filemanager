package com.sasfmlzr.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sasfmlzr.filemanager.api.adapter.PagerFileListAdapter;
import com.sasfmlzr.filemanager.api.fragment.EmptyPagerFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    protected static final File DEFAULT_PATH = Environment
            .getExternalStorageDirectory();

    private ViewPager viewPager;

    public void addTabs(ViewPager viewPager) {
        PagerFileListAdapter adapter = new PagerFileListAdapter(getSupportFragmentManager(), getApplicationContext());
        adapter.addFragment(EmptyPagerFragment.newInstance(DEFAULT_PATH));
        adapter.addFragment(EmptyPagerFragment.newInstance(new File(DEFAULT_PATH, "Android")));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.pager);
        if (savedInstanceState != null) {
            PagerFileListAdapter adapter = new PagerFileListAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(adapter);
        } else {
            addTabs(viewPager);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
