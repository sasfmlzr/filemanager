package com.sasfmlzr.filemanager.api.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;
import com.sasfmlzr.filemanager.BuildConfig;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.other.TypeFiles;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileOperation {
    private List<File> listFiles(String path, Context context){
        ArrayList<File> listFiles = new ArrayList<>();
        final File file = new File(path);
        if (!listFiles.isEmpty()) {
            listFiles.clear();
        }
        if (file.exists() && file.canRead()) {
            listFiles.addAll(Arrays.asList(file.listFiles()));
        } else {
            Toast.makeText(context, context.getString(R.string.cant_read_folder), Toast.LENGTH_SHORT).show();
        }
        return listFiles;
    }

    private List<File> fileModelLoad(String path, Context context){
        List<File> filesList = new ArrayList<>();
        List<File> pathsList = new ArrayList<>();
        List<File> listFiles;
        FileOperation fileOperation = new FileOperation();
        listFiles = fileOperation.listFiles(path, context);
        for (File pathToFile:listFiles) {
            File file = new File(pathToFile.getAbsolutePath());
            if(file.canRead() && file.exists()){
                if(file.isFile()){
                    filesList.add(file);
                }else{
                    pathsList.add(file);
                }
            }
        }
        Collections.sort(pathsList,  new Comparator<File>(){
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        Collections.sort(filesList,  new Comparator<File>(){
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        List<File> fileModelList = new ArrayList<>(pathsList);
        fileModelList.addAll(filesList);
        return fileModelList;
    }

    public static ArrayList<File> loadPath(String path, Context context) {
        File file = new File(path);
        if (!file.isDirectory()){
            return null;
        }
        FileOperation fileOperation = new FileOperation();
            return new ArrayList<>(fileOperation.fileModelLoad(path, context));
    }

    public static void openFile(final Context context, final File target) {
        final String fileType = TypeFiles.getFileType(target);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, context.getString(R.string.cantopenfile)
                                    + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
