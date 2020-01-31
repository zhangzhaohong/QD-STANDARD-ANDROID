package com.autumn.framework.entertainment.music_player;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.X5WebView.ui.X5WebGameActivity;
import com.autumn.framework.data.HttpUtil;
import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.data.ScreenUtils;
import com.autumn.framework.data.SpUtil;
import com.autumn.framework.entertainment.DouYinDownloader.Encode;
import com.autumn.framework.entertainment.OkHttpDownloader.DownManager;
import com.autumn.framework.entertainment.manager.Data_manager_5;
import com.autumn.framework.entertainment.manager.Data_manager_7_hotMusic;
import com.autumn.framework.entertainment.manager.Data_manager_Music_List;
import com.autumn.framework.entertainment.manager.Data_manager_kg_mv;
import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.framework.entertainment.manager.Music_player_manager;
import com.autumn.framework.entertainment.runtime.Music_Checker_runtime;
import com.autumn.framework.music.MusicService;
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
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ywl5320.bean.TimeBean;
import com.ywl5320.util.WlTimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hotapk.fastandrutils.utils.FToastUtils;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.wcy.lrcview.LrcView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by ywl on 2018/1/11.
 */

public class MusicPlayerActivity extends BaseMusicActivity implements LrcView.OnPlayClickListener {

