package com.autumn.framework.update;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;
import com.autumn.circleprogress.DonutProgress;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.View.ScanView;
import com.autumn.framework.data.App_info;
import com.autumn.framework.data.DataUtil;
import com.autumn.framework.data.Data_editer;
import com.autumn.framework.data.FileDownloadThread;
import com.autumn.framework.data.SpUtil;
import com.autumn.framework.entity.AppInfo;
import com.autumn.framework.service.DownloadService;
import com.autumn.framework.update.data.Update_url_manager;
import com.autumn.framework.update.runtime.Update_Url_Checker_runtime;
import com.autumn.framework.util.Utils;
import com.autumn.progressbar.CircleProgressBar;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.data.VersionUtil;
import com.autumn.sdk.manager.update.update_manager;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import cn.hotapk.fastandrutils.utils.FToastUtils;

import static com.autumn.framework.entity.AppInfo.STATUS_COMPLETE;
import static com.autumn.framework.entity.AppInfo.STATUS_CONNECTING;
import static com.autumn.framework.entity.AppInfo.STATUS_DOWNLOADING;
import static com.autumn.framework.entity.AppInfo.STATUS_INSTALLED;
import static com.autumn.reptile.MyApplication.getAppContext;

/**
 * Created by zhang on 2018/6/22.
 */

public class AppUpdateActivity extends BaseActivity {

    public static String software_version;
    public static String changelog;
    public static String download_url;
    public static String download_code;
    public static String force_update;
    public static boolean version_release = false;
    public static boolean from_LoginActivity = false;
    public static boolean changelog_now_version = false;
    public static boolean FindFullUpdateManual = false;
    private String TAG = AppUpdateActivity.class.getSimpleName();
    private DonutProgress donutProgress;
    private CircleProgressBar download_progress;
    private ProgressBar mProgressbar;
    private Uri apk_uri;
    private long mExitTime;
    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";
    private static final String IS_AUTO_LOGIN = "Is_auto_login";

    private String UPDATE_INFO_MAIN = "Update_info_main";
    private String BUILD_MAIN = "Build_main";
    private String VERSION_MAIN = "Version_main";
    private String CHANGELOG_MAIN = "Changelog_main";
    private String UPDATE_DATE_MAIN = "Update_date_main";

    private String UPDATE_INFO_PATCH = "Update_info_patch";
    private String BUILD_PATCH = "Build_patch";
    private String CHANGELOG_PATCH = "Changelog_patch";

