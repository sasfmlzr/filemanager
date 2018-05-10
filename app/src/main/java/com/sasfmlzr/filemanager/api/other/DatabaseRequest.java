package com.sasfmlzr.filemanager.api.other;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class DatabaseRequest {
    private SQLiteDatabase db;

    DatabaseRequest(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE IF NOT EXISTS cacheSizeDirectory (path TEXT UNIQUE, size TEXT)");
    }

    public void insertCacheSizeDirectory(String path, String size) {
        db.execSQL("INSERT OR REPLACE INTO cacheSizeDirectory VALUES ('"+ path + "','" + size + "');");
    }

    public HashMap<String, String> selectCacheSizeDirectory() {
        HashMap<String, String> hashSizeDirectory = new HashMap<>();
        Cursor query = db.rawQuery("SELECT * FROM cacheSizeDirectory;", null);

        if (query.moveToFirst()) {
            do {
                String path = query.getString(0);
                String size = query.getString(1);
                hashSizeDirectory.put(path, size);
            }
            while (query.moveToNext());
        }
        query.close();
        return hashSizeDirectory;
    }
}
