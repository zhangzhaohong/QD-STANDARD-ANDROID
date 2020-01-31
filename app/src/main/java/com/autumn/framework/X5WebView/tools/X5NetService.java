package com.autumn.framework.X5WebView.tools;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.autumn.framework.Log.LogUtil;
import com.autumn.reptile.MyApplication;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by tzw on 2018/3/14.
 */

public class X5NetService extends IntentService {

    public static final String TAG = LogTAG.x5webview;
    public X5NetService(){
        super(TAG);
    }
    public X5NetService(String name) {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        String CHANNEL_ID_STRING = "com.autumn.reptile.subscribe";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "初始化信息", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(101, new Notification());
        }*/
        //Log.d(TAG, "service oncreate");
        //handler = new MyHandler();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public void onHandleIntent(@Nullable Intent intent) {
        initX5Web();
        //preinitX5WebCore();
    }
    public void initX5Web() {

        QbSdk.setDownloadWithoutWifi(true);

        if (!QbSdk.isTbsCoreInited()) {
            // 设置X5初始化完成的回调接口
            QbSdk.preInit(MyApplication.getAppContext(), null);
            LogUtil.d("X5NetService" +
                    "\n{" +
                    "\nStatus" +
                    "\n[" +
                    "\nMessage[QbSdk.isTbsCoreInited: true 已经加载x5内核]" +
                    "\n]" +
                    "\n}");
        }

        QbSdk.initX5Environment(MyApplication.getAppContext(), cb);
        //x5内核初始化接口

        /*try {
            QbSdk.initX5Environment(getApplicationContext(),  cb);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("X5内核" + e.toString());
            //Toast.makeText(getApplicationContext(),"x5内核启动失败",Toast.LENGTH_LONG);
        }finally {
//			QbSdk.setDownloadWithoutWifi(true);
            //非wifi网络下是否允许下载内核，默认是false

        }*/
    }

    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onCoreInitFinished() {

        }

        @Override
        public void onViewInitFinished(boolean b) {
            //progressBar.setVisibility(View.INVISIBLE);
            //Log.d(TAG, "onViewInitFinished: x5内核初始化:" + b);
            LogUtil.d("X5NetService" +
                    "\n{" +
                    "\nStatus" +
                    "\n[" +
                    "\nX5加载状态[" + b + "]" +
                    "\n]" +
                    "\n}");
        }
    };

    private void preinitX5WebCore() {

        if(!QbSdk.isTbsCoreInited()) {

            // preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view

            QbSdk.preInit(getApplicationContext(), null);// 设置X5初始化完成的回调接口

        }
    }

}