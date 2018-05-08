package com.sasfmlzr.filemanager.api.other;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Locale;

public class TypeFiles {

    public static String getFileType(File file) {
        if (file.isDirectory()) {
            return null;
        }
        String type = null;
        final String extension = getExtension(file.getName());
        if (!extension.isEmpty()) {
            final String extensionLowerCase = extension.toLowerCase(Locale.getDefault());
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extensionLowerCase);
        }
        if (type == null) {
            type = "*/*";
        }
        return type;
    }

    private static String getExtension(String name) {
        int index = name.lastIndexOf(".");
        return index == -1 ? "" : name.substring(index + 1, name.length());
    }
}
