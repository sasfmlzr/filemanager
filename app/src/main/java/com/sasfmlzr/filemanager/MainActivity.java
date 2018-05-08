package com.sasfmlzr.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.sasfmlzr.filemanager.api.adapter.PagerFileListAdapter;
import com.sasfmlzr.filemanager.api.fragment.EmptyPagerFragment;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    protected static final File DEFAULT_PATH = Environment
            .getExternalStorageDirectory();
    private static final int PERMISSION_CODE_READ_EXTERNAL_STORAGE = 0;
    private ViewPager viewPager;

    public void addTabs(ViewPager viewPager) {
        PagerFileListAdapter adapter = new PagerFileListAdapter(getSupportFragmentManager(), getApplicationContext());
        adapter.addFragment(EmptyPagerFragment.newInstance(DEFAULT_PATH));
        adapter.addFragment(EmptyPagerFragment.newInstance(new File(DEFAULT_PATH, "Android")));
        viewPager.setAdapter(adapter);
    }

    public void loadViewPager(Bundle savedInstanceState) {
        viewPager = findViewById(R.id.pager);
        if (savedInstanceState != null) {
            PagerFileListAdapter adapter = new PagerFileListAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(adapter);
        } else {
            addTabs(viewPager);
        }
    }

    public void requestReadPermissions() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this)
                .getLayoutInflater()
                .getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //onPause();
        requestReadPermissions();

        loadViewPager(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            } else {
                Toast.makeText(this, this.getString(R.string.allow_permission), Toast.LENGTH_SHORT).show();
            }
        }
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
