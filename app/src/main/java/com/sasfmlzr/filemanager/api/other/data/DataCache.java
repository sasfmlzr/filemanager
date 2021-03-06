package com.sasfmlzr.filemanager.api.other.data;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataCache {
    public static final String AUTHORITY = "com.sasfmlzr.filemanager.provider";
    public static final String TABLE_NAME = "cacheSizeDirectory";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;
    private static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public interface Columns extends BaseColumns {
        String PATH = "Path";
        String SIZE = "Size";
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    public static Uri buildUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }

    public static long getId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
