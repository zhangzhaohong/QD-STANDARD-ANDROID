package com.autumn.reptile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;

import com.autumn.framework.Log.CrashHandler;
import com.autumn.framework.Log.DumpUtil;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.Log.LogUtils;
import com.autumn.framework.about.phone_info;
import com.autumn.framework.data.AES256;
import com.autumn.framework.data.Data_editer;
import com.autumn.framework.data.key_manager_data;
import com.autumn.framework.guide.WelcomeGuideActivity;
import com.autumn.framework.permission.PermissionListener;
import com.autumn.framework.permission.PermissionUtil;
import com.autumn.framework.status.login_status_manager;
import com.autumn.framework.update.FindUpdateActivity;
import com.autumn.framework.update.Update_release;
import com.autumn.framework.user.LoginActivity;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.runtime.app.app_check_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.NetUtil;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.squareup.leakcanary.RefWatcher;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class Splash extends BaseActivity {

    private static final String DEVICE_INFO = "Device_info";
    private static final String DEVICE_IMEI = "Device_imei";
    private static final String DEVICE_IMSI = "Device_imsi";
    private static final String DEVICE_MAC = "Device_mac";
    private static final String DEVICE_STATUS = "Device_status";
    private static final int INFO_PHONE_NUMBER = 5;
    private static final String GUIDE_INFO = "Guide_info";
    private static final String IS_NOT_FIRST = "Is_not_first";
    private String DEVICE_SERIAL_NUMBER = "Device_serial_number";
    private String DEVICE_CPU = "Device_cpu";
    private String DEVICE_MANUFACTURER = "Device_manufacturer";
    private String DEVICE_PRODUCT = "Device_product";
    private static final int INFO_MANUFACTURER = 7;
    private static final int INFO_PRODUCT = 10;
    private static final int INFO_CPU = 9;
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
    private String DEVICE_ID_CODE = "Device_id_code";
    private static final String APP_NOTICE_MANAGER = "App_notice_manager";
    private static final String APP_NOTICE_MANAGER_BETA = "App_notice_manager_beta";
    private static final String APP_NOTICE_MANAGER_DEBUG = "App_notice_manager_debug";
    private static final String VERSION_CODE = "Version_code";
    private String ALLOW_SETTING = "Allow_setting";
    private ViewPager view_pager;
    private View view1;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;

    public void init() {
        try {
            Boolean is_auto = getBoolean(SETTING, AUTO_POST);
            Boolean is_savelog = getBoolean(SETTING, AUTO_SAVELOG);
            Boolean Allow_D = getBoolean(SETTING, ALLOW_D);
            Boolean Allow_E = getBoolean(SETTING, ALLOW_E);
            Boolean Allow_I = getBoolean(SETTING, ALLOW_I);
            Boolean Allow_V = getBoolean(SETTING, ALLOW_V);
            Boolean Allow_W = getBoolean(SETTING, ALLOW_W);
            Boolean Allow_WTF = getBoolean(SETTING, ALLOW_WTF);
            Boolean Allow_DEBUG = getBoolean(SETTING, ALLOW_DEBUG);
            Boolean Allow_Setting = getBoolean(SETTING, ALLOW_SETTING);
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
            if (!is_auto && Allow_Setting){
                crashHandler.Auto_post = is_auto;
            }else{
                writeBoolean(SETTING, AUTO_POST, true);
                crashHandler.Auto_post = true;
            }
            //crashHandler.Auto_post = is_auto;
            LogUtil.isSaveLog = is_savelog;
            LogUtil.allowD = Allow_D;
            LogUtil.allowE = Allow_E;
            LogUtil.allowI = Allow_I;
            LogUtil.allowV = Allow_V;
            LogUtil.allowW = Allow_W;
            LogUtil.allowWtf = Allow_WTF;
            LogUtils.isDebug = Allow_DEBUG;
        }catch (Exception w){
            w.printStackTrace();
        }
    }

    @SuppressLint("InlinedApi")
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onResume() {
        super.onResume();
        //LogUtil.d("Splash-开始加载启动图（resume）");
        //ImageView splash_background = (ImageView)findViewById(R.id.splash_image);
        //splash_background.setImageBitmap(BitmapUtil.getImage(BitmapUtil.ReadBitmapById(Splash.this, R.drawable.splash)));
        //Glide.with(this).load(R.drawable.splash).into(splash_background);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setBackgroundResource(R.drawable.splash);
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) { // 当前类不是该Task的根部，那么之前启动
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) { // 当前类是从桌面启动的
                    finish(); // finish掉该类，直接打开该Task中现存的Activity
                    return;
                }
            }
        }
        long startTime = System.currentTimeMillis();
        //内存泄漏
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        ActivtyStack.getScreenManager().pushActivity(this);
        try {
            setContentView(R.layout.activity_splash);
        }catch (Exception e){
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
            int currentVersion = android.os.Build.VERSION.SDK_INT;
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
        //Log.d("SplashActivity", "time:" + time);
        /*View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }*/
        initState();
        login_status_manager.init();
        // 每次加入stack
        //ActivtyStack.getScreenManager().pushActivity(this);
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        LogUtil.d("SplashActivity\n{\ninit_time[" + time + "]\n}");
        //LogUtil.i("添加Splash到Stack");

        /*view_pager = (ViewPager) findViewById(R.id.splash_ViewPager);
        aList = new ArrayList<View>();
        LayoutInflater view_container = getLayoutInflater();
        view1 = view_container.inflate(R.layout.splash_view, null,false);
        aList.add(view1);
        //new Register_view(view_container.inflate(R.layout.content_register, null));
        mAdapter = new MyPagerAdapter(aList);
        view_pager.setAdapter(mAdapter);*/

        Bugly.init(getApplicationContext(), getString(R.string.bugly_app_id), false);
        CrashReport.initCrashReport(getApplicationContext(), getString(R.string.bugly_app_id),false);
        LoginActivity.BUGLY_INIT_STATUS = true;
        LoginActivity.FROM_SPLASH = true;
        MainActivity.bottomNavigationBar_init_Status = false;

        //启动逻辑

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions
                    //request中申请多个权限
                    .request(PERMISSIONS_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            //根据Boolean来判断申请成功后和申请失败后的逻辑判断
                            if (aBoolean) {
                                //Toast.makeText(Splash.this, "申请成功", Toast.LENGTH_SHORT).show();
                                //SUtils.initialize(getApplication());
                                File_system_init();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //ToastUtil.setTextColor(ContextCompat.getColor(Splash.this, R.color.colorPrimaryDark));
                                }
                                if (!getBoolean(DEVICE_INFO, DEVICE_STATUS)) {
                                    if (ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    String serial_number = "unknown";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        serial_number = Build.getSerial();
                                    } else {
                                        serial_number = Build.SERIAL;
                                    }
                                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                    String IMEI = TelephonyMgr.getDeviceId();
                                    String IMSI = TelephonyMgr.getSubscriberId();// String
                                    phone_info info = new phone_info();
                                    writeValue(DEVICE_INFO, DEVICE_MANUFACTURER, info.get_info(INFO_MANUFACTURER));
                                    writeValue(DEVICE_INFO, DEVICE_PRODUCT, info.get_info(INFO_PRODUCT));
                                    writeValue(DEVICE_INFO, DEVICE_CPU, info.get_info(INFO_CPU));
                                    writeValue(DEVICE_INFO, DEVICE_SERIAL_NUMBER, serial_number);
                                    writeValue(DEVICE_INFO, DEVICE_IMEI, IMEI);
                                    writeValue(DEVICE_INFO, DEVICE_IMSI, IMSI);
                                    writeValue(DEVICE_INFO, DEVICE_ID_CODE, getUniquePsuedoID());
                                    BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
                                    m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                    if (m_BluetoothAdapter == null) {
                                        //adapter null method
                                    } else {
                                        @SuppressLint("HardwareIds") String MAC = m_BluetoothAdapter.getAddress();
                                        if (MAC == null) {
                                            //MAC NULL METHOD
                                        } else {
                                            if (MAC.equals("02:00:00:00:00:00")) {
                                                MAC = getMacAddr();
                                            }
                                        }
                                        writeValue(DEVICE_INFO, DEVICE_MAC, MAC);
                                    }
                                    writeBoolean(DEVICE_INFO, DEVICE_STATUS, true);
                                    writeValue(APP_NOTICE_MANAGER, VERSION_CODE, "0");
                                    writeValue(APP_NOTICE_MANAGER_BETA, VERSION_CODE, "0");
                                    writeValue(APP_NOTICE_MANAGER_DEBUG, VERSION_CODE, "0");
                                }
                                String Device_seirial_number = getValue(DEVICE_INFO, DEVICE_SERIAL_NUMBER);
                                if (Device_seirial_number != null){
                                    if (Device_seirial_number.equals("unknown")){
                                        String serial_number = "unknown";
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            serial_number = Build.getSerial();
                                        } else {
                                            serial_number = Build.SERIAL;
                                        }
                                        writeValue(DEVICE_INFO, DEVICE_SERIAL_NUMBER, serial_number);
                                    }
                                }
                                if (NetUtil.isConnected()) {
                                    String applicationName = getApplicationName();
                                    //System.out.println("App_info"+applicationName);
                                    //System.out.println("App_info"+getPackageName());
                                    new Thread(new app_check_runtime(h1, Splash.this, applicationName, getPackageName(), getString(R.string.app_key))).start();
                                } else {
                                    new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                            .setCancelable(false)
                                            .setMessage("网络未连接，请连接网络后重试！")//设置显示的内容
                                            .setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub
                                                    finish();
                                                }
                                            }).show();//在按键响应事件中显示此对话框
                                }
                            } else {
                                //Toast.makeText(Splash.this, "申请成功", Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                        .setCancelable(false)
                                        .setMessage("权限异常，请重新获取，或手动打开\n如果未正常授予权限，会导致本程序无法正常打开")//设置显示的内容
                                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                finish();
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                            }
                        }
                    });

        }else {
            PermissionUtil permissionUtil = new PermissionUtil(this);
            permissionUtil.requestPermissions(PERMISSIONS_STORAGE,
                    new PermissionListener() {
                        @Override
                        public void onGranted() {

                            //Toast.makeText(Splash.this, "申请成功", Toast.LENGTH_SHORT).show();
                            //SUtils.initialize(getApplication());
                            File_system_init();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                //ToastUtil.setTextColor(ContextCompat.getColor(Splash.this, R.color.colorPrimaryDark));
                            }
                            if (!getBoolean(DEVICE_INFO, DEVICE_STATUS)) {
                                if (ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                String serial_number = "unknown";
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    serial_number = Build.getSerial();
                                } else {
                                    serial_number = Build.SERIAL;
                                }
                                TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                String IMEI = TelephonyMgr.getDeviceId();
                                String IMSI = TelephonyMgr.getSubscriberId();// String
                                phone_info info = new phone_info();
                                writeValue(DEVICE_INFO, DEVICE_MANUFACTURER, info.get_info(INFO_MANUFACTURER));
                                writeValue(DEVICE_INFO, DEVICE_PRODUCT, info.get_info(INFO_PRODUCT));
                                writeValue(DEVICE_INFO, DEVICE_CPU, info.get_info(INFO_CPU));
                                writeValue(DEVICE_INFO, DEVICE_SERIAL_NUMBER, serial_number);
                                writeValue(DEVICE_INFO, DEVICE_IMEI, IMEI);
                                writeValue(DEVICE_INFO, DEVICE_IMSI, IMSI);
                                writeValue(DEVICE_INFO, DEVICE_ID_CODE, getUniquePsuedoID());
                                BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
                                m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                if (m_BluetoothAdapter == null) {
                                    //adapter null method
                                } else {
                                    @SuppressLint("HardwareIds") String MAC = m_BluetoothAdapter.getAddress();
                                    if (MAC == null) {
                                        //MAC NULL METHOD
                                    } else {
                                        if (MAC.equals("02:00:00:00:00:00")) {
                                            MAC = getMacAddr();
                                        }
                                    }
                                    writeValue(DEVICE_INFO, DEVICE_MAC, MAC);
                                }
                                writeBoolean(DEVICE_INFO, DEVICE_STATUS, true);
                                writeValue(APP_NOTICE_MANAGER, VERSION_CODE, "0");
                                writeValue(APP_NOTICE_MANAGER_BETA, VERSION_CODE, "0");
                                writeValue(APP_NOTICE_MANAGER_DEBUG, VERSION_CODE, "0");
                            }
                            String Device_seirial_number = getValue(DEVICE_INFO, DEVICE_SERIAL_NUMBER);
                            if (Device_seirial_number != null){
                                if (Device_seirial_number.equals("unknown")){
                                    String serial_number = "unknown";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        serial_number = Build.getSerial();
                                    } else {
                                        serial_number = Build.SERIAL;
                                    }
                                    writeValue(DEVICE_INFO, DEVICE_SERIAL_NUMBER, serial_number);
                                }
                            }
                            if (NetUtil.isConnected()) {
                                String applicationName = getApplicationName();
                                //System.out.println("App_info"+applicationName);
                                //System.out.println("App_info"+getPackageName());
                                new Thread(new app_check_runtime(h1, Splash.this, applicationName, getPackageName(), getString(R.string.app_key))).start();
                            } else {
                                new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                        .setCancelable(false)
                                        .setMessage("网络未连接，请连接网络后重试！")//设置显示的内容
                                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                finish();
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                            }

                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {


                        }

                        @Override
                        public void onShouldShowRationale(List<String> deniedPermission) {
                            /*new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                    .setCancelable(false)
                                    .setMessage("权限异常，请重新获取，或手动打开\n如果未正常授予权限，会导致本程序无法正常打开")//设置显示的内容
                                    .setPositiveButton("退出",new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            finish();
                                        }
                                    }).show();*/
                            //在按键响应事件中显示此对话框
                            new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                    .setCancelable(false)
                                    .setMessage("权限异常，请重新获取，或手动打开\n如果未正常授予权限，会导致本程序无法正常打开")//设置显示的内容
                                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            finish();
                                        }
                                    }).show();//在按键响应事件中显示此对话框
                        }
                    });
        }

        //创建PermissionUtil对象，参数为继承自V4包的 FragmentActivity
        /*PermissionUtil permissionUtil = new PermissionUtil(Splash.this);
        permissionUtil.requestPermissions(PERMISSIONS_STORAGE,
                new PermissionListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {


                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                .setCancelable(false)
                                .setMessage("权限异常，请重新获取，或手动打开\n如果未正常授予权限，会导致本程序无法正常打开")//设置显示的内容
                                .setPositiveButton("退出",new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        // TODO Auto-generated method stub
                                        finish();
                                    }
                                }).show();//在按键响应事件中显示此对话框
                    }
                });*/
    }

    Handler h1=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case app_check_runtime.FINDER_IMAGE_1:
                    if (String.valueOf(msg.obj).equals(getString(R.string.app_check_success))) {

                        initApp();

                    }else{
                        new AlertDialog.Builder(Splash.this).setTitle("错误")//设置对话框标题
                                .setCancelable(false)
                                .setMessage(String.valueOf(msg.obj))//设置显示的内容
                                .setPositiveButton("立即下载最新版本",new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        // TODO Auto-generated method stub
                                        Intent intent = new Intent(Splash.this, Update_release.class);
                                        startActivity(intent);
                                        AtyTransitionUtil.exitToRight(Splash.this);
                                    }
                                }).show();//在按键响应事件中显示此对话框
                    }
                    //h1.removeCallbacksAndMessages(null);
                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }
    };

    private void initApp() {

        if (getBoolean(GUIDE_INFO,IS_NOT_FIRST)){
            Intent intent = new Intent(Splash.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(Splash.this);
        }else{
            Intent intent = new Intent(Splash.this, WelcomeGuideActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(Splash.this);
        }

    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> viewLists;

        public MyPagerAdapter() {
        }

        public MyPagerAdapter(ArrayList<View> viewLists) {
            super();
            this.viewLists = viewLists;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewLists.get(position));
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     */
    private void initState() {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean lightStatusBar = false;
                StatusBarCompat.setStatusBarColor(this, getColor(R.color.purple_splash), lightStatusBar);
            }
            QMUIStatusBarHelper.setStatusBarLightMode(Splash.this);
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
        }
    }


    //写入数据
    public void writeValue(String file,String name,String value){

        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            value = AES256.aesEncrypt(value, key);
            SharedPreferences activityPreferences = Splash.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = activityPreferences.edit();
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
            SharedPreferences activityPreferences = Splash.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = activityPreferences.edit();
            editor.putBoolean(name, value);
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
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = Splash.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            String Value = AES256.aesDecrypt(pref.getString(name,""), key);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();

            LogUtil.e(e.toString());
        }
        return null;
    }
    public boolean getBoolean(String file,String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = Splash.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            Boolean Value = pref.getBoolean(name,false);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void File_system_init()
    {
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs");
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash");
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump");
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache");

        CrashHandler.PATH = Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash/";
        LogUtil.LOG_PATH = Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) +  getString(R.string.logs_path) ;
        DumpUtil.LOG_PATH = Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump/";

        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        Data_editer.deleteDirectory(path);

        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download");

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出Splash");
        //h1.removeCallbacksAndMessages(null);
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

}
