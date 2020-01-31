package com.autumn.reptile;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.autumn.framework.Log.CrashHandler;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.Log.LogUtils;
import com.autumn.framework.View.IosAlertDialog;
import com.autumn.framework.X5WebView.ui.SelfOperationActivity;
import com.autumn.framework.beta.beta_application_activity;
import com.autumn.framework.data.AES256;
import com.autumn.framework.data.App_info;
import com.autumn.framework.data.LogServerManger;
import com.autumn.framework.data.Page_manager;
import com.autumn.framework.data.key_manager_data;
import com.autumn.framework.entertainment.DouYinDownloader.MyService;
import com.autumn.framework.entertainment.manager.Video_download_manager;
import com.autumn.framework.entertainment.music_player.MusicPlayerActivity;
import com.autumn.framework.music.MusicService;
import com.autumn.framework.music.activity.PlayActivity;
import com.autumn.framework.setting.super_setting;
import com.autumn.framework.update.FindUpdateActivity;
import com.autumn.framework.user.LoginActivity;
import com.autumn.framework.user.UserInfoActivity;
import com.autumn.framework.user.level_show_manager;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.app_interface.AboutFragement;
import com.autumn.reptile.app_interface.EntertainmentFragement;
import com.autumn.reptile.app_interface.HomeFragment;
import com.autumn.reptile.app_interface.RulesFragement;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.manager.notice.notice_manager;
import com.autumn.sdk.manager.user.user_info_manager;
import com.autumn.sdk.runtime.notice.beta_notice_runtime;
import com.autumn.sdk.runtime.notice.debug_notice_runtime;
import com.autumn.sdk.runtime.notice.notice_runtime;
import com.autumn.sdk.runtime.update.check_update_version_runtime;
import com.autumn.sdk.runtime.user.get_available_date_runtime;
import com.autumn.sdk.runtime.user.get_private_name_runtime;
import com.autumn.sdk.runtime.user.user_info_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tracup.library.Tracup;

import java.util.Objects;

import cn.hotapk.fastandrutils.utils.FLogUtils;
import cn.hotapk.fastandrutils.utils.FToastUtils;

