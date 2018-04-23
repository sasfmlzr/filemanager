package com.sasfmlzr.filemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.sasfmlzr.filemanager.api.fragment.FragmentFileView;

public class MainActivity extends AppCompatActivity implements FragmentFileView.OnArticleSelectedListener {
    private FragmentFileView fragment;

    public void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            fragment = FragmentFileView.newInstance(Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fileviewonactivity, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleSelected(String currentPath) {
        fragment = FragmentFileView.newInstance(currentPath);
        getSupportFragmentManager().beginTransaction().replace(R.id.fileviewonactivity, fragment)
                .addToBackStack(null).commit();
    }

    
}
