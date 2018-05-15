package com.mobile.urbanfix.urban_fix;

import android.util.Log;

public class Logger {

    private final static String TAG = "Script";

    public static void logI(String log) {
        Log.i(TAG, log);
    }

    public static void logE(String log) {
        Log.e(TAG, log);
    }
}
