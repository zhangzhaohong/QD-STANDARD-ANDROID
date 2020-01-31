package com.autumn.framework.entertainment.music_player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.data.SpUtil;
import com.autumn.framework.entertainment.OkHttpDownloader.DownManager;
import com.autumn.framework.entertainment.bean.UpdateUI;
import com.autumn.framework.entertainment.manager.Data_manager_5;
import com.autumn.framework.entertainment.manager.Data_manager_7_hotMusic;
import com.autumn.framework.entertainment.manager.Data_manager_Music_List;
import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.framework.entertainment.manager.Music_player_manager;
import com.autumn.framework.entertainment.music_service.MusicService;
import com.autumn.framework.entertainment.receiver.MusicReceiver;
import com.autumn.framework.entertainment.runtime.Music_Checker_runtime;
import com.autumn.framework.entertainment.utils.AppUtils;
import com.autumn.framework.entertainment.utils.TiUtils;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.statusbar.StatusBarCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * 五步走
 * 【1】 绑定服务 ，并获取中间人对象（IBinder）
 * 【2】 通过中间人进行相关操作
 *      包括Notification（图标、下一次事件等）更新；Activity界面的更新
 * 【3】定时更新播放进度、监听准备成功以及播放完成后的操作
 *【4】动态注册广播，通知栏触发——PendingIntent
 *【5】根据广播内容，更新状态等
 *
 *【6】释放资源
 *  解除绑定、清除通知栏信息；注销广播接收者
 *
 * 注意
 *      清单文件中，添加网络权限，以及声明service
 *      同时设置android:launchMode="singleTask"，避免通知栏点击创建多个activity
 */

public class MusicPlayerActivityBack extends BaseActivity {

    private Button playBtn;

    private static ImageView player_play;
    private ImageView player_next;
    private static ImageView player_stop;
    private ImageView player_last;
    private TextView player_title;
    private TextView player_singer;
    private TextView player_name;
    private ImageView player_image;
    private Bundle extras;
    private int id;
    private String[] name;
    private String[] singer;
    private String[] imgURL;
    private String[] url;
    private String music_name;
    private String music_singer;
    private String music_imgURL;
    private ImageView player_back;

    public static MusicService.MyBinder myBinder;//中间人对象
    private MusicPlayerActivityBack.MyConn myConn;

    private static int duration;//音频总长度
    private static int currentPosition;//当前进度

    private static int status; //0初始状态，1暂停，2播放，3播放完成
    private SeekBar seekbar;
    private TextView tv_current_time;
    private TextView tv_time;
    public static Notification notification;
    private static NotificationManager notificationManager;
    private static Context mContext;
    private MusicReceiver receive;

    private String key;

    static String PALYER_TAG;

    static final String TAG="音频";
    private QMUITipDialog tipDialog;
    private String MUSIC_SERVICE;
    private String MUSIC_CONFIG = "Music_config";
    private String NETEASE = "Netease";
    private String TENCENT = "Tencent";
    private String KUGOU = "Kugou";
    private String KUWO = "Kuwo";
    private String Netease_quality;
    private String Tencent_quality;
    private String Kugou_quality;
    private String Kuwo_quality;
    private boolean player_status = false;
    private ImageView music_download;
    private ImageView music_change_quality;
    private DownManager downManger;
    private String[] items;
    private int checkedIndex;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String MIGU = "Migu";
    private String BAIDU = "Baidu";
    private String Migu_quality;
    private String Baidu_quality;
    private String from;

