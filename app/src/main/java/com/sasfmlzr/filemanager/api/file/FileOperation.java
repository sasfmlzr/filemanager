package com.sasfmlzr.filemanager.api.file;

import android.content.Context;
import android.widget.Toast;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.other.RootCommands;
import com.sasfmlzr.filemanager.api.other.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOperation {

    public List<String> listFiles(String path, Context context){
        ArrayList<String> listFiles = new ArrayList<>();
        final boolean showhidden = Settings.showHiddenFiles();
        final File file = new File(path);

        if (!listFiles.isEmpty())
            listFiles.clear();
        //if (file.exists()) {
        if (file.exists() && file.canRead()) {
            String[] list = file.list();

            // add files/folder to ArrayList depending on hidden status
            for (String aList : list) {
                if (!showhidden) {
                    if (aList.charAt(0) != '.')
                        listFiles.add(path + "/" + aList);
                } else {
                    listFiles.add(path + "/" + aList);
                }
            }
        } else if (Settings.rootAccess()) {
            listFiles = RootCommands.listFiles(file.getAbsolutePath(), showhidden);
        } else {
            Toast.makeText(context, context.getString(R.string.cantreadfolder), Toast.LENGTH_SHORT).show();
        }
        return listFiles;
    }
}