import static com.autumn.framework.status.login_status_manager.get_login_status;
import static com.autumn.reptile.MyApplication.getAppContext;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationBar.OnTabSelectedListener {

    private String SETTING = "Config";
    private String AUTO_POST = "Auto_post";
    private String AUTO_SAVELOG = "Auto_savelog";
    private String ALLOW_D = "Allow_d";
    private String ALLOW_E = "Allow_e";
    private String ALLOW_I = "Allow_i";
    private String ALLOW_V = "Allow_v";
    private String ALLOW_W = "Allow_w";
    private String ALLOW_WTF = "Allow_wtf";
    private String ALLOW_DEBUG = "Allow_debug";
    private String ALLOW_ALL = "Allow_all";
    private String ALLOW_SETTING = "Allow_setting";
    private static final String USER_PRIVATE_NAME = "User_private_name";
    private static final String USER_AVAILABLE_DATE = "User_available_date" ;
    private static final String APP_NOTICE_MANAGER = "App_notice_manager";
    private static final String APP_NOTICE_MANAGER_BETA = "App_notice_manager_beta";
    private static final String APP_NOTICE_MANAGER_DEBUG = "App_notice_manager_debug";
    private static final String VERSION_CODE = "Version_code";
    public static boolean is_auto_restart= false;
    public static boolean splash_start=false;
    public static boolean is_login = false;
    public static String user_level;
    public static int integrals;
    public static int prestige;
    public static int grow_integrals;
    public static boolean update_notice = false;
    private ViewPager mViewPager;
    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";
    private static final String IS_AUTO_LOGIN = "Is_auto_login";
    private static final String USER_KEY = "User_key";
    public static String user_key;
    public static String app_notice_version;
    public static String app_notice_content;
    private PowerManager.WakeLock wakeLock;
    private static final String TAG = "com.autumn.reptile";
    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = Page_manager.getLastPosition();
    private HomeFragment mHomeFragment;
    private AboutFragement mAboutFragment;
    private RulesFragement mRulesFragement;
    private long mExitTime = 0;
    private String log_level;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private EntertainmentFragement mEntertainmentFragement;
    public static boolean bottomNavigationBar_init_Status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        if (!get_login_status()){
            super.onStop();
            //登录状态异常
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
        }else {
            setContentView(R.layout.activity_main);
        }
        //setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
        }
        initState();
        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);
        //LogUtil.i("添加MainActivity到Stack");

        new Thread(new check_update_version_runtime(h6,MainActivity.this)).start();

        /**
         * bottomNavigation 设置
         */

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        if (bottomNavigationBar == null && bottomNavigationBar_init_Status){
            //finish();
            //super.onDestroy();
            Intent intent = new Intent(this, Splash.class);
            startActivity(intent);
            return;
        }else {
            bottomNavigationBar_init_Status = true;
        }

        try {
            /** 导航基础设置 包括按钮选中效果 导航栏背景色等 */
            bottomNavigationBar
                    .setTabSelectedListener(this)
                    .setMode(BottomNavigationBar.MODE_SHIFTING)
                    /**
                     *  setMode() 内的参数有三种模式类型：
                     *  MODE_DEFAULT 自动模式：导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
                     *  MODE_FIXED 固定模式：未选中的Item显示文字，无切换动画效果。
                     *  MODE_SHIFTING 切换模式：未选中的Item不显示文字，选中的显示文字，有切换动画效果。
                     */

                    .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                    /**
                     *  setBackgroundStyle() 内的参数有三种样式
                     *  BACKGROUND_STYLE_DEFAULT: 默认样式 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC
                     *                                    如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
                     *  BACKGROUND_STYLE_STATIC: 静态样式 点击无波纹效果
                     *  BACKGROUND_STYLE_RIPPLE: 波纹样式 点击有波纹效果
                     */

                    .setActiveColor("#4B0082") //选中颜色
                    .setInActiveColor("#9F79EE") //未选中颜色
                    .setBarBackgroundColor("#F0F0F0");//导航栏背景色

            /** 添加导航按钮 */
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.bottom_1, "首页"))
                    .addItem(new BottomNavigationItem(R.drawable.entertainment_logo, "娱乐"))
                    .addItem(new BottomNavigationItem(R.drawable.bottom_3, "成长规则"))
                    .addItem(new BottomNavigationItem(R.drawable.bottom_4, "关于"))
                    //.addItem(new BottomNavigationItem(R.drawable.ic_my, "个人设置"))
                    .setFirstSelectedPosition(lastSelectedPosition)
                    .initialise(); //initialise 一定要放在 所有设置的最后一项

            setDefaultFragment();//设置默认导航栏

        }catch (Exception e){
            bottomNavigationBar_init_Status = false;
            Intent intent = new Intent(this, Splash.class);
            startActivity(intent);
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, super_setting.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(MainActivity.this);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headview=navigationView.getHeaderView(0);

        ImageView imageView = (ImageView)headview.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(MainActivity.this);
            }
        });

        ImageButton head_iv= (ImageButton) headview.findViewById(R.id.imageButton);

        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        final TextView login_username = (TextView)headview.findViewById(R.id.nav_textView1);
        final TextView user_available_date = (TextView)headview.findViewById(R.id.nav_textView2);

        //LinearLayout user_level_view = (LinearLayout)headview.findViewById(R.id.user_level_view);
        //user_level_view.setVisibility(View.GONE);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

                //login_username.setText(getValue(USER_INFO,USER_PRIVATE_NAME));
                //user_available_date.setText(getValue(USER_INFO,USER_AVAILABLE_DATE));

                new Thread(new user_info_runtime(h7,MainActivity.this,getValue(USER_INFO,LOGIN_USERNAME))).start();


                if (getValue(USER_INFO, USER_PRIVATE_NAME).equals("") || getValue(USER_INFO, USER_PRIVATE_NAME) == null) {

                    new Thread(new get_private_name_runtime(h1, MainActivity.this, getValue(USER_INFO, LOGIN_USERNAME))).start();

                } else {
                    login_username.setText(getValue(USER_INFO, USER_PRIVATE_NAME));
                    new Thread(new get_private_name_runtime(h1, MainActivity.this, getValue(USER_INFO, LOGIN_USERNAME))).start();
                }

                if (getValue(USER_INFO, USER_AVAILABLE_DATE).equals("") || getValue(USER_INFO, USER_AVAILABLE_DATE) == null) {

                    new Thread(new get_available_date_runtime(h2, MainActivity.this, getValue(USER_INFO, LOGIN_USERNAME))).start();

                } else {
                    user_available_date.setText("到期日期：" + getValue(USER_INFO, USER_AVAILABLE_DATE));
                    new Thread(new get_available_date_runtime(h2, MainActivity.this, getValue(USER_INFO, LOGIN_USERNAME))).start();
                }

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //LogUtil.i(getValue(USER_INFO,LOGIN_USERNAME));
        //getValue(USER_INFO,LOGIN_USERNAME);

        if(get_login_status()){

            new Thread(new user_info_runtime(h7,MainActivity.this,getValue(USER_INFO,LOGIN_USERNAME))).start();
            new Thread(new get_private_name_runtime(h1, MainActivity.this, getValue(USER_INFO, LOGIN_USERNAME))).start();
            new Thread(new get_available_date_runtime(h2, MainActivity.this, getValue(USER_INFO, LOGIN_USERNAME))).start();

        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
        }
        //LogUtil.i("======================》程序唤醒《======================");
        LogUtil.d("MainActivity" +
                "\n{" +
                "\nStatus[程序唤醒]" +
                "\n}");
    }

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出MainActivity");
        ActivtyStack.getScreenManager().popActivity(this);
        if (LogServerManger.getLogServerStatus()){
            FLogUtils.getInstance().stopLogServer();
            LogServerManger.setLogServerStatus(false);
        }
        stopMusicService();
        stopShortVideoService();
        super.onDestroy();
    }

    private void stopShortVideoService() {
        Intent intent_service=new Intent(MainActivity.this, MyService.class);
        stopService(intent_service);
        Video_download_manager.setStatus(false);
    }

    private void stopMusicService() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        stopService(intent);
        MusicPlayerActivity.destroyNotification();
        PlayActivity.destroyNotification();
    }

    @Override
    protected void onStop() {
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        } else {
            if(keyCode==KeyEvent.KEYCODE_BACK){
                if((System.currentTimeMillis()-mExitTime)>2000){
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime=System.currentTimeMillis();
                }else{
                    int currentVersion = android.os.Build.VERSION.SDK_INT;

                    try {
                        Intent intent = new Intent(MainActivity.this, MusicService.class);
                        stopService(intent);
                    }catch (Exception e){

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
                    ActivtyStack.getScreenManager().popAllActivityExceptOne();

                    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
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
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_act_self) {

            Intent intent = new Intent(MainActivity.this, SelfOperationActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);

            return true;
        }

        if (id == R.id.action_beta_application) {
            Intent intent = new Intent(MainActivity.this,beta_application_activity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
            return true;
        }

        if (id == R.id.action_update) {
            FindUpdateActivity.FROM_CHANGELOG_PAGE = false;
            Intent intent = new Intent(MainActivity.this, FindUpdateActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
            return true;
        }

        if (id == R.id.action_feedback) {

            Tracup.showIntrMessage(this);

            return true;
        }

        if (id == R.id.action_super_settings) {
            //Intent intent = new Intent(MainActivity.this,super_setting.class);
            //startActivity(intent);
            //AtyTransitionUtil.exitToRight(MainActivity.this);
            init_super_setting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (id == R.id.nav_info) {
            Intent intent = new Intent(MainActivity.this,UserInfoActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
            // Handle the camera action
        } else if (id == R.id.nav_beta_application) {
            Intent intent = new Intent(MainActivity.this,beta_application_activity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
        } else if (id == R.id.nav_manage) {
            //Intent intent = new Intent(this, X5WebGameActivity.class);
            //Bundle bundle = new Bundle();
            //bundle.putString("key", HttpUrlConstance.BAI_DU);
            //intent.putExtras(bundle);
            //startActivity(intent);
            Intent intent = new Intent(MainActivity.this,SelfOperationActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
        }else if (id == R.id.nav_update) {
            FindUpdateActivity.FROM_CHANGELOG_PAGE = false;
            Intent intent = new Intent(MainActivity.this,FindUpdateActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
        }  else if (id == R.id.nav_setting) {
            //Intent intent = new Intent(MainActivity.this,super_setting.class);
            //startActivity(intent);
            //AtyTransitionUtil.exitToRight(MainActivity.this);
            init_super_setting();
        } else if (id == R.id.nav_send) {

            Tracup.showIntrMessage(this);
            //ThemeDialog dialog = new ThemeDialog();
            //dialog.show(getSupportFragmentManager(), "theme");

        }else if(id==R.id.logout) {

            new IosAlertDialog(MainActivity.this).builder().setCancelable(false).setTitle("注意").setMsg("是否退出此账号？")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("退出", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //新用户可以看到第一页
                    Page_manager.setLastPosition(0);

                    /*try {
                        Intent intent = new Intent(MainActivity.this, MusicService.class);
                        stopService(intent);
                    }catch (Exception e){

                    }*/

                    stopMusicService();
                    stopShortVideoService();

                    if (LogServerManger.getLogServerStatus()){
                        FLogUtils.getInstance().stopLogServer();
                        LogServerManger.setLogServerStatus(false);
                    }

                    writeBoolean(SETTING, ALLOW_SETTING, false);
                    writeBoolean(SETTING,AUTO_POST,true);
                    writeBoolean(SETTING,AUTO_SAVELOG,false);
                    writeBoolean(SETTING,ALLOW_ALL,false);
                    writeBoolean(SETTING,ALLOW_D,false);
                    writeBoolean(SETTING,ALLOW_E,false);
                    writeBoolean(SETTING,ALLOW_I,false);
                    writeBoolean(SETTING,ALLOW_V,false);
                    writeBoolean(SETTING,ALLOW_W,false);
                    writeBoolean(SETTING,ALLOW_WTF,false);
                    writeBoolean(SETTING,ALLOW_DEBUG,false);
                    // TODO Auto-generated method stub
                    writeBoolean(USER_INFO, IS_AUTO_LOGIN, false);
                    //LogUtil.i("======================》退出账号《======================");
                    //LogUtil.i("======================》停止全部服务《======================");
                    LogUtil.d("MainActivity" +
                            "\n{" +
                            "\nStatus" +
                            "\n[" +
                            "\n退出账号" +
                            "\n停止全部服务[service]" +
                            "\n]" +
                            "\n}");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(MainActivity.this);
                }
            }).show();

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 设置默认导航栏
     */
    private void setDefaultFragment() {
        switch (lastSelectedPosition){
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance("首页");
                }
                //transaction.replace(R.id.view_container, mHomeFragment);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mHomeFragment).commit();
                break;

            case 1:
                if (mEntertainmentFragement == null) {
                    mEntertainmentFragement = EntertainmentFragement.newInstance("娱乐");
                }
                //transaction.replace(R.id.view_container, mEntertainmentFragement);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mEntertainmentFragement).commit();
                break;

            case 2:
                if (mRulesFragement == null) {
                    mRulesFragement = RulesFragement.newInstance("成长规则");
                }
                //transaction.replace(R.id.view_container, mRulesFragement);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mRulesFragement).commit();
                break;

            case 3:
                if (mAboutFragment == null) {
                    mAboutFragment = AboutFragement.newInstance("关于");
                }
                //transaction.replace(R.id.view_container, mAboutFragment);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mAboutFragment).commit();
                break;

            default:
                break;
        }
        //FragmentManager fm = getFragmentManager();
        //FragmentTransaction transaction = fm.beginTransaction();
        //mHomeFragment = HomeFragment.newInstance("首页");
        //transaction.replace(R.id.view_container, mHomeFragment);
        //transaction.commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mHomeFragment).commit();
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case get_private_name_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals("")||String.valueOf(msg.obj).equals(null)) {
                        new Thread(new get_private_name_runtime(h1, MainActivity.this,getValue(USER_INFO,LOGIN_USERNAME))).start();
                    }else{
                        //writeValue(USER_INFO,USER_PRIVATE_NAME,String.valueOf(msg.obj));
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        navigationView.setNavigationItemSelectedListener(MainActivity.this);


                        View headview=navigationView.getHeaderView(0);

                        writeValue(USER_INFO,USER_PRIVATE_NAME,String.valueOf(msg.obj));

                        TextView login_username = (TextView)findViewById(R.id.nav_textView1);
                        login_username.setText(String.valueOf(msg.obj));
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
                case get_available_date_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals("")||String.valueOf(msg.obj).equals(null)) {
                        new Thread(new get_available_date_runtime(h1, MainActivity.this,getValue(USER_INFO,LOGIN_USERNAME))).start();
                    }else{
                        //writeValue(USER_INFO,USER_PRIVATE_NAME,String.valueOf(msg.obj));
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        navigationView.setNavigationItemSelectedListener(MainActivity.this);


                        View headview=navigationView.getHeaderView(0);

                        writeValue(USER_INFO,USER_AVAILABLE_DATE,String.valueOf(msg.obj));

                        TextView user_available_date = (TextView)findViewById(R.id.nav_textView2);
                        user_available_date.setText("到期日期："+String.valueOf(msg.obj));
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
                case notice_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.notice_new_version))){

                        app_notice_version = notice_manager.get_app_notice_version();
                        app_notice_content = notice_manager.get_app_notice_content();

                        if(getValue(APP_NOTICE_MANAGER,VERSION_CODE).equals("")||getValue(APP_NOTICE_MANAGER,VERSION_CODE).equals(null)){
                            show_notice();
                            writeValue(APP_NOTICE_MANAGER,VERSION_CODE,app_notice_version);
                        }else{
                            if (Integer.parseInt(getValue(APP_NOTICE_MANAGER,VERSION_CODE))>=Integer.parseInt(app_notice_version)){

                            }else{
                                show_notice();
                                writeValue(APP_NOTICE_MANAGER,VERSION_CODE,app_notice_version);
                            }
                        }
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.notice_new_version_not_exist))){
                        //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }else{
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
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
                case beta_notice_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.notice_new_version))){

                        app_notice_version = notice_manager.get_app_notice_version();
                        app_notice_content = notice_manager.get_app_notice_content();

                        if(getValue(APP_NOTICE_MANAGER_BETA,VERSION_CODE).equals("")||getValue(APP_NOTICE_MANAGER_BETA,VERSION_CODE).equals(null)){
                            show_notice();
                            writeValue(APP_NOTICE_MANAGER_BETA,VERSION_CODE,app_notice_version);
                        }else{
                            if (Integer.parseInt(getValue(APP_NOTICE_MANAGER_BETA,VERSION_CODE))>=Integer.parseInt(app_notice_version)){

                            }else{
                                show_notice();
                                writeValue(APP_NOTICE_MANAGER_BETA,VERSION_CODE,app_notice_version);
                            }
                        }
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.notice_new_version_not_exist))){
                        //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }else{
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
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
                case debug_notice_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.notice_new_version))){

                        app_notice_version = notice_manager.get_app_notice_version();
                        app_notice_content = notice_manager.get_app_notice_content();

                        if(getValue(APP_NOTICE_MANAGER_DEBUG,VERSION_CODE).equals("")||getValue(APP_NOTICE_MANAGER_DEBUG,VERSION_CODE).equals(null)){
                            show_notice();
                            writeValue(APP_NOTICE_MANAGER_DEBUG,VERSION_CODE,app_notice_version);
                        }else{
                            if (Integer.parseInt(getValue(APP_NOTICE_MANAGER_DEBUG,VERSION_CODE))>=Integer.parseInt(app_notice_version)){

                            }else{
                                show_notice();
                                writeValue(APP_NOTICE_MANAGER_DEBUG,VERSION_CODE,app_notice_version);
                            }
                        }
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.notice_new_version_not_exist))){
                        //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }else{
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h6=new Handler()
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

                    if (String.valueOf(msg.obj).equals("beta") && App_info.type.equals("beta")){
                        //beta
                        //Beta.autoCheckUpgrade = false;
                        //Bugly.init(getApplicationContext(), getString(R.string.bugly_app_id), true);
                        new Thread(new beta_notice_runtime(h4,MainActivity.this)).start();
                        //LogUtil.i(software_version);
                    }else{
                        //release
                        //Beta.autoCheckUpgrade = false;
                        //Bugly.init(getApplicationContext(), getString(R.string.bugly_app_id), true);
                        new Thread(new notice_runtime(h3,MainActivity.this)).start();
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h7=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case user_info_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.info_full))) {

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        navigationView.setNavigationItemSelectedListener(MainActivity.this);
                        View headview=navigationView.getHeaderView(0);


                        user_level = user_info_manager.get_user_level();
                        integrals = user_info_manager.get_integrals();
                        prestige = user_info_manager.get_prestige();
                        grow_integrals = user_info_manager.get_grow_integrals();
                        log_level = user_info_manager.get_log_level();
                        //showProgress(false);

                        level_manager(user_level);
                        bxx_manager();
                        qxx_manager();
                        yl_manager();
                        ty_manager();
                        hg_manager();

                        init_log_system();

                        //LinearLayout user_level_view = (LinearLayout)headview.findViewById(R.id.user_level_view);
                        //user_level_view.setVisibility(View.VISIBLE);

                    }else{
                        //showProgress(false);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }
                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    private void show_notice() {
        new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                .setTitle("公告")
                .setMessage(app_notice_content.replaceAll("\\[换行]","\n"))
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
        /*new IosAlertDialog(MainActivity.this).builder().setCancelable(false).setTitle("公告").setMsg(app_notice_content.replaceAll("\\[换行]","\n"))
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();*/
    }

    public void level_manager(String level){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headview=navigationView.getHeaderView(0);
        ImageView vip = (ImageView)headview.findViewById(R.id.vip_view);
        ImageView svip = (ImageView)headview.findViewById(R.id.svip_view);
        if (level == null){
            svip.setVisibility(View.GONE);
            vip.setVisibility(View.GONE);
        }else {
            if (level.equals("svip")) {
                //level = "svip  (3倍加速成长中)";
                vip.setVisibility(View.GONE);
                svip.setVisibility(View.VISIBLE);
            }
            if (level.equals("vip")) {
                //level = "vip  (2倍加速成长中)";
                svip.setVisibility(View.GONE);
                vip.setVisibility(View.VISIBLE);
            }
            if (level.equals("普通用户")) {
                //level = "普通用户  (慢速成长中)";
                svip.setVisibility(View.GONE);
                vip.setVisibility(View.GONE);
            }
        }
    }

    public void bxx_manager(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headview=navigationView.getHeaderView(0);
        ImageView level_bxx = (ImageView)headview.findViewById(R.id.level_bxx);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_bxx(String.valueOf(grow_integrals)) == 0){
            level_bxx.setVisibility(View.GONE);
        }else{
            level_bxx.setVisibility(View.VISIBLE);
        }
    }

    public void qxx_manager(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headview=navigationView.getHeaderView(0);
        ImageView level_qxx1 = (ImageView)headview.findViewById(R.id.level_qxx1);
        ImageView level_qxx2 = (ImageView)headview.findViewById(R.id.level_qxx2);
        ImageView level_qxx3 = (ImageView)headview.findViewById(R.id.level_qxx3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_qxx(String.valueOf(grow_integrals)) == 0){
            level_qxx1.setVisibility(View.GONE);
            level_qxx2.setVisibility(View.GONE);
            level_qxx3.setVisibility(View.GONE);
        }else if (level_show_manager.get_qxx(String.valueOf(grow_integrals)) == 1){
            level_qxx1.setVisibility(View.VISIBLE);
            level_qxx2.setVisibility(View.GONE);
            level_qxx3.setVisibility(View.GONE);
        }else if (level_show_manager.get_qxx(String.valueOf(grow_integrals)) == 2){
            level_qxx1.setVisibility(View.VISIBLE);
            level_qxx2.setVisibility(View.VISIBLE);
            level_qxx3.setVisibility(View.GONE);
        }else{
            level_qxx1.setVisibility(View.GONE);
            level_qxx2.setVisibility(View.GONE);
            level_qxx3.setVisibility(View.GONE);
        }
    }

    public void yl_manager(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headview=navigationView.getHeaderView(0);
        ImageView level_yl1 = (ImageView)headview.findViewById(R.id.level_yl1);
        ImageView level_yl2 = (ImageView)headview.findViewById(R.id.level_yl2);
        ImageView level_yl3 = (ImageView)headview.findViewById(R.id.level_yl3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_yl(String.valueOf(grow_integrals)) == 0){
            level_yl1.setVisibility(View.GONE);
            level_yl2.setVisibility(View.GONE);
            level_yl3.setVisibility(View.GONE);
        }else if (level_show_manager.get_yl(String.valueOf(grow_integrals)) == 1){
            level_yl1.setVisibility(View.VISIBLE);
            level_yl2.setVisibility(View.GONE);
            level_yl3.setVisibility(View.GONE);
        }else if (level_show_manager.get_yl(String.valueOf(grow_integrals)) == 2){
            level_yl1.setVisibility(View.VISIBLE);
            level_yl2.setVisibility(View.VISIBLE);
            level_yl3.setVisibility(View.GONE);
        }else{
            level_yl1.setVisibility(View.GONE);
            level_yl2.setVisibility(View.GONE);
            level_yl3.setVisibility(View.GONE);
        }
    }

    public void ty_manager(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headview=navigationView.getHeaderView(0);
        ImageView level_ty1 = (ImageView)headview.findViewById(R.id.level_ty1);
        ImageView level_ty2 = (ImageView)headview.findViewById(R.id.level_ty2);
        ImageView level_ty3 = (ImageView)headview.findViewById(R.id.level_ty3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_ty(String.valueOf(grow_integrals)) == 0){
            level_ty1.setVisibility(View.GONE);
            level_ty2.setVisibility(View.GONE);
            level_ty3.setVisibility(View.GONE);
        }else if (level_show_manager.get_ty(String.valueOf(grow_integrals)) == 1){
            level_ty1.setVisibility(View.VISIBLE);
            level_ty2.setVisibility(View.GONE);
            level_ty3.setVisibility(View.GONE);
        }else if (level_show_manager.get_ty(String.valueOf(grow_integrals)) == 2){
            level_ty1.setVisibility(View.VISIBLE);
            level_ty2.setVisibility(View.VISIBLE);
            level_ty3.setVisibility(View.GONE);
        }else{
            level_ty1.setVisibility(View.GONE);
            level_ty2.setVisibility(View.GONE);
            level_ty3.setVisibility(View.GONE);
        }
    }

    public void hg_manager(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headview=navigationView.getHeaderView(0);
        ImageView level_hg1 = (ImageView)headview.findViewById(R.id.level_hg1);
        ImageView level_hg2 = (ImageView)headview.findViewById(R.id.level_hg2);
        ImageView level_hg3 = (ImageView)headview.findViewById(R.id.level_hg3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_hg(String.valueOf(grow_integrals)) == 0){
            level_hg1.setVisibility(View.GONE);
            level_hg2.setVisibility(View.GONE);
            level_hg3.setVisibility(View.GONE);
        }else if (level_show_manager.get_hg(String.valueOf(grow_integrals)) == 1){
            level_hg1.setVisibility(View.VISIBLE);
            level_hg2.setVisibility(View.GONE);
            level_hg3.setVisibility(View.GONE);
        }else if (level_show_manager.get_hg(String.valueOf(grow_integrals)) == 2){
            level_hg1.setVisibility(View.VISIBLE);
            level_hg2.setVisibility(View.VISIBLE);
            level_hg3.setVisibility(View.GONE);
        }else{
            level_hg1.setVisibility(View.GONE);
            level_hg2.setVisibility(View.GONE);
            level_hg3.setVisibility(View.GONE);
        }
    }

    //写入数据
    public void writeValue(String file,String name,String value){

        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            //LogUtils.i(file);
            name = AES256.aesEncrypt(name, key);
            //LogUtils.i(name);
            value = AES256.aesEncrypt(value, key);
            //LogUtils.i(value);
            SharedPreferences.Editor editor = getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE).edit();
            editor.putString(name,value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void writeBoolean(String file,String name,Boolean value){
        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences.Editor editor = getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE).edit();
            editor.putBoolean(name,value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //读取数据
    public String getValue(String file, String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            //LogUtil.i("JB51"+file);
            //LogUtil.i("JB51"+name);
            name = AES256.aesEncrypt(name, key);
            //LogUtil.i("JB51"+name);
            SharedPreferences pref = getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE);
            //LogUtil.i("JB51");
            //LogUtil.i("JB51"+pref.getString(name,""));
            String Value = AES256.aesDecrypt(pref.getString(name,""), key);
            //LogUtil.i("JB51"+Value);
            return Value;
        } catch (Exception e) {
            //e.printStackTrace();
            LogUtil.i(e.toString());
        }
        return null;
    }
    public boolean getBoolean(String file,String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE);
            Boolean Value = pref.getBoolean(name,false);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     */
    private void initState() {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
            //boolean lightStatusBar = false;
            //StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.colorPrimary), lightStatusBar);

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

    @Override
    public void onTabSelected(int position) {

        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        Page_manager.setLastPosition(position);
        //FragmentManager fm = this.getFragmentManager();
        //开启事务
        //FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance("首页");
                }
                //transaction.replace(R.id.view_container, mHomeFragment);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mHomeFragment).commit();
                break;

            case 1:
                if (mEntertainmentFragement == null) {
                    mEntertainmentFragement = EntertainmentFragement.newInstance("娱乐");
                }
                //transaction.replace(R.id.view_container, mEntertainmentFragement);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mEntertainmentFragement).commit();
                break;

            case 2:
                if (mRulesFragement == null) {
                    mRulesFragement = RulesFragement.newInstance("成长规则");
                }
                //transaction.replace(R.id.view_container, mRulesFragement);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mRulesFragement).commit();
                break;

            case 3:
                if (mAboutFragment == null) {
                    mAboutFragment = AboutFragement.newInstance("关于");
                }
                //transaction.replace(R.id.view_container, mAboutFragment);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,mAboutFragment).commit();
                break;

            default:
                break;
        }

        //transaction.commit();// 事务提交

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void init_super_setting(){

        if (!App_info.type.equals("beta")) {
            writeBoolean(SETTING, ALLOW_SETTING, false);
            log_level = "无染色模式";

            writeBoolean(SETTING, AUTO_POST, true);
            writeBoolean(SETTING, AUTO_SAVELOG, false);
            writeBoolean(SETTING, ALLOW_ALL, false);
            writeBoolean(SETTING, ALLOW_D, false);
            writeBoolean(SETTING, ALLOW_E, false);
            writeBoolean(SETTING, ALLOW_I, false);
            writeBoolean(SETTING, ALLOW_V, false);
            writeBoolean(SETTING, ALLOW_W, false);
            writeBoolean(SETTING, ALLOW_WTF, false);
            writeBoolean(SETTING, ALLOW_DEBUG, false);

        }
        Boolean Allow_Setting = getBoolean(SETTING, ALLOW_SETTING);
        if (!Allow_Setting && App_info.type.equals("beta")) {
            if (log_level == null) {
                //FToastUtils.init().setRoundRadius(30).show("参数异常");
            } else {
                if (log_level.equals("无染色模式")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, false);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, false);
                    writeBoolean(SETTING, ALLOW_E, false);
                    writeBoolean(SETTING, ALLOW_I, false);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, false);

                } else if (log_level.equals("一级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, false);
                    writeBoolean(SETTING, ALLOW_E, false);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("二级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, false);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("三级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, true);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("四级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, true);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, true);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("五级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, true);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, true);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, true);
                    writeBoolean(SETTING, ALLOW_W, true);
                    writeBoolean(SETTING, ALLOW_WTF, true);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                }

            }

            Boolean is_auto = getBoolean(SETTING, AUTO_POST);
            Boolean is_savelog = getBoolean(SETTING, AUTO_SAVELOG);
            Boolean Allow_ALL = getBoolean(SETTING, ALLOW_ALL);
            Boolean Allow_D = getBoolean(SETTING, ALLOW_D);
            Boolean Allow_E = getBoolean(SETTING, ALLOW_E);
            Boolean Allow_I = getBoolean(SETTING, ALLOW_I);
            Boolean Allow_V = getBoolean(SETTING, ALLOW_V);
            Boolean Allow_W = getBoolean(SETTING, ALLOW_W);
            Boolean Allow_WTF = getBoolean(SETTING, ALLOW_WTF);
            Boolean Allow_DEBUG = getBoolean(SETTING, ALLOW_DEBUG);

            super_setting.is_auto = is_auto;
            super_setting.is_savelog = is_savelog;
            super_setting.Allow_ALL = Allow_ALL;
            super_setting.Allow_D = Allow_D;
            super_setting.Allow_E = Allow_E;
            super_setting.Allow_I = Allow_I;
            super_setting.Allow_V = Allow_V;
            super_setting.Allow_W = Allow_W;
            super_setting.Allow_WTF = Allow_WTF;
            super_setting.Allow_DEBUG = Allow_DEBUG;
            super_setting.Allow_SETTING = Allow_Setting;

            Intent intent = new Intent(MainActivity.this, super_setting.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);

        }else{
            Boolean is_auto = getBoolean(SETTING, AUTO_POST);
            Boolean is_savelog = getBoolean(SETTING, AUTO_SAVELOG);
            Boolean Allow_ALL = getBoolean(SETTING, ALLOW_ALL);
            Boolean Allow_D = getBoolean(SETTING, ALLOW_D);
            Boolean Allow_E = getBoolean(SETTING, ALLOW_E);
            Boolean Allow_I = getBoolean(SETTING, ALLOW_I);
            Boolean Allow_V = getBoolean(SETTING, ALLOW_V);
            Boolean Allow_W = getBoolean(SETTING, ALLOW_W);
            Boolean Allow_WTF = getBoolean(SETTING, ALLOW_WTF);
            Boolean Allow_DEBUG = getBoolean(SETTING, ALLOW_DEBUG);

            super_setting.is_auto = is_auto;
            super_setting.is_savelog = is_savelog;
            super_setting.Allow_ALL = Allow_ALL;
            super_setting.Allow_D = Allow_D;
            super_setting.Allow_E = Allow_E;
            super_setting.Allow_I = Allow_I;
            super_setting.Allow_V = Allow_V;
            super_setting.Allow_W = Allow_W;
            super_setting.Allow_WTF = Allow_WTF;
            super_setting.Allow_DEBUG = Allow_DEBUG;
            super_setting.Allow_SETTING = Allow_Setting;

            Intent intent = new Intent(MainActivity.this, super_setting.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(MainActivity.this);
        }

    }

    private void init_log_system(){

        if (!App_info.type.equals("beta")) {
            writeBoolean(SETTING, ALLOW_SETTING, false);
            log_level = "无染色模式";

            writeBoolean(SETTING, AUTO_POST, true);
            writeBoolean(SETTING, AUTO_SAVELOG, false);
            writeBoolean(SETTING, ALLOW_ALL, false);
            writeBoolean(SETTING, ALLOW_D, false);
            writeBoolean(SETTING, ALLOW_E, false);
            writeBoolean(SETTING, ALLOW_I, false);
            writeBoolean(SETTING, ALLOW_V, false);
            writeBoolean(SETTING, ALLOW_W, false);
            writeBoolean(SETTING, ALLOW_WTF, false);
            writeBoolean(SETTING, ALLOW_DEBUG, false);

        }
        Boolean Allow_Setting = getBoolean(SETTING, ALLOW_SETTING);
        if (!Allow_Setting && App_info.type.equals("beta")) {
            if (log_level == null){
                //FToastUtils.init().setRoundRadius(30).show("参数异常");
            }else {
                if (log_level.equals("无染色模式")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, false);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, false);
                    writeBoolean(SETTING, ALLOW_E, false);
                    writeBoolean(SETTING, ALLOW_I, false);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, false);

                } else if (log_level.equals("一级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, false);
                    writeBoolean(SETTING, ALLOW_E, false);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("二级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, false);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("三级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, true);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, false);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("四级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, false);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, true);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, false);
                    writeBoolean(SETTING, ALLOW_W, true);
                    writeBoolean(SETTING, ALLOW_WTF, false);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                } else if (log_level.equals("五级染色")) {

                    writeBoolean(SETTING, AUTO_POST, true);
                    writeBoolean(SETTING, AUTO_SAVELOG, true);
                    writeBoolean(SETTING, ALLOW_ALL, true);
                    writeBoolean(SETTING, ALLOW_D, true);
                    writeBoolean(SETTING, ALLOW_E, true);
                    writeBoolean(SETTING, ALLOW_I, true);
                    writeBoolean(SETTING, ALLOW_V, true);
                    writeBoolean(SETTING, ALLOW_W, true);
                    writeBoolean(SETTING, ALLOW_WTF, true);
                    writeBoolean(SETTING, ALLOW_DEBUG, true);

                }

                Boolean is_auto = getBoolean(SETTING, AUTO_POST);
                Boolean is_savelog = getBoolean(SETTING, AUTO_SAVELOG);
                Boolean Allow_D = getBoolean(SETTING, ALLOW_D);
                Boolean Allow_E = getBoolean(SETTING, ALLOW_E);
                Boolean Allow_I = getBoolean(SETTING, ALLOW_I);
                Boolean Allow_V = getBoolean(SETTING, ALLOW_V);
                Boolean Allow_W = getBoolean(SETTING, ALLOW_W);
                Boolean Allow_WTF = getBoolean(SETTING, ALLOW_WTF);
                Boolean Allow_DEBUG = getBoolean(SETTING, ALLOW_DEBUG);
                CrashHandler crashHandler = CrashHandler.getInstance();
                crashHandler.init(getApplicationContext());
                crashHandler.Auto_post = is_auto;
                LogUtil.isSaveLog = is_savelog;
                LogUtil.allowD = Allow_D;
                LogUtil.allowE = Allow_E;
                LogUtil.allowI = Allow_I;
                LogUtil.allowV = Allow_V;
                LogUtil.allowW = Allow_W;
                LogUtil.allowWtf = Allow_WTF;
                LogUtils.isDebug = Allow_DEBUG;

            }
        }else {
            Boolean is_auto = getBoolean(SETTING, AUTO_POST);
            Boolean is_savelog = getBoolean(SETTING, AUTO_SAVELOG);
            Boolean Allow_D = getBoolean(SETTING, ALLOW_D);
            Boolean Allow_E = getBoolean(SETTING, ALLOW_E);
            Boolean Allow_I = getBoolean(SETTING, ALLOW_I);
            Boolean Allow_V = getBoolean(SETTING, ALLOW_V);
            Boolean Allow_W = getBoolean(SETTING, ALLOW_W);
            Boolean Allow_WTF = getBoolean(SETTING, ALLOW_WTF);
            Boolean Allow_DEBUG = getBoolean(SETTING, ALLOW_DEBUG);
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
            crashHandler.Auto_post = is_auto;
            LogUtil.isSaveLog = is_savelog;
            LogUtil.allowD = Allow_D;
            LogUtil.allowE = Allow_E;
            LogUtil.allowI = Allow_I;
            LogUtil.allowV = Allow_V;
            LogUtil.allowW = Allow_W;
            LogUtil.allowWtf = Allow_WTF;
            LogUtils.isDebug = Allow_DEBUG;
        }
    }

}
