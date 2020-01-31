package com.autumn.framework.entertainment.OkHttpDownloader;

import android.os.Environment;
import android.util.Log;
import android.webkit.DownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by reeman on 2017/7/21.
 */

public class DownUtil implements Callback {

    OkHttpClient mOkHttpClient;
    DownListener downListener;
    String downPath;
    String downUrl;

    public DownUtil(DownListener downListener) {
        this.downListener = downListener;
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    public void downFile(String downUrl, String downPath) {
        this.downUrl = downUrl;
        this.downPath = downPath;
        try {
            downListener.downStart();
            String downDir = downPath.substring(0, downPath.lastIndexOf("/")).trim();
            String downName = downPath.substring(downPath.lastIndexOf("/") + 1, downPath.length()).trim();
            Log.i("down", "下载地址==" + downDir + "   下载的名字==" + downName);
            File destDir = new File(downDir);
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdirs();
            }
            Request request = new Request.Builder().url(downUrl).build();
            mOkHttpClient.newCall(request).enqueue(this);
        } catch (Exception e) {
            downListener.downFailed(e.toString());
            Log.d("down", "=================error==" + e.toString());
        }
    }

    public void cacleDown() {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
//        for (Call call : dispatcher.queuedCalls()) {
//            call.cancel();
//        }
        for (Call call : dispatcher.runningCalls()) {
            call.cancel();
        }
    }


    @Override
    public void onFailure(Call call, IOException e) {
        downListener.downFailed(e.toString());
        Log.d("down", "onFailure");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            File file = new File(downPath);
            if (file.exists()) {
                Log.i("down", "文件存在，删除文件==");
                file.delete();
            }
            if (!file.exists() && file.isFile()) {
                Log.i("down", "下载文件不存在创建文件==");
                boolean isCreat = file.createNewFile();
                Log.i("down", "创建文件==" + isCreat);
            }
            fos = new FileOutputStream(file);
            long sum = 0;
            long saveSum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                final int progress = (int) (sum * 1.0f / total * 100);
                final long speed = sum - saveSum;
                saveSum = sum;
                Log.d("down", "===================" + progress + "  speed =" + speed);
                updateProgress(progress, speed);
            }
            fos.flush();
            downListener.downSuccess(downPath);
            Log.d("down", "=================success==");
        } catch (Exception e) {
            downListener.downFailed(e.toString());
            Log.d("down", "=================error==" + e.toString());
        } finally {
            if (is != null)
                is.close();
            if (fos != null)
                fos.close();
        }
    }

    int lastProgress = 0;

    private void updateProgress(int progress, long speed) {
        if (progress > lastProgress) {
            downListener.downProgress(progress, speed);
        }
        lastProgress = progress;
    }


}
