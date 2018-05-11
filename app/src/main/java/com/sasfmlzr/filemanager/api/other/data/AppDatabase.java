package com.sasfmlzr.filemanager.api.other.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cacheSizeDirectory.db";
    public static final int DATABASE_VERSION = 1;

    private static AppDatabase instance = null;

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DataCache.TABLE_NAME + "(" +
                DataCache.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                DataCache.Columns.PATH + " TEXT UNIQUE NOT NULL, " +
                DataCache.Columns.SIZE + " TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
