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
    private final String pathMain = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();
    private ListView mFileList;
    private FileExploreAdapter mFileExploreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                FileModel fileModels = (FileModel)parent.getItemAtPosition(position);
                loadPath(fileModels.getPathFile(), getApplicationContext());
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
        loadPath(path, this);
    }

    private void loadPath(String path, Context context){
        if((new File(path).isDirectory())){
            List<FileModel> fileModel = new ArrayList<>();
            if (!path.equals(pathMain)){
                fileModel.add(0, new FileModel("...", "", pathMain, 0));
            }
            FileOperation fileOperation = new FileOperation();
            fileModel.addAll(fileOperation.fileModelLoad(path, context));
            Settings.updatePreferences(context);
            mFileExploreAdapter = new FileExploreAdapter(context, R.layout.current_item_file, fileModel);
            mFileList.setAdapter(mFileExploreAdapter);
        }
    }

    public void onClick(View view){
        FileOperation fileOperation = new FileOperation();
        System.out.print("asd");
        List<String> list = new ArrayList<>();
        String path = "";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "emulated";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "storage";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/storage";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/storage/emulated";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/storage/emulated/0";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/storage/emulated/0/";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/storage/self/";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/storage/sdcard";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        path = "/sdcard";
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        /*path = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        list = fileOperation.listFiles(path, this);
        System.out.println(path + " " + list.size());
        ArrayList<String>  ddd = new ArrayList<>();
        ddd=sss();
        for(String s : ddd){
            System.out.println(s);
        }*/


    }

}
