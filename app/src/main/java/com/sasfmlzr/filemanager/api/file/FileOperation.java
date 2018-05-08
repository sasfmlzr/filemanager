package com.sasfmlzr.filemanager.api.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.sasfmlzr.filemanager.BuildConfig;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.other.TypeFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileOperation {
    private List<File> listFiles(File path, Context context) {
        ArrayList<File> listFiles = new ArrayList<>();
        if (!listFiles.isEmpty()) {
            listFiles.clear();
        }
        if (path.exists() && path.canRead()) {
            listFiles.addAll(Arrays.asList(path.listFiles()));
        } else {
            Toast.makeText(context, context.getString(R.string.cant_read_folder), Toast.LENGTH_SHORT).show();
        }
        return listFiles;
    }

    private List<File> fileModelLoad(File path, Context context) {
        List<File> filesList = new ArrayList<>();
        List<File> pathsList = new ArrayList<>();
        List<File> listFiles;
        FileOperation fileOperation = new FileOperation();
        listFiles = fileOperation.listFiles(path, context);
        for (File pathToFile : listFiles) {
            File file = new File(pathToFile.getAbsolutePath());
            if (file.canRead() && file.exists()) {
                if (file.isFile()) {
                    filesList.add(file);
                } else {
                    pathsList.add(file);
                }
            }
        }
        Collections.sort(pathsList, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));
        Collections.sort(filesList, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));
        List<File> fileModelList = new ArrayList<>(pathsList);
        fileModelList.addAll(filesList);
        return fileModelList;
    }

    public static ArrayList<File> loadPath(File path, Context context) {
        if (!path.isDirectory()) {
            return null;
        }
        FileOperation fileOperation = new FileOperation();
        return new ArrayList<>(fileOperation.fileModelLoad(path, context));
    }

    public static void openFile(final Context context, final File target) {
        final String fileType = TypeFiles.getFileType(target);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri contentUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".fileprovider", target);
        intent.setDataAndType(contentUri, fileType);
        if (fileType != null && !fileType.equals("*/*")) {
            intent.setDataAndType(contentUri, fileType);
            if (context.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                Toast.makeText(context, R.string.cantopenfile, Toast.LENGTH_SHORT).show();
                return;
            }
            context.startActivity(intent);
        }
    }

    public static List<File> getParentsFile(File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        while (file.getParent() != null) {
            file = new File(file.getParent());
            files.add(file);
        }
        Collections.reverse(files);
        return files;
    }
}