    private static final String PLAYER_PLAY_MODE = "Player_play_mode";
    private static final String FORGROUND = "Forground";
    private static final String BACKGROUND = "Background";
    private Bundle extras;
    private int id;
    private String[] name;
    private String[] singer;
    private String[] imgURL;
    private String[] url;
    private String music_name;
    private String music_singer;
    private String music_imgURL;
    private String key;
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
    //private ImageView music_download;
    //private ImageView music_change_quality;
    private DownManager downManger;
    private String[] items;
    private int checkedIndex;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String MIGU = "Migu";
    private String BAIDU = "Baidu";
    private String Migu_quality;
    private String Baidu_quality;
    private String from;
    public static Notification notification;
    private static NotificationManager notificationManager;
    static String PALYER_TAG;
    private static Context mContext;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.viewGroup)
    ViewGroup group;
    //@BindView(R.id.iv_point)
    private ImageView ivPoint;
    //@BindView(R.id.rl_cd)
    private RelativeLayout rlCd;
    @BindView(R.id.tv_nowtime)
    TextView tvNowTime;
    @BindView(R.id.tv_totaltime)
    TextView tvTotalTime;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    //@BindView(R.id.iv_center)
    private ImageView ivCenter;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.pb_load)
    ProgressBar pbLoad;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.music_download)
    ImageView music_download;
    @BindView(R.id.music_change_quality)
    ImageView music_change_quality;
    @BindView(R.id.mv_icon)
    ImageView mv_icon;
    //@BindView(R.id.lrc_view)
    //LrcView lrcView;

    //2个view
    private View view1;
    private View view2;
    //用来存放view并传递给viewPager的适配器。
    private ArrayList<View> pageview;
    //用来存放圆点，没有写第四步的话，就不要定义一下三个变量了。
    private ImageView[] tips = new ImageView[2];
    private ImageView imageView;
    //private LrcView lrcView;
    private ValueAnimator cdAnimator;
    private ValueAnimator pointAnimator;
    private LinearInterpolator lin;
    private EventBusBean eventNextBean;
    private EventBusBean eventSeekBean;
    private SeekBean seekBean;
    private List<ChannelSchedulBean> datas;

    private int position = 0;
    private static final String PLAYER_SETTING = "Player_setting";
    private OkHttpClient client;
    private LrcView lrcView;
    private LrcView lrcViewSingle;
    private String MV_CONFIG = "Mv_config";
    private String neteaseMvQuality;
    private String tencentMvQuality;
    private String kugouMvQuality;
    private String kuwoMvQuality;
    private String miguMvQuality;
    private String baiduMvQuality;
    private String mv_url_final;

    public static void destroyNotification() {
        if (notificationManager!=null)
            notificationManager.cancel(PALYER_TAG,101);//通过tag和id,清除通知栏信息
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_layout);
        mContext = this;
        //destroyNotification();
        setTitleTrans(R.color.color_trans);
        setBackView();
        setTitleLine(R.color.color_trans);
        noMv(true);
        //setRightView(R.drawable.svg_menu_white);

        //将view加进pageview中
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        view1 = getLayoutInflater().inflate(R.layout.content_music_player_p1,null);
        view2 = getLayoutInflater().inflate(R.layout.content_music_player_p2,null);
        pageview = new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);

        //viewPager下面的圆点，ViewGroup
        group = (ViewGroup)findViewById(R.id.viewGroup);
        tips = new ImageView[pageview.size()];
        for(int i =0;i<pageview.size();i++){
            imageView = new ImageView(MusicPlayerActivity.this);
            ScreenUtils.init(MusicPlayerActivity.this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(60,60));
            int padding = ScreenUtils.dp2px(3);
            imageView.setPadding(padding, 0, padding, 0);
            //imageView.setLayoutParams(new ViewGroup.LayoutParams(60,60));
            //imageView.setPadding(40, 0, 40, 0);
            tips[i] = imageView;

            //默认第一张图显示为选中状态
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.ic_play_page_indicator_selected);
            } else {
                tips[i].setBackgroundResource(R.drawable.ic_play_page_indicator_unselected);
            }

            group.addView(tips[i]);
        }
        //这里的mypagerAdapter是第三步定义好的。
        viewPager.setAdapter(new mypagerAdapter(pageview));
        //这里的GuiPageChangeListener是第四步定义好的。
        viewPager.addOnPageChangeListener(new GuidePageChangeListener());

        ivPoint = (ImageView)view1.findViewById(R.id.iv_point);
        rlCd = (RelativeLayout)view1.findViewById(R.id.rl_cd);
        ivCenter = (ImageView)view1.findViewById(R.id.iv_center);
        lrcView = (LrcView)view2.findViewById(R.id.lrc_view_full);
        lrcView.setOnPlayClickListener(this::onPlayClick);
        lrcViewSingle = (LrcView)view1.findViewById(R.id.lrc_view_single);

        //lrcView.loadLrc("");
        //lrcView = findViewById(R.id.lrc_view);
        //setTitle(getPlayBean().getName());
        //get Data
        showProgress(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            from = extras.getString("from");
        }else {
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            super.onBackPressed();
        }
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
            Intent intent = VpnService.prepare(MusicPlayerActivity.this);
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

        }else if (NetworkChecker.isWifiProxy(MusicPlayerActivity.this)){
            FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
            super.onBackPressed();
        }

        //getInfo(id);

        getPlayBean().setTiming(3);
        getPlayBean().setChannelId("0");

        getInfo(id);

        init_service();

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
                    Intent intent = VpnService.prepare(MusicPlayerActivity.this);
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

                }else if (NetworkChecker.isWifiProxy(MusicPlayerActivity.this)){
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
                    Intent intent = VpnService.prepare(MusicPlayerActivity.this);
                    if (intent != null) {
                        startActivityForResult(intent, 0);
                    } else {
                        onActivityResult(0, RESULT_OK, null);
                    }

                    for (int i = 0; i <= 10; i ++){
                        if (NetworkChecker.isVpnUsed()){
                            i ++;
                        }else {
                            //status =0;

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

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                                .setCheckedIndex(checkedIndex)
                                                .setCancelable(false)
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

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                                .setCheckedIndex(checkedIndex)
                                                .setCancelable(false)
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

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                                .setCheckedIndex(checkedIndex)
                                                .setCancelable(false)
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

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                                .setCheckedIndex(checkedIndex)
                                                .setCancelable(false)
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

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                                .setCheckedIndex(checkedIndex)
                                                .setCancelable(false)
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
                                        items = new String[]{"64", "128", "320", "flac"};
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
                                            case "64":
                                                checkedIndex = 0;
                                                break;
                                            case "128":
                                                checkedIndex = 1;
                                                break;
                                            case "320":
                                                checkedIndex = 2;
                                                break;
                                            case "flac":
                                                checkedIndex = 3;
                                                break;
                                        }

                                        new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                                .setCheckedIndex(checkedIndex)
                                                .setCancelable(false)
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

                }else if (NetworkChecker.isWifiProxy(MusicPlayerActivity.this)){
                    if (tipDialog.isShowing())
                        tipDialog.dismiss();
                    FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                    //super.onBackPressed();
                }else {

                    //status =0;

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

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                        .setCheckedIndex(checkedIndex)
                                        .setCancelable(false)
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

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                        .setCheckedIndex(checkedIndex)
                                        .setCancelable(false)
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

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                        .setCheckedIndex(checkedIndex)
                                        .setCancelable(false)
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

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                        .setCheckedIndex(checkedIndex)
                                        .setCancelable(false)
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

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                        .setCheckedIndex(checkedIndex)
                                        .setCancelable(false)
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
                                items = new String[]{"64", "128", "320", "flac"};
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
                                    case "64":
                                        checkedIndex = 0;
                                        break;
                                    case "128":
                                        checkedIndex = 1;
                                        break;
                                    case "320":
                                        checkedIndex = 2;
                                        break;
                                    case "flac":
                                        checkedIndex = 3;
                                        break;
                                }

                                new QMUIDialog.CheckableDialogBuilder(MusicPlayerActivity.this)
                                        .setCheckedIndex(checkedIndex)
                                        .setCancelable(false)
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

        playRadio();

    }

    private void playRadio()
    {
        if (getPlayBean().getUrl() == null || playUrl == null){
            //null
            destroyNotification();
        }else {
            if (!getPlayBean().getUrl().equals(playUrl)) {
                destroyNotification();
                setCdRodio(0f);
                if (eventNextBean == null) {
                    eventNextBean = new EventBusBean(EventType.MUSIC_NEXT, getPlayBean().getUrl());
                } else {
                    eventNextBean.setType(EventType.MUSIC_NEXT);
                    eventNextBean.setObject(getPlayBean().getUrl());
                }
                EventBus.getDefault().post(eventNextBean);
                playUrl = getPlayBean().getUrl();
                getTimeBean().setTotalSecs(0);
                getTimeBean().setCurrSecs(0);
            }
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
                //updateNotification(1);
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
        if (!isPlaying()){
            Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
            stopService(intent);
            destroyNotification();
            super.onBackPressed();
            return;
        }
        SpUtil spUtil = new SpUtil();
        String player_setting = spUtil.getValue(MusicPlayerActivity.this, PLAYER_SETTING, PLAYER_PLAY_MODE);
        if (player_setting == null){
            showConfirmMessageDialog();
        }else if (player_setting.equals(""))
            showConfirmMessageDialog();
        else if (player_setting.equals(BACKGROUND)){
            //NOTHING
            super.onBackPressed();
        }else {
            Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
            stopService(intent);
            destroyNotification();
            super.onBackPressed();
        }
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (NetworkChecker.isVpnUsed()){

            if (tipDialog.isShowing())
                tipDialog.dismiss();

            //vpn
            FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
            Intent intent = VpnService.prepare(MusicPlayerActivity.this);
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

        }else if (NetworkChecker.isWifiProxy(MusicPlayerActivity.this)){
            if (tipDialog.isShowing())
                tipDialog.dismiss();
            FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
            super.onBackPressed();
        }
        super.onResume();
        updateTime(getTimeBean());
        rlCd.setRotation(getCdRodio());
        ivPoint.setRotation(-40f);
        //getHistoryData("0", MyApplication.getInstance().getToken());
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
            if (getPlayBean().getUrl() != null) {
                if (!getPlayBean().getUrl().equals("")) {
                    pauseMusic(false);
                    ivStatus.setImageResource(R.drawable.pause_selector);
                    if (ivPoint.getRotation() == -40f) {
                        startPointAnimat(-40f, 0f);
                    }
                }else {
                    if(musicStatus == PLAY_STATUS_PLAYING)
                    {
                        pauseMusic(true);
                        ivStatus.setImageResource(R.drawable.play_selector);
                    }
                }
            }else {
                if(musicStatus == PLAY_STATUS_PLAYING)
                {
                    pauseMusic(true);
                    ivStatus.setImageResource(R.drawable.play_selector);
                    getPlayBean().setUrl("");
                    playRadio();
                }
            }
        }
        else if(musicStatus == PLAY_STATUS_ERROR)
        {
            ivStatus.setImageResource(R.drawable.play_selector);
            getPlayBean().setUrl("");
            //playUrl = "";
            playRadio();
        }else if(musicStatus == PLAY_STATUS_COMPLETE)
        {
            ivStatus.setImageResource(R.drawable.play_selector);
            //getPlayBean().setUrl("");
            //playUrl = "";
            //playRadio();
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
        ivPoint.setPivotX(CommonUtil.dip2px(MusicPlayerActivity.this, 17));
        ivPoint.setPivotY(CommonUtil.dip2px(MusicPlayerActivity.this, 15));
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
        LogUtil.d("Animat:\nFrom-" + from + "\nEnd-" + end);
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
            LogUtil.d("AnimatStart:\nFrom-" + from + "\nEnd-" + end);
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
                /*if (lrcView.hasLrc())
                    lrcView.updateTime(getProgress());
                else
                    lrcView.setLabel("暂无歌词");*/
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
        pauseMusic(true);
        noMv(true);
        if (next){
            if (id >= 0 && id < name.length - 1){
                id += 1;
                getInfo(id);

                change_service();

            }else {
                FToastUtils.init().setRoundRadius(30).show("暂无下一首");
            }
        }else {
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putBundle("extras", extras);
        outState.putStringArray("name", name);
        outState.putStringArray("singer", singer);
        outState.putStringArray("imgURL", imgURL);
        outState.putStringArray("url", url);
        outState.putString("music_name", music_name);
        outState.putString("music_singer", music_singer);
        outState.putString("music_imgURL", music_imgURL);
        outState.putString("music_url", key);*/
        //outState.putString("key1", "value1");
        super.onSaveInstanceState(outState);
        LogUtil.d("onSaveInstanceState");
        //Log.i("INFO", "Activity3: onSaveInstanceState, key1[value1]");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            /*extras = savedInstanceState.getBundle("extras");
            name = savedInstanceState.getStringArray("name");
            singer = savedInstanceState.getStringArray("singer");
            imgURL = savedInstanceState.getStringArray("imgURL");
            url = savedInstanceState.getStringArray("url");
            music_name = savedInstanceState.getString("music_name");
            music_singer = savedInstanceState.getString("music_singer");
            music_imgURL = savedInstanceState.getString("music_imgURL");
            key = savedInstanceState.getString("music_url");
            showProgress(true);
            init_service();*/
            LogUtil.d("onRestoreInstanceState");
        }
        //Log.i("INFO", "Activity3: onRestoreInstanceState, key1[" + savedInstanceState.getString("key1") + "]");
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
                contentView.setTextViewText(R.id.tv_extra, getPlayBean().getSinger());
            }

            //contentView.setTextViewText(R.id.tv_extra, music_singer);

            //contentView.setImageViewResource(R.id.iv_play, R.drawable.ting);//button显示为正在播放

            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status", "pause");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);

            /*Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
            PendingIntent intent = PendingIntent.getActivity(this, 1,
                    notificationIntent, 0);*/

            mBuilder.setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher)
                    /*.setContentIntent(intent)*/
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
                contentView.setTextViewText(R.id.tv_extra, getPlayBean().getSinger());
            }

            //contentView.setTextViewText(R.id.tv_extra, music_singer);

            //contentView.setTextViewText(R.id.tv_title, music_name);

            //contentView.setTextViewText(R.id.tv_extra, music_singer);

            //contentView.setImageViewResource(R.id.iv_play, R.drawable.ting);//button显示为正在播放


            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status", "pause");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);

            /*Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
            PendingIntent intent = PendingIntent.getActivity(this, 1,
                    notificationIntent, 0);*/

            mBuilder.setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher)
                    /*.setContentIntent(intent)*/;//点击事件

            notification = mBuilder.build();
            notification.flags = notification.FLAG_NO_CLEAR;//设置通知点击或滑动时不被清除


            notificationManager.notify(PALYER_TAG, 101, notification);//开启通知

        }

    }

    /**
     * 更新状态栏
     */
    public void updateNotification(){

        if(musicStatus == PLAY_STATUS_PLAYING)
        {
            pauseMusic(true);
            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.ting);
            Intent intentPlay = new Intent(PALYER_TAG);//下一次意图，并设置action标记为"play"，用于接收广播时过滤意图信息
            //intentPlay.putExtra("status","pause");
            PendingIntent pIntentPlay = PendingIntent.getBroadcast(mContext, 2, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPlay);//为控件注册事件
            ivStatus.setImageResource(R.drawable.play_selector);
        }
        else if(musicStatus == PLAY_STATUS_PAUSE)
        {
            pauseMusic(false);
            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.play);
            Intent intentPause = new Intent(PALYER_TAG);
            //intentPause.putExtra("status","continue");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(mContext, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);
            ivStatus.setImageResource(R.drawable.pause_selector);
            if(ivPoint.getRotation() == -40f)
            {
                startPointAnimat(-40f, 0f);
            }
        }
        else if(musicStatus == PLAY_STATUS_ERROR || musicStatus == PLAY_STATUS_COMPLETE)
        {
            pauseMusic(false);
            notification.contentView.setImageViewResource(R.id.iv_play,R.drawable.play);
            Intent intentPause = new Intent(PALYER_TAG);
            intentPause.putExtra("status","continue");
            PendingIntent pIntentPause = PendingIntent.getBroadcast(mContext, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentView.setOnClickPendingIntent(R.id.iv_play, pIntentPause);
            playUrl = "";
            playRadio();
        }

        //【5】更新操作
        /*if (type==1){//播放中
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


        }*/


        notificationManager.notify(PALYER_TAG,101, notification);//开启通知

    }

    private void change_quality(){
        if(musicStatus == PLAY_STATUS_PLAYING)
        {
            pauseMusic(true);
            ivStatus.setImageResource(R.drawable.play_selector);
            getPlayBean().setUrl("");
            //playUrl = "";
            playRadio();
            updateTime(getTimeBean());
        }
        else if(musicStatus == PLAY_STATUS_PAUSE)
        {
            getPlayBean().setUrl("");
            //playUrl = "";
            playRadio();
            updateTime(getTimeBean());
            /*pauseMusic(false);
            ivStatus.setImageResource(R.drawable.pause_selector);
            if(ivPoint.getRotation() == -40f)
            {
                startPointAnimat(-40f, 0f);
            }*/
        }
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

        ////status =0;
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

    private void showDownloadProgress(Boolean status){
        if (status){
            tipDialog = new QMUITipDialog.Builder(MusicPlayerActivity.this)
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
                            ////player_status =false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                        }else if (url_302.equals("")){
                            ////player_status =false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                        }else {
                            FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path));
                            FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Music");

                            File path = new File(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Music");

                            String downPath = "";
                            String music_name_d = "";
                            String music_singer_d = "";
                            if (music_name.contains("/"))
                                music_name_d = music_name.replaceAll("/", "-");
                            else
                                music_name_d = music_name;
                            if (music_singer.contains("/"))
                                music_singer_d = music_singer.replaceAll("/", "-");
                            else
                                music_singer_d = music_singer;
                            downPath = path + "/" + music_name_d + "-" + music_singer_d;
                            if (url_302.contains(".mp3")){
                                downPath = downPath + ".mp3";
                            }else if (url_302.contains(".m4a")){
                                downPath = downPath + ".m4a";
                            }else if (url_302.contains(".ape")){
                                downPath = downPath + ".ape";
                            }else if (url_302.contains(".flac")){
                                downPath = downPath + ".flac";
                            }else if (url_302.contains(".dff")){
                                downPath = downPath + ".dff";
                            }else {
                                FToastUtils.init().setRoundRadius(30).show("未知文件名，请在下载完成后手动修改");
                            }
                            //String downPath = path + "/" + name[getAdapterPosition()] + "-" + singer[getAdapterPosition()] + ".mp3";
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            downManger = new DownManager(MusicPlayerActivity.this);
                            downManger.downSatrt(url_302, downPath, "是否要下载" + music_singer + "唱的《" + music_name + "》？");
                            /*//player_status =true;
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
                        ////player_status =false;
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

        /*Log.d("销毁","onDestroy()");
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
        //status =0;

        showProgress(true);

        init_service();
        */
        showProgress(true);
        init_service();

    }

    private void init_view() {

        /*if(getPlayBean().getTiming() == 0)
        {
            tvTip.setText("（回放）");
        }
        else if(getPlayBean().getTiming() == 1)
        {
            tvTip.setText("（直播）");
        }else if(getPlayBean().getTiming() == 2){
            tvTip.setText(getPlayBean().getSinger());
        }*/

        tvSubTitle.setText(getPlayBean().getSubname());
        tvTip.setText(getPlayBean().getSinger());
        lin = new LinearInterpolator();
        initPointAnimat();
        initCDAnimat();
        //Intent intent = new Intent(this, MusicService.class);
        //intent.putExtra("url", getPlayBean().getUrl());
        //startService(intent);
        Glide.with(this).load(getPlayBean().getImg()).apply(RequestOptions.placeholderOf(R.mipmap.icon_cd_default_bg)).into(ivCenter);
        Glide.with(this).load(R.mipmap.icon_gray_bg)
                .apply(bitmapTransform(new BlurTransformation(25, 3)).placeholder(R.mipmap.icon_gray_bg))
                .into(ivBg);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                position = getTimeBean().getTotalSecs() * progress / 100;
                tvNowTime.setText(WlTimeUtil.secdsToDateFormat(position, getTimeBean().getTotalSecs()));
                if (lrcView.hasLrc()) {
                    lrcView.updateTime((position + 1) * 1000);
                    lrcViewSingle.updateTime((position + 2) * 1000);
                    /*if (position >= 1)
                        lrcView.updateTime((position - 1) * 1000);*/
                    //lrcView.updateTime(position * 1000);
                }else {
                    lrcView.setLabel("暂无歌词");
                    lrcViewSingle.setLabel("暂无歌词");
                }
                LogUtil.d("MusicPlayer[seekbar]:" + position);
                LogUtil.d("MusicPlayer[seekbarProgress]:" + progress);
                LogUtil.d("MusicPlayerSec:" + getTimeBean().getCurrSecs());
                LogUtil.d("MusicPlayerSec*P:" + getTimeBean().getCurrSecs() * progress);
                LogUtil.d("MusicPlayerTotal:" + getTimeBean().getTotalSecs());
                LogUtil.d("MusicPlayerTotal*P:" + getTimeBean().getTotalSecs() * progress);
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
                //MyLog.d("position:" + position);
                LogUtil.d("MusicPlayer[seekbar]" + position);
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
        //playRadio();

    }

    private void getInfo(int id) {

        //status =0;
        //MUSIC_SERVICE = Music_player_manager.getService();
        if (Music_player_manager.getService() == null){
            FToastUtils.init().setRoundRadius(30).show("服务异常！");
            MusicPlayerActivity.super.onBackPressed();
        }else if (Music_player_manager.getService().equals("")){
            FToastUtils.init().setRoundRadius(30).show("服务异常！");
            MusicPlayerActivity.super.onBackPressed();
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
                Netease_quality = rw.getValue(MusicPlayerActivity.this, MUSIC_CONFIG, NETEASE);
                if (Netease_quality == null){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, NETEASE, "320000");
                    Netease_quality = "320000";
                }else if (Netease_quality.equals("")){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, NETEASE, "320000");
                    Netease_quality = "320000";
                }
                break;
            case "tencent":
                Tencent_quality = rw.getValue(MusicPlayerActivity.this, MUSIC_CONFIG, TENCENT);
                if (Tencent_quality == null){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, TENCENT, "192");
                    Tencent_quality = "192";
                }else if (Tencent_quality.equals("")){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, TENCENT, "192");
                    Tencent_quality = "192";
                }
                break;
            case "kugou":
                Kugou_quality = rw.getValue(MusicPlayerActivity.this, MUSIC_CONFIG, KUGOU);
                if (Kugou_quality == null){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, KUGOU, "128");
                    Kugou_quality = "128";
                }else if (Kugou_quality.equals("")){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, KUGOU, "128");
                    Kugou_quality = "128";
                }
                break;
            case "kuwo":
                Kuwo_quality = rw.getValue(MusicPlayerActivity.this, MUSIC_CONFIG, KUWO);
                if (Kuwo_quality == null){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, KUWO, "128");
                    Kuwo_quality = "128";
                }else if (Kuwo_quality.equals("")){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, KUWO, "128");
                    Kuwo_quality = "128";
                }
                break;
            case "migu":
                Migu_quality = rw.getValue(MusicPlayerActivity.this, MUSIC_CONFIG, MIGU);
                if (Migu_quality == null){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, MIGU, "128");
                    //migu_music_quality.setText("128");
                    Migu_quality = "128";
                }else if (Migu_quality.equals("")){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, MIGU, "128");
                    //migu_music_quality.setText("128");
                    Migu_quality = "128";
                }
                break;
            case "baidu":
                Baidu_quality = rw.getValue(MusicPlayerActivity.this, MUSIC_CONFIG, BAIDU);
                if (Baidu_quality == null){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, BAIDU, "128");
                    //baidu_music_quality.setText("128");
                    Baidu_quality = "128";
                }else if (Baidu_quality.equals("")){
                    rw.writeValue(MusicPlayerActivity.this, MUSIC_CONFIG, BAIDU, "128");
                    //baidu_music_quality.setText("128");
                    Baidu_quality = "128";
                }
                break;
        }
        if (Host_manager.getMusicHost() == null){
            FToastUtils.init().setRoundRadius(30).show("域名异常！");
            MusicPlayerActivity.super.onBackPressed();
        }else if (Host_manager.getMusicHost().equals("")){
            FToastUtils.init().setRoundRadius(30).show("域名异常！");
            MusicPlayerActivity.super.onBackPressed();
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
            MusicPlayerActivity.super.onBackPressed();
        }else if (key.equals("")){
            FToastUtils.init().setRoundRadius(30).show("链接异常！");
            MusicPlayerActivity.super.onBackPressed();
        }else {
            LogUtil.d("\n" +
                    "PlayBean:" + getPlayBean().getUrl() + "\n" +
                    "PlayUrl:" + playUrl + "\n" +
                    "302Url:" + Music_player_manager.getUrl() + "\n" +
                    "MainUrl:" + Music_player_manager.getMainMusicUrl() + "\n" +
                    "Name[id]:" + name[id] + "\n" +
                    "Singer[id]:" + singer[id] + "\n" +
                    "name:" + getPlayBean().getSubname() + "\n" +
                    "singer:" + getPlayBean().getSinger());
            if (Music_player_manager.getMainMusicUrl() != null && getPlayBean().getUrl() != null && Music_player_manager.getUrl() != null && name != null && getPlayBean().getSubname() != null && singer != null && getPlayBean().getSinger() != null){
                if (!key.equals(Music_player_manager.getUrl()) && !getPlayBean().getSubname().equals(name[id]) && !getPlayBean().getSinger().equals(singer[id]) || !getPlayBean().getUrl().equals(Music_player_manager.getUrl()) && !getPlayBean().getUrl().equals("") && !Music_player_manager.getUrl().equals("")){
                    LogUtil.d("MUSIC-STATUS:" + musicStatus);
                    if(musicStatus == PLAY_STATUS_PLAYING)
                    {
                        pauseMusic(true);
                        ivStatus.setImageResource(R.drawable.play_selector);
                        getPlayBean().setUrl("");
                        //playUrl = "";
                        playRadio();
                        updateTime(getTimeBean());
                    }
                    else if(musicStatus == PLAY_STATUS_PAUSE)
                    {
                        getPlayBean().setUrl("");
                        //playUrl = "";
                        playRadio();
                        updateTime(getTimeBean());
                    }
                    lrcView.setLabel("暂无歌词");
                    lrcViewSingle.setLabel("暂无歌词");
                }
            }
            LogUtil.i("KEY\n[\n" + key + "\n]");
            //key = url[id];
            music_name = name[id];
            music_singer = singer[id];
            music_imgURL = imgURL[id];
            getPlayBean().setSubname(music_name);
            getPlayBean().setSinger(music_singer);
            getPlayBean().setImg(music_imgURL);
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
            tipDialog = new QMUITipDialog.Builder(MusicPlayerActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在加载")
                    .create(false);
            tipDialog.show();
            ivStatus.setEnabled(true);
            //player_play.setEnabled(false);
            //player_stop.setEnabled(false);
            //FToastUtils.init().setRoundRadius(30).show("正在加载音乐，请稍候！");
        }else {
            if (tipDialog.isShowing())
                tipDialog.dismiss();
            ivStatus.setEnabled(true);
            //player_play.setEnabled(true);
            //player_stop.setEnabled(true);
            //FToastUtils.init().setRoundRadius(30).show("加载完成！");
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
        }else {
            Music_player_manager.setMainMusicUrl(key);
            new Thread(new Music_Checker_runtime(h1, key)).start();
        }
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

    private void showConfirmMessageDialog() {
        QMUIDialog.CheckBoxMessageDialogBuilder CheckedBoxDialog = new QMUIDialog.CheckBoxMessageDialogBuilder(MusicPlayerActivity.this);
        SpUtil spUtil = new SpUtil();
        CheckedBoxDialog
                .setTitle("是否继续后台播放?")
                .setMessage("记住我的选择")
                .setChecked(false)
                .setCancelable(true)
                .addAction("停止播放", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (CheckedBoxDialog.isChecked()){
                            spUtil.writeValue(MusicPlayerActivity.this, PLAYER_SETTING, PLAYER_PLAY_MODE, FORGROUND);
                        }
                        Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
                        stopService(intent);
                        destroyNotification();
                        dialog.dismiss();
                        MusicPlayerActivity.super.onBackPressed();
                    }
                })
                .addAction("后台播放", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (CheckedBoxDialog.isChecked()){
                            spUtil.writeValue(MusicPlayerActivity.this, PLAYER_SETTING, PLAYER_PLAY_MODE, BACKGROUND);
                        }
                        dialog.dismiss();
                        MusicPlayerActivity.super.onBackPressed();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @Override
    public boolean onPlayClick(long time) {
        LogUtil.d("MusicPlayer:" + time);
        if (getPlayBean().getUrl() != null) {
            if (!getPlayBean().getUrl().equals("")) {
                if(eventSeekBean == null)
                {
                    if(seekBean == null)
                    {
                        seekBean = new SeekBean();
                    }
                    seekBean.setPosition((int) time / 1000 - 1);
                    seekBean.setSeekingfinished(true);
                    seekBean.setShowTime(true);

                    eventSeekBean = new EventBusBean(EventType.MUSIC_SEEK_TIME, seekBean);
                }
                else
                {
                    if(seekBean == null)
                    {
                        seekBean = new SeekBean();
                    }
                    seekBean.setPosition((int) time / 1000 - 1);
                    seekBean.setSeekingfinished(true);
                    seekBean.setShowTime(true);
                    eventSeekBean.setType(EventType.MUSIC_SEEK_TIME);
                    eventSeekBean.setObject(seekBean);
                }
                EventBus.getDefault().post(eventSeekBean);
                if (!isPlaying())
                    pauseMusic(false);
                return true;
            }else
                return false;
        }else
            return false;
    }

    //网略请求回调
    private class MyCallback implements okhttp3.Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            int code2=response.code();
            if (code2==200){
                //得到服务器返回的具体内容
                String responseData=response.body().string();
                LogUtil.d("response:" + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lrcView.loadLrc(responseData);
                        lrcViewSingle.loadLrc(responseData);
                        //lrcView = findViewById(R.id.lrc_view);
                        //lrcView.loadLrc("");
                        //lrcView.loadLrc(responseData);
                    }
                });
                //lrcView.loadLrc(responseData);
            }

        }
        @Override
        public void onFailure(Call call, IOException e){
            if (e instanceof SocketTimeoutException) {
                //判断超时异常
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("访问超时");
                //sendMyMessage(-1);
            }
            if (e instanceof ConnectException) {
                //判断连接异常，
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("连接异常");
                //sendMyMessage(-2);
            }
        }
    }

    //网略请求回调
    private class MvCheckCallback implements okhttp3.Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            int code2=response.code();
            if (code2==200){
                //得到服务器返回的具体内容
                String responseData=response.body().string();
                LogUtil.d("response:" + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            FToastUtils.init().setRoundRadius(30).show("Error" + new JSONObject(responseData).getString("code") + ":" + new JSONObject(responseData).getString("msg"));
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            noMv(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            noMv(true);
                        }

                    }
                });
                //lrcView.loadLrc(responseData);
            }else if (code2 == 302){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tipDialog.isShowing())
                            tipDialog.dismiss();
                        noMv(false);
                        pauseMusic(true);
                        Intent intent = new Intent(MusicPlayerActivity.this, X5WebGameActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", mv_url_final);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tipDialog.isShowing())
                            tipDialog.dismiss();
                        noMv(true);
                    }
                });
            }

        }

        @Override
        public void onFailure(Call call, IOException e){
            if (e instanceof SocketTimeoutException) {
                //判断超时异常
                noMv(true);
                FToastUtils.init().setRoundRadius(30).show("访问超时");
                //sendMyMessage(-1);
            }
            if (e instanceof ConnectException) {
                //判断连接异常，
                noMv(true);
                FToastUtils.init().setRoundRadius(30).show("连接异常");
                //sendMyMessage(-2);
            }
        }
    }

    //网略请求回调
    private class MyMusicDetailCallback implements okhttp3.Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            int code2=response.code();
            mv_url_final = "";
            if (code2==200){
                //得到服务器返回的具体内容
                String responseData=response.body().string();
                LogUtil.d("response:" + responseData);
                if (Music_player_manager.getService() == null){
                    FToastUtils.init().setRoundRadius(30).show("服务异常！");
                    //MusicPlayerActivity.super.onBackPressed();
                }else if (Music_player_manager.getService().equals("")){
                    FToastUtils.init().setRoundRadius(30).show("服务异常！");
                    //MusicPlayerActivity.super.onBackPressed();
                }else {
                    final SpUtil rw = new SpUtil();
                    switch (Music_player_manager.getService()){
                        case "0":
                            MUSIC_SERVICE = "netease";
                            neteaseMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, NETEASE);
                            if (neteaseMvQuality == null){
                                neteaseMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, NETEASE, neteaseMvQuality);
                            }else if (neteaseMvQuality.equals("")){
                                neteaseMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, NETEASE, neteaseMvQuality);
                            }
                            break;
                        case "1":
                            MUSIC_SERVICE = "tencent";
                            tencentMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, TENCENT);
                            if (tencentMvQuality == null){
                                tencentMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, TENCENT, tencentMvQuality);
                            }else if (tencentMvQuality.equals("")){
                                tencentMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, TENCENT, tencentMvQuality);
                            }
                            break;
                        case "2":
                            MUSIC_SERVICE = "kugou";
                            kugouMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, KUGOU);
                            if (kugouMvQuality == null) {
                                kugouMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, KUGOU, kugouMvQuality);
                            }else if (kugouMvQuality.equals("")) {
                                kugouMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, KUGOU, kugouMvQuality);
                            }
                            break;
                        case "3":
                            MUSIC_SERVICE = "kuwo";
                            kuwoMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, KUWO);
                            if (kuwoMvQuality == null){
                                kuwoMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, KUWO, kuwoMvQuality);
                            }else if (kuwoMvQuality.equals("")){
                                kuwoMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, KUWO, kuwoMvQuality);
                            }
                            break;
                        case "4":
                            MUSIC_SERVICE = "migu";
                            miguMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, MIGU);
                            if (miguMvQuality == null){
                                miguMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, MIGU, miguMvQuality);
                            }else if (miguMvQuality.equals("")){
                                miguMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, MIGU, miguMvQuality);
                            }
                            break;
                        case "5":
                            MUSIC_SERVICE = "baidu";
                            baiduMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, BAIDU);
                            if (baiduMvQuality == null){
                                baiduMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, BAIDU, baiduMvQuality);
                            }else if (baiduMvQuality.equals("")){
                                baiduMvQuality = "480";
                                rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, BAIDU, baiduMvQuality);
                            }
                            break;
                    }
                }
                switch (MUSIC_SERVICE){
                    case "netease":
                        try {
                            JSONObject data = new JSONObject(responseData);
                            if (data.getInt("code") == 200){
                                if ((data.getJSONObject("data")).getInt("code") == 200) {
                                    JSONArray music_data = data.getJSONObject("data").getJSONArray("songs");
                                    String mv_id = new JSONObject(music_data.get(0).toString()).getString("mv");
                                    if (mv_id == null)
                                        noMv(true);
                                    else if (mv_id.equals(""))
                                        noMv(true);
                                    else if (mv_id.equals("0"))
                                        noMv(true);
                                    else {
                                        noMv(false);
                                        setMV_ICON(MUSIC_SERVICE, mv_id, neteaseMvQuality);
                                    }
                                }else
                                    noMv(true);
                            }else {
                                noMv(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "tencent":
                        try {
                            JSONObject data = new JSONObject(responseData);
                            if (data.getInt("code") == 200){
                                JSONArray music_data = data.getJSONArray("data");
                                    String mv_id = new JSONObject(new JSONObject(music_data.get(0).toString()).getString("mv")).getString("vid");
                                    if (mv_id == null)
                                        noMv(true);
                                    else if (mv_id.equals(""))
                                        noMv(true);
                                    else if (mv_id.equals("0"))
                                        noMv(true);
                                    else {
                                        noMv(false);
                                        setMV_ICON(MUSIC_SERVICE, mv_id, tencentMvQuality);
                                    }
                            }else {
                                noMv(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "kugou":
                        try {
                            JSONObject data = new JSONObject(responseData);
                            if (data.getInt("code") == 200){
                                JSONArray music_data = new JSONArray(new JSONObject(data.getString("data")).getString("info"));
                                String mv_id = new JSONObject(music_data.get(0).toString()).getString("mvhash");
                                if (mv_id == null)
                                    noMv(true);
                                else if (mv_id.equals(""))
                                    noMv(true);
                                else if (mv_id.equals("0"))
                                    noMv(true);
                                else {
                                    noMv(false);
                                    setMV_ICON(MUSIC_SERVICE, mv_id, kugouMvQuality);
                                }
                            }else {
                                noMv(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.d("MusicPlayer[Exception]" + e.toString());
                        }
                        break;
                    case "kuwo":
                        /*try {
                            JSONObject data = new JSONObject(responseData);
                            if (data.getInt("code") == 200){
                                JSONArray music_data = new JSONObject(data.getString("data")).getJSONArray("info");
                                String mv_id = new JSONObject(music_data.get(0).toString()).getString("mvhash");
                                if (mv_id == null)
                                    noMv(true);
                                else if (mv_id.equals(""))
                                    noMv(true);
                                else if (mv_id.equals("0"))
                                    noMv(true);
                                else {
                                    noMv(false);
                                    setMV_ICON(MUSIC_SERVICE, mv_id, kugouMvQuality);
                                }
                            }else {
                                noMv(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        noMv(true);
                        break;
                    case "migu":
                        try {
                            JSONObject data = new JSONObject(responseData);
                            if (data.getInt("code") == 200){
                                JSONArray music_data = new JSONObject(data.getString("data")).getJSONArray("resultList");
                                String mv_id = new JSONObject(new JSONObject(music_data.get(0).toString()).getJSONArray("mvList").get(0).toString()).getString("copyrightId");
                                if (mv_id == null)
                                    noMv(true);
                                else if (mv_id.equals(""))
                                    noMv(true);
                                else if (mv_id.equals("0"))
                                    noMv(true);
                                else {
                                    noMv(false);
                                    setMV_ICON(MUSIC_SERVICE, mv_id, miguMvQuality);
                                }
                            }else {
                                noMv(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "baidu":
                        /*try {
                            JSONObject data = new JSONObject(responseData);
                            if (data.getInt("code") == 200){
                                JSONArray music_data = new JSONObject(data.getString("data")).getJSONArray("info");
                                String mv_id = new JSONObject(music_data.get(0).toString()).getString("mvhash");
                                if (mv_id == null)
                                    noMv(true);
                                else if (mv_id.equals(""))
                                    noMv(true);
                                else if (mv_id.equals("0"))
                                    noMv(true);
                                else {
                                    noMv(false);
                                    setMV_ICON(MUSIC_SERVICE, mv_id, kugouMvQuality);
                                }
                            }else {
                                noMv(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        noMv(true);
                        break;
                }
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //lrcView.loadLrc(responseData);
                        //lrcViewSingle.loadLrc(responseData);
                        //lrcView = findViewById(R.id.lrc_view);
                        //lrcView.loadLrc("");
                        //lrcView.loadLrc(responseData);
                    }
                });*/
                //lrcView.loadLrc(responseData);
            }

        }

        @Override
        public void onFailure(Call call, IOException e){
            if (e instanceof SocketTimeoutException) {
                //判断超时异常
                noMv(true);
                FToastUtils.init().setRoundRadius(30).show("访问超时");
                //sendMyMessage(-1);
            }
            if (e instanceof ConnectException) {
                //判断连接异常，
                noMv(true);
                FToastUtils.init().setRoundRadius(30).show("连接异常");
                //sendMyMessage(-2);
            }
        }
    }

    private void setMV_ICON(String music_service, String mvId, String MvQuality) {

        String mv_url = "";

        if (from == null){
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            //super.onBackPressed();
        }else if (from.equals("")){
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            //super.onBackPressed();
        }else if (from.equals("SearchPage")) {
            if (Host_manager.getMusicHost() != null){
                if (!Host_manager.getMusicHost().equals("")){
                    mv_url = Host_manager.getMusicHost() + "/" + music_service + "/mvUrl?id=" + mvId + "&quality=" + MvQuality;
                    setMV_intent(mv_url);
                }else {
                    FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                    noMv(true);
                }
            }else {
                FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                noMv(true);
            }
        }else if (from.equals("HotMusic")){
            if (Host_manager.getHotMusicHost() != null){
                if (!Host_manager.getHotMusicHost().equals("")){
                    mv_url = Host_manager.getHotMusicHost() + "/" + music_service + "/mvUrl?id=" + mvId + "&quality=" + MvQuality;
                    setMV_intent(mv_url);
                }else {
                    FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                    noMv(true);
                }
            }else {
                FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                noMv(true);
            }
        }else if (from.equals("MusicList")){
            if (Host_manager.getHotMusicHost() != null){
                if (!Host_manager.getHotMusicHost().equals("")){
                    mv_url = Host_manager.getHotMusicHost() + "/" + music_service + "/mvUrl?id=" + mvId + "&quality=" + MvQuality;
                    setMV_intent(mv_url);
                }else {
                    FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                    noMv(true);
                }
            }else {
                FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                noMv(true);
            }
        }else {
            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
            noMv(true);
            //super.onBackPressed();
        }


        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });*/

        //setMV_intent(mv_url);
    }

    private void setMV_intent(String mv_url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                noMv(false);
                mv_icon.setClickable(true);
                mv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*new Thread(){
                            @Override
                            public void run() {

                                String key = "";
                                try {
                                    key = Encode.work(String.valueOf((int) (System.currentTimeMillis() / 1000)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                HttpUtil.sendOkHttpRequest(mv_url, new MvCheckCallback(), key);
                                mv_url_final = mv_url;
                                //tipDialog.show();
                                showProgress(true);
                            }
                        }.start();*/
                        pauseMusic(true);
                        Intent intent = new Intent(MusicPlayerActivity.this, X5WebGameActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", mv_url);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }
        });
    }

    private void noMv(boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    mv_icon.setVisibility(View.GONE);
                    mv_icon.setClickable(false);
                }else {
                    mv_icon.setVisibility(View.VISIBLE);
                    mv_icon.setClickable(true);
                }
                //lrcView.loadLrc(responseData);
                //lrcViewSingle.loadLrc(responseData);
                //lrcView = findViewById(R.id.lrc_view);
                //lrcView.loadLrc("");
                //lrcView.loadLrc(responseData);
            }
        });
        //mv_icon.setVisibility(View.GONE);
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

                    Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
                    client=new OkHttpClient();

                    LogUtil.d("Music_Checker_Handle");

                    if (String.valueOf(msg.obj).equals("访问成功")){

                        String url_302 = Music_player_manager.getUrl();
                        if (url_302 == null) {
                            //player_status =false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            getPlayBean().setUrl("");
                            //playUrl = "";
                            playRadio();
                            stopService(intent);
                            lrcView.setLabel("暂无歌词");
                            lrcViewSingle.setLabel("暂无歌词");
                            updateTime(getTimeBean());
                            ivStatus.setImageResource(R.drawable.play_selector);
                            ivCenter.setLongClickable(false);
                            destroyNotification();
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            MusicPlayerActivity.super.onBackPressed();
                        }else if (url_302.equals("")){
                            //player_status =false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            getPlayBean().setUrl("");
                            playRadio();
                            stopService(intent);
                            lrcView.setLabel("暂无歌词");
                            lrcViewSingle.setLabel("暂无歌词");
                            updateTime(getTimeBean());
                            ivStatus.setImageResource(R.drawable.play_selector);
                            ivCenter.setLongClickable(false);
                            destroyNotification();
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            MusicPlayerActivity.super.onBackPressed();
                        }/*else if (url_302.contains(".ape")) {
                            //player_status =false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("检测到音乐源为ape，暂不支持试听，仅支持下载");
                        }else if (url_302.contains(".dff")) {
                            //player_status =false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).showLong("检测到音乐源为dsd，暂不支持试听，仅支持下载，下载后请使用VIPER HIFI扫描该文件夹后进行播放");
                        }*/else {
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            if (getPlayBean().getUrl() == null)
                                getPlayBean().setUrl(url_302);
                            else if (!getPlayBean().getUrl().equals(url_302))
                                getPlayBean().setUrl(url_302);
                            //destroyNotification();
                            //initNotification();
                            //playUrl = url_302;
                            //player_status =true;
                            //Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
                            intent.putExtra("url", url_302);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(intent);
                            }else {
                                startService(intent);
                            }
                            playRadio();

                            lrcView.setLabel("暂无歌词");
                            lrcViewSingle.setLabel("暂无歌词");

                            //client=new OkHttpClient();

                            new Thread(){
                                @Override
                                public void run() {

                                    String lrc_url = "";
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
                                    String key = "";
                                    try {
                                        key = Encode.work(String.valueOf((int) (System.currentTimeMillis() / 1000)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (from == null){
                                        FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        //super.onBackPressed();
                                    }else if (from.equals("")){
                                        FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        //super.onBackPressed();
                                    }else if (from.equals("SearchPage")) {
                                        if (Host_manager.getMusicHost() != null){
                                            if (!Host_manager.getMusicHost().equals("")){
                                                lrc_url = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/lrc?id=" + url[id];
                                                LogUtil.d("LRC:" + lrc_url);
                                                HttpUtil.sendOkHttpRequest(lrc_url, new MyCallback(),key);
                                            }else {
                                                FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                            }
                                        }else {
                                            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        }
                                    }else if (from.equals("HotMusic")){
                                        if (Host_manager.getHotMusicHost() != null){
                                            if (!Host_manager.getHotMusicHost().equals("")){
                                                lrc_url = Host_manager.getHotMusicHost() + "/" + MUSIC_SERVICE + "/lrc?id=" + url[id];
                                                LogUtil.d("LRC:" + lrc_url);
                                                HttpUtil.sendOkHttpRequest(lrc_url, new MyCallback(),key);
                                            }else {
                                                FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                            }
                                        }else {
                                            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        }
                                    }else if (from.equals("MusicList")){
                                        if (Host_manager.getHotMusicHost() != null){
                                            if (!Host_manager.getHotMusicHost().equals("")){
                                                lrc_url = Host_manager.getHotMusicHost() + "/" + MUSIC_SERVICE + "/lrc?id=" + url[id];
                                                LogUtil.d("LRC:" + lrc_url);
                                                HttpUtil.sendOkHttpRequest(lrc_url, new MyCallback(),key);
                                            }else {
                                                FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                            }
                                        }else {
                                            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        }
                                    }else {
                                        FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        //super.onBackPressed();
                                    }

                                    //LogUtil.d("LRC:" + lrc_url);
                                    //HttpUtil.sendOkHttpRequest(content, new MyCallback(),key);

                                }
                            }.start();
                            //lrcView.loadLrc("");
                            ivCenter.setLongClickable(true);
                            ivCenter.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Intent intent = new Intent(MusicPlayerActivity.this, com.autumn.framework.music.MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("key", url_302);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    MusicPlayerActivity.super.onBackPressed();
                                    return false;
                                }
                            });

                        }
                    }else{

                        //访问失败
                        //player_status =false;
                        if (tipDialog.isShowing())
                            tipDialog.dismiss();
                        /*if(musicStatus == PLAY_STATUS_PLAYING)
                        {
                            pauseMusic(true);
                        }*/
                        getPlayBean().setUrl("");
                        //playUrl = "";
                        playRadio();
                        stopService(intent);
                        lrcView.setLabel("暂无歌词");
                        lrcViewSingle.setLabel("暂无歌词");
                        updateTime(getTimeBean());
                        /*initCDAnimat();
                        //rlCd.setRotation(getCdRodio());
                        if(ivPoint.getRotation() == 0f)
                        {
                            startPointAnimat(0f, -40f);
                        }else
                            ivPoint.setRotation(-40f);*/
                        ivStatus.setImageResource(R.drawable.play_selector);
                        ivCenter.setLongClickable(false);
                        destroyNotification();
                        //initNotification();
                        //stopService(intent);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        //mRefreshLayout.finishRefresh();
                        //mRefreshLayout.finishLoadmore();

                    }

                    //client=new OkHttpClient();

                    new Thread(){
                        @Override
                        public void run() {

                            String music_detail_url = "";
                            //String lrc_url = "";
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
                            if (MUSIC_SERVICE == "kugou"){

                                String[] mvList = Data_manager_kg_mv.getUrl_all();

                                final SpUtil rw = new SpUtil();

                                kugouMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, KUGOU);
                                if (kugouMvQuality == null) {
                                    kugouMvQuality = "480";
                                    rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, KUGOU, kugouMvQuality);
                                }else if (kugouMvQuality.equals("")) {
                                    kugouMvQuality = "480";
                                    rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, KUGOU, kugouMvQuality);
                                }

                                if (mvList[id] == null){
                                    noMv(true);
                                    FToastUtils.init().setRoundRadius(30).show("数据为空");
                                }else if (mvList[id].equals("")){
                                    noMv(true);
                                    FToastUtils.init().setRoundRadius(30).show("数据为空");
                                }else {
                                    noMv(false);
                                    setMV_ICON(MUSIC_SERVICE, mvList[id], kugouMvQuality);
                                    //FToastUtils.init().setRoundRadius(30).show(mvList[id]);
                                }

                            }else if (MUSIC_SERVICE == "migu"){
                                /*String[] mvList = Data_manager_migu_mv.getUrl_all();
                                final SpUtil rw = new SpUtil();
                                miguMvQuality = rw.getValue(MusicPlayerActivity.this, MV_CONFIG, MIGU);
                                if (miguMvQuality == null){
                                    miguMvQuality = "480";
                                    rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, MIGU, miguMvQuality);
                                }else if (miguMvQuality.equals("")){
                                    miguMvQuality = "480";
                                    rw.writeValue(MusicPlayerActivity.this, MV_CONFIG, MIGU, miguMvQuality);
                                }
                                if (mvList[id] == null){
                                    noMv(true);
                                    FToastUtils.init().setRoundRadius(30).show("数据为空");
                                }else if (mvList[id].equals("")){
                                    noMv(true);
                                    FToastUtils.init().setRoundRadius(30).show("数据为空");
                                }else {
                                    noMv(false);
                                    setMV_ICON(MUSIC_SERVICE, mvList[id], miguMvQuality);
                                    //FToastUtils.init().setRoundRadius(30).show(mvList[id]);
                                }*/
                                noMv(true);
                            }else {
                                String key = "";
                                try {
                                    key = Encode.work(String.valueOf((int) (System.currentTimeMillis() / 1000)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (from == null) {
                                    FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                    //super.onBackPressed();
                                } else if (from.equals("")) {
                                    FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                    //super.onBackPressed();
                                } else if (from.equals("SearchPage")) {
                                    if (Host_manager.getMusicHost() != null) {
                                        if (!Host_manager.getMusicHost().equals("")) {
                                            music_detail_url = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/song?id=" + url[id];
                                            //LogUtil.d("LRC:" + lrc_url);
                                            HttpUtil.sendOkHttpRequest(music_detail_url, new MyMusicDetailCallback(), key);
                                        } else {
                                            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        }
                                    } else {
                                        FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                    }
                                } else if (from.equals("HotMusic")) {
                                    if (Host_manager.getHotMusicHost() != null) {
                                        if (!Host_manager.getHotMusicHost().equals("")) {
                                            music_detail_url = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/song?id=" + url[id];
                                            //LogUtil.d("LRC:" + lrc_url);
                                            HttpUtil.sendOkHttpRequest(music_detail_url, new MyMusicDetailCallback(), key);
                                        } else {
                                            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        }
                                    } else {
                                        FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                    }
                                } else if (from.equals("MusicList")) {
                                    if (Host_manager.getHotMusicHost() != null) {
                                        if (!Host_manager.getHotMusicHost().equals("")) {
                                            music_detail_url = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/song?id=" + url[id];
                                            //LogUtil.d("LRC:" + lrc_url);
                                            HttpUtil.sendOkHttpRequest(music_detail_url, new MyMusicDetailCallback(), key);
                                        } else {
                                            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                        }
                                    } else {
                                        FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                    }
                                } else {
                                    FToastUtils.init().setRoundRadius(30).show("程序异常，请稍候重试");
                                    //super.onBackPressed();
                                }

                            }

                            //LogUtil.d("LRC:" + lrc_url);
                            //HttpUtil.sendOkHttpRequest(content, new MyCallback(),key);

                        }
                    }.start();

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    class mypagerAdapter extends PagerAdapter {
        private ArrayList<View> pageview1;
        public mypagerAdapter(ArrayList<View> pageview1){
            this.pageview1 = pageview1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d("MusicViewPagerDestroy",position+"");
            if (pageview1.get(position)!=null) {
                container.removeView(pageview1.get(position));
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageview1.get(position));
            Log.d("MusicViewPagerInstanti",position+"");
            return pageview1.get(position);
        }

        @Override
        public int getCount() {
            return pageview1.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object==view;
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


        @Override
        //切换view时，下方圆点的变化。
        public void onPageSelected(int position) {
            tips[position].setBackgroundResource(R.drawable.ic_play_page_indicator_selected);
            //这个图片就是选中的view的圆点
            for(int i=0;i<pageview.size();i++){
                if (position != i) {
                    tips[i].setBackgroundResource(R.drawable.ic_play_page_indicator_unselected);
                    //这个图片是未选中view的圆点
                }
            }
        }
    }

}
