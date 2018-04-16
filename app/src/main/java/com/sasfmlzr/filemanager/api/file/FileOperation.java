package com.sasfmlzr.filemanager.api.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;
import com.sasfmlzr.filemanager.BuildConfig;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.model.FileModel;
import com.sasfmlzr.filemanager.api.other.TypeFiles;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FileOperation {
    private final String pathMain = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();
    private List<String> listFiles(String path, Context context){
        ArrayList<String> listFiles = new ArrayList<>();
        final File file = new File(path);
        if (!listFiles.isEmpty()) {
            listFiles.clear();
        }
        if (file.exists() && file.canRead()) {
            String[] list = file.list();
            for (String aList : list) {
                listFiles.add(path + "/" + aList);
            }
        } else {
            Toast.makeText(context, context.getString(R.string.cant_read_folder), Toast.LENGTH_SHORT).show();
        }
        return listFiles;
    }

    private List<FileModel> fileModelLoad(String path, Context context){
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault());
        List<FileModel> filesList = new ArrayList<>();
        List<FileModel> pathsList = new ArrayList<>();
        FileOperation fileOperation = new FileOperation();
        List<String> listFiles;
        listFiles = fileOperation.listFiles(path, context);
        for (String pathToFile:listFiles) {
            File file = new File(pathToFile);
            if(file.canRead() && file.exists()){
                if(file.isFile()){
                    filesList.add(new FileModel(file.getName(),df.format(file.lastModified()), pathToFile, R.drawable.file));
                }else{
                    pathsList.add(new FileModel(file.getName(),df.format(file.lastModified()), pathToFile, R.drawable.path));
                }
            }
        }
        Collections.sort(pathsList,  new Comparator<FileModel>(){
            @Override
            public int compare(FileModel lhs, FileModel rhs) {
                return lhs.getNameFile().compareTo(rhs.getNameFile());
            }
        });
        Collections.sort(filesList,  new Comparator<FileModel>(){
            @Override
            public int compare(FileModel lhs, FileModel rhs) {
                return lhs.getNameFile().compareTo(rhs.getNameFile());
            }
        });
        List<FileModel> fileModelList = new ArrayList<>(pathsList);
        fileModelList.addAll(filesList);
        return fileModelList;
    }

    public FileExploreAdapter loadPath(String path, Context context) {
        FileExploreAdapter fileExploreAdapter;
        File file = new File(path);
        if((file.isDirectory())) {
            List<FileModel> fileModel = new ArrayList<>();
            if (!path.equals(pathMain)) {
                fileModel.add(0, new FileModel("...", "", pathMain, 0));
            }
            FileOperation fileOperation = new FileOperation();
            fileModel.addAll(fileOperation.fileModelLoad(path, context));
            fileExploreAdapter = new FileExploreAdapter(context, R.layout.current_item_file, fileModel);
            return fileExploreAdapter;
        } else {
            return null;
        }
    }

    public void openFile(final Context context, final File target) {
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
