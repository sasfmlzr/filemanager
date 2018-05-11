package com.sasfmlzr.filemanager.api.provider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.view.View;

import com.sasfmlzr.filemanager.api.other.data.DataCache;

public class CacheProviderOperation {

    public static void addToContentProvider(View view, String path, String size) {
        ContentResolver contentResolver = view.getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(DataCache.Columns.PATH, path);
        values.put(DataCache.Columns.SIZE, size);
        Uri uri = contentResolver.insert(DataCache.CONTENT_URI, values);
    }


}
