package com.sasfmlzr.filemanager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;
import com.sasfmlzr.filemanager.api.model.FileModel;
import com.sasfmlzr.filemanager.api.other.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected final int READ_EXTERNAL_STORAGE = 0;

    private ListView mFileList;
    private FileExploreAdapter mFileExploreAdapter;
    private final FileOperation mFileOperation = new FileOperation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                FileModel fileModels = (FileModel)parent.getItemAtPosition(position);
                mFileExploreAdapter = mFileOperation.loadPath(fileModels.getPathFile(), getApplicationContext());
                mFileList.setAdapter(mFileExploreAdapter);
            }
        };
        mFileList.setOnItemClickListener(itemListener);
    }

    private void init() {
        mFileList = findViewById(R.id.mFileList);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
            //System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE
            );
        }
        String path = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        mFileExploreAdapter = mFileOperation.loadPath(path, this);
        mFileList.setAdapter(mFileExploreAdapter);
    }



    public void onClick(View view){
    }

}
