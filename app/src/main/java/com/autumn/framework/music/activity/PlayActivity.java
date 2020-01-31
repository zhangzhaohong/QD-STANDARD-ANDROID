package com.autumn.framework.music.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ywl5320.bean.TimeBean;
import com.ywl5320.util.WlTimeUtil;
import com.autumn.framework.music.MusicService;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.framework.music.base.BaseMusicActivity;
import com.autumn.framework.music.beans.ChannelSchedulBean;
import com.autumn.framework.music.beans.EventBusBean;
import com.autumn.framework.music.beans.SeekBean;
import com.autumn.framework.music.config.EventType;
import com.autumn.framework.music.http.serviceapi.RadioApi;
import com.autumn.framework.music.http.subscribers.HttpSubscriber;
import com.autumn.framework.music.http.subscribers.SubscriberOnListener;
import com.autumn.framework.music.log.MyLog;
import com.autumn.framework.music.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by ywl on 2018/1/11.
 */

public class PlayActivity extends BaseMusicActivity {

    public static Notification notification;
    private static NotificationManager notificationManager;
    static String PALYER_TAG;
    private static Context mContext;
    @BindView(R.id.iv_point)
    ImageView ivPoint;
    @BindView(R.id.rl_cd)
    RelativeLayout rlCd;
    @BindView(R.id.tv_nowtime)
    TextView tvNowTime;
    @BindView(R.id.tv_totaltime)
    TextView tvTotalTime;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.iv_center)
    ImageView ivCenter;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.pb_load)
    ProgressBar pbLoad;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private ValueAnimator cdAnimator;
    private ValueAnimator pointAnimator;
    private LinearInterpolator lin;
    private EventBusBean eventNextBean;
    private EventBusBean eventSeekBean;
    private SeekBean seekBean;
    private List<ChannelSchedulBean> datas;

    private int position = 0;

    public static void destroyNotification() {
        if (notificationManager!=null)
            notificationManager.cancel(PALYER_TAG,101);//通过tag和id,清除通知栏信息
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_layout);
        mContext = this;
        //destroyNotification();
        setTitleTrans(R.color.color_trans);
        setBackView();
        setTitleLine(R.color.color_trans);
        setRightView(R.drawable.svg_menu_white);
        setTitle(getPlayBean().getName());
        if(getPlayBean().getTiming() == 0)
        {
            tvTip.setText("（回放）");
        }
        else if(getPlayBean().getTiming() == 1)
        {
            tvTip.setText("（直播）");
        }
        tvSubTitle.setText(getPlayBean().getSubname());

        lin = new LinearInterpolator();
        initPointAnimat();
        initCDAnimat();
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("url", getPlayBean().getUrl());
        startService(intent);
        Glide.with(this).load(getPlayBean().getImg()).apply(RequestOptions.placeholderOf(R.mipmap.icon_cd_default_bg)).into(ivCenter);
        Glide.with(this).load(R.mipmap.icon_gray_bg)
                .apply(bitmapTransform(new BlurTransformation(25, 3)).placeholder(R.mipmap.icon_gray_bg))
                .into(ivBg);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                position = getTimeBean().getTotalSecs() * progress / 100;
                tvNowTime.setText(WlTimeUtil.secdsToDateFormat(position, getTimeBean().getTotalSecs()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(eventSeekBean == null)
                {
                    if(seekBean == null)
                    {
                        seekBean = new SeekBean();
                    }
                    seekBean.setPosition(position);
                    seekBean.setSeekingfinished(false);
                    seekBean.setShowTime(false);

                    eventSeekBean = new EventBusBean(EventType.MUSIC_SEEK_TIME, seekBean);
                }
                else
                {
                    if(seekBean == null)
                    {
                        seekBean = new SeekBean();
                    }
                    seekBean.setPosition(position);
                    seekBean.setSeekingfinished(false);
                    seekBean.setShowTime(false);

                    eventSeekBean.setType(EventType.MUSIC_SEEK_TIME);
                    eventSeekBean.setObject(seekBean);
                }
                EventBus.getDefault().post(eventSeekBean);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyLog.d("position:" + position);
                if(eventSeekBean == null)
                {
                    if(seekBean == null)
                    {
                        seekBean = new SeekBean();
                    }
                    seekBean.setPosition(position);
                    seekBean.setSeekingfinished(false);
                    seekBean.setShowTime(false);

                    eventSeekBean = new EventBusBean(EventType.MUSIC_SEEK_TIME, seekBean);
                }
                else
                {
                    if(seekBean == null)
                    {
                        seekBean = new SeekBean();
                    }
                    seekBean.setPosition(position);
                    seekBean.setSeekingfinished(true);
                    seekBean.setShowTime(true);
                    eventSeekBean.setType(EventType.MUSIC_SEEK_TIME);
                    eventSeekBean.setObject(seekBean);
                }
                EventBus.getDefault().post(eventSeekBean);
            }
        });
        playRadio();
    }

    private void playRadio()
    {
        if(!getPlayBean().getUrl().equals(playUrl))
        {
            destroyNotification();
            setCdRodio(0f);
            if(eventNextBean == null)
            {
                eventNextBean = new EventBusBean(EventType.MUSIC_NEXT, getPlayBean().getUrl());
            }
            else
            {
                eventNextBean.setType(EventType.MUSIC_NEXT);
                eventNextBean.setObject(getPlayBean().getUrl());
            }
            EventBus.getDefault().post(eventNextBean);
            playUrl = getPlayBean().getUrl();
            getTimeBean().setTotalSecs(0);
            getTimeBean().setCurrSecs(0);
        }
        initTime();
    }



    @Override
    public void onClickMenu() {
        super.onClickMenu();
        showHistory(getPlayBean().getChannelId());
    }

    @Override
    public void onMusicStatus(int status) {
        super.onMusicStatus(status);
        switch (status)
        {
            case PLAY_STATUS_ERROR:
                if(ivPoint.getRotation() == 0f)
                {
                    startPointAnimat(0f, -40f);
                }
                ivStatus.setImageResource(R.drawable.play_selector);
                break;
            case PLAY_STATUS_LOADING:
                pbLoad.setVisibility(View.VISIBLE);
                ivStatus.setVisibility(View.GONE);
                if(ivPoint.getRotation() == -40f)
                {
                    rlCd.setRotation(getCdRodio());
                    startPointAnimat(-40f, 0f);
                }
                ivStatus.setImageResource(R.drawable.pause_selector);
                break;
            case PLAY_STATUS_UNLOADING:
                pbLoad.setVisibility(View.GONE);
                ivStatus.setVisibility(View.VISIBLE);
                break;
            case PLAY_STATUS_PLAYING:
                //destroyNotification();
                initNotification();
                if(ivPoint.getRotation() == -40f)
                {
                    rlCd.setRotation(getCdRodio());
                    startPointAnimat(-40f, 0f);
                }
                ivStatus.setImageResource(R.drawable.pause_selector);
                break;
            case PLAY_STATUS_PAUSE:
                if(ivPoint.getRotation() == 0f)
                {
                    startPointAnimat(0f, -40f);
                }
                ivStatus.setImageResource(R.drawable.play_selector);
                break;
            case PLAY_STATUS_RESUME:
                break;
            case PLAY_STATUS_COMPLETE:
                if(ivPoint.getRotation() == 0f)
                {
                    startPointAnimat(0f, -40f);
                }
                ivStatus.setImageResource(R.drawable.play_selector);
                break;
            default:
                break;
        }
    }


    @Override
    public void timeInfo(TimeBean timeBean) {
        super.timeInfo(timeBean);
        updateTime(timeBean);
    }

    @Override
    public void onPlayHistoryChange() {
        super.onPlayHistoryChange();
        if(getPlayBean().getTiming() == 0)
        {
            tvTip.setText("（回放）");
        }
        else if(getPlayBean().getTiming() == 1)
        {
            tvTip.setText("（直播）");
        }
        tvSubTitle.setText(getPlayBean().getSubname());
        initTime();
        updateTime(getTimeBean());
    }

    @Override
    public void onClickBack() {
        this.finish();
        super.onClickBack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTime(getTimeBean());
        rlCd.setRotation(getCdRodio());
        ivPoint.setRotation(-40f);
        getHistoryData(getPlayBean().getChannelId(), MyApplication.getInstance().getToken());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pointAnimator.cancel();
        cdAnimator.cancel();
        pointAnimator = null;
        cdAnimator = null;
    }

    @OnClick(R.id.iv_status)
    public void onClickStatus(View view)
    {
        if(musicStatus == PLAY_STATUS_PLAYING)
        {
            pauseMusic(true);
            ivStatus.setImageResource(R.drawable.play_selector);
        }
        else if(musicStatus == PLAY_STATUS_PAUSE)
        {
            pauseMusic(false);
            ivStatus.setImageResource(R.drawable.pause_selector);
            if(ivPoint.getRotation() == -40f)
            {
                startPointAnimat(-40f, 0f);
            }
        }
        else if(musicStatus == PLAY_STATUS_ERROR || musicStatus == PLAY_STATUS_COMPLETE)
        {
            playUrl = "";
            playRadio();
        }
    }

    @OnClick(R.id.iv_pre)
    public void onClickPre(View view)
    {
        playNext(false);
    }

    @OnClick(R.id.iv_next)
    public void onClickNext(View view)
    {
        playNext(true);
    }


    /**
     * 初始化指针动画
     */
    private void initPointAnimat()
    {
        ivPoint.setPivotX(CommonUtil.dip2px(PlayActivity.this, 17));
        ivPoint.setPivotY(CommonUtil.dip2px(PlayActivity.this, 15));
        pointAnimator = ValueAnimator.ofFloat(0, 0);
        pointAnimator.setTarget(ivPoint);
        pointAnimator.setRepeatCount(0);
        pointAnimator.setDuration(300);
        pointAnimator.setInterpolator(lin);
        pointAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (Float) animation.getAnimatedValue();
                ivPoint.setRotation(current);
            }
        });

        pointAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                MyLog.d("onAnimationStart");
                if(!isPlaying())
                {
                    pauseCDanimat();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                MyLog.d("onAnimationEnd");
                if(isPlaying())
                {
                    resumeCDanimat();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                MyLog.d("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                MyLog.d("onAnimationRepeat");
            }
        });

    }

    /**
     * 开始指针动画
     * @param from
     * @param end
     */
    private void startPointAnimat(float from, float end)
    {
        if(pointAnimator != null)
        {
            if(from < end)
            {
                if(!isPlaying())
                {
                    return;
                }
            }
            else
            {
                if(isPlaying())
                {
                    return;
                }
            }
            pointAnimator.setFloatValues(from, end);
            pointAnimator.start();
        }
    }

    /**
     * 初始化CD动画
     */
    private void initCDAnimat()
    {
        cdAnimator = ValueAnimator.ofFloat(rlCd.getRotation(), 360f + rlCd.getRotation());
        cdAnimator.setTarget(rlCd);
        cdAnimator.setRepeatCount(ValueAnimator.INFINITE);
        cdAnimator.setDuration(15000);
        cdAnimator.setInterpolator(lin);
        cdAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (Float) animation.getAnimatedValue();
                setCdRodio(current);
                rlCd.setRotation(current);
            }
        });
    }

    /**
     * 开始cd动画
     */
    private void resumeCDanimat()
    {
        if(cdAnimator != null && !cdAnimator.isRunning())
        {
            cdAnimator.setFloatValues(rlCd.getRotation(), 360f + rlCd.getRotation());
            cdAnimator.start();
        }
    }

    /**
     * 暂停CD动画
     */
    private void pauseCDanimat()
    {
        if(cdAnimator != null && cdAnimator.isRunning())
        {
            cdAnimator.cancel();
        }
    }

    private void updateTime(TimeBean timeBean)
    {
        if(timeBean != null)
        {
            if(timeBean.getTotalSecs() <= 0)
            {
                if(seekBar.getVisibility() == View.VISIBLE)
                {
                    seekBar.setVisibility(View.GONE);
                    tvTotalTime.setVisibility(View.GONE);
                }
                tvNowTime.setText(WlTimeUtil.secdsToDateFormat(timeBean.getCurrSecs(), timeBean.getTotalSecs()));
            }
            else
            {
                if(seekBar.getVisibility() == View.GONE)
                {
                    seekBar.setVisibility(View.VISIBLE);
                    tvTotalTime.setVisibility(View.VISIBLE);
                }
                tvTotalTime.setText(WlTimeUtil.secdsToDateFormat(timeBean.getTotalSecs(), timeBean.getTotalSecs()));
                tvNowTime.setText(WlTimeUtil.secdsToDateFormat(timeBean.getCurrSecs(), timeBean.getTotalSecs()));
                seekBar.setProgress(getProgress());
            }
        }
    }

    private void initTime() {
        if(getTimeBean().getTotalSecs() > 0)
        {
            seekBar.setVisibility(View.VISIBLE);
            tvTotalTime.setVisibility(View.VISIBLE);
            seekBar.setProgress(getProgress());
        }
        else
        {
            seekBar.setVisibility(View.GONE);
            tvTotalTime.setVisibility(View.GONE);
        }
    }

    public void getHistoryData(String chennelId, String token)
    {
        RadioApi.getInstance().getHistoryByChannelId(chennelId, token, new HttpSubscriber<List<ChannelSchedulBean>>(new SubscriberOnListener<List<ChannelSchedulBean>>() {
            @Override
            public void onSucceed(List<ChannelSchedulBean> data) {
                if(data != null)
                {
                    MyLog.d(data);
                    if(datas == null)
                    {
                        datas = new ArrayList<>();
                    }
                    datas.clear();
                    datas.addAll(data);
                    addIndexForHistory(datas, getPlayBean());
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    private void playNext(boolean next)
    {
        if(datas != null && datas.size() > 0)
        {
            int size = datas.size();
            for(int i = 0; i < size; i++)
            {
                ChannelSchedulBean channelSchedulBean = datas.get(i);
                if(channelSchedulBean.getIndex() == getPlayBean().getIndex())//当前播放的节目
                {
                    if(next)
                    {
                        if(i == size - 1)
                        {
                            showToast("已经是最新节目了");
                        }
                        else if(i < size - 1)
                        {
                            channelSchedulBean = datas.get(i+1);
                            getPlayBean().setSubname(channelSchedulBean.getName());
                            if(channelSchedulBean.getTiming() == 0)
                            {
                                if(!TextUtils.isEmpty(channelSchedulBean.getDownLoadUrl()))
                                {
                                    getPlayBean().setUrl(channelSchedulBean.getDownLoadUrl());
                                }
                                else {
                                    getPlayBean().setUrl(channelSchedulBean.getStreams().get(0).getUrl());
                                }
                            }
                            else if(channelSchedulBean.getTiming() == 1)
                            {
                                getPlayBean().setUrl(liveUrl);
                            }
                            getPlayBean().setIndex(channelSchedulBean.getIndex());
                            getPlayBean().setTiming(channelSchedulBean.getTiming());
                            playRadio();
                            onPlayHistoryChange();
                        }
                        break;
                    }
                    else
                    {
                        if(i == 0)
                        {
                            showToast("已经没有节目了");
                        }
                        else if(i > 0)
                        {
                            channelSchedulBean = datas.get(i-1);
                            getPlayBean().setSubname(channelSchedulBean.getName());
                            if(channelSchedulBean.getTiming() == 0)
                            {
                                if(!TextUtils.isEmpty(channelSchedulBean.getDownLoadUrl()))
                                {
                                    getPlayBean().setUrl(channelSchedulBean.getDownLoadUrl());
                                }
                                else {
                                    getPlayBean().setUrl(channelSchedulBean.getStreams().get(0).getUrl());
                                }
                            }
                            else if(channelSchedulBean.getTiming() == 1)
                            {
                                getPlayBean().setUrl(liveUrl);
                            }
                            getPlayBean().setIndex(channelSchedulBean.getIndex());
                            getPlayBean().setTiming(channelSchedulBean.getTiming());
                            playRadio();
                            onPlayHistoryChange();
                        }
                        break;
                    }
                }
            }
        }
        else
        {
            showToast("没有历史节目");
        }
    }

    /**
     * 初始化通知栏
     */
    private void initNotification() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"com.autumn.reptile.music");
//        notification = new Notification();

//        notification.icon = R.mipmap.ic_launcher;//图标
            RemoteViews contentView = new RemoteViews(getPackageName(),
                    R.layout.notification_control_radio);
//        notification.contentView = contentView;

            contentView.setImageViewResource(R.id.iv_pic, R.drawable.n_18);//图片展示

            contentView.setTextViewText(R.id.tv_title, getPlayBean().getSubname());

            if(getPlayBean().getTiming() == 0)
            {
                contentView.setTextViewText(R.id.tv_extra, "（回放）");
            }
            else if(getPlayBean().getTiming() == 1)
            {
                contentView.setTextViewText(R.id.tv_extra, "（直播）");
            }else {
                contentView.setTextViewText(R.id.tv_extra, "");
            }

            //contentView.setTextViewText(R.id.tv_extra, music_singer);

            //contentView.setImageViewResource(R.id.iv_play, R.drawable.ting);//button显示为正在播放

            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status", "pause");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);

            Intent notificationIntent = new Intent(this, PlayActivity.class);
            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            mBuilder.setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(intent)
                    .setVibrate(new long[]{0})
                    .setSound(null);//点击事件

            notification = mBuilder.build();
            notification.flags = notification.FLAG_NO_CLEAR;//设置通知点击或滑动时不被清除


            notificationManager.notify(PALYER_TAG, 101, notification);//开启通知

        }else {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//        notification = new Notification();

//        notification.icon = R.mipmap.ic_launcher;//图标
            RemoteViews contentView = new RemoteViews(getPackageName(),
                    R.layout.notification_control_radio);
//        notification.contentView = contentView;

            contentView.setImageViewResource(R.id.iv_pic, R.drawable.n_18);//图片展示

            contentView.setTextViewText(R.id.tv_title, getPlayBean().getSubname());

            if(getPlayBean().getTiming() == 0)
            {
                contentView.setTextViewText(R.id.tv_extra, "（回放）");
            }
            else if(getPlayBean().getTiming() == 1)
            {
                contentView.setTextViewText(R.id.tv_extra, "（直播）");
            }else {
                contentView.setTextViewText(R.id.tv_extra, "");
            }

            //contentView.setTextViewText(R.id.tv_extra, music_singer);

            //contentView.setTextViewText(R.id.tv_title, music_name);

            //contentView.setTextViewText(R.id.tv_extra, music_singer);

            //contentView.setImageViewResource(R.id.iv_play, R.drawable.ting);//button显示为正在播放


            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status", "pause");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);

            Intent notificationIntent = new Intent(this, PlayActivity.class);
            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            mBuilder.setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(intent);//点击事件

            notification = mBuilder.build();
            notification.flags = notification.FLAG_NO_CLEAR;//设置通知点击或滑动时不被清除


            notificationManager.notify(PALYER_TAG, 101, notification);//开启通知

        }

    }

    /**
     * 更新状态栏
     * @param  type  图标样式：1是正在播放状态，2是停止状态； 3播放完成
     */
    public static void updateNotification(int type){

        //【5】更新操作
        if (type==1){//播放中
            //status =2;
            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.ting);
            Intent intentPlay = new Intent(PALYER_TAG);//下一次意图，并设置action标记为"play"，用于接收广播时过滤意图信息
            intentPlay.putExtra("status","pause");
            PendingIntent pIntentPlay = PendingIntent.getBroadcast(mContext, 2, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPlay);//为控件注册事件

            //Button_Refresh(false);
            //button.setText("暂停");

        }else {//暂停或者播放完成

            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.play);

            Intent intentPause = new Intent(PALYER_TAG);

            if (type==2){//暂停
                //status=1;
                //Button_Refresh(true);
                //button.setText("继续");
                intentPause.putExtra("status","continue");
            }else{//3播放完成
                //Button_Refresh(true);
                //button.setText("重新开始");
                intentPause.putExtra("status","replay");//下一步
            }



            PendingIntent pIntentPause = PendingIntent.getBroadcast(mContext, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);


        }


        notificationManager.notify(PALYER_TAG,101, notification);//开启通知

    }

}
