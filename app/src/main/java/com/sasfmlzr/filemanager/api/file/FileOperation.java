package com.sasfmlzr.filemanager.api.file;

import android.content.Context;

import com.sasfmlzr.filemanager.api.FileList;
import com.sasfmlzr.filemanager.api.model.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOperation {

    public List<FileModel> readPathFiles(String path, Context context){
        List<FileModel> fileModelList = new ArrayList<>();
        final File file = new File(path);
        if (!fileModelList.isEmpty()){
            fileModelList.clear();
        }

        if (file.exists() && file.canRead()) {
            String[] list = file.list();
        }




        return fileModelList;
    }
}
