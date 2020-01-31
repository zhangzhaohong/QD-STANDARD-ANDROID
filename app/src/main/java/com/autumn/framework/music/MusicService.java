package com.autumn.framework.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.autumn.framework.entertainment.manager.Music_player_manager;
import com.autumn.framework.music.beans.EventBusBean;
import com.autumn.framework.music.beans.SeekBean;
import com.autumn.framework.music.config.EventType;
import com.autumn.framework.music.log.MyLog;
import com.autumn.framework.service.AssistService;
import com.autumn.reptile.R;
import com.ywl5320.bean.TimeBean;
import com.ywl5320.libenum.SampleRateEnum;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnLoadListener;
import com.ywl5320.listener.OnPauseResumeListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnVolumeDBListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ywl on 2018/1/12.
 */

public class MusicService extends Service{


    private WlMusic wlMusic;
    private String url;
    private EventBusBean timeEventBean;
    private EventBusBean errorEventBean;
    private EventBusBean loadEventBean;
    private EventBusBean completeEventBean;
    private EventBusBean pauseResumtEventBean;
    private NotificationManager notificationManager;
    private NotificationChannel channel;
    private int importance;
    private String channelId;
    private String channelName;
    private ServiceConnection mConnection;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        wlMusic = WlMusic.getInstance();

