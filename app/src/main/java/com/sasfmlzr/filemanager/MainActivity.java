package com.sasfmlzr.filemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.sasfmlzr.filemanager.api.fragment.FragmentFileView;

public class MainActivity extends AppCompatActivity {
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
            final FragmentFileView fragment = FragmentFileView.newInstance("My Content");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentfileview, fragment)
                    .commit();
        }
/*
        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            //final FragmentFileView fragment = new FragmentFileView();
            final FragmentFileView fragment = FragmentFileView.newInstance("My Content");
            ft.add(R.id.fragmentfileview, fragment);
            ft.commit();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
