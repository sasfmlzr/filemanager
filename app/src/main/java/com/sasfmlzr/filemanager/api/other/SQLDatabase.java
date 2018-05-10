package com.sasfmlzr.filemanager.api.other;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class SQLDatabase {
    private SQLiteDatabase db;

    public DatabaseRequest databaseRequest() {
        return new DatabaseRequest(db);
    }

    // --------CONNECTION TO DATABASE--------
    public void connectDatabase(Context context) {
        this.db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
    }

    public void closeConnect() {
        db.close();
    }
}
