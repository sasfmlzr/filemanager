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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    protected static final String STRING_CURRENT_PATH = "currentPath";
    protected static final String DEFAULT_PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();
    protected static final int READ_EXTERNAL_STORAGE = 0;

    private ListView fileListView;
    private String currentPath;

    public void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(STRING_CURRENT_PATH, currentPath);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fileListView = findViewById(R.id.fileList);
        Intent intent = getIntent();
        if (intent.hasExtra(STRING_CURRENT_PATH)) {
            setAdapter(intent.getStringExtra(STRING_CURRENT_PATH));
        } else {
            setAdapter(DEFAULT_PATH);
        }
        requestPermissions();
        fileListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter(String path) {
        List<File> fileList = FileOperation.loadPath(path, this);
        FileExploreAdapter fileExploreAdapter = new FileExploreAdapter(this,
                R.layout.current_item_file, fileList);
        fileListView.setAdapter(fileExploreAdapter);
    }

    protected void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.permission_is_not_granted,
                    Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAdapter(DEFAULT_PATH);
            } else {
                Toast.makeText(this, this.getString(R.string.allow_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        File fileModels = (File) parent.getItemAtPosition(position);
        currentPath = fileModels.getAbsolutePath();
        File file = new File(currentPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                start(this);
            } else if (file.isFile()) {
                FileOperation.openFile(getApplicationContext(), file);
            }
        }
    }
}
