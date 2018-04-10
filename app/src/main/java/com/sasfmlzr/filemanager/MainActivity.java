package com.sasfmlzr.filemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.model.FileModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        List<FileModel> fileModel = new ArrayList<>();
        fileModel = fileModelLoad();

        mFileExploreAdapter = new FileExploreAdapter(this, R.layout.current_item_file, fileModel);
        mFileList.setAdapter(mFileExploreAdapter);
    }


    private List<FileModel> fileModelLoad (){

        List<FileModel> mfileModel = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mfileModel.add(new FileModel("asds","asdas","sadasa", R.drawable.file));
        }
        return mfileModel;
    }



    public void onClick(View view){
        System.out.print("asd");
    }

}
