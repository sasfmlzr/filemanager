package com.sasfmlzr.filemanager.api.other;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.sasfmlzr.filemanager.R;

public class Permissions {
    private static final int READ_EXTERNAL_STORAGE = 0;

    public static void requestReadPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity.getLayoutInflater().getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity.getLayoutInflater().getContext(), R.string.permission_is_not_granted,
                    Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        }
    }
}
