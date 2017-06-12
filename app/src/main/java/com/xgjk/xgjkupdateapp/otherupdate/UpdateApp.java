package com.xgjk.xgjkupdateapp.otherupdate;

import android.content.Context;

import com.xgjk.xgjkupdateapp.MainActivity;

/**
 * Created by XG on 2017/6/9.
 */

public class UpdateApp {

    private static void checkVersionApp(Context context, int cruentVersion, int lineVersion, UpdateAppBean updateAppBean) {
        if (cruentVersion>lineVersion) {

            if (isMainPage(context)) {
                //1.未强制更新、用户没有更新，可以再次提醒一次更新版本
            }

            if (updateAppBean.isForceUpdate()) {//强制更新
                    //弹出对话框
                    
            }else {//非强制更新

            }
        }else {

        }
    }


    // 是否是主页
    private static boolean isMainPage(Context context) {
        return context.getClass().getName().equals(MainActivity.class.getName());
    }
}
