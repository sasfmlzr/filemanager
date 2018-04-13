package com.sasfmlzr.filemanager;

import android.Manifest;
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
import static com.sasfmlzr.filemanager.api.other.Param.countActivity;

public class MainActivity extends AppCompatActivity {
    protected static final int READ_EXTERNAL_STORAGE = 0;
    private ListView fileList;
    private FileExploreAdapter fileExploreAdapter;
    private String currentPath;
    private final FileOperation fileOperation = new FileOperation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileList = findViewById(R.id.fileList);
        Intent intent = getIntent();
        if (intent.hasExtra("mCurrentPath")) {
            String currentPath = intent.getStringExtra("mCurrentPath");
            fileExploreAdapter = fileOperation.loadPath(currentPath, getApplicationContext());
            fileList.setAdapter(fileExploreAdapter);
        }
        init(countActivity);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                FileModel fileModels = (FileModel)parent.getItemAtPosition(position);
                currentPath=fileModels.getPathFile();
                File file = new File(fileModels.getPathFile());
                if (file.isDirectory()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("mCurrentPath", currentPath);
                    countActivity++;
                    startActivity(intent);
                }
            }
        };
        fileList.setOnItemClickListener(itemListener);
    }
    public void onClick(View view){}
    protected void init(int countActivity) {
        if (countActivity==0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permission_is_not_granted,
                        Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE);
            }
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

}
