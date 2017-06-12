package com.xgjk.xgjkupdateapp.baseupdate;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by XG on 2017/6/12.
 */

public class FileDownLoadTask extends Thread {
    private Handler handlerCallback = null;
    /**
     * 文件保存路径 完整的文件路径包括文件名
     */
    private final String savePath;
    /**
     * 文件下载路径
     */
    private final String downloadUrl;
    /**
     * 下载进度回调
     */
    private ProgressCallBack callback = null;
    /**
     * 是否停止下载， true 停止下载 false不停止
     */
    private volatile boolean isStop = false;


    public FileDownLoadTask(String savePath, String downloadUrl, ProgressCallBack callback) {
        super();
        this.savePath = savePath;
        this.downloadUrl = downloadUrl;
        this.callback = callback;
    }

    public FileDownLoadTask(String savePath, String downloadUrl, Handler callback) {
        super();
        this.savePath = savePath;
        this.downloadUrl = downloadUrl;
        this.handlerCallback = callback;
    }

    @Override
    public void run() {
        super.run();
        downLoadFile();
    }

    public void stopThead() {
        isStop = true;
    }

    @SuppressWarnings("resource")
    private void downLoadFile() {
        showMsg(ProgressCallBack.DOWNLOAD_START, "文件开始下载！");
        // 检查文件，检查下载路径
        File saveFile = new File(savePath);
        if (TextUtils.isEmpty(downloadUrl)) {
            showMsg(ProgressCallBack.DOWNLOAD_ERROR, "下载路径不正确！");
            return;
        }
        // 开始下载
        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            if (httpConnection.getResponseCode() == 404) {
                showMsg(ProgressCallBack.DOWNLOAD_ERROR, "网络文件不存在！");
                return;
            }
            is = httpConnection.getInputStream();

            // 获取要下载的文件的长度
            long totalSize = httpConnection.getContentLength();
            // 获取本地以及下载或者还没有下载的文件的长度
            if (totalSize != saveFile.length()) {
                long downloadedCount = saveFile.length();
                if (downloadedCount > totalSize) {
                    saveFile.delete();
                    downloadedCount = 0;
                }

                fos = new FileOutputStream(saveFile, true);
                // 跳过已经下载的部分
                long skipByte = 0;// 跳过的值
                long skip = downloadedCount;// 预期跳过
                // 用于解决更新是调用回调太频繁的问题（主要是notification不能频繁更新）
                int updateData = 0;

                while (skipByte != downloadedCount) {
                    if (isStop) {
                        return;
                    }
                    long realSkip = is.skip(skip);
                    skipByte = realSkip + skipByte;
                    skip = downloadedCount - skipByte;
                    int percent = (int) (skipByte * 100 / totalSize);// 读取本地的文件进度
                    if (percent > updateData) {
                        updateData = percent;
                        showMsg(ProgressCallBack.DOWNLOAD_DOING, percent + "");
                    }
                }
                // 用于解决更新是调用回调太频繁的问题（主要是notification不能频繁更新），对数据进行重置。
                updateData = 0;
                int readsize = 0;
                byte[] buffer = new byte[1024];
                while ((readsize = is.read(buffer)) > 0) {
                    if (isStop) {
                        return;
                    }
                    fos.write(buffer, 0, readsize);
                    downloadedCount += readsize;
                    int percent = (int) (downloadedCount * 100 / totalSize);
                    if (percent > 100) {
                        showMsg(ProgressCallBack.DOWNLOAD_ERROR, "文件下载错误！");
                        saveFile.delete();
                        return;
                    }
                    if (percent > updateData) {
                        updateData = percent;
                        showMsg(ProgressCallBack.DOWNLOAD_DOING, percent + "");
                    }
                }
            }
            showMsg(ProgressCallBack.DOWNLOAD_END, "文件下载完成");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            showMsg(ProgressCallBack.DOWNLOAD_ERROR, "文件下载失败！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showMsg(ProgressCallBack.DOWNLOAD_ERROR, "文件下载失败！");
        } catch (IOException e) {
            e.printStackTrace();
            showMsg(ProgressCallBack.DOWNLOAD_ERROR, "文件下载失败！");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpConnection != null) {
                httpConnection.disconnect();
            }

        }

    }

    /**
     * 显示下载过程中的信息
     *
     * @param code
     * @param msg
     */
    private void showMsg(int code, String msg) {
        if (callback != null) {
            callback.onDownload(code, msg);
        }
        if (handlerCallback != null) {
            Message message = Message.obtain();
            message.arg1 = code;
            message.obj = msg;
            handlerCallback.sendMessage(message);
        }
    }

    public interface ProgressCallBack {
        /**
         * 开始下载
         */
        public static final int DOWNLOAD_START = 0;
        /**
         * 正在下载
         */
        public static final int DOWNLOAD_DOING = 1;
        /**
         * 下载出错
         */
        public static final int DOWNLOAD_ERROR = 2;
        /**
         * 下载完成
         */
        public static final int DOWNLOAD_END = 3;

        /**
         * 下载信息
         *
         * @param
         */
        void onDownload(int code, Object msg);
    }
}