        /*notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            //Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
            //Log.i(TAG, mChannel.toString());
            channelId = "com.autumn.reptile.music";
            channelName = "音乐消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
            notification = new Notification.Builder(this)
                    .setChannelId(channelId)
                    .setContentTitle("播放器正在播放中")
                    .setContentText("")
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("播放器正在播放中")
                    .setContentText("")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setChannel(id);//无效
            notification = notificationBuilder.build();
        }
        startForeground(101, notification);*/
        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        String CHANNEL_ID_STRING = "com.autumn.reptile.subscribe";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (null == mConnection) {
                mConnection = new AssistServiceConnection();
            }
            this.bindService(new Intent(this, AssistService.class), mConnection,
                    Service.BIND_AUTO_CREATE);
            /*mChannel = new NotificationChannel(CHANNEL_ID_STRING, "播放器播放中", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);*/
        }else {
            Notification notification = new Notification.Builder(getApplicationContext()).build();
            startForeground(1,notification);
        }
    }

    private class AssistServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //Log.d(TAG, "MyService: onServiceDisconnected");
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //Log.d(TAG, "MyService: onServiceConnected");
            // sdk >=18
            // 的，会在通知栏显示service正在运行，这里不要让用户感知，所以这里的实现方式是利用2个同进程的service，利用相同的notificationID，
            // 2个service分别startForeground，然后只在1个service里stopForeground，这样即可去掉通知栏的显示
            Service assistService = ((AssistService.LocalBinder) binder)
                    .getService();
            MusicService.this.startForeground(1, getNormalNotification());
            assistService.startForeground(1, getNormalNotification());
            assistService.stopForeground(true);

            MusicService.this.unbindService(mConnection);
            mConnection = null;
        }
    }

    private Notification getNormalNotification(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            //Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
            //Log.i(TAG, mChannel.toString());
            channelId = "com.autumn.reptile.music";
            channelName = "音乐消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
            notification = new Notification.Builder(this)
                    .setChannelId(channelId)
                    .setContentTitle("播放器正在播放中")
                    .setContentText("BackgroundService")
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        }
        return notification;
    }

    private Notification getNotification() {
        // 定义一个notification
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        Notification notification = null;
        String CHANNEL_ID_STRING = "com.autumn.reptile.subscribe";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "播放器播放中", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            //startForeground(1, notification);
        }
        /*Notification notification = new Notification();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(this, "My title", "My content",
                pendingIntent);*/
        return notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wlMusic != null)
        {
            wlMusic.stop();
        }
        EventBus.getDefault().unregister(this);
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            url = intent.getStringExtra("url");
            if (url != null && Music_player_manager.getUrl() != null){
                if (!url.equals(Music_player_manager.getUrl())){
                    if(wlMusic != null)
                    {
                        wlMusic.stop();
                    }
                }
            }
        }catch (Exception e){
            onDestroy();
        }
        wlMusic = WlMusic.getInstance();
        wlMusic.setSource(url);
        wlMusic.setPlayCircle(true);
        wlMusic.setVolume(100);
        try {
            if (url == null)
                onDestroy();
            else if (url.contains(".dff"))
                wlMusic.setConvertSampleRate(SampleRateEnum.RATE_48000);
            else if (url.contains(".flac"))
                wlMusic.setConvertSampleRate(SampleRateEnum.RATE_48000);
            else
                wlMusic.setConvertSampleRate(SampleRateEnum.RATE_32000);
        }catch (Exception e){
            onDestroy();
        }
        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                MyLog.e("onPrepared.................");
                wlMusic.start();
            }
        });

        wlMusic.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(TimeBean timeBean) {
                if(timeEventBean == null)
                {
                    timeEventBean = new EventBusBean(EventType.MUSIC_TIME_INFO, timeBean);
                }
                else
                {
                    timeEventBean.setObject(timeBean);
                    timeEventBean.setType(EventType.MUSIC_TIME_INFO);
                }
                EventBus.getDefault().post(timeEventBean);
            }
        });

        wlMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                if(errorEventBean == null)
                {
                    errorEventBean = new EventBusBean(EventType.MUSIC_ERROR, msg);
                }
                else
                {
                    errorEventBean.setType(EventType.MUSIC_ERROR);
                    errorEventBean.setObject(msg);
                }
                EventBus.getDefault().post(errorEventBean);
                url = "";
            }
        });

        wlMusic.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(boolean load) {
                if(loadEventBean == null)
                {
                    loadEventBean = new EventBusBean(EventType.MUSIC_LOAD, load);
                }
                else
                {
                    loadEventBean.setType(EventType.MUSIC_LOAD);
                    loadEventBean.setObject(load);
                }
                EventBus.getDefault().post(loadEventBean);
            }
        });

        wlMusic.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                if(completeEventBean == null)
                {
                    completeEventBean = new EventBusBean(EventType.MUSIC_COMPLETE, true);
                }
                else
                {
                    completeEventBean.setType(EventType.MUSIC_COMPLETE);
                    completeEventBean.setObject(true);
                }
                EventBus.getDefault().post(completeEventBean);
                url = "";
            }
        });

        wlMusic.setOnPauseResumeListener(new OnPauseResumeListener() {
            @Override
            public void onPause(boolean pause) {
                if(pauseResumtEventBean == null)
                {
                    pauseResumtEventBean = new EventBusBean(EventType.MUSIC_PAUSE_RESUME_RESULT, pause);
                }
                else
                {
                    pauseResumtEventBean.setType(EventType.MUSIC_PAUSE_RESUME_RESULT);
                    pauseResumtEventBean.setObject(pause);
                }
                EventBus.getDefault().post(pauseResumtEventBean);
            }
        });

        wlMusic.setOnVolumeDBListener(new OnVolumeDBListener() {
            @Override
            public void onVolumeDB(int db) {
//                MyLog.d(db);
            }
        });

        wlMusic.prePared();

        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsg(final EventBusBean messBean) {
        if(messBean.getType() == EventType.MUSIC_PAUSE_RESUME)
        {
            boolean pause = (boolean) messBean.getObject();
            if(pause)
            {
                wlMusic.pause();
            }
            else
            {
                wlMusic.resume();
            }
        }
        else if(messBean.getType() == EventType.MUSIC_NEXT)
        {
            if(wlMusic != null)
            {
                String u = (String) messBean.getObject();
                if(!url.equals(u))
                {
                    url = u;
                    wlMusic.playNext(url);
                }
            }
        }
        else if(messBean.getType() == EventType.MUSIC_SEEK_TIME)
        {
            if(wlMusic != null)
            {
                SeekBean seekBean = (SeekBean) messBean.getObject();
                wlMusic.seek(seekBean.getPosition(), seekBean.isSeekingfinished(), seekBean.isShowTime());
            }
        }
    }
}
