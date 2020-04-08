package com.example.login_application;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;


/**
 * Created by Mary on 2016-02-16.
 */
public class HProgressDialog extends ProgressDialog {
    static Context mContext;
    private static ProgressDialog mProgress;

    public HProgressDialog(Context context) {
        super(context);
        this.mContext = context;
        mProgress = new ProgressDialog(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void showProgressDialog(String title, String msg) {
        mProgress.setTitle(title);
        mProgress.setMessage(msg);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.show();
        mProgress.setContentView(R.layout.custom_progress);
    }

    public void showProgressDialog() {
        DevLog.d("progress ", "     progress start " + mContext.getClass().getSimpleName());
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.show();
        mProgress.setContentView(R.layout.custom_progress);
    }

    public static void stopProgressDialog() {
        if (mProgress == null) {
            return;
        }

        if (mProgress.isShowing()) {
            DevLog.d("progress ", "    progress stop " + mContext.getClass().getSimpleName());
            mProgress.dismiss();
        }

        mProgress = null;
    }
}
