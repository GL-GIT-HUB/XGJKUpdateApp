package com.xgjk.xgjkupdateapp.otherupdate;

import android.content.Context;

/**
 * Created by XG on 2017/6/9.
 */

public class DialogUtils {

    public static void showUpdateDialog(Context context, String msg,UpdateDialog.Callback callback) {
        UpdateDialog dialog = new UpdateDialog(context,msg);
        dialog.setCallback(callback);
        dialog.show();
    }
}
