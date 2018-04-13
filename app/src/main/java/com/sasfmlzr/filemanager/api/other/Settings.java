package com.sasfmlzr.filemanager.api.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Settings {

    private Settings() {}

    private static SharedPreferences mPrefs;
    /*  update preferences current context*/
    public static void updatePreferences(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //rootAccess();
    }
    /** show hidden files*/
    public static boolean showHiddenFiles() {
        return true;
     //   return mPrefs.getBoolean("displayhiddenfiles", true);
    }
    /** access root for files*/
    public static boolean rootAccess() {
        return false;
        //return mPrefs.getBoolean("enablerootaccess", false) && RootTools.isAccessGiven();
    }


}
