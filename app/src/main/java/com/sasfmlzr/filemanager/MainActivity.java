package com.sasfmlzr.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mFileList = findViewById(R.id.mFileList);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE
            );
        }

        List<FileModel> fileModel = new ArrayList<>();
        fileModel = fileModelLoad();
        Settings.updatePreferences(this);
        mFileExploreAdapter = new FileExploreAdapter(this, R.layout.current_item_file, fileModel);
        mFileList.setAdapter(mFileExploreAdapter);
    }


    private List<FileModel> fileModelLoad (){

        List<FileModel> mfileModel = new ArrayList<>();
        String path = "storage/emulated/0";
        FileOperation fileOperation = new FileOperation();
        List<String> listFiles = new ArrayList<>();
        listFiles = fileOperation.listFiles(path, this);

        for (String string:listFiles) {
            mfileModel.add(new FileModel(string,"asdas","sadasa", R.drawable.file));
        }
        return mfileModel;
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

    public ArrayList<String> sss (){
        String path = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
      //  String path = "/storage/";
        File f = new File(path);
        ArrayList<String> fileList = new ArrayList<>();
        File[] files = f.listFiles();
        fileList.clear();
        for (File file : files){
            fileList.add(file.getPath());
        }
        return fileList;
    }
}
