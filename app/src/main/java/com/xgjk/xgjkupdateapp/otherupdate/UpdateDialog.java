package com.xgjk.xgjkupdateapp.otherupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xgjk.xgjkupdateapp.R;
import com.xgjk.xgjkupdateapp.baseupdate.FileDownLoadTask;

import java.io.File;

/**
 * Created by XG on 2017/6/9.
 */

public class UpdateDialog extends BaseDialog implements View.OnClickListener, Handler.Callback {

    private ProgressBar mProgressbar;
    private TextView mTips;
    private final Handler handler = new Handler(this);
    private Button mCancel;
    private Button mEnsure;
    private File mDownLoadFile;
    private Context context;

    public UpdateDialog(@NonNull Context context) {
        super(context);
    }

    public UpdateDialog(@NonNull Context context, String title) {
        super(context);
        this.context = context;
        initView(context, title);
    }

    private void initView(Context context, String title) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_update, null, false);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
        mEnsure = (Button) view.findViewById(R.id.btn_ensure);
        mProgressbar = (ProgressBar) view.findViewById(R.id.pb_progressBar);
        mTips = (TextView) view.findViewById(R.id.tv_tip);
        mEnsure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mLayoutParams.gravity = Gravity.CENTER;
        setContentView(view);
        initDownLoad();
    }

    private void initDownLoad() {
        int mLineVersion = 1;
        String downLoadpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/download";
        Log.i("tag",TextUtils.isEmpty(downLoadpath)+"-----tag");
        if (TextUtils.isEmpty(downLoadpath)) {
            //空间不足，请加载SD卡
            return;
        }
        File downDir = new File(downLoadpath);
        if (downDir.exists()) {
            downDir.mkdir();
        }

        mDownLoadFile = new File(downDir.getPath(), "name" + mLineVersion + ".apk");
        mTips.setText("准备开始下载");
        mProgressbar.setProgress(0);
        mProgressbar.setMax(100);

        new FileDownLoadTask(mDownLoadFile.getPath(), "http://172.16.25.209/name.apk", handler).start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ensure://确认
//                if (mCallback != null) {
//                    mCallback.ensure();
//                }
                installApp();

                break;
            case R.id.btn_cancel://取消
                dismiss();
                break;
        }
    }

    private void installApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mDownLoadFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private Callback mCallback;

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case FileDownLoadTask.ProgressCallBack.DOWNLOAD_START:
                mProgressbar.setVisibility(ProgressBar.VISIBLE);
                break;
            case FileDownLoadTask.ProgressCallBack.DOWNLOAD_DOING:
                mProgressbar.setProgress(Integer.parseInt((String) msg.obj));
                mTips.setText(String.format("已经下载：%1$s%%", (String) msg.obj));
                break;
            case FileDownLoadTask.ProgressCallBack.DOWNLOAD_END:
                mProgressbar.setProgress(100);
                // 下载成功后保存当前的版本号与文件的路径
                // 下载完成
                mTips.setText("下载完成");
                mEnsure.setText("安装");
                mEnsure.setVisibility(View.VISIBLE);
                break;
            case FileDownLoadTask.ProgressCallBack.DOWNLOAD_ERROR:
                mTips.setText("下载失败");
                mEnsure.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    public interface Callback {
        void ensure();
    }
}
