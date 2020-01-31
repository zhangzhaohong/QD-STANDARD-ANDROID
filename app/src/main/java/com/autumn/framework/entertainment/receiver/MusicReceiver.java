package com.autumn.framework.entertainment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.autumn.framework.entertainment.music_player.MusicPlayerActivityBack;
import com.autumn.framework.entertainment.utils.AppUtils;

import static com.autumn.framework.entertainment.music_player.MusicPlayerActivityBack.myBinder;

/**
 * Created by Administrator on 2018/5/24.
 */

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();//获取action标记，用户区分点击事件
        String status = intent.getStringExtra("status");

        Log.d("音频接收器","action =   "+action+"   status ="+status);
        if (myBinder==null)
            return;

        if (!AppUtils.getPackageName(context).equals(action)){
            return;
        }

        if ("pause".equals(status)) {
            myBinder.pause();
            MusicPlayerActivityBack.updateNotification(2);
        } else if ("continue".equals(status)) {
            myBinder.moveon();
            MusicPlayerActivityBack.updateNotification(1);

        }else if ("replay".equals(status)) {
            myBinder.rePlay();
            MusicPlayerActivityBack.updateNotification(1);

        }
    }
}
