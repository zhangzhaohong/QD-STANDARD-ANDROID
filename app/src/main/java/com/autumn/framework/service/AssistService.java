package com.autumn.framework.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AssistService extends Service {
    private static final String TAG = "Service";
    private NotificationManager notificationManager;

    public class LocalBinder extends Binder {
        public AssistService getService() {
            return AssistService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "AssistService: onBind()");
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager!=null)
            notificationManager.cancel(1);//通过tag和id,清除通知栏信息
        super.onDestroy();
        Log.d(TAG, "AssistService: onDestroy()");
    }
}
