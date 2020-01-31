package com.autumn.framework.update;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.autumn.framework.View.ScanView;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.manager.update.update_manager;
import com.autumn.sdk.runtime.update.update_release_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;

import java.lang.reflect.Field;
import java.util.Objects;

public class Update_release extends BaseActivity {

    public static boolean FindFullUpdateManual = false;
    private long mExitTime;
    private int check_times = 0;
    private Button retry;
    private Button exit;
    private ScanView scanView_release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_update_release);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getColorPrimary());
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        TextView statusText = (TextView)findViewById(R.id.statusText);
        statusText.setText("正在检测最新正式版");
        ScanView update = (ScanView)findViewById(R.id.scanView_release);
        update.start();
        new Thread(new update_release_runtime(h1,Update_release.this)).start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        retry = (Button)findViewById(R.id.retry);
        exit = (Button)findViewById(R.id.exit);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //retry.setVisibility(View.GONE);
                //exit.setVisibility(View.GONE);
                TextView statusText = (TextView)findViewById(R.id.statusText);
                statusText.setText("正在检测最新正式版");
                retry.setEnabled(false);
                retry.setVisibility(View.GONE);
                exit.setVisibility(View.GONE);
                scanView_release.start();
                new Thread(new update_release_runtime(h1, Update_release.this)).start();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FindFullUpdateManual) {
                    Intent intent = new Intent(Update_release.this, MainActivity.class);
                    startActivity(intent);

                    AtyTransitionUtil.exitToRight(Update_release.this);
                } else {
                    int currentVersion = Build.VERSION.SDK_INT;

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
            }
        });
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case update_release_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));

                    TextView statusText = (TextView)findViewById(R.id.statusText);

                    scanView_release = (ScanView)findViewById(R.id.scanView_release);
                    //scanView_release.stop();

                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){

                        scanView_release.stop();

                        statusText.setText(String.valueOf(msg.obj));

                        retry = (Button)findViewById(R.id.retry);
                        exit = (Button)findViewById(R.id.exit);
                        retry.setVisibility(View.GONE);
                        exit.setVisibility(View.GONE);
                        retry.setEnabled(true);

                        //AppUpdateActivity.version_release = true;
                        AppUpdateActivity.changelog_now_version = false;
                        AppUpdateActivity.from_LoginActivity = true;
                        if (!FindFullUpdateManual) AppUpdateActivity.FindFullUpdateManual = false;

                        if (update_manager.get_software_version() == null||update_manager.get_download_url() == null){
                            new Thread(new update_release_runtime(h1,Update_release.this)).start();
                        }else {
                            if (update_manager.get_software_version().equals("") || update_manager.get_download_url().equals("") || update_manager.get_software_version() == null || update_manager.get_download_url() == null) {
                                if (check_times < 5 && check_times >= 0) {
                                    check_times += 1;
                                    new Thread(new update_release_runtime(h1, Update_release.this)).start();
                                } else {
                                    check_times = 0;
                                    scanView_release.stop();
                                    retry = (Button) findViewById(R.id.retry);
                                    exit = (Button) findViewById(R.id.exit);
                                    retry.setVisibility(View.VISIBLE);
                                    exit.setVisibility(View.VISIBLE);
                                }
                            } else {

                                check_times = 0;
                                Intent intent = new Intent(Update_release.this, AppUpdateActivity.class);
                                startActivity(intent);

                                AtyTransitionUtil.exitToRight(Update_release.this);

                            }

                        }


                    }else if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        if (check_times < 5 && check_times >= 0){
                            check_times += 1;
                            new Thread(new update_release_runtime(h1,Update_release.this)).start();
                        }else{
                            check_times = 0;
                            scanView_release.stop();
                            statusText.setText(String.valueOf(msg.obj));
                            retry = (Button)findViewById(R.id.retry);
                            exit = (Button)findViewById(R.id.exit);
                            retry.setVisibility(View.VISIBLE);
                            exit.setVisibility(View.VISIBLE);
                            retry.setEnabled(true);
                        }
                    }else{

                        scanView_release.stop();
                        statusText.setText(String.valueOf(msg.obj));
                        retry = (Button)findViewById(R.id.retry);
                        exit = (Button)findViewById(R.id.exit);
                        retry.setVisibility(View.VISIBLE);
                        exit.setVisibility(View.VISIBLE);
                        retry.setEnabled(true);

                    }


                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    public boolean onKeyDown(int keyCode,KeyEvent event){
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (FindFullUpdateManual) {
                    Intent intent = new Intent(Update_release.this, MainActivity.class);
                    startActivity(intent);

                    AtyTransitionUtil.exitToRight(Update_release.this);
                } else {
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
                                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
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

                }
                return true;
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

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出Update_release");
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

}