    //private boolean on_service_connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initState();
        }
        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        //SUtils.initialize(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        EventBus.getDefault().register(this);
        mContext = this;
        PALYER_TAG= AppUtils.getPackageName(this);

        player_back = (ImageView)findViewById(R.id.player_back);
        playBtn = (Button) findViewById(R.id.play);
        seekbar = (SeekBar) findViewById(R.id.player_progress_bar);
        player_play = (ImageView)findViewById(R.id.player_play);
        player_stop = (ImageView)findViewById(R.id.player_stop);
        player_next = (ImageView)findViewById(R.id.player_next);
        player_last = (ImageView)findViewById(R.id.player_last);
        player_title = (TextView)findViewById(R.id.player_title);
        player_name = (TextView)findViewById(R.id.player_name);
        player_singer = (TextView)findViewById(R.id.player_singer);
        player_image = (ImageView)findViewById(R.id.player_image);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        tv_time = (TextView) findViewById(R.id.tv_time);
        music_download = (ImageView)findViewById(R.id.music_download);
        music_change_quality = (ImageView)findViewById(R.id.music_change_quality);

        showProgress(true);

        extras = getIntent().getExtras();
        id = extras.getInt("id");
        from = extras.getString("from");
        //FToastUtils.init().setRoundRadius(30).show(key);

        if (from == null){
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            super.onBackPressed();
        }else if (from.equals("")){
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            super.onBackPressed();
        }else if (from.equals("SearchPage")) {
            name = Data_manager_5.getName_all();
            singer = Data_manager_5.getSinger_all();
            imgURL = Data_manager_5.getBackground_all();
            url = Data_manager_5.getUrl_all();
        }else if (from.equals("HotMusic")){
            name = Data_manager_7_hotMusic.getName_all();
            singer = Data_manager_7_hotMusic.getSinger_all();
            imgURL = Data_manager_7_hotMusic.getBackground_all();
            url = Data_manager_7_hotMusic.getUrl_all();
        }else if (from.equals("MusicList")){
            name = Data_manager_Music_List.getName_all();
            singer = Data_manager_Music_List.getSinger_all();
            imgURL = Data_manager_Music_List.getPicUrl_all();
            url = Data_manager_Music_List.getUrl_all();
            Music_player_manager.setMusicService(String.valueOf(1));
        }else {
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            super.onBackPressed();
        }

        if (NetworkChecker.isVpnUsed()){

            if (tipDialog.isShowing())
                tipDialog.dismiss();

            //vpn
            FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
            Intent intent = VpnService.prepare(MusicPlayerActivityBack.this);
            if (intent != null) {
                startActivityForResult(intent, 0);
            } else {
                onActivityResult(0, RESULT_OK, null);
            }

            for (int i = 0; i <= 10; i ++){
                if (NetworkChecker.isVpnUsed()){
                    i ++;
                }else {
                    showProgress(true);
                    break;
                }
                if (i == 10){
                    if (tipDialog.isShowing())
                        tipDialog.dismiss();
                    FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                    super.onBackPressed();
                    break;
                }
            }

        }else if (NetworkChecker.isWifiProxy(MusicPlayerActivityBack.this)){
            FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
            super.onBackPressed();
        }

        //getInfo(id);

        getInfo(id);
        //init_view();

        status = 0;

        init_service();

        player_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0){
                    FToastUtils.init().setRoundRadius(30).show("暂无上一首");
                }else if (id > 0 && id < name.length){
                    id -= 1;
                    getInfo(id);

                    change_service();

                }else {
                    FToastUtils.init().setRoundRadius(30).show("暂无上一首");
                }
            }
        });

        player_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id >= 0 && id < name.length - 1){
                    id += 1;
                    getInfo(id);

                    change_service();

                }else {
                    FToastUtils.init().setRoundRadius(30).show("暂无下一首");
                }
            }
        });

        player_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerActivityBack.super.onBackPressed();
            }
        });

        music_download.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FToastUtils.init().setRoundRadius(30).show("下载当前播放音乐（可能与选择音质不同）");
                return true;
            }
        });

        music_change_quality.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FToastUtils.init().setRoundRadius(30).show("临时切换音质");
                return true;
            }
        });

        music_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadProgress(true);

                if (NetworkChecker.isVpnUsed()){

                    if (tipDialog.isShowing())
                        tipDialog.dismiss();

                    //vpn
                    FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
                    Intent intent = VpnService.prepare(MusicPlayerActivityBack.this);
                    if (intent != null) {
                        startActivityForResult(intent, 0);
                    } else {
                        onActivityResult(0, RESULT_OK, null);
                    }

                    for (int i = 0; i <= 10; i ++){
                        if (NetworkChecker.isVpnUsed()){
                            i ++;
                        }else {
                            getDownloadInfo(id);
                            break;
                        }
                        if (i == 10){
                            FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                            //super.onBackPressed();
                            break;
                        }
                    }

                }else if (NetworkChecker.isWifiProxy(MusicPlayerActivityBack.this)){
                    if (tipDialog.isShowing())
                        tipDialog.dismiss();
                    FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                    //super.onBackPressed();
                }else {

                    getDownloadInfo(id);

                }
            }
        });

        music_change_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkChecker.isVpnUsed()){

                    if (tipDialog.isShowing())
                        tipDialog.dismiss();

                    //vpn
                    FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
                    Intent intent = VpnService.prepare(MusicPlayerActivityBack.this);
                    if (intent != null) {
                        startActivityForResult(intent, 0);
                    } else {
                        onActivityResult(0, RESULT_OK, null);
                    }

                    for (int i = 0; i <= 10; i ++){
                        if (NetworkChecker.isVpnUsed()){
                            i ++;
                        }else {
                            status = 0;

                            if (MUSIC_SERVICE == null) {
                                FToastUtils.init().setRoundRadius(30).show("服务异常！");
                                //MusicPlayerActivity.super.onBackPressed();
                            } else if (MUSIC_SERVICE.equals("")) {
                                FToastUtils.init().setRoundRadius(30).show("服务异常！");
                                //MusicPlayerActivity.super.onBackPressed();
                            } else {
                                switch (MUSIC_SERVICE) {
                                    case "netease":
                                        items = new String[]{"128000", "192000", "320000", "999000"};
                                        checkedIndex = 2;
                                        if (Netease_quality == null) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
                                            //netease_music_quality.setText("320000");
                                            Netease_quality = "320000";
                                        } else if (Netease_quality.equals("")) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
                                            //netease_music_quality.setText("320000");
                                            Netease_quality = "320000";
                                        }
                                        switch (Netease_quality) {
                                            case "128000":
                                                checkedIndex = 0;
                                                break;
                                            case "192000":
                                                checkedIndex = 1;
                                                break;
                                            case "320000":
                                                checkedIndex = 2;
                                                break;
                                            case "999000":
                                                checkedIndex = 3;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                                .setCheckedIndex(checkedIndex)
                                                .addItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                        //rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, items[which]);
                                                        //netease_music_quality.setText(items[which]);
                                                        Netease_quality = items[which];
                                                        dialog.dismiss();
                                                        FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                        change_quality();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();

                                        //change_service();
                                        break;
                                    case "tencent":
                                        items = new String[]{"24", "96", "128", "192", "320", "ape", "flac"};
                                        checkedIndex = 3;
                                        if (Tencent_quality == null) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
                                            //tencent_music_quality.setText("192");
                                            Tencent_quality = "192";
                                        } else if (Tencent_quality.equals("")) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
                                            //tencent_music_quality.setText("192");
                                            Tencent_quality = "192";
                                        }
                                        switch (Tencent_quality) {
                                            case "24":
                                                checkedIndex = 0;
                                                break;
                                            case "96":
                                                checkedIndex = 1;
                                                break;
                                            case "128":
                                                checkedIndex = 2;
                                                break;
                                            case "192":
                                                checkedIndex = 3;
                                                break;
                                            case "320":
                                                checkedIndex = 4;
                                                break;
                                            case "ape":
                                                checkedIndex = 5;
                                                break;
                                            case "flac":
                                                checkedIndex = 6;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                                .setCheckedIndex(checkedIndex)
                                                .addItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                        //rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, items[which]);
                                                        //tencent_music_quality.setText(items[which]);
                                                        Tencent_quality = items[which];
                                                        dialog.dismiss();
                                                        FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                        change_quality();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();
                                        break;
                                    case "kugou":
                                        items = new String[]{"128", "320", "flac", "hr", "dsd"};
                                        checkedIndex = 0;
                                        if (Kugou_quality == null) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
                                            //kugou_music_quality.setText("128");
                                            Kugou_quality = "128";
                                        } else if (Kugou_quality.equals("")) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
                                            //kugou_music_quality.setText("128");
                                            Kugou_quality = "128";
                                        }
                                        switch (Kugou_quality) {
                                            case "128":
                                                checkedIndex = 0;
                                                break;
                                            case "320":
                                                checkedIndex = 1;
                                                break;
                                            case "flac":
                                                checkedIndex = 2;
                                                break;
                                            case "hr":
                                                checkedIndex = 3;
                                                break;
                                            case "dsd":
                                                checkedIndex = 4;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                                .setCheckedIndex(checkedIndex)
                                                .addItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                        //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, items[which]);
                                                        //kugou_music_quality.setText(items[which]);
                                                        Kugou_quality = items[which];
                                                        dialog.dismiss();
                                                        FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                        change_quality();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();
                                        break;
                                    case "kuwo":
                                        items = new String[]{"128", "192", "320", "ape", "flac"};
                                        checkedIndex = 0;
                                        if (Kuwo_quality == null) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
                                            //kuwo_music_quality.setText("128");
                                            Kuwo_quality = "128";
                                        } else if (Kuwo_quality.equals("")) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
                                            //kuwo_music_quality.setText("128");
                                            Kuwo_quality = "128";
                                        }
                                        switch (Kuwo_quality) {
                                            case "128":
                                                checkedIndex = 0;
                                                break;
                                            case "192":
                                                checkedIndex = 1;
                                                break;
                                            case "320":
                                                checkedIndex = 2;
                                                break;
                                            case "ape":
                                                checkedIndex = 3;
                                                break;
                                            case "flac":
                                                checkedIndex = 4;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                                .setCheckedIndex(checkedIndex)
                                                .addItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                        //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, items[which]);
                                                        //kuwo_music_quality.setText(items[which]);
                                                        Kuwo_quality = items[which];
                                                        dialog.dismiss();
                                                        FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                        change_quality();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();
                                        break;
                                    case "migu":
                                        items = new String[]{"128", "192", "320", "flac"};
                                        checkedIndex = 0;
                                        if (Migu_quality == null)
                                            Migu_quality = "128";
                                        else if (Migu_quality.equals(""))
                                            Migu_quality = "128";
                                        switch (Migu_quality) {
                                            case "128":
                                                checkedIndex = 0;
                                                break;
                                            case "192":
                                                checkedIndex = 1;
                                                break;
                                            case "320":
                                                checkedIndex = 2;
                                                break;
                                            case "flac":
                                                checkedIndex = 3;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                                .setCheckedIndex(checkedIndex)
                                                .addItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                        //rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, items[which]);
                                                        //migu_music_quality.setText(items[which]);
                                                        Migu_quality = items[which];
                                                        dialog.dismiss();
                                                        FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                        change_quality();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();
                                        break;
                                    case "baidu":
                                        items = new String[]{"128", "320"};
                                        checkedIndex = 0;
                                        if (Baidu_quality == null) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
                                            //baidu_music_quality.setText("128");
                                            Baidu_quality = "128";
                                        } else if (Baidu_quality.equals("")) {
                                            //rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
                                            //baidu_music_quality.setText("128");
                                            Baidu_quality = "128";
                                        }
                                        switch (Baidu_quality) {
                                            case "128":
                                                checkedIndex = 0;
                                                break;
                                            case "320":
                                                checkedIndex = 1;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                                .setCheckedIndex(checkedIndex)
                                                .addItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                        //rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, items[which]);
                                                        //baidu_music_quality.setText(items[which]);
                                                        Baidu_quality = items[which];
                                                        dialog.dismiss();
                                                        FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                        change_quality();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();
                                        break;
                                }
                    /*showProgress(true);
                    if (Host_manager.getMusicHost() == null){
                        FToastUtils.init().setRoundRadius(30).show("域名异常！");
                        //MusicPlayerActivity.super.onBackPressed();
                    }else if (Host_manager.getMusicHost().equals("")){
                        FToastUtils.init().setRoundRadius(30).show("域名异常！");
                        //MusicPlayerActivity.super.onBackPressed();
                    }else {
                        switch (MUSIC_SERVICE) {
                            case "netease":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Netease_quality);
                                break;
                            case "tencent":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Tencent_quality);
                                break;
                            case "kugou":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kugou_quality);
                                break;
                            case "kuwo":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kuwo_quality);
                                break;
                        }
                        change_service();
                    }*/
                            }
                            //getDownloadInfo(id);
                            break;
                        }
                        if (i == 10){
                            FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                            //super.onBackPressed();
                            break;
                        }
                    }

                }else if (NetworkChecker.isWifiProxy(MusicPlayerActivityBack.this)){
                    if (tipDialog.isShowing())
                        tipDialog.dismiss();
                    FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                    //super.onBackPressed();
                }else {

                    status = 0;

                    if (MUSIC_SERVICE == null) {
                        FToastUtils.init().setRoundRadius(30).show("服务异常！");
                        //MusicPlayerActivity.super.onBackPressed();
                    } else if (MUSIC_SERVICE.equals("")) {
                        FToastUtils.init().setRoundRadius(30).show("服务异常！");
                        //MusicPlayerActivity.super.onBackPressed();
                    } else {
                        switch (MUSIC_SERVICE) {
                            case "netease":
                                items = new String[]{"128000", "192000", "320000", "999000"};
                                checkedIndex = 2;
                                if (Netease_quality == null) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
                                    //netease_music_quality.setText("320000");
                                    Netease_quality = "320000";
                                } else if (Netease_quality.equals("")) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
                                    //netease_music_quality.setText("320000");
                                    Netease_quality = "320000";
                                }
                                switch (Netease_quality) {
                                    case "128000":
                                        checkedIndex = 0;
                                        break;
                                    case "192000":
                                        checkedIndex = 1;
                                        break;
                                    case "320000":
                                        checkedIndex = 2;
                                        break;
                                    case "999000":
                                        checkedIndex = 3;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                        .setCheckedIndex(checkedIndex)
                                        .addItems(items, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                //rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, items[which]);
                                                //netease_music_quality.setText(items[which]);
                                                Netease_quality = items[which];
                                                dialog.dismiss();
                                                FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                change_quality();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();

                                //change_service();
                                break;
                            case "tencent":
                                items = new String[]{"24", "96", "128", "192", "320", "ape", "flac"};
                                checkedIndex = 3;
                                if (Tencent_quality == null) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
                                    //tencent_music_quality.setText("192");
                                    Tencent_quality = "192";
                                } else if (Tencent_quality.equals("")) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
                                    //tencent_music_quality.setText("192");
                                    Tencent_quality = "192";
                                }
                                switch (Tencent_quality) {
                                    case "24":
                                        checkedIndex = 0;
                                        break;
                                    case "96":
                                        checkedIndex = 1;
                                        break;
                                    case "128":
                                        checkedIndex = 2;
                                        break;
                                    case "192":
                                        checkedIndex = 3;
                                        break;
                                    case "320":
                                        checkedIndex = 4;
                                        break;
                                    case "ape":
                                        checkedIndex = 5;
                                        break;
                                    case "flac":
                                        checkedIndex = 6;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                        .setCheckedIndex(checkedIndex)
                                        .addItems(items, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                //rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, items[which]);
                                                //tencent_music_quality.setText(items[which]);
                                                Tencent_quality = items[which];
                                                dialog.dismiss();
                                                FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                change_quality();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                                break;
                            case "kugou":
                                items = new String[]{"128", "320", "flac", "hr", "dsd"};
                                checkedIndex = 0;
                                if (Kugou_quality == null) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
                                    //kugou_music_quality.setText("128");
                                    Kugou_quality = "128";
                                } else if (Kugou_quality.equals("")) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
                                    //kugou_music_quality.setText("128");
                                    Kugou_quality = "128";
                                }
                                switch (Kugou_quality) {
                                    case "128":
                                        checkedIndex = 0;
                                        break;
                                    case "320":
                                        checkedIndex = 1;
                                        break;
                                    case "flac":
                                        checkedIndex = 2;
                                        break;
                                    case "hr":
                                        checkedIndex = 3;
                                        break;
                                    case "dsd":
                                        checkedIndex = 4;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                        .setCheckedIndex(checkedIndex)
                                        .addItems(items, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, items[which]);
                                                //kugou_music_quality.setText(items[which]);
                                                Kugou_quality = items[which];
                                                dialog.dismiss();
                                                FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                change_quality();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                                break;
                            case "kuwo":
                                items = new String[]{"128", "192", "320", "ape", "flac"};
                                checkedIndex = 0;
                                if (Kuwo_quality == null) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
                                    //kuwo_music_quality.setText("128");
                                    Kuwo_quality = "128";
                                } else if (Kuwo_quality.equals("")) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
                                    //kuwo_music_quality.setText("128");
                                    Kuwo_quality = "128";
                                }
                                switch (Kuwo_quality) {
                                    case "128":
                                        checkedIndex = 0;
                                        break;
                                    case "192":
                                        checkedIndex = 1;
                                        break;
                                    case "320":
                                        checkedIndex = 2;
                                        break;
                                    case "ape":
                                        checkedIndex = 3;
                                        break;
                                    case "flac":
                                        checkedIndex = 4;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                        .setCheckedIndex(checkedIndex)
                                        .addItems(items, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                //rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, items[which]);
                                                //kuwo_music_quality.setText(items[which]);
                                                Kuwo_quality = items[which];
                                                dialog.dismiss();
                                                FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                change_quality();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                                break;
                            case "migu":
                                items = new String[]{"128", "192", "320", "flac"};
                                checkedIndex = 0;
                                if (Migu_quality == null)
                                    Migu_quality = "128";
                                else if (Migu_quality.equals(""))
                                    Migu_quality = "128";
                                switch (Migu_quality) {
                                    case "128":
                                        checkedIndex = 0;
                                        break;
                                    case "192":
                                        checkedIndex = 1;
                                        break;
                                    case "320":
                                        checkedIndex = 2;
                                        break;
                                    case "flac":
                                        checkedIndex = 3;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                        .setCheckedIndex(checkedIndex)
                                        .addItems(items, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                //rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, items[which]);
                                                //migu_music_quality.setText(items[which]);
                                                Migu_quality = items[which];
                                                dialog.dismiss();
                                                FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                change_quality();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                                break;
                            case "baidu":
                                items = new String[]{"128", "320"};
                                checkedIndex = 0;
                                if (Baidu_quality == null) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
                                    //baidu_music_quality.setText("128");
                                    Baidu_quality = "128";
                                } else if (Baidu_quality.equals("")) {
                                    //rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
                                    //baidu_music_quality.setText("128");
                                    Baidu_quality = "128";
                                }
                                switch (Baidu_quality) {
                                    case "128":
                                        checkedIndex = 0;
                                        break;
                                    case "320":
                                        checkedIndex = 1;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivityBack.this)
                                        .setCheckedIndex(checkedIndex)
                                        .addItems(items, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                                //rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, items[which]);
                                                //baidu_music_quality.setText(items[which]);
                                                Baidu_quality = items[which];
                                                dialog.dismiss();
                                                FToastUtils.init().setRoundRadius(30).show("切换成功！");
                                                change_quality();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                                break;
                        }
                    /*showProgress(true);
                    if (Host_manager.getMusicHost() == null){
                        FToastUtils.init().setRoundRadius(30).show("域名异常！");
                        //MusicPlayerActivity.super.onBackPressed();
                    }else if (Host_manager.getMusicHost().equals("")){
                        FToastUtils.init().setRoundRadius(30).show("域名异常！");
                        //MusicPlayerActivity.super.onBackPressed();
                    }else {
                        switch (MUSIC_SERVICE) {
                            case "netease":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Netease_quality);
                                break;
                            case "tencent":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Tencent_quality);
                                break;
                            case "kugou":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kugou_quality);
                                break;
                            case "kuwo":
                                key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kuwo_quality);
                                break;
                        }
                        change_service();
                    }*/
                    }

                }

            }
        });

    }

    private void change_quality(){
        //showProgress(true);
        if (Host_manager.getMusicHost() == null){
            FToastUtils.init().setRoundRadius(30).show("域名异常！");
            //MusicPlayerActivity.super.onBackPressed();
        }else if (Host_manager.getMusicHost().equals("")){
            FToastUtils.init().setRoundRadius(30).show("域名异常！");
            //MusicPlayerActivity.super.onBackPressed();
        }else {
            switch (MUSIC_SERVICE) {
                case "netease":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Netease_quality);
                    break;
                case "tencent":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Tencent_quality);
                    break;
                case "kugou":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kugou_quality);
                    break;
                case "kuwo":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kuwo_quality);
                    break;
                case "migu":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Migu_quality);
                    break;
                case "baidu":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Baidu_quality);
                    break;
            }
            change_service();
        }
    }

    private void getDownloadInfo(int id) {

            //status = 0;
            //MUSIC_SERVICE = Music_player_manager.getService();
            if (Music_player_manager.getService() == null){
                FToastUtils.init().setRoundRadius(30).show("服务异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else if (Music_player_manager.getService().equals("")){
                FToastUtils.init().setRoundRadius(30).show("服务异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else {
                switch (Music_player_manager.getService()){
                    case "0":
                        MUSIC_SERVICE = "netease";
                        break;
                    case "1":
                        MUSIC_SERVICE = "tencent";
                        break;
                    case "2":
                        MUSIC_SERVICE = "kugou";
                        break;
                    case "3":
                        MUSIC_SERVICE = "kuwo";
                        break;
                    case "4":
                        MUSIC_SERVICE = "migu";
                        break;
                    case "5":
                        MUSIC_SERVICE = "baidu";
                        break;
                }
            }
            //final SpUtil rw = new SpUtil();
            switch (MUSIC_SERVICE){
                case "netease":
                    //Netease_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, NETEASE);
                    if (Netease_quality == null){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, NETEASE, "320000");
                        Netease_quality = "320000";
                    }else if (Netease_quality.equals("")){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, NETEASE, "320000");
                        Netease_quality = "320000";
                    }
                    break;
                case "tencent":
                    //Tencent_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, TENCENT);
                    if (Tencent_quality == null){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, TENCENT, "192");
                        Tencent_quality = "192";
                    }else if (Tencent_quality.equals("")){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, TENCENT, "192");
                        Tencent_quality = "192";
                    }
                    break;
                case "kugou":
                    //Kugou_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUGOU);
                    if (Kugou_quality == null){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUGOU, "128");
                        Kugou_quality = "128";
                    }else if (Kugou_quality.equals("")){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUGOU, "128");
                        Kugou_quality = "128";
                    }
                    break;
                case "kuwo":
                    //Kuwo_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUWO);
                    if (Kuwo_quality == null){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUWO, "128");
                        Kuwo_quality = "128";
                    }else if (Kuwo_quality.equals("")){
                        //rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUWO, "128");
                        Kuwo_quality = "128";
                    }
                    break;
                case "migu":
                    if (Migu_quality == null)
                        Migu_quality = "128";
                    else if (Migu_quality.equals(""))
                        Migu_quality = "128";
                    break;
                case "baidu":
                    if (Baidu_quality == null)
                        Baidu_quality = "128";
                    else if (Baidu_quality.equals(""))
                        Baidu_quality = "128";
                    break;
            }
            if (Host_manager.getMusicHost() == null){
                FToastUtils.init().setRoundRadius(30).show("域名异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else if (Host_manager.getMusicHost().equals("")){
                FToastUtils.init().setRoundRadius(30).show("域名异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else {
                switch (MUSIC_SERVICE) {
                    case "netease":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Netease_quality);
                        break;
                    case "tencent":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Tencent_quality);
                        break;
                    case "kugou":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kugou_quality);
                        break;
                    case "kuwo":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kuwo_quality);
                        break;
                    case "migu":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Migu_quality);
                        break;
                    case "baidu":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Baidu_quality);
                        break;
                }
            }
            init_download_service();
            /*if (key == null){
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else if (key.equals("")){
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else {
                LogUtil.i("KEY\n[\n" + key + "\n]");
                new Thread(new Music_Checker_runtime(h1, key)).start();
                //key = url[id];
                //music_name = name[id];
                //music_singer = singer[id];
                //music_imgURL = imgURL[id];
                //init_view();
            }*/
        }

        @Override
        public void  onResume() {

            if (NetworkChecker.isVpnUsed()){

                if (tipDialog.isShowing())
                    tipDialog.dismiss();

                //vpn
                FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
                Intent intent = VpnService.prepare(MusicPlayerActivityBack.this);
                if (intent != null) {
                    startActivityForResult(intent, 0);
                } else {
                    onActivityResult(0, RESULT_OK, null);
                }

                for (int i = 0; i <= 10; i ++){
                    if (NetworkChecker.isVpnUsed()){
                        i ++;
                    }else {
                        break;
                    }
                    if (i == 10){
                        FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                        super.onBackPressed();
                        break;
                    }
                }

            }else if (NetworkChecker.isWifiProxy(MusicPlayerActivityBack.this)){
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                super.onBackPressed();
            }

            super.onResume();

        }

        private void showDownloadProgress(Boolean status){
            if (status){
                tipDialog = new QMUITipDialog.Builder(MusicPlayerActivityBack.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在加载")
                        .create(false);
                tipDialog.show();
                //player_play.setEnabled(false);
                //player_stop.setEnabled(false);
                //FToastUtils.init().setRoundRadius(30).show("正在加载音乐，请稍候！");
            }else {
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                //player_play.setEnabled(true);
                //player_stop.setEnabled(true);
                //FToastUtils.init().setRoundRadius(30).show("加载完成！");
            }
        }

        private void init_download_service() {

            if (key == null){
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
            }else if (key.equals("")){
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
            }else
                new Thread(new Music_Checker_runtime(h2, key)).start();
        /*Intent intent=new Intent(this,MusicService.class);
        intent.putExtra("url",key);
        myConn = new MyConn();
        //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
        bindService(intent, myConn,BIND_AUTO_CREATE);


        //【4】动态注册广播（具体操作由通知栏触发---PendingIntent）
        receive = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PALYER_TAG);
        registerReceiver(receive, filter);*/

        }

        Handler h2=new Handler()
        {

            @Override
            public void handleMessage(Message msg)
            {
                // TODO: Implement this method
                switch (msg.what)
                {
                    case Music_Checker_runtime.FINDER_IMAGE_1:
                        //inderout1.setTextSize(15);
                        //finderout1.setGravity(Gravity.CENTER);
                        //finderout1.setText(msg.obj.toString());



                        if (String.valueOf(msg.obj).equals("访问成功")){

                            String url_302 = Music_player_manager.getUrl();
                            if (url_302 == null) {
                                //player_status = false;
                                if (tipDialog.isShowing())
                                    tipDialog.dismiss();
                                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            }else if (url_302.equals("")){
                                //player_status = false;
                                if (tipDialog.isShowing())
                                    tipDialog.dismiss();
                                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            }else {
                                FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path));
                                FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Music");

                                File path = new File(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Music");

                                String downPath = "";
                                if (url_302.contains(".mp3")){
                                    downPath = path + "/" + music_name + "-" + music_singer + ".mp3";
                                }else if (url_302.contains(".ape")){
                                    downPath = path + "/" + music_name + "-" + music_singer + ".ape";
                                }else if (url_302.contains(".flac")){
                                    downPath = path + "/" + music_name + "-" + music_singer + ".flac";
                                }else if (url_302.contains(".dff")){
                                    downPath = path + "/" + music_name + "-" + music_singer + ".dff";
                                }else {
                                    FToastUtils.init().setRoundRadius(30).show("未知文件名，请在下载完成后手动修改");
                                    downPath = path + "/" + music_name + "-" + music_singer;
                                }
                                //String downPath = path + "/" + name[getAdapterPosition()] + "-" + singer[getAdapterPosition()] + ".mp3";
                                if (tipDialog.isShowing())
                                    tipDialog.dismiss();
                                downManger = new DownManager(MusicPlayerActivityBack.this);
                                downManger.downSatrt(url_302, downPath, "是否要下载" + music_singer + "唱的《" + music_name + "》？");
                            /*player_status = true;
                            Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
                            intent.putExtra("url", url_302);
                            myConn = new MyConn();
                            //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
                            bindService(intent, myConn, BIND_AUTO_CREATE);


                            //【4】动态注册广播（具体操作由通知栏触发---PendingIntent）
                            receive = new MusicReceiver();
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(PALYER_TAG);
                            registerReceiver(receive, filter);*/
                            }
                        }else{

                            //访问失败
                            //player_status = false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                            //mRefreshLayout.finishRefresh();
                            //mRefreshLayout.finishLoadmore();

                        }

                        this.removeCallbacksAndMessages(null);
                        break;
                }
            }

        };

    private void change_service() {

        Log.d("销毁","onDestroy()");
        //【6】解除绑定并注销
        if (myConn!=null)
            unbindService(myConn);
        myConn=null;

        if (notificationManager!=null)
            notificationManager.cancel(PALYER_TAG,101);//通过tag和id,清除通知栏信息

        if (player_status)
            unregisterReceiver(receive);

        seekbar.setMax(0);
        seekbar.setProgress(0);
        Button_Refresh(true);
        tv_current_time.setText("00:00");
        tv_time.setText("00:00");
        status = 0;

        showProgress(true);

        init_service();

    }

    private void init_view() {
        player_title.setText(music_name);
        player_name.setText(music_name);
        player_singer.setText(music_singer);
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(R.drawable.aio_image_default);
        options.error(R.drawable.empty_photo);
        Glide.with(MyApplication.getAppContext())
                .load(music_imgURL)
                .apply(options)
                .into(player_image);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myBinder.seekToPosition(seekBar.getProgress());
                tv_current_time.setText(TiUtils.getTime(seekBar.getProgress()/1000));
            }
        });

    }

    private void getInfo(int id) {

        status = 0;
        //MUSIC_SERVICE = Music_player_manager.getService();
        if (Music_player_manager.getService() == null){
            FToastUtils.init().setRoundRadius(30).show("服务异常！");
            MusicPlayerActivityBack.super.onBackPressed();
        }else if (Music_player_manager.getService().equals("")){
            FToastUtils.init().setRoundRadius(30).show("服务异常！");
            MusicPlayerActivityBack.super.onBackPressed();
        }else {
            switch (Music_player_manager.getService()){
                case "0":
                    MUSIC_SERVICE = "netease";
                    break;
                case "1":
                    MUSIC_SERVICE = "tencent";
                    break;
                case "2":
                    MUSIC_SERVICE = "kugou";
                    break;
                case "3":
                    MUSIC_SERVICE = "kuwo";
                    break;
                case "4":
                    MUSIC_SERVICE = "migu";
                    break;
                case "5":
                    MUSIC_SERVICE = "baidu";
                    break;
            }
        }
        final SpUtil rw = new SpUtil();
        switch (MUSIC_SERVICE){
            case "netease":
                Netease_quality = rw.getValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, NETEASE);
                if (Netease_quality == null){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, NETEASE, "320000");
                    Netease_quality = "320000";
                }else if (Netease_quality.equals("")){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, NETEASE, "320000");
                    Netease_quality = "320000";
                }
                break;
            case "tencent":
                Tencent_quality = rw.getValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, TENCENT);
                if (Tencent_quality == null){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, TENCENT, "192");
                    Tencent_quality = "192";
                }else if (Tencent_quality.equals("")){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, TENCENT, "192");
                    Tencent_quality = "192";
                }
                break;
            case "kugou":
                Kugou_quality = rw.getValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, KUGOU);
                if (Kugou_quality == null){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, KUGOU, "128");
                    Kugou_quality = "128";
                }else if (Kugou_quality.equals("")){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, KUGOU, "128");
                    Kugou_quality = "128";
                }
                break;
            case "kuwo":
                Kuwo_quality = rw.getValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, KUWO);
                if (Kuwo_quality == null){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, KUWO, "128");
                    Kuwo_quality = "128";
                }else if (Kuwo_quality.equals("")){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, KUWO, "128");
                    Kuwo_quality = "128";
                }
                break;
            case "migu":
                Migu_quality = rw.getValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, MIGU);
                if (Migu_quality == null){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, MIGU, "128");
                    //migu_music_quality.setText("128");
                    Migu_quality = "128";
                }else if (Migu_quality.equals("")){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, MIGU, "128");
                    //migu_music_quality.setText("128");
                    Migu_quality = "128";
                }
                break;
            case "baidu":
                Baidu_quality = rw.getValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, BAIDU);
                if (Baidu_quality == null){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, BAIDU, "128");
                    //baidu_music_quality.setText("128");
                    Baidu_quality = "128";
                }else if (Baidu_quality.equals("")){
                    rw.writeValue(MusicPlayerActivityBack.this, MUSIC_CONFIG, BAIDU, "128");
                    //baidu_music_quality.setText("128");
                    Baidu_quality = "128";
                }
                break;
        }
        if (Host_manager.getMusicHost() == null){
            FToastUtils.init().setRoundRadius(30).show("域名异常！");
            MusicPlayerActivityBack.super.onBackPressed();
        }else if (Host_manager.getMusicHost().equals("")){
            FToastUtils.init().setRoundRadius(30).show("域名异常！");
            MusicPlayerActivityBack.super.onBackPressed();
        }else {
            switch (MUSIC_SERVICE) {
                case "netease":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Netease_quality);
                    break;
                case "tencent":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Tencent_quality);
                    break;
                case "kugou":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kugou_quality);
                    break;
                case "kuwo":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kuwo_quality);
                    break;
                case "migu":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Migu_quality);
                    break;
                case "baidu":
                    key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Baidu_quality);
                    break;
            }
        }
        if (key == null){
            FToastUtils.init().setRoundRadius(30).show("链接异常！");
            MusicPlayerActivityBack.super.onBackPressed();
        }else if (key.equals("")){
            FToastUtils.init().setRoundRadius(30).show("链接异常！");
            MusicPlayerActivityBack.super.onBackPressed();
        }else {
            LogUtil.i("KEY\n[\n" + key + "\n]");
            //key = url[id];
            music_name = name[id];
            music_singer = singer[id];
            music_imgURL = imgURL[id];
            init_view();
        }
    }

    public	static String getURLEncoderString(String str) {
        String result = "";
        if(null == str) {
            return"";
        }
        try
        {
            String ENCODE = null;
            result = URLEncoder.encode(str, "UTF8");
        }
        catch
                (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void showProgress(Boolean status){
        if (status){
            tipDialog = new QMUITipDialog.Builder(MusicPlayerActivityBack.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在加载")
                    .create(false);
            tipDialog.show();
            player_play.setEnabled(false);
            player_stop.setEnabled(false);
            //FToastUtils.init().setRoundRadius(30).show("正在加载音乐，请稍候！");
        }else {
            if (tipDialog.isShowing())
                tipDialog.dismiss();
            player_play.setEnabled(true);
            player_stop.setEnabled(true);
            //FToastUtils.init().setRoundRadius(30).show("加载完成！");
        }
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initState() {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            //LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            //linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            //int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            //params.height = statusHeight;
            //linear_bar.setLayoutParams(params);

            boolean lightStatusBar = false;
            StatusBarCompat.setStatusBarColor(this, getColor(R.color.black_primary_dark), lightStatusBar);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black_primary_dark));

        }
    }

    /**
     * 获取主题颜色
     * @return
     */
    public int getColorPrimary(){
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取主题颜色
     * @return
     */
    public int getDarkColorPrimary(){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
                    R.layout.notification_control);
//        notification.contentView = contentView;

            contentView.setImageViewResource(R.id.iv_pic, R.drawable.n_18);//图片展示

            contentView.setTextViewText(R.id.tv_title, music_name);

            contentView.setTextViewText(R.id.tv_extra, music_singer);

            contentView.setImageViewResource(R.id.iv_play, R.drawable.ting);//button显示为正在播放


            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status", "pause");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);

            Intent notificationIntent = new Intent(this, MusicPlayerActivityBack.class);
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
                    R.layout.notification_control);
//        notification.contentView = contentView;

            contentView.setImageViewResource(R.id.iv_pic, R.drawable.n_18);//图片展示

            contentView.setTextViewText(R.id.tv_title, music_name);

            contentView.setTextViewText(R.id.tv_extra, music_singer);

            contentView.setImageViewResource(R.id.iv_play, R.drawable.ting);//button显示为正在播放


            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status", "pause");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);

            Intent notificationIntent = new Intent(this, MusicPlayerActivityBack.class);
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
            status =2;
            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.ting);
            Intent intentPlay = new Intent(PALYER_TAG);//下一次意图，并设置action标记为"play"，用于接收广播时过滤意图信息
            intentPlay.putExtra("status","pause");
            PendingIntent pIntentPlay = PendingIntent.getBroadcast(mContext, 2, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPlay);//为控件注册事件

            Button_Refresh(false);
            //button.setText("暂停");

        }else {//暂停或者播放完成

            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.play);

            Intent intentPause = new Intent(PALYER_TAG);

            if (type==2){//暂停
                status=1;
                Button_Refresh(true);
                //button.setText("继续");
                intentPause.putExtra("status","continue");
            }else{//3播放完成
                Button_Refresh(true);
                //button.setText("重新开始");
                intentPause.putExtra("status","replay");//下一步
            }



            PendingIntent pIntentPause = PendingIntent.getBroadcast(mContext, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);


        }


        notificationManager.notify(PALYER_TAG,101, notification);//开启通知

    }

    private static void Button_Refresh(boolean b) {
        if (b){
            player_stop.setVisibility(View.GONE);
            player_play.setVisibility(View.VISIBLE);
        }else {
            player_play.setVisibility(View.GONE);
            player_stop.setVisibility(View.VISIBLE);
        }
    }

    public class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取中间人对象
            myBinder = (MusicService.MyBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)  //3.0之后，需要加注解
    public void onEventMainThread(UpdateUI updateUI){
        int flag = updateUI.getFlag();

        Log.d("音频状态",status+" ---- "+flag);
        //【3】设置进度
        if (flag==1){//准备完成，获取音频长度
            duration = (int) updateUI.getData();
            Log.d(TAG,"总长度"+duration);
            //设置总长度
            seekbar.setMax(duration);
            tv_time.setText(TiUtils.getTime(duration/1000));

            showProgress(false);

        }else  if (flag==2){//播放完成
            Log.d(TAG,"播放完成～");

            status=3;//已完成
            //重置信息
            seekbar.setProgress(0);
            tv_current_time.setText("00:00");
            Button_Refresh(true);
            //button.setText("重新播放");

            updateNotification(3);

        }else if (flag==3){//更新进度
            if (status==3)//避免播放完成通知与线程通知冲突
                return;
            currentPosition = (int) updateUI.getData();
            Log.d(TAG,"当前进度"+currentPosition);

            //设置进度
            tv_current_time.setText(TiUtils.getTime(currentPosition/1000));
            seekbar.setProgress(currentPosition);
        }
    }

    public void play(View v) {
        //【2】 通过中间人进行相关操作

        Log.d("当前状态","status = "+status);
        switch (status){
            case 0://初始状态

                //init_service();

                //播放
                myBinder.play();
                status=2;
                Button_Refresh(false);
                //button.setText("暂停");

                //初始化通知栏
                initNotification();
                break;

            case 1://暂停
                //继续播放
                myBinder.moveon();
                status=2;
                Button_Refresh(false);
                //button.setText("暂停");
                updateNotification(1);
                break;

            case 2://播放中
                //暂停
                myBinder.pause();
                status=1;
                Button_Refresh(true);
                //button.setText("继续播放");
                updateNotification(2);
                break;

            case 3://播放完成
                //重新开始
                myBinder.rePlay();
                status=2;
                Button_Refresh(false);
                //button.setText("暂停");
                updateNotification(1);
                break;


        }
    }

    private void init_service() {

        if (key == null){
            if (tipDialog.isShowing())
                tipDialog.dismiss();
            FToastUtils.init().setRoundRadius(30).show("链接异常！");
        }else if (key.equals("")){
            if (tipDialog.isShowing())
                tipDialog.dismiss();
            FToastUtils.init().setRoundRadius(30).show("链接异常！");
        }else
            new Thread(new Music_Checker_runtime(h1, key)).start();
        /*Intent intent=new Intent(this,MusicService.class);
        intent.putExtra("url",key);
        myConn = new MyConn();
        //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
        bindService(intent, myConn,BIND_AUTO_CREATE);


        //【4】动态注册广播（具体操作由通知栏触发---PendingIntent）
        receive = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PALYER_TAG);
        registerReceiver(receive, filter);*/

    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case Music_Checker_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals("访问成功")){

                        String url_302 = Music_player_manager.getUrl();
                        if (url_302 == null) {
                            player_status = false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                        }else if (url_302.equals("")){
                            player_status = false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                        }else if (url_302.contains(".ape")) {
                            player_status = false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("检测到音乐源为ape，暂不支持试听，仅支持下载");
                        }else if (url_302.contains(".dff")) {
                            player_status = false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).showLong("检测到音乐源为dsd，暂不支持试听，仅支持下载，下载后请使用VIPER HIFI扫描该文件夹后进行播放");
                        }else {
                            player_status = true;
                            Intent intent = new Intent(MusicPlayerActivityBack.this, MusicService.class);
                            intent.putExtra("url", url_302);
                            myConn = new MyConn();
                            //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
                            bindService(intent, myConn, BIND_AUTO_CREATE);


                            //【4】动态注册广播（具体操作由通知栏触发---PendingIntent）
                            receive = new MusicReceiver();
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(PALYER_TAG);
                            registerReceiver(receive, filter);
                        }
                    }else{

                        //访问失败
                        player_status = false;
                        if (tipDialog.isShowing())
                            tipDialog.dismiss();
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        //mRefreshLayout.finishRefresh();
                        //mRefreshLayout.finishLoadmore();

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    @Override
    protected void onDestroy() {

        ActivtyStack.getScreenManager().popActivity(this);

        if (tipDialog.isShowing())
            tipDialog.dismiss();

        super.onDestroy();

        Log.d("销毁","onDestroy()");

        //【6】解除绑定并注销
        if (myConn!=null)
            unbindService(myConn);
        myConn=null;

        if (notificationManager!=null)
            notificationManager.cancel(PALYER_TAG,101);//通过tag和id,清除通知栏信息

        EventBus.getDefault().unregister(this);

        if (player_status)
            unregisterReceiver(receive);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


//        finish();

    }

}