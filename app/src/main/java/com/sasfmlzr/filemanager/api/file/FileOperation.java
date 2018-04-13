package com.sasfmlzr.filemanager.api.file;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.model.FileModel;
import com.sasfmlzr.filemanager.api.other.RootCommands;
import com.sasfmlzr.filemanager.api.other.Settings;
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
        final boolean showHidden = Settings.showHiddenFiles();
        final File file = new File(path);
        if (!listFiles.isEmpty()) {
            listFiles.clear();
        }
        if (file.exists() && file.canRead()) {
            String[] list = file.list();
            for (String aList : list) {
                if (!showHidden) {
                    if (aList.charAt(0) != '.')
                        listFiles.add(path + "/" + aList);
                } else {
                    listFiles.add(path + "/" + aList);
                }
            }
        } else if (Settings.rootAccess()) {
            listFiles = RootCommands.listFiles(file.getAbsolutePath(), showHidden);
        } else {
            Toast.makeText(context, context.getString(R.string.cant_read_folder), Toast.LENGTH_SHORT).show();
        }
        return listFiles;
    }

    private List<FileModel> fileModelLoad(String path, Context context){
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault());
        List<FileModel> mFilesList = new ArrayList<>();
        List<FileModel> mPathsList = new ArrayList<>();
        FileOperation fileOperation = new FileOperation();
        List<String> listFiles;
        listFiles = fileOperation.listFiles(path, context);
        for (String pathToFile:listFiles) {
            File file = new File(pathToFile);
            if(file.canRead() && file.exists()){
                if(file.isFile()){
                    mFilesList.add(new FileModel(file.getName(),df.format(file.lastModified()), pathToFile, R.drawable.file));
                }else{
                    mPathsList.add(new FileModel(file.getName(),df.format(file.lastModified()), pathToFile, R.drawable.path));
                }
            }
        }
        Collections.sort(mPathsList,  new Comparator<FileModel>(){
            @Override
            public int compare(FileModel lhs, FileModel rhs) {
                return lhs.getNameFile().compareTo(rhs.getNameFile());
            }
        });
        Collections.sort(mFilesList,  new Comparator<FileModel>(){
            @Override
            public int compare(FileModel lhs, FileModel rhs) {
                return lhs.getNameFile().compareTo(rhs.getNameFile());
            }
        });
        List<FileModel> mFileModel = new ArrayList<>(mPathsList);
        mFileModel.addAll(mFilesList);
        return mFileModel;
    }

    public FileExploreAdapter loadPath(String path, Context context) {
        FileExploreAdapter mFileExploreAdapter;
        if((new File(path).isDirectory())) {
            List<FileModel> fileModel = new ArrayList<>();
            if (!path.equals(pathMain)) {
                fileModel.add(0, new FileModel("...", "", pathMain, 0));
            }
            FileOperation fileOperation = new FileOperation();
            fileModel.addAll(fileOperation.fileModelLoad(path, context));
            Settings.updatePreferences(context);
            mFileExploreAdapter = new FileExploreAdapter(context, R.layout.current_item_file, fileModel);
            return mFileExploreAdapter;
        } else {
            return null;
        }
    }
}
