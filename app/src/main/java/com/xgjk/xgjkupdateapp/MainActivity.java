package com.xgjk.xgjkupdateapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xgjk.xgjkupdateapp.otherupdate.DialogUtils;
import com.xgjk.xgjkupdateapp.otherupdate.UpdateDialog;

public class MainActivity extends AppCompatActivity {
    boolean isForceUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void updateClick(View view) {
        initView();
//        initUpdata();
    }

    private void initUpdata() {
        //1、弹出对话框
        //2、强制更新、非强制更新
        //3、更新页面更新、通知栏进行更新
        //4、安装
        //5、取消之后定时提醒
        if (isForceUpdate) {

        } else {

        }
    }

    private void initView() {
        DialogUtils.showUpdateDialog(MainActivity.this, "update", new UpdateDialog.Callback() {
            @Override
            public void ensure() {
                Toast.makeText(MainActivity.this, "update", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

}
