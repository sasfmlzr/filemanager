package com.sasfmlzr.filemanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import java.io.File;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    protected static final String STRING_CURRENT_PATH = "currentPath";
    protected static final int READ_EXTERNAL_STORAGE = 0;
    private static boolean firstLaunchActivity = true;
    private ListView fileList;
    private FileExploreAdapter fileExploreAdapter;
    private static String currentPath;
    private final FileOperation fileOperation = new FileOperation();
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(STRING_CURRENT_PATH, currentPath);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileList = findViewById(R.id.fileList);
        Intent intent = getIntent();
        if (intent.hasExtra(STRING_CURRENT_PATH)) {
            String currentPath = intent.getStringExtra(STRING_CURRENT_PATH);
            fileExploreAdapter = fileOperation.loadPath(currentPath, getApplicationContext());
            fileList.setAdapter(fileExploreAdapter);
        }
        init(firstLaunchActivity);
        fileList.setOnItemClickListener(this);
    }

    public void onClick(View view){}

    protected void init(boolean countActivity) {
        if (countActivity) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permission_is_not_granted,
                        Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE);
            }
            setAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (requestCode==READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAdapter();
            } else {
                Toast.makeText(this, this.getString(R.string.allow_permission),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAdapter(){
        String path = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        fileExploreAdapter = fileOperation.loadPath(path, this);
        fileList.setAdapter(fileExploreAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        FileModel fileModels = (FileModel)parent.getItemAtPosition(position);
        currentPath=fileModels.getPathFile();
        File file = new File(fileModels.getPathFile());
        if (file.isDirectory()) {
            firstLaunchActivity=false;
            start(this);
        }
    }
}
