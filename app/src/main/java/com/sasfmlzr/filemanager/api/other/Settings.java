package com.sasfmlzr.filemanager.api.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.stericson.RootTools.RootTools;

public class Settings {

    private Settings() {}

    private static SharedPreferences mPrefs;

    public static void updatePreferences(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        rootAccess();
    }

    public static boolean showHiddenFiles() {
        return mPrefs.getBoolean("displayhiddenfiles", true);
    }

    public static boolean rootAccess() {
        return mPrefs.getBoolean("enablerootaccess", false) && RootTools.isAccessGiven();
    }


}
