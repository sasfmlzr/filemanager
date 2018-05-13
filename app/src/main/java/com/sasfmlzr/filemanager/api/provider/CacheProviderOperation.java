package com.sasfmlzr.filemanager.api.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.sasfmlzr.filemanager.api.other.data.DataCache;

import java.util.HashMap;

public class CacheProviderOperation {
    private static final String TAG = "CacheProviderOperation";

    public static void addToContentProvider(ContentResolver contentResolver, String path, String size) {
        ContentValues values = new ContentValues();
        values.put(DataCache.Columns.PATH, path);
        values.put(DataCache.Columns.SIZE, size);
        contentResolver.insert(DataCache.CONTENT_URI, values);
    }

    public static HashMap<String, String> selectAllToContentProvider(ContentResolver contentResolver) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] projection = {
                DataCache.Columns.PATH,
                DataCache.Columns.SIZE,
        };
        Cursor cursor = contentResolver.query(DataCache.CONTENT_URI,
                projection,
                null,
                null,
                DataCache.Columns.PATH);
        if (cursor != null) {
            Log.d(TAG, "count: " + cursor.getCount());
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i = i + 2) {
                    hashMap.put(cursor.getString(i), cursor.getString(i + 1));
                    //Log.d(TAG, cursor.getColumnName(i) + " : " + cursor.getString(i));
                }
                //Log.d(TAG, "=========================");
            }
            cursor.close();
        }
        return hashMap;
    }


}
