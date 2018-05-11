package com.sasfmlzr.filemanager.api.provider;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sasfmlzr.filemanager.api.other.data.AppDatabase;
import com.sasfmlzr.filemanager.api.other.data.DataCache;

public class CacheSizeProvider extends ContentProvider {

    private static final UriMatcher MATCHER = buildUriMatcher();
    public static final int CACHEDIRECTORY = 100;
    public static final int CACHEDIRECTORY_ID = 101;

    private AppDatabase mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DataCache.AUTHORITY, DataCache.TABLE_NAME, CACHEDIRECTORY);
        matcher.addURI(DataCache.AUTHORITY, DataCache.TABLE_NAME + "/#", CACHEDIRECTORY_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (code) {
            case CACHEDIRECTORY:
                queryBuilder.setTables(DataCache.TABLE_NAME);
                break;
            case CACHEDIRECTORY_ID:
                queryBuilder.setTables(DataCache.TABLE_NAME);
                long taskId = DataCache.getId(uri);
                queryBuilder.appendWhere(DataCache.Columns._ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = MATCHER.match(uri);
        switch (match) {
            case CACHEDIRECTORY:
                return DataCache.CONTENT_TYPE;
            case CACHEDIRECTORY_ID:
                return DataCache.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = MATCHER.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;
        switch (match) {
            case CACHEDIRECTORY:
                db = mOpenHelper.getWritableDatabase();
                //recordId = db.execSQL();
                recordId = db.insertWithOnConflict(DataCache.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (recordId > 0) {
                    returnUri = DataCache.buildUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert: " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = MATCHER.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String selectionCriteria = selection;

        if (match != CACHEDIRECTORY && match != CACHEDIRECTORY_ID)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        if (match == CACHEDIRECTORY_ID) {
            long taskId = DataCache.getId(uri);
            selectionCriteria = DataCache.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.delete(DataCache.TABLE_NAME, selectionCriteria, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = MATCHER.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if (match != CACHEDIRECTORY && match != CACHEDIRECTORY_ID)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        if (match == CACHEDIRECTORY_ID) {
            long taskId = DataCache.getId(uri);
            selectionCriteria = DataCache.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.update(DataCache.TABLE_NAME, values, selectionCriteria, selectionArgs);
    }
}
