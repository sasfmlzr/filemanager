package com.sasfmlzr.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sasfmlzr.filemanager.api.model.FileModel;

import java.io.File;

import static com.sasfmlzr.filemanager.api.other.Param.sCountActivity;
/** View activity*/
public class MainActivity extends AbstractActivity {


    private ListView mFileList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFileList = findViewById(R.id.mFileList);
        setmFileList(mFileList);
        Intent intent = getIntent();

        if(intent.hasExtra("mCurrentPath")){
        String currentPath = intent.getStringExtra("mCurrentPath");
        setmFileExploreAdapter(getmFileOperation().loadPath(currentPath, getApplicationContext()));
        mFileList.setAdapter(getmFileExploreAdapter());
        }
        beforeInit(sCountActivity);

        Toast.makeText(this, "Main Activity",Toast.LENGTH_SHORT).show();

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                FileModel fileModels = (FileModel)parent.getItemAtPosition(position);
                setmCurrentPath(fileModels.getPathFile());
                File file = new File(fileModels.getPathFile());
                if(file.isDirectory()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("mCurrentPath", getmCurrentPath());
                    sCountActivity++;
                    startActivity(intent);
                }
            }
        };
        mFileList.setOnItemClickListener(itemListener);
    }

    public void onClick(View view){
    }

}
