/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.grst.hotelapp.util;

import android.content.Context;
import android.util.Log;

import com.grst.hotelapp.ui.HomeFragmentPage;

import java.io.File;

public class FileUtil {
    private static String TAG = FileUtil.class.getCanonicalName();
    public static File getSaveFile(Context context) {

        File file = new File(context.getFilesDir(), "pic.jpg");

        Log.i(TAG, "file=============: " + file);
        return file;
    }
}
