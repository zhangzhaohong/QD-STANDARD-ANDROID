package com.autumn.framework.update;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autumn.circleprogress.DonutProgress;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.View.ScanView;
import com.autumn.framework.data.App_info;
import com.autumn.framework.data.DataUtil;
import com.autumn.framework.data.FileDownloadThread;
import com.autumn.framework.data.SpUtil;
import com.autumn.progressbar.CircleProgressBar;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.data.VersionUtil;
import com.autumn.sdk.manager.update.update_manager;
import com.autumn.sdk.runtime.update.beta_update_runtime;
import com.autumn.sdk.runtime.update.check_update_version_runtime;
import com.autumn.sdk.runtime.update.debug_update_runtime;
import com.autumn.sdk.runtime.update.update_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Timer;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class FindUpdateActivity extends BaseActivity {

    public static String software_version;
    public static boolean update_notice = false;
    public static boolean FROM_CHANGELOG_PAGE = false;
    private DonutProgress donutProgress;
    private Timer timer;
    private CircleProgressBar download_progress;
    private ProgressBar mProgressbar;
    private static final String TAG = FindUpdateActivity.class.getSimpleName();
    private boolean r = false;
    private int progress_start = 0;
    private ScanView mScanView;
    private int count = 0;
    private LinearLayout now_version;
    private String UPDATE_INFO_MAIN = "Update_info_main";
    private String BUILD_MAIN = "Build_main";
    private String VERSION_MAIN = "Version_main";
    private String CHANGELOG_MAIN = "Changelog_main";
    private String UPDATE_DATE_MAIN = "Update_date_main";

    private String UPDATE_INFO_PATCH = "Update_info_patch";
    private String BUILD_PATCH = "Build_patch";
    private String CHANGELOG_PATCH = "Changelog_patch";
    private LinearLayout now_version_patch;
    private int find_update = 0;

    private boolean new_Version_now = false;
    private int find_update_check = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_find_update);

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
            //ToastUtil.setTextColor(ContextCompat.getColor(FindUpdateActivity.this, R.color.colorPrimaryDark));
        }

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

        final Button check_update = (Button)findViewById(R.id.check_update);
        donutProgress = (DonutProgress)findViewById(R.id.donut_progress);
        download_progress = (CircleProgressBar)findViewById(R.id.download_progressbar);
        mProgressbar = (ProgressBar) findViewById(R.id.update_progress);

        if(DataUtil.find_words(getString(R.string.app_software_version),"Patch")) {
            LinearLayout now_version_patch = (LinearLayout)findViewById(R.id.now_version_patch);
            now_version_patch.setVisibility(View.VISIBLE);
        }else{
            LinearLayout now_version_patch = (LinearLayout)findViewById(R.id.now_version_patch);
            now_version_patch.setVisibility(View.GONE);
        }


        if (FROM_CHANGELOG_PAGE){
            FROM_CHANGELOG_PAGE = false;
            TextView do_not_have_new_version = (TextView) findViewById(R.id.do_not_have_new_version);
            do_not_have_new_version.setVisibility(View.VISIBLE);
        }else {
            FROM_CHANGELOG_PAGE = false;
            //自动检测
            TextView do_not_have_new_version = (TextView) findViewById(R.id.do_not_have_new_version);
            do_not_have_new_version.setVisibility(View.GONE);
            TextView finding_new_version = (TextView) findViewById(R.id.finding_new_version);
            finding_new_version.setVisibility(View.VISIBLE);

            find_update = 0;
            find_update_check = 0;

            mScanView = (ScanView) findViewById(R.id.scanView_find);
            mScanView.start();

            check_update.setEnabled(false);

            //自动分配
            new Thread(new check_update_version_runtime(h4, FindUpdateActivity.this)).start();
        }


        //日志查看
        now_version = (LinearLayout)findViewById(R.id.now_version);
        now_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_Version_now){

                }else {
                    AppUpdateActivity.changelog_now_version = true;

                    ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                    ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                    ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                    new_Version_now = false;
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.VISIBLE);

                    SpUtil rw = new SpUtil();
                    if ((rw.getValue(FindUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN) == null || (rw.getValue(FindUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN).equals("")))) {
                        AppUpdateActivity.software_version = getString(R.string.app_name) + " " + getString(R.string.app_version) + " (" + getString(R.string.app_code_main) + ")";
                        AppUpdateActivity.changelog = "";
                    } else {
                        if (rw.getValue(FindUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN).equals(VersionUtil.build_version(FindUpdateActivity.this, getString(R.string.app_software_version))) || rw.getValue(FindUpdateActivity.this, UPDATE_INFO_MAIN, BUILD_MAIN).equals(VersionUtil.sp_version(FindUpdateActivity.this, getString(R.string.app_software_version)))) {
                            AppUpdateActivity.software_version = getString(R.string.app_name) + " " + getString(R.string.app_version) + " (" + getString(R.string.app_code_main) + ")";
                            AppUpdateActivity.changelog = rw.getValue(FindUpdateActivity.this, UPDATE_INFO_MAIN, CHANGELOG_MAIN);
                        } else {
                            AppUpdateActivity.software_version = getString(R.string.app_name) + " " + getString(R.string.app_version) + " (" + getString(R.string.app_code_main) + ")";
                            AppUpdateActivity.changelog = "";
                        }
                    }
                    Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
                }
            }
        });
        now_version_patch = (LinearLayout)findViewById(R.id.now_version_patch);
        now_version_patch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_Version_now){

                }else {
                    AppUpdateActivity.changelog_now_version = true;

                    ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                    ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                    ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                    new_Version_now = false;
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.VISIBLE);

                    SpUtil rw = new SpUtil();
                    if ((rw.getValue(FindUpdateActivity.this, UPDATE_INFO_PATCH, BUILD_PATCH) == null || (rw.getValue(FindUpdateActivity.this, UPDATE_INFO_PATCH, BUILD_PATCH).equals("")))) {
                        AppUpdateActivity.software_version = getString(R.string.app_name) + " " + getString(R.string.app_version) + " (" + getString(R.string.app_code) + ")";
                        AppUpdateActivity.changelog = "";
                    } else {
                        String patch_version = String.valueOf(getString(R.string.app_code).indexOf("Patch") + 5);
                        //LogUtil.i(patch_version);
                        //LogUtil.i(getString(R.string.app_build) + patch_version);
                        if (rw.getValue(FindUpdateActivity.this, UPDATE_INFO_PATCH, BUILD_PATCH).equals(VersionUtil.patch_version(FindUpdateActivity.this, getString(R.string.app_software_version)))) {
                            AppUpdateActivity.software_version = getString(R.string.app_name) + " " + getString(R.string.app_version) + " (" + getString(R.string.app_code) + ")";
                            AppUpdateActivity.changelog = rw.getValue(FindUpdateActivity.this, UPDATE_INFO_PATCH, CHANGELOG_PATCH);
                        } else {
                            AppUpdateActivity.software_version = getString(R.string.app_name) + " " + getString(R.string.app_version) + " (" + getString(R.string.app_code) + ")";
                            AppUpdateActivity.changelog = "";
                        }
                    }
                    Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
                }
            }
        });

        Button download_update = (Button)findViewById(R.id.download_update);

        download_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donutProgress.setVisibility(View.GONE);
                download_progress.setVisibility(View.VISIBLE);
                doDownload();
            }
        });

        check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FROM_CHANGELOG_PAGE = false;
                //release
                //new Thread(new update_runtime(h1,FindUpdateActivity.this)).start();
                //beta
                //new Thread(new beta_update_runtime(h2,FindUpdateActivity.this)).start();
                //debug
                //new Thread(new debug_update_runtime(h3,FindUpdateActivity.this)).start();
                //FToastUtils.init().setRoundRadius(30).show("正在检测");
                TextView do_not_have_new_version = (TextView)findViewById(R.id.do_not_have_new_version);
                do_not_have_new_version.setVisibility(View.GONE);
                TextView finding_new_version = (TextView)findViewById(R.id.finding_new_version);
                finding_new_version.setVisibility(View.VISIBLE);

                find_update = 0;
                find_update_check = 0;

                mScanView = (ScanView) findViewById(R.id.scanView_find);
                mScanView.start();

                check_update.setEnabled(false);

                //自动分配
                new Thread(new check_update_version_runtime(h4,FindUpdateActivity.this)).start();

                AppUpdateActivity.FindFullUpdateManual = false;

                //download_update.setVisibility(View.VISIBLE);

            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                Intent intent = new Intent(FindUpdateActivity.this, MainActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
            }
        });

        //mScanView = (ScanView) findViewById(R.id.scanView_find);
        //mScanView.start();

        //final TextView textView = (TextView) findViewById(R.id.speedText);

        /*mHandler = new Handler();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                count++;
                textView.setText(String.format("%s%s", count, "%"));
                if (count == 100) {
                    mScanView.stop();
                    textView.setText("OK");
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //四个参数的含义。1，group的id,2,item的id,3,是否排序，4，将要显示的内容
        menu.add(0,1,0,"下载最新完整包");
        //menu.add(0,2,0,"菜单二");
        //menu.add(0,3,0,"菜单三");
        //menu.add(0,4,0,"菜单四");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                //Toast.makeText(FindUpdateActivity.this,"菜单一",Toast.LENGTH_SHORT).show();
                find_update = 6;
                find_update_check = 6;
                h1.removeCallbacksAndMessages(null);
                h2.removeCallbacksAndMessages(null);
                h3.removeCallbacksAndMessages(null);
                h4.removeCallbacksAndMessages(null);
                h5.removeCallbacksAndMessages(null);
                AppUpdateActivity.FindFullUpdateManual = true;
                Update_release.FindFullUpdateManual = true;
                Intent intent = new Intent(FindUpdateActivity.this, Update_release.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
                break;
        }
        return true;
    }

    @SuppressLint("NewApi")
    private void stop_update_notice() {

        NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        Objects.requireNonNull(manager).cancel(1);

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
                FToastUtils.init().setRoundRadius(30).show("下载完成！");
            }
            //mMessageView.setText("下载进度:" + progress + " %");

            download_progress.setProgress(progress);

        }
    };

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void doDownload() {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        LogUtil.i(path);
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) +  getString(R.string.update_path));
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 设置progressBar初始化
        mProgressbar.setProgress(0);

        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
        //String downloadUrl = "https://update.meternity.cn/V2.9.7.4395_debug_CheckIn_20180407_r18HF171121_build29473_2974395_jiagu_sign.apk";

        String downloadUrl = "http://storage.tracup.com/o_1cj7rf3vl8ug1mf71ckd1ama53n3c.apk?attname=8.0.0.100&";
        String fileName = "app.apk";
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

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
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


    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case update_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){
                        AppUpdateActivity.changelog_now_version = false;
                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = true;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.GONE);
                        imageView4.setVisibility(View.GONE);

                        software_version = update_manager.get_software_version();

                        TextView do_not_have_new_version = (TextView)findViewById(R.id.do_not_have_new_version);
                        do_not_have_new_version.setVisibility(View.GONE);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        LinearLayout new_version = (LinearLayout)findViewById(R.id.new_version);
                        new_version.setVisibility(View.VISIBLE);
                        TextView finding_new_version = (TextView)findViewById(R.id.finding_new_version);
                        finding_new_version.setVisibility(View.GONE);
                        TextView new_software_version = (TextView)findViewById(R.id.new_software_version);
                        new_software_version.setText(software_version);

                        Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setVisibility(View.GONE);

                        mScanView = (ScanView) findViewById(R.id.scanView_find);
                        mScanView.stop();

                        //Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setEnabled(true);

                        new_version.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //AppUpdateActivity.version_release = true;
                                Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                                startActivity(intent);

                                AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
                            }
                        });

                        //AppUpdateActivity.version_release = true;
                        Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                        startActivity(intent);

                        AtyTransitionUtil.exitToRight(FindUpdateActivity.this);

                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = false;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);

                        find_update = find_update + 1;
                        if (find_update <= 5) {
                            //自动分配
                            new Thread(new check_update_version_runtime(h4, FindUpdateActivity.this)).start();
                        }else {
                            TextView finding_new_version = (TextView) findViewById(R.id.finding_new_version);
                            finding_new_version.setVisibility(View.GONE);
                            TextView do_not_have_new_version = (TextView) findViewById(R.id.do_not_have_new_version);
                            do_not_have_new_version.setVisibility(View.VISIBLE);

                            mScanView = (ScanView) findViewById(R.id.scanView_find);
                            mScanView.stop();

                            Button check_update = (Button)findViewById(R.id.check_update);
                            check_update.setEnabled(true);
                        }

                    }else{

                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = false;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);

                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        mScanView = (ScanView) findViewById(R.id.scanView_find);
                        mScanView.stop();

                        Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setEnabled(true);
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };


    Handler h2=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case beta_update_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){
                        AppUpdateActivity.changelog_now_version = false;
                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = true;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.GONE);
                        imageView4.setVisibility(View.GONE);

                        software_version = update_manager.get_software_version();

                        TextView do_not_have_new_version = (TextView)findViewById(R.id.do_not_have_new_version);
                        do_not_have_new_version.setVisibility(View.GONE);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        LinearLayout new_version = (LinearLayout)findViewById(R.id.new_version);
                        new_version.setVisibility(View.VISIBLE);
                        TextView finding_new_version = (TextView)findViewById(R.id.finding_new_version);
                        finding_new_version.setVisibility(View.GONE);
                        TextView new_software_version = (TextView)findViewById(R.id.new_software_version);
                        new_software_version.setText(software_version);

                        Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setVisibility(View.GONE);

                        mScanView = (ScanView) findViewById(R.id.scanView_find);
                        mScanView.stop();

                        //Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setEnabled(true);

                        new_version.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //AppUpdateActivity.version_release = false;
                                Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                                startActivity(intent);

                                AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
                            }
                        });

                        //AppUpdateActivity.version_release = false;
                        Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                        startActivity(intent);

                        AtyTransitionUtil.exitToRight(FindUpdateActivity.this);

                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = false;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);

                        find_update = find_update + 1;
                        if (find_update <= 5) {
                            //自动分配
                            new Thread(new check_update_version_runtime(h4, FindUpdateActivity.this)).start();
                        }else {
                            TextView finding_new_version = (TextView) findViewById(R.id.finding_new_version);
                            finding_new_version.setVisibility(View.GONE);
                            TextView do_not_have_new_version = (TextView) findViewById(R.id.do_not_have_new_version);
                            do_not_have_new_version.setVisibility(View.VISIBLE);

                            mScanView = (ScanView) findViewById(R.id.scanView_find);
                            mScanView.stop();

                            Button check_update = (Button)findViewById(R.id.check_update);
                            check_update.setEnabled(true);
                        }
                    }else{

                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = false;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);

                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        mScanView = (ScanView) findViewById(R.id.scanView_find);
                        mScanView.stop();

                        Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setEnabled(true);
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h3=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case debug_update_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){
                        AppUpdateActivity.changelog_now_version = false;
                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = true;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.GONE);
                        imageView4.setVisibility(View.GONE);

                        software_version = update_manager.get_software_version();

                        TextView do_not_have_new_version = (TextView)findViewById(R.id.do_not_have_new_version);
                        do_not_have_new_version.setVisibility(View.GONE);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        LinearLayout new_version = (LinearLayout)findViewById(R.id.new_version);
                        new_version.setVisibility(View.VISIBLE);
                        TextView finding_new_version = (TextView)findViewById(R.id.finding_new_version);
                        finding_new_version.setVisibility(View.GONE);
                        TextView new_software_version = (TextView)findViewById(R.id.new_software_version);
                        new_software_version.setText(software_version);

                        Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setVisibility(View.GONE);

                        mScanView = (ScanView) findViewById(R.id.scanView_find);
                        mScanView.stop();

                        //Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setEnabled(true);

                        new_version.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                                startActivity(intent);

                                AtyTransitionUtil.exitToRight(FindUpdateActivity.this);
                            }
                        });

                        Intent intent = new Intent(FindUpdateActivity.this, AppUpdateActivity.class);
                        startActivity(intent);

                        AtyTransitionUtil.exitToRight(FindUpdateActivity.this);

                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = false;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);

                        find_update = find_update + 1;
                        if (find_update <= 5) {
                            //自动分配
                            new Thread(new check_update_version_runtime(h4, FindUpdateActivity.this)).start();
                        }else {
                            TextView finding_new_version = (TextView) findViewById(R.id.finding_new_version);
                            finding_new_version.setVisibility(View.GONE);
                            TextView do_not_have_new_version = (TextView) findViewById(R.id.do_not_have_new_version);
                            do_not_have_new_version.setVisibility(View.VISIBLE);

                            mScanView = (ScanView) findViewById(R.id.scanView_find);
                            mScanView.stop();

                            Button check_update = (Button)findViewById(R.id.check_update);
                            check_update.setEnabled(true);
                        }

                    }else{

                        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
                        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
                        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                        new_Version_now = false;
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);

                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        mScanView = (ScanView) findViewById(R.id.scanView_find);
                        mScanView.stop();

                        Button check_update = (Button)findViewById(R.id.check_update);
                        check_update.setEnabled(true);
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h4=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case check_update_version_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));



                    if (String.valueOf(msg.obj).equals("beta")){
                        if (App_info.type.equals("beta")){
                            //beta
                            //find_update = 0;
                            new Thread(new beta_update_runtime(h2,FindUpdateActivity.this)).start();
                        }else {
                            //find_update_check = 0 ;
                            new Thread(new beta_update_runtime(h5,FindUpdateActivity.this)).start();
                        }
                        //LogUtil.i(software_version);
                    }else{
                        //release
                        //AppUpdateActivity.version_release = true;
                        new Thread(new update_runtime(h1,FindUpdateActivity.this)).start();
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h5=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case beta_update_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){

                        //beta
                        //find_update = 0;
                        new Thread(new beta_update_runtime(h2,FindUpdateActivity.this)).start();

                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        find_update_check = find_update_check + 1;
                        if (find_update_check <= 5) {
                            //自动分配
                            new Thread(new check_update_version_runtime(h4, FindUpdateActivity.this)).start();
                        }else {
                            //release
                            //find_update = 0;
                            //AppUpdateActivity.version_release = true;
                            new Thread(new update_runtime(h1,FindUpdateActivity.this)).start();
                        }
                    }else{
                        //release
                        //find_update = 0;
                        //AppUpdateActivity.version_release = true;
                        new Thread(new update_runtime(h1,FindUpdateActivity.this)).start();
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出FindUpdateActivity");
        //LogUtil.i("FindUpdateActivity---destroy");
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        //LogUtil.i("FindUpdateActivity---stop");
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(FindUpdateActivity.this, MainActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(FindUpdateActivity.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}
