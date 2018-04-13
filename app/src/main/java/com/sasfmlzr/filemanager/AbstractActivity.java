package com.sasfmlzr.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;
/** Abstract activity need for easily reading code MainActivity*/
public abstract class AbstractActivity extends AppCompatActivity {
    protected final int READ_EXTERNAL_STORAGE = 0;
    private ListView mFileList;
    private FileExploreAdapter mFileExploreAdapter;
    private String mCurrentPath;
    private final FileOperation mFileOperation = new FileOperation();
    public void beforeInit(int countActivity){
        if (countActivity==0){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE
                );
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String path = Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath();
                    mFileExploreAdapter = getmFileOperation().loadPath(path, this);
                    mFileList.setAdapter(mFileExploreAdapter);

                } else {
                    Toast.makeText(this, "Please allow permissions",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public ListView getmFileList() {
        return mFileList;
    }

    public void setmFileList(ListView mFileList) {
        this.mFileList = mFileList;
    }

    public FileExploreAdapter getmFileExploreAdapter() {
        return mFileExploreAdapter;
    }

    public void setmFileExploreAdapter(FileExploreAdapter mFileExploreAdapter) {
        this.mFileExploreAdapter = mFileExploreAdapter;
    }

    public String getmCurrentPath() {
        return mCurrentPath;
    }

    public void setmCurrentPath(String mCurrentPath) {
        this.mCurrentPath = mCurrentPath;
    }

    public FileOperation getmFileOperation() {
        return mFileOperation;
    }
}