    private String fileName = null;
    private ScanView mScanView;
    private DownloadReceiver mReceiver;
    public static File mDownloadDir;
    private TextView speedText;
    private TextView versionText;
    private Button update_download_complete;
    private Button update_download;
    private LinearLayout update_downloading;
    private Button update_go_on;
    private Button update_stop;
    private Button update_cancel;
    private String UPDATE_STATE = "Update_state";
    private DownloadInfo downloadInfo;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_update);
        mDownloadDir = new File(Environment.getExternalStorageDirectory() + getString(R.string.files_path) + getString(R.string.logs_path) , "download");
        //LogUtil.i("progress" + mDownloadDir.toString());
        //register();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }

        initState();

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);
        //SUtils.initialize(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(AppUpdateActivity.this, R.color.colorPrimaryDark));
        }

        new Thread(new Runnable() {//利用Runnable接口实现线程
            @Override
            public void run() {

                FindUpdateActivity.update_notice = false;

                //stop_update_notice();
                if (!FindUpdateActivity.update_notice) {
                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    Objects.requireNonNull(manager).cancelAll();
                }

            }
        }).start();

        /*new Thread(new Runnable() {//利用Runnable接口实现线程
            @Override
            public void run() {

                FindUpdateActivity.update_notice = false;

                while (!FindUpdateActivity.update_notice) {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                }

            }
        }).start();*/

        donutProgress = (DonutProgress) findViewById(R.id.app_donut_progress);
        download_progress = (CircleProgressBar) findViewById(R.id.app_download_progressbar);
        mProgressbar = (ProgressBar) findViewById(R.id.app_update_progress);


        update_download = (Button) findViewById(R.id.update_download);
        update_downloading = (LinearLayout) findViewById(R.id.update_downloading);
        update_go_on = (Button)findViewById(R.id.update_go_on);
        update_stop = (Button)findViewById(R.id.update_stop);
        update_cancel = (Button)findViewById(R.id.update_cancel);
        update_download_complete = (Button) findViewById(R.id.update_download_complete);
        versionText = (TextView) findViewById(R.id.versionText);
        speedText = (TextView) findViewById(R.id.speedText);
        mScanView = (ScanView) findViewById(R.id.scanView_update);


        if (!changelog_now_version) {
            software_version = update_manager.get_software_version();
            changelog = update_manager.get_changelog();
            download_code = update_manager.get_download_code();
            download_url = update_manager.get_download_url();
            force_update = update_manager.get_force_update();
        }

        if (software_version == null || changelog == null){

            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍后重试！");
            Intent intent = new Intent(AppUpdateActivity.this, MainActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(AppUpdateActivity.this);

        }else if (software_version.equals("")){

            FToastUtils.init().setRoundRadius(30).show("程序异常，请稍后重试！");
            Intent intent = new Intent(AppUpdateActivity.this, MainActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(AppUpdateActivity.this);

        }

        //初始化
        if (changelog_now_version) {
            update_download.setVisibility(View.GONE);
            update_downloading.setVisibility(View.GONE);
            update_download_complete.setVisibility(View.GONE);
            update_go_on.setVisibility(View.GONE);
            update_stop.setVisibility(View.GONE);
            update_cancel.setVisibility(View.GONE);
            versionText.setVisibility(View.VISIBLE);
            versionText.setText(getString(R.string.app_version));
            speedText.setVisibility(View.VISIBLE);
        } else {
            update_download.setVisibility(View.VISIBLE);
            update_downloading.setVisibility(View.GONE);
            update_download_complete.setVisibility(View.GONE);
            update_go_on.setVisibility(View.GONE);
            update_stop.setVisibility(View.GONE);
            update_cancel.setVisibility(View.GONE);
            versionText.setVisibility(View.VISIBLE);
            versionText.setText(getString(R.string.app_version));
            speedText.setVisibility(View.VISIBLE);

            unRegister();
            DownloadService.destory(AppUpdateActivity.this);
            cancel("update");

        }

        //写入更新内容
        TextView software_version_new = (TextView) findViewById(R.id.software_version_new);
        software_version_new.setText(software_version);

        TextView app_new_changelog = (TextView) findViewById(R.id.app_new_changelog);
        if (changelog == null) {
            app_new_changelog.setText("暂无更新日志");
        } else if (changelog.equals("")) {
            app_new_changelog.setText("暂无更新日志");
        } else {
            if (changelog_now_version  && changelog.equals("恢复最新正式版本")){
                app_new_changelog.setText("暂无更新日志");
            }else {
                app_new_changelog.setText(changelog.replaceAll("\\[换行]", "\n"));
            }
        }

        update_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App_info.type.equals("beta") && FindFullUpdateManual){
                    showMessageNegativeDialog();
                }else if (!update_manager.get_download_url().equals("") && update_manager.get_download_url().contains("meternity.cn/api/LanZouApi.php?url=")){
                    FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                    register();

                    update_download.setEnabled(false);
                    update_stop.setEnabled(true);
                    update_cancel.setEnabled(true);
                    update_go_on.setEnabled(true);
                    update_download_complete.setEnabled(true);

                    mScanView.start();

                    donutProgress.setVisibility(View.GONE);
                    download_progress.setVisibility(View.VISIBLE);
                    update_download.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.VISIBLE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.VISIBLE);
                    update_cancel.setVisibility(View.VISIBLE);
                    versionText.setVisibility(View.VISIBLE);
                    speedText.setVisibility(View.GONE);

                    new Thread(new Update_Url_Checker_runtime(h1, update_manager.get_download_url())).start();
                }else{
                    FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                    register();

                    update_download.setEnabled(false);
                    update_stop.setEnabled(true);
                    update_cancel.setEnabled(true);
                    update_go_on.setEnabled(true);
                    update_download_complete.setEnabled(true);

                    mScanView.start();

                    donutProgress.setVisibility(View.GONE);
                    download_progress.setVisibility(View.VISIBLE);
                    update_download.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.VISIBLE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.VISIBLE);
                    update_cancel.setVisibility(View.VISIBLE);
                    versionText.setVisibility(View.VISIBLE);
                    speedText.setVisibility(View.GONE);

                    //Data_editer.deleteDirectory(path);

                    //doDownload();
                    AppInfo appInfo = new AppInfo();
                    //LogUtil.i("progress" + appInfo.getStatus());
                    if (appInfo.getStatus() == STATUS_DOWNLOADING || appInfo.getStatus() == STATUS_CONNECTING) {
                        pause("update");
                    } else if (appInfo.getStatus() == STATUS_COMPLETE) {
                        install(appInfo);
                    } else if (appInfo.getStatus() == STATUS_INSTALLED) {
                        unInstall();
                    } else {
                        if (!update_manager.get_download_url().equals("")) {
                            appInfo.setUrl(update_manager.get_download_url());
                        }else {
                            appInfo.setUrl(null);
                            FToastUtils.init().setRoundRadius(30).show("下载链接非法，请联系管理员！");
                        }
                        download(1, "update", appInfo);
                    }
                }

                //Intent intent = new Intent(AppUpdateActivity.this, DownloadService.class);
                //startService(intent);// 启动服务

            }
        });

        update_download_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取SD卡路径
                /*String path = Environment.getExternalStorageDirectory()
                        + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                String fileName = "app.apk";
                String filepath = path + "/" + fileName;

                setPermission(path);
                installNormal(AppUpdateActivity.this, filepath);*/

                update_download_complete.setEnabled(false);

                SpUtil rw = new SpUtil();
                if (DataUtil.find_words(software_version, "Patch")) {
                    rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_PATCH, BUILD_PATCH, VersionUtil.patch_version(AppUpdateActivity.this, software_version));
                    rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_PATCH, CHANGELOG_PATCH, changelog);
                } else if (DataUtil.find_words(software_version, "SP")) {
                    rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN, VersionUtil.sp_version(AppUpdateActivity.this, software_version));
                    rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN, changelog);
                } else {
                    rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN, VersionUtil.build_version(AppUpdateActivity.this, software_version));
                    rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN, changelog);
                }

                // 获取SD卡路径
                String path = Environment.getExternalStorageDirectory()
                        + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                //String fileName = "app.apk";
                AppInfo appInfo = new AppInfo();
                String filepath = path + "/" + appInfo.getName() + ".apk";

                setPermission(path);
                installNormal(AppUpdateActivity.this, filepath);

                update_download_complete.setEnabled(true);

            }
        });

        update_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_stop.setEnabled(false);
                update_stop.setVisibility(View.GONE);
                update_go_on.setVisibility(View.VISIBLE);
                pause("update");
            }
        });

        update_go_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_go_on.setEnabled(false);
                mScanView.start();
                update_go_on.setVisibility(View.GONE);
                update_stop.setVisibility(View.VISIBLE);
                go_on("update");
            }
        });

        update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_cancel.setEnabled(false);
                mScanView.stop();
                download_progress.setVisibility(View.GONE);
                update_downloading.setVisibility(View.GONE);
                update_download_complete.setVisibility(View.GONE);
                update_go_on.setVisibility(View.GONE);
                update_stop.setVisibility(View.GONE);
                update_cancel.setVisibility(View.GONE);
                update_download.setVisibility(View.VISIBLE);
                versionText.setVisibility(View.VISIBLE);
                speedText.setVisibility(View.VISIBLE);
                versionText.setText(getString(R.string.app_version));
                cancel("update");
                //DownloadManager.getInstance().cancel("update");
                //update_download.setEnabled(true);
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        if (from_LoginActivity && !FindFullUpdateManual) {
            back.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                if (changelog_now_version){
                    FindUpdateActivity.FROM_CHANGELOG_PAGE = true;
                    Intent intent = new Intent(AppUpdateActivity.this, FindUpdateActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                }else {
                    Intent intent = new Intent(AppUpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                }
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        if (update_manager.get_software_version() == null || update_manager.get_download_url() == null || update_manager.get_changelog() == null || software_version == null || download_url == null || changelog == null){
            super.onNewIntent(intent);
            setIntent(intent);
            setContentView(R.layout.activity_update);
            mDownloadDir = new File(Environment.getExternalStorageDirectory() + getString(R.string.files_path) + getString(R.string.logs_path) , "download");
            //LogUtil.i("progress" + mDownloadDir.toString());
            //register();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
                //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
                //getWindow().setNavigationBarColor(Color.BLUE);
            }

            initState();

            // 每次加入stack
            ActivtyStack.getScreenManager().pushActivity(this);
            //SUtils.initialize(getApplication());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //ToastUtil.setTextColor(ContextCompat.getColor(AppUpdateActivity.this, R.color.colorPrimaryDark));
            }

            new Thread(new Runnable() {//利用Runnable接口实现线程
                @Override
                public void run() {

                    FindUpdateActivity.update_notice = false;

                    //stop_update_notice();
                    if (!FindUpdateActivity.update_notice) {
                        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        Objects.requireNonNull(manager).cancelAll();
                    }

                }
            }).start();

        /*new Thread(new Runnable() {//利用Runnable接口实现线程
            @Override
            public void run() {

                FindUpdateActivity.update_notice = false;

                while (!FindUpdateActivity.update_notice) {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                }

            }
        }).start();*/

            donutProgress = (DonutProgress) findViewById(R.id.app_donut_progress);
            download_progress = (CircleProgressBar) findViewById(R.id.app_download_progressbar);
            mProgressbar = (ProgressBar) findViewById(R.id.app_update_progress);


            update_download = (Button) findViewById(R.id.update_download);
            update_downloading = (LinearLayout) findViewById(R.id.update_downloading);
            update_go_on = (Button)findViewById(R.id.update_go_on);
            update_stop = (Button)findViewById(R.id.update_stop);
            update_cancel = (Button)findViewById(R.id.update_cancel);
            update_download_complete = (Button) findViewById(R.id.update_download_complete);
            versionText = (TextView) findViewById(R.id.versionText);
            speedText = (TextView) findViewById(R.id.speedText);
            mScanView = (ScanView) findViewById(R.id.scanView_update);


            if (!changelog_now_version) {
                software_version = update_manager.get_software_version();
                changelog = update_manager.get_changelog();
                download_code = update_manager.get_download_code();
                download_url = update_manager.get_download_url();
                force_update = update_manager.get_force_update();
            }

            //初始化
            if (changelog_now_version) {
                update_download.setVisibility(View.GONE);
                update_downloading.setVisibility(View.GONE);
                update_download_complete.setVisibility(View.GONE);
                update_go_on.setVisibility(View.GONE);
                update_stop.setVisibility(View.GONE);
                update_cancel.setVisibility(View.GONE);
                versionText.setVisibility(View.VISIBLE);
                versionText.setText(getString(R.string.app_version));
                speedText.setVisibility(View.VISIBLE);
            } else {
                update_download.setVisibility(View.VISIBLE);
                update_downloading.setVisibility(View.GONE);
                update_download_complete.setVisibility(View.GONE);
                update_go_on.setVisibility(View.GONE);
                update_stop.setVisibility(View.GONE);
                update_cancel.setVisibility(View.GONE);
                versionText.setVisibility(View.VISIBLE);
                versionText.setText(getString(R.string.app_version));
                speedText.setVisibility(View.VISIBLE);

                unRegister();
                DownloadService.destory(AppUpdateActivity.this);
                cancel("update");

            }

            //写入更新内容
            TextView software_version_new = (TextView) findViewById(R.id.software_version_new);
            software_version_new.setText(software_version);

            TextView app_new_changelog = (TextView) findViewById(R.id.app_new_changelog);
            if (changelog == "" || changelog == null) {
                app_new_changelog.setText("暂无更新日志");
            } else {
                if (changelog_now_version  && changelog.equals("恢复最新正式版本")){
                    app_new_changelog.setText("暂无更新日志");
                }else {
                    app_new_changelog.setText(changelog.replaceAll("\\[换行]", "\n"));
                }
            }

            update_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (App_info.type.equals("beta") && FindFullUpdateManual){
                        showMessageNegativeDialog();
                    }else if (!update_manager.get_download_url().equals("") && update_manager.get_download_url().contains("meternity.cn/api/LanZouApi.php?url=")){
                        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                        register();

                        update_download.setEnabled(false);
                        update_stop.setEnabled(true);
                        update_cancel.setEnabled(true);
                        update_go_on.setEnabled(true);
                        update_download_complete.setEnabled(true);

                        mScanView.start();

                        donutProgress.setVisibility(View.GONE);
                        download_progress.setVisibility(View.VISIBLE);
                        update_download.setVisibility(View.GONE);
                        update_downloading.setVisibility(View.VISIBLE);
                        update_go_on.setVisibility(View.GONE);
                        update_stop.setVisibility(View.VISIBLE);
                        update_cancel.setVisibility(View.VISIBLE);
                        versionText.setVisibility(View.VISIBLE);
                        versionText.setText("正在校验");
                        speedText.setVisibility(View.GONE);

                        new Thread(new Update_Url_Checker_runtime(h1, update_manager.get_download_url())).start();
                    }else{
                        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                        register();

                        update_download.setEnabled(false);
                        update_stop.setEnabled(true);
                        update_cancel.setEnabled(true);
                        update_go_on.setEnabled(true);
                        update_download_complete.setEnabled(true);

                        mScanView.start();

                        donutProgress.setVisibility(View.GONE);
                        download_progress.setVisibility(View.VISIBLE);
                        update_download.setVisibility(View.GONE);
                        update_downloading.setVisibility(View.VISIBLE);
                        update_go_on.setVisibility(View.GONE);
                        update_stop.setVisibility(View.VISIBLE);
                        update_cancel.setVisibility(View.VISIBLE);
                        versionText.setVisibility(View.VISIBLE);
                        speedText.setVisibility(View.GONE);

                        //Data_editer.deleteDirectory(path);

                        //doDownload();
                        AppInfo appInfo = new AppInfo();
                        //LogUtil.i("progress" + appInfo.getStatus());
                        if (appInfo.getStatus() == STATUS_DOWNLOADING || appInfo.getStatus() == STATUS_CONNECTING) {
                            pause("update");
                        } else if (appInfo.getStatus() == STATUS_COMPLETE) {
                            install(appInfo);
                        } else if (appInfo.getStatus() == STATUS_INSTALLED) {
                            unInstall();
                        } else {
                            if (!update_manager.get_download_url().equals("")) {
                                appInfo.setUrl(update_manager.get_download_url());
                            }else {
                                appInfo.setUrl(null);
                                FToastUtils.init().setRoundRadius(30).show("下载链接非法，请联系管理员！");
                            }
                            download(1, "update", appInfo);
                        }
                    }

                    //Intent intent = new Intent(AppUpdateActivity.this, DownloadService.class);
                    //startService(intent);// 启动服务

                }
            });

            update_download_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 获取SD卡路径
                /*String path = Environment.getExternalStorageDirectory()
                        + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                String fileName = "app.apk";
                String filepath = path + "/" + fileName;

                setPermission(path);
                installNormal(AppUpdateActivity.this, filepath);*/

                    update_download_complete.setEnabled(false);

                    SpUtil rw = new SpUtil();
                    if (DataUtil.find_words(software_version, "Patch")) {
                        rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_PATCH, BUILD_PATCH, VersionUtil.patch_version(AppUpdateActivity.this, software_version));
                        rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_PATCH, CHANGELOG_PATCH, changelog);
                    } else if (DataUtil.find_words(software_version, "SP")) {
                        rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN, VersionUtil.sp_version(AppUpdateActivity.this, software_version));
                        rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN, changelog);
                    } else {
                        rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN, VersionUtil.build_version(AppUpdateActivity.this, software_version));
                        rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN, changelog);
                    }

                    // 获取SD卡路径
                    String path = Environment.getExternalStorageDirectory()
                            + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                    //String fileName = "app.apk";
                    AppInfo appInfo = new AppInfo();
                    String filepath = path + "/" + appInfo.getName() + ".apk";

                    setPermission(path);
                    installNormal(AppUpdateActivity.this, filepath);

                    update_download_complete.setEnabled(true);

                }
            });

            update_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_stop.setEnabled(false);
                    update_stop.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.VISIBLE);
                    pause("update");
                }
            });

            update_go_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_go_on.setEnabled(false);
                    mScanView.start();
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.VISIBLE);
                    go_on("update");
                }
            });

            update_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_cancel.setEnabled(false);
                    mScanView.stop();
                    download_progress.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.GONE);
                    update_download_complete.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.GONE);
                    update_download.setVisibility(View.VISIBLE);
                    versionText.setVisibility(View.VISIBLE);
                    speedText.setVisibility(View.VISIBLE);
                    versionText.setText(getString(R.string.app_version));
                    cancel("update");
                    //DownloadManager.getInstance().cancel("update");
                    //update_download.setEnabled(true);
                }
            });

            ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

            if (from_LoginActivity && !FindFullUpdateManual) {
                back.setVisibility(View.GONE);
            }

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                    if (changelog_now_version){
                        FindUpdateActivity.FROM_CHANGELOG_PAGE = true;
                        Intent intent = new Intent(AppUpdateActivity.this, FindUpdateActivity.class);
                        startActivity(intent);
                        AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                    }else {
                        Intent intent = new Intent(AppUpdateActivity.this, MainActivity.class);
                        startActivity(intent);
                        AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                    }
                }
            });
        }else {
            if (changelog_now_version || !software_version.equals(update_manager.get_software_version()) || !download_url.equals(update_manager.get_download_url()) || !changelog.equals(update_manager.get_changelog())) {
                super.onNewIntent(intent);
                setIntent(intent);
                setContentView(R.layout.activity_update);
                mDownloadDir = new File(Environment.getExternalStorageDirectory() + getString(R.string.files_path) + getString(R.string.logs_path), "download");
                //LogUtil.i("progress" + mDownloadDir.toString());
                //register();

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
                    //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
                    //getWindow().setNavigationBarColor(Color.BLUE);
                }

                initState();

                // 每次加入stack
                ActivtyStack.getScreenManager().pushActivity(this);
                //SUtils.initialize(getApplication());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //ToastUtil.setTextColor(ContextCompat.getColor(AppUpdateActivity.this, R.color.colorPrimaryDark));
                }

                new Thread(new Runnable() {//利用Runnable接口实现线程
                    @Override
                    public void run() {

                        FindUpdateActivity.update_notice = false;

                        //stop_update_notice();
                        if (!FindUpdateActivity.update_notice) {
                            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            Objects.requireNonNull(manager).cancelAll();
                        }

                    }
                }).start();

        /*new Thread(new Runnable() {//利用Runnable接口实现线程
            @Override
            public void run() {

                FindUpdateActivity.update_notice = false;

                while (!FindUpdateActivity.update_notice) {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                }

            }
        }).start();*/

                donutProgress = (DonutProgress) findViewById(R.id.app_donut_progress);
                download_progress = (CircleProgressBar) findViewById(R.id.app_download_progressbar);
                mProgressbar = (ProgressBar) findViewById(R.id.app_update_progress);


                update_download = (Button) findViewById(R.id.update_download);
                update_downloading = (LinearLayout) findViewById(R.id.update_downloading);
                update_go_on = (Button) findViewById(R.id.update_go_on);
                update_stop = (Button) findViewById(R.id.update_stop);
                update_cancel = (Button) findViewById(R.id.update_cancel);
                update_download_complete = (Button) findViewById(R.id.update_download_complete);
                versionText = (TextView) findViewById(R.id.versionText);
                speedText = (TextView) findViewById(R.id.speedText);
                mScanView = (ScanView) findViewById(R.id.scanView_update);


                if (!changelog_now_version) {
                    software_version = update_manager.get_software_version();
                    changelog = update_manager.get_changelog();
                    download_code = update_manager.get_download_code();
                    download_url = update_manager.get_download_url();
                    force_update = update_manager.get_force_update();
                }

                //初始化
                if (changelog_now_version) {
                    update_download.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.GONE);
                    update_download_complete.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    versionText.setText(getString(R.string.app_version));
                    speedText.setVisibility(View.VISIBLE);
                } else {
                    update_download.setVisibility(View.VISIBLE);
                    update_downloading.setVisibility(View.GONE);
                    update_download_complete.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    versionText.setText(getString(R.string.app_version));
                    speedText.setVisibility(View.VISIBLE);

                    unRegister();
                    DownloadService.destory(AppUpdateActivity.this);
                    cancel("update");

                }

                //写入更新内容
                TextView software_version_new = (TextView) findViewById(R.id.software_version_new);
                software_version_new.setText(software_version);

                TextView app_new_changelog = (TextView) findViewById(R.id.app_new_changelog);
                if (changelog == "" || changelog == null) {
                    app_new_changelog.setText("暂无更新日志");
                } else {
                    if (changelog_now_version && changelog.equals("恢复最新正式版本")) {
                        app_new_changelog.setText("暂无更新日志");
                    } else {
                        app_new_changelog.setText(changelog.replaceAll("\\[换行]", "\n"));
                    }
                }

                update_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (App_info.type.equals("beta") && FindFullUpdateManual) {
                            showMessageNegativeDialog();
                        }else if (!update_manager.get_download_url().equals("") && update_manager.get_download_url().contains("meternity.cn/api/LanZouApi.php?url=")) {
                            FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                            register();

                            update_download.setEnabled(false);
                            update_stop.setEnabled(true);
                            update_cancel.setEnabled(true);
                            update_go_on.setEnabled(true);
                            update_download_complete.setEnabled(true);

                            mScanView.start();

                            donutProgress.setVisibility(View.GONE);
                            download_progress.setVisibility(View.VISIBLE);
                            update_download.setVisibility(View.GONE);
                            update_downloading.setVisibility(View.VISIBLE);
                            update_go_on.setVisibility(View.GONE);
                            update_stop.setVisibility(View.VISIBLE);
                            update_cancel.setVisibility(View.VISIBLE);
                            versionText.setVisibility(View.VISIBLE);
                            versionText.setText("正在校验");
                            speedText.setVisibility(View.GONE);

                            new Thread(new Update_Url_Checker_runtime(h1, update_manager.get_download_url())).start();
                        }else {
                            FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                            register();

                            update_download.setEnabled(false);
                            update_stop.setEnabled(true);
                            update_cancel.setEnabled(true);
                            update_go_on.setEnabled(true);
                            update_download_complete.setEnabled(true);

                            mScanView.start();

                            donutProgress.setVisibility(View.GONE);
                            download_progress.setVisibility(View.VISIBLE);
                            update_download.setVisibility(View.GONE);
                            update_downloading.setVisibility(View.VISIBLE);
                            update_go_on.setVisibility(View.GONE);
                            update_stop.setVisibility(View.VISIBLE);
                            update_cancel.setVisibility(View.VISIBLE);
                            versionText.setVisibility(View.VISIBLE);
                            speedText.setVisibility(View.GONE);

                            //Data_editer.deleteDirectory(path);

                            //doDownload();
                            AppInfo appInfo = new AppInfo();
                            //LogUtil.i("progress" + appInfo.getStatus());
                            if (appInfo.getStatus() == STATUS_DOWNLOADING || appInfo.getStatus() == STATUS_CONNECTING) {
                                pause("update");
                            } else if (appInfo.getStatus() == STATUS_COMPLETE) {
                                install(appInfo);
                            } else if (appInfo.getStatus() == STATUS_INSTALLED) {
                                unInstall();
                            } else {
                                if (!update_manager.get_download_url().equals("")) {
                                    appInfo.setUrl(update_manager.get_download_url());
                                } else {
                                    appInfo.setUrl(null);
                                    FToastUtils.init().setRoundRadius(30).show("下载链接非法，请联系管理员！");
                                }
                                download(1, "update", appInfo);
                            }
                        }

                        //Intent intent = new Intent(AppUpdateActivity.this, DownloadService.class);
                        //startService(intent);// 启动服务

                    }
                });

                update_download_complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 获取SD卡路径
                /*String path = Environment.getExternalStorageDirectory()
                        + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                String fileName = "app.apk";
                String filepath = path + "/" + fileName;

                setPermission(path);
                installNormal(AppUpdateActivity.this, filepath);*/

                        update_download_complete.setEnabled(false);

                        SpUtil rw = new SpUtil();
                        if (DataUtil.find_words(software_version, "Patch")) {
                            rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_PATCH, BUILD_PATCH, VersionUtil.patch_version(AppUpdateActivity.this, software_version));
                            rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_PATCH, CHANGELOG_PATCH, changelog);
                        } else if (DataUtil.find_words(software_version, "SP")) {
                            rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN, VersionUtil.sp_version(AppUpdateActivity.this, software_version));
                            rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN, changelog);
                        } else {
                            rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN, VersionUtil.build_version(AppUpdateActivity.this, software_version));
                            rw.writeValue(AppUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN, changelog);
                        }

                        // 获取SD卡路径
                        String path = Environment.getExternalStorageDirectory()
                                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                        //String fileName = "app.apk";
                        AppInfo appInfo = new AppInfo();
                        String filepath = path + "/" + appInfo.getName() + ".apk";

                        setPermission(path);
                        installNormal(AppUpdateActivity.this, filepath);

                        update_download_complete.setEnabled(true);

                    }
                });

                update_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update_stop.setEnabled(false);
                        update_stop.setVisibility(View.GONE);
                        update_go_on.setVisibility(View.VISIBLE);
                        pause("update");
                    }
                });

                update_go_on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update_go_on.setEnabled(false);
                        mScanView.start();
                        update_go_on.setVisibility(View.GONE);
                        update_stop.setVisibility(View.VISIBLE);
                        go_on("update");
                    }
                });

                update_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update_cancel.setEnabled(false);
                        mScanView.stop();
                        download_progress.setVisibility(View.GONE);
                        update_downloading.setVisibility(View.GONE);
                        update_download_complete.setVisibility(View.GONE);
                        update_go_on.setVisibility(View.GONE);
                        update_stop.setVisibility(View.GONE);
                        update_cancel.setVisibility(View.GONE);
                        update_download.setVisibility(View.VISIBLE);
                        versionText.setVisibility(View.VISIBLE);
                        speedText.setVisibility(View.VISIBLE);
                        versionText.setText(getString(R.string.app_version));
                        cancel("update");
                        //DownloadManager.getInstance().cancel("update");
                        //update_download.setEnabled(true);
                    }
                });

                ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

                if (from_LoginActivity && !FindFullUpdateManual) {
                    back.setVisibility(View.GONE);
                }

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                        if (changelog_now_version) {
                            FindUpdateActivity.FROM_CHANGELOG_PAGE = true;
                            Intent intent = new Intent(AppUpdateActivity.this, FindUpdateActivity.class);
                            startActivity(intent);
                            AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                        } else {
                            Intent intent = new Intent(AppUpdateActivity.this, MainActivity.class);
                            startActivity(intent);
                            AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                        }
                    }
                });

            }

        }

    }

    private void showMessageNegativeDialog() {

        new QMUIDialog.MessageDialogBuilder(AppUpdateActivity.this)

                .setTitle("提示")

                .setMessage("确定退出beta测试嘛？\n退出后在内测结束前您仍然可以检测到并更新beta版本，如需使用正式版本，请勿更新！")

                .addAction("取消", new QMUIDialogAction.ActionListener() {

                    @Override

                    public void onClick(QMUIDialog dialog, int index) {

                        dialog.dismiss();

                    }

                })

                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {

                    @Override

                    public void onClick(QMUIDialog dialog, int index) {

                        //Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
                        register();

                        update_download.setEnabled(false);
                        update_stop.setEnabled(true);
                        update_cancel.setEnabled(true);
                        update_go_on.setEnabled(true);
                        update_download_complete.setEnabled(true);

                        mScanView.start();

                        donutProgress.setVisibility(View.GONE);
                        download_progress.setVisibility(View.VISIBLE);
                        update_download.setVisibility(View.GONE);
                        update_downloading.setVisibility(View.VISIBLE);
                        update_go_on.setVisibility(View.GONE);
                        update_stop.setVisibility(View.VISIBLE);
                        update_cancel.setVisibility(View.VISIBLE);
                        versionText.setVisibility(View.VISIBLE);
                        //versionText.setText("正在校验");
                        speedText.setVisibility(View.GONE);

                        if (!update_manager.get_download_url().equals("") && update_manager.get_download_url().contains("meternity.cn/api/LanZouApi.php?url=")) {
                            versionText.setText("正在校验");
                            new Thread(new Update_Url_Checker_runtime(h1, update_manager.get_download_url())).start();
                        }else {

                            //Data_editer.deleteDirectory(path);

                            //doDownload();
                            AppInfo appInfo = new AppInfo();
                            //LogUtil.i("progress" + appInfo.getStatus());
                            if (appInfo.getStatus() == STATUS_DOWNLOADING || appInfo.getStatus() == STATUS_CONNECTING) {
                                pause("update");
                            } else if (appInfo.getStatus() == STATUS_COMPLETE) {
                                install(appInfo);
                            } else if (appInfo.getStatus() == STATUS_INSTALLED) {
                                unInstall();
                            } else {
                                if (!update_manager.get_download_url().equals("")) {
                                    appInfo.setUrl(update_manager.get_download_url());
                                } else {
                                    appInfo.setUrl(null);
                                    FToastUtils.init().setRoundRadius(30).show("下载链接非法，请联系管理员！");
                                }
                                download(1, "update", appInfo);
                            }

                        }

                    }

                })

                .create(mCurrentDialogStyle).show();

    }

    private void download(int position, String tag, AppInfo info) {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        Data_editer.deleteDirectory(path);
        //LogUtil.i(path);
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));

        DownloadManager.getInstance().delete("update");
        download_progress.setProgress(0);
        //versionText.setText("0.0MB/0.0MB");
        AppInfo appInfo = new AppInfo();
        appInfo.setProgress(0);
        appInfo.setDownloadPerSize("");

        DownloadService.intentDownload(AppUpdateActivity.this, position, tag, info);
    }

    private void cancel(String tag){
        DownloadService.intentCancel(AppUpdateActivity.this, tag);
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        Data_editer.deleteDirectory(path);
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download");
        DownloadManager.getInstance().delete(tag);
    }

    private void go_on(String tag){
        DownloadService.intentGoOn(AppUpdateActivity.this, tag);
    }

    private void pause(String tag) {
        DownloadService.intentPause(AppUpdateActivity.this, tag);
    }

    private void pauseAll() {
        DownloadService.intentPauseAll(AppUpdateActivity.this);
    }

    private void cancelAll(){
        DownloadService.intentCancelAll(AppUpdateActivity.this);
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        Data_editer.deleteDirectory(path);
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download");
    }

    private void install(AppInfo appInfo) {
        Utils.installApp(AppUpdateActivity.this, new File(mDownloadDir, appInfo.getName() + ".apk"));
    }

    private void unInstall() {
        Utils.unInstallApp(AppUpdateActivity.this, getString(R.string.app_package_name));
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (from_LoginActivity && !FindFullUpdateManual) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

                } else {
                    int currentVersion = Build.VERSION.SDK_INT;

                    new Thread(new Runnable() {//利用Runnable接口实现线程
                        @Override
                        public void run() {

                            FindUpdateActivity.update_notice = false;

                            //stop_update_notice();
                            if (!FindUpdateActivity.update_notice) {
                                NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                Objects.requireNonNull(manager).cancelAll();
                            }

                        }
                    }).start();
                    ActivtyStack.getScreenManager().popAllActivityExceptOne();

                    if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        System.exit(0);
                    } else {// android2.1
                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        am.restartPackage(getPackageName());
                    }
                }
                return true;
            }
        }else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                if (changelog_now_version){
                    FindUpdateActivity.FROM_CHANGELOG_PAGE = true;
                    Intent intent = new Intent(AppUpdateActivity.this, FindUpdateActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                }else {
                    Intent intent = new Intent(AppUpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(AppUpdateActivity.this);
                }

                return true;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     */
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
            StatusBarCompat.setStatusBarColor(this, getColorPrimary(), lightStatusBar);

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
     * 使用Handler更新UI界面信息
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mProgressbar.setProgress(msg.getData().getInt("size"));

            float temp = (float) mProgressbar.getProgress()
                    / (float) mProgressbar.getMax();

            int progress = (int) (temp * 100);
            if (progress == 100) {
                final LinearLayout update_downloading = (LinearLayout) findViewById(R.id.update_downloading);
                final Button update_download_complete = (Button) findViewById(R.id.update_download_complete);
                update_downloading.setVisibility(View.GONE);
                update_download_complete.setVisibility(View.VISIBLE);

                mScanView = (ScanView) findViewById(R.id.scanView_update);
                mScanView.stop();

                FToastUtils.init().setRoundRadius(30).show("下载完成！");

                // 获取SD卡路径
                String path = Environment.getExternalStorageDirectory()
                        + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
                //String fileName = "app.apk";
                String filepath = path + "/" + fileName;

                setPermission(path);

                SpUtil rw = new SpUtil();
                if (DataUtil.find_words(software_version,"Patch")){
                    rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_PATCH,BUILD_PATCH, VersionUtil.patch_version(AppUpdateActivity.this,software_version));
                    rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_PATCH,CHANGELOG_PATCH,changelog);
                }else if (DataUtil.find_words(software_version,"SP")){
                    rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,BUILD_MAIN, VersionUtil.sp_version(AppUpdateActivity.this,software_version));
                    rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,CHANGELOG_MAIN,changelog);
                }else{
                    rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,BUILD_MAIN, VersionUtil.build_version(AppUpdateActivity.this,software_version));
                    rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,CHANGELOG_MAIN,changelog);
                }

                installNormal(AppUpdateActivity.this, filepath);

            }
            //mMessageView.setText("下载进度:" + progress + " %");

            TextView versionText = (TextView)findViewById(R.id.versionText);
            TextView speedText = (TextView)findViewById(R.id.speedText);
            versionText.setVisibility(View.GONE);
            speedText.setVisibility(View.GONE);
            download_progress.setProgress(progress);

        }
    };

    //普通安装
    private static void installNormal(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.autumn.reptile.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 提升读写权限
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void doDownload() {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        Data_editer.deleteDirectory(path);
        LogUtil.i(path);
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path));
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 设置progressBar初始化
        mProgressbar.setProgress(0);

        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
        //String downloadUrl = "https://update.meternity.cn/V2.9.7.4395_debug_CheckIn_20180407_r18HF171121_build29473_2974395_jiagu_sign.apk";

        String downloadUrl = download_url;
        //String downloadUrl = "http://shouji.360tpcdn.com/LhB_M7lVtCEZTqvL-kpRGs1Q-y4b0v77bK2dXs1cDpVVgVQKopCt7zRs7Q20lP7RVjSnI77ZTbcpd9Ps1M6yfQ2.apk?sign=c9d172b0006c32d8";
        int Min = 100000000;
        int Max = 999999999;
        int random_code = Min + (int)(Math.random() * ((Max - Min) + 1));
        fileName = random_code + ".apk";
        int threadNum = 32;
        String filepath = path + "/" + fileName;
        Log.d(TAG, "download file  path:" + filepath);
        downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
        task.start();
    }

    /**
     * 多线程文件下载
     *
     * @author yangxiaolong
     * @2014-8-7
     */
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                Log.d(TAG, "download file http path:" + downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                mProgressbar.setMax(fileSize);

                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", downloadedAllSize);
                    mHandler.sendMessage(msg);
                    // Log.d(TAG, "current downloadSize:" + downloadedAllSize);
                    Thread.sleep(1000);// 休息1秒后再读取下载进度
                }
                Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出AppUpdateActivity");
        unRegister();
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        //unRegister();
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        AppInfo appInfo = new AppInfo();
        outState.putInt(UPDATE_STATE,appInfo.getStatus());
    }

    private void register() {
        mReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
        LocalBroadcastManager.getInstance(getAppContext()).registerReceiver(mReceiver, intentFilter);
    }

    private void unRegister() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getAppContext()).unregisterReceiver(mReceiver);
        }
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case Update_Url_Checker_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals("访问成功")){

                        String url_302 = Update_url_manager.getUrl();
                        LogUtil.d("url---" + url_302);
                        if (url_302 == null) {
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            failed_302_checked();
                        }else if (url_302.equals("")){
                            FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            failed_302_checked();
                        }else {

                            AppInfo appInfo = new AppInfo();
                            //LogUtil.i("progress" + appInfo.getStatus());
                            if (appInfo.getStatus() == STATUS_DOWNLOADING || appInfo.getStatus() == STATUS_CONNECTING) {
                                pause("update");
                            } else if (appInfo.getStatus() == STATUS_COMPLETE) {
                                install(appInfo);
                            } else if (appInfo.getStatus() == STATUS_INSTALLED) {
                                unInstall();
                            } else {
                                if (!url_302.equals("")) {
                                    //FToastUtils.init().show("开始下载" + url_302);
                                    LogUtil.d("Start Download:" + url_302);
                                    appInfo.setUrl(url_302);
                                    LogUtil.d("Download:" + appInfo.getUrl());
                                } else {
                                    appInfo.setUrl(null);
                                    FToastUtils.init().setRoundRadius(30).show("下载链接非法，请联系管理员！");
                                    //failed_302_checked();
                                }
                                download(1, "update", appInfo);
                            }

                        }
                    }else{
                        //访问失败
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        failed_302_checked();
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    private void failed_302_checked() {
        //appInfo.setStatus(AppInfo.STATUS_DOWNLOAD_ERROR);
        //appInfo.setDownloadPerSize("");
        speedText.setVisibility(View.VISIBLE);
        donutProgress.setVisibility(View.GONE);
        versionText.setVisibility(View.VISIBLE);
        versionText.setText(getString(R.string.app_version));
        download_progress.setVisibility(View.GONE);
        update_download.setVisibility(View.VISIBLE);
        update_downloading.setVisibility(View.GONE);
        update_go_on.setVisibility(View.GONE);
        update_stop.setVisibility(View.GONE);
        update_cancel.setVisibility(View.GONE);
        update_download_complete.setVisibility(View.GONE);
        download_progress.setProgress(0);
        mScanView.stop();
        //appInfo.setProgress(0);
        //appInfo.setDownloadPerSize("");
        //tmpInfo.setProgress(0);
        //tmpInfo.setDownloadPerSize("");

        unRegister();
        DownloadService.destory(AppUpdateActivity.this);
        DownloadManager.getInstance().cancel("update");
        DownloadManager.getInstance().cancelAll();
        DownloadManager.getInstance().delete("update");

        update_stop.setEnabled(true);
        update_cancel.setEnabled(true);
        update_go_on.setEnabled(true);
        update_download.setEnabled(true);
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            versionText = (TextView) findViewById(R.id.versionText);
            download_progress = (CircleProgressBar) findViewById(R.id.app_download_progressbar);
            donutProgress = (DonutProgress) findViewById(R.id.app_donut_progress);
            speedText = (TextView) findViewById(R.id.speedText);
            mScanView = (ScanView) findViewById(R.id.scanView_update);
            update_download = (Button) findViewById(R.id.update_download);
            update_downloading = (LinearLayout) findViewById(R.id.update_downloading);
            update_go_on = (Button)findViewById(R.id.update_go_on);
            update_stop = (Button)findViewById(R.id.update_stop);
            update_cancel = (Button)findViewById(R.id.update_cancel);
            update_download_complete = (Button) findViewById(R.id.update_download_complete);

            final String action = intent.getAction();
            if (action == null || !action.equals(DownloadService.ACTION_DOWNLOAD_BROAD_CAST)) {
                return;
            }
            final int position = intent.getIntExtra(DownloadService.EXTRA_POSITION, -1);
            final AppInfo tmpInfo = (AppInfo) intent.getSerializableExtra(DownloadService.EXTRA_APP_INFO);
            if (tmpInfo == null || position == -1) {
                return;
            };
            final AppInfo appInfo = new AppInfo();
            //appInfo.setName("com.autumn.reptile");
            final int status = tmpInfo.getStatus();
            switch (status) {
                case STATUS_CONNECTING:

                    appInfo.setStatus(STATUS_CONNECTING);
                    speedText.setVisibility(View.GONE);
                    donutProgress.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    download_progress.setVisibility(View.VISIBLE);
                    //mScanView.start();
                    //appInfo.setProgress(0);
                    //appInfo.setDownloadPerSize("");
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    download_progress.setProgress(appInfo.getProgress());
                    versionText.setText("正在连接服务器");
                    //versionText.setText(tmpInfo.getDownloadPerSize());

                    update_go_on.setEnabled(true);

                    LogUtil.d("Download Info:" + appInfo.getUrl());

                    break;

                case STATUS_DOWNLOADING:

                    appInfo.setStatus(STATUS_DOWNLOADING);
                    speedText.setVisibility(View.GONE);
                    donutProgress.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    download_progress.setVisibility(View.VISIBLE);
                    update_download.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.VISIBLE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.VISIBLE);
                    update_cancel.setVisibility(View.VISIBLE);
                    update_download_complete.setVisibility(View.GONE);
                    //mScanView.start();
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());

                    download_progress.setProgress(appInfo.getProgress());
                    versionText.setText(tmpInfo.getDownloadPerSize());

                    update_go_on.setEnabled(true);

                    //LogUtil.i("progress"+ tmpInfo.getDownloadPerSize());

                    break;
                case STATUS_COMPLETE:

                    appInfo.setStatus(STATUS_COMPLETE);
                    speedText.setVisibility(View.GONE);
                    donutProgress.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    download_progress.setVisibility(View.VISIBLE);
                    update_download.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.GONE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.GONE);
                    update_download_complete.setVisibility(View.VISIBLE);
                    mScanView.stop();
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());

                    download_progress.setProgress(appInfo.getProgress());
                    versionText.setVisibility(View.GONE);

                    File apk = new File(mDownloadDir + "/" + appInfo.getName() + ".apk");
                    if (apk.isFile() && apk.exists()) {
                        //String packageName = Utils.getApkFilePackage(getAppContext(), apk);

                        SpUtil rw = new SpUtil();
                        if (DataUtil.find_words(software_version,"Patch")){
                            rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_PATCH,BUILD_PATCH, VersionUtil.patch_version(AppUpdateActivity.this,software_version));
                            rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_PATCH,CHANGELOG_PATCH,changelog);
                        }else if (DataUtil.find_words(software_version,"SP")){
                            rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,BUILD_MAIN, VersionUtil.sp_version(AppUpdateActivity.this,software_version));
                            rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,CHANGELOG_MAIN,changelog);
                        }else{
                            rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,BUILD_MAIN, VersionUtil.build_version(AppUpdateActivity.this,software_version));
                            rw.writeValue(AppUpdateActivity.this,UPDATE_INFO_MAIN,CHANGELOG_MAIN,changelog);
                        }

                        //installNormal(getAppContext(),mDownloadDir + "/" + appInfo.getName() + ".apk");
                        String packageName = "com.autumn.reptile";
                        appInfo.setPackageName(packageName);
                        if (Utils.isAppInstalled(getAppContext(), packageName)) {
                            appInfo.setStatus(STATUS_INSTALLED);
                        }

                        unRegister();
                        DownloadService.destory(getAppContext());
                        update_go_on.setEnabled(true);
                        update_download.setEnabled(true);
                    }

                    //LogUtil.i("progress" + tmpInfo.getDownloadPerSize());
                    //LogUtil.i("progress" + appInfo.getName());
                    //LogUtil.i("progress" + mDownloadDir);
                    //LogUtil.i("progress" + Utils.getApkFilePackage(getAppContext(), apk));

                    break;

                case AppInfo.STATUS_PAUSED:

                    appInfo.setStatus(AppInfo.STATUS_PAUSED);
                    speedText.setVisibility(View.GONE);
                    donutProgress.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    download_progress.setVisibility(View.VISIBLE);
                    update_download.setVisibility(View.GONE);
                    update_downloading.setVisibility(View.VISIBLE);
                    update_go_on.setVisibility(View.VISIBLE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.VISIBLE);
                    update_download_complete.setVisibility(View.GONE);
                    mScanView.stop();

                    update_stop.setEnabled(true);
                    update_cancel.setEnabled(true);
                    update_go_on.setEnabled(true);
                    update_download.setEnabled(true);


                    break;
                case AppInfo.STATUS_NOT_DOWNLOAD:

                    appInfo.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
                    speedText.setVisibility(View.VISIBLE);
                    donutProgress.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    versionText.setText(getString(R.string.app_version));
                    download_progress.setVisibility(View.GONE);
                    update_download.setVisibility(View.VISIBLE);
                    update_downloading.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.GONE);
                    update_download_complete.setVisibility(View.GONE);
                    download_progress.setProgress(0);
                    mScanView.stop();
                    appInfo.setProgress(0);
                    appInfo.setDownloadPerSize("");
                    tmpInfo.setProgress(0);
                    tmpInfo.setDownloadPerSize("");

                    unRegister();
                    DownloadService.destory(AppUpdateActivity.this);
                    DownloadManager.getInstance().cancel("update");
                    DownloadManager.getInstance().cancelAll();
                    //cancel("update");
                    DownloadManager.getInstance().delete("update");


                    update_stop.setEnabled(true);
                    update_cancel.setEnabled(true);
                    update_go_on.setEnabled(true);
                    update_download.setEnabled(true);

                    //unRegister();

                    break;
                case AppInfo.STATUS_DOWNLOAD_ERROR:

                    appInfo.setStatus(AppInfo.STATUS_DOWNLOAD_ERROR);
                    appInfo.setDownloadPerSize("");
                    speedText.setVisibility(View.VISIBLE);
                    donutProgress.setVisibility(View.GONE);
                    versionText.setVisibility(View.VISIBLE);
                    versionText.setText(getString(R.string.app_version));
                    download_progress.setVisibility(View.GONE);
                    update_download.setVisibility(View.VISIBLE);
                    update_downloading.setVisibility(View.GONE);
                    update_go_on.setVisibility(View.GONE);
                    update_stop.setVisibility(View.GONE);
                    update_cancel.setVisibility(View.GONE);
                    update_download_complete.setVisibility(View.GONE);
                    download_progress.setProgress(0);
                    mScanView.stop();
                    appInfo.setProgress(0);
                    appInfo.setDownloadPerSize("");
                    tmpInfo.setProgress(0);
                    tmpInfo.setDownloadPerSize("");

                    unRegister();
                    DownloadService.destory(AppUpdateActivity.this);
                    DownloadManager.getInstance().cancel("update");
                    DownloadManager.getInstance().cancelAll();
                    DownloadManager.getInstance().delete("update");

                    update_stop.setEnabled(true);
                    update_cancel.setEnabled(true);
                    update_go_on.setEnabled(true);
                    update_download.setEnabled(true);

                    //unRegister();

                    break;
            }
        }
    }

}
