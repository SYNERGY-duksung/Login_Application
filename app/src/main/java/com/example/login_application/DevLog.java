package com.example.login_application;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class DevLog {
    private final static String TAG = "duksung_in";

    private final static boolean LOG_ENABLE = true;
    private final static boolean LOG_FILE_ENABLE = false;

    public static BufferedWriter out;

    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.i(tag, msg);
        }

        if (LOG_FILE_ENABLE) {
            fileOnDevice(tag + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.e(tag, msg);
        }

        if (LOG_FILE_ENABLE) {
            fileOnDevice(tag + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.d(tag, msg);
        }

        if (LOG_FILE_ENABLE) {
            fileOnDevice(tag + msg);
        }
    }

    public static void fileOnDevice(String msg) {
        File Root = Environment.getExternalStorageDirectory();
        if (Root.canWrite()) {
            File LogFile = new File(Root, "duksungin.log");
            FileWriter LogWriter;

            try {
                LogWriter = new FileWriter(LogFile, true);
                out = new BufferedWriter(LogWriter);
                Date date = new Date();
                out.write(date.toString() + " : " + msg + "\n");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }


}
