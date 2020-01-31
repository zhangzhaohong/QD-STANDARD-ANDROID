package com.autumn.reptile;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;
import com.autumn.framework.Log.CrashHandler;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.X5WebView.tools.X5NetService;
import com.liyi.sutils.utils.SUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tracup.library.Tracup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import cn.hotapk.fastandrutils.utils.FApplication;

import static android.support.constraint.Constraints.TAG;

public class MyApplication extends FApplication {

    private static Context mContext;
    private static MyApplication app_instance;
    private static Application instance;
    private Tracup tracup;
    private boolean mRunning = false;
    private Handler mStartTraceupHandler;
    private boolean mStartTraceupRunning = false;
    private boolean mStartBasicRunning = false;
    private Handler mStartBasicHandler;
    private boolean mStartX5Running = false;
    private Handler mStartX5Handler;
    private RefWatcher refWatcher;
    private Long token;

    @Override
    public void onCreate(){
        super.onCreate();

        SUtils.initialize(this);

        //CPU_MEM_manager.getInstance().init(getApplicationContext(), 100L);
        //CPU_MEM_manager.getInstance().start();

        if (!LeakCanary.isInAnalyzerProcess(this))
            refWatcher = LeakCanary.install(this);

        Bugly.init(this, getString(R.string.bugly_app_id), false);

        // 在调用TBS初始化、创建WebView之前进行如下配置，以开启优化方案(仅Android 5.1+生效)
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        QbSdk.canLoadX5FirstTimeThirdApp(MyApplication.this);
        preInitX5Core();
        QbSdk.setDownloadWithoutWifi(true);

        HandlerThread thread_basic = new HandlerThread("MyHandlerThread_basic");
        thread_basic.start();//创建一个HandlerThread并启动它
        mStartBasicHandler = new Handler(thread_basic.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        mStartBasicHandler.post(mStartBasicRunnable);//将线程post到Handler中
        mStartBasicRunning = true;

        HandlerThread thread_traceup = new HandlerThread("MyHandlerThread_traceup");
        thread_traceup.start();//创建一个HandlerThread并启动它
        mStartTraceupHandler = new Handler(thread_traceup.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        mStartTraceupHandler.post(mStartTraceupRunnable);//将线程post到Handler中
        mStartTraceupRunning = true;

        /*HandlerThread thread_x5 = new HandlerThread("MyHandlerThread_x5");
        thread_x5.start();//创建一个HandlerThread并启动它
        mStartX5Handler = new Handler(thread_x5.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        mStartX5Handler.post(mStartX5Runnable);//将线程post到Handler中
        mStartX5Running = true;*/

        long start = System.currentTimeMillis();

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        CrashReport.initCrashReport(this, getString(R.string.bugly_app_id), false);
        long end = System.currentTimeMillis();
        long init_time = end - start;
        LogUtil.i("init time--->" + init_time + "ms");

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());


        //上下文
        mContext = this;
        instance = this;

    }

    private void preInitX5Core() {
        //预加载x5内核
        Intent intent = new Intent(this, X5NetService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        //startService(intent);
    }

    //实现耗时操作的线程
    Runnable mStartBasicRunnable = new Runnable() {

        @Override
        public void run() {
            //----------模拟耗时的操作，开始---------------
            while(mStartBasicRunning){
                Log.i(TAG, "thread-basic");
                //refWatcher = LeakCanary.install(MyApplication.this);
                initDownloader();
                //MobSDK.init(MyApplication.this);
                /*XGPushManager.registerPush(MyApplication.this, new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        //token在设备卸载重装的时候有可能会变
                        Log.d("TPush", "注册成功，设备token为：" + data);
                    }
                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                    }
                });*/
                mStartBasicRunning = false;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //----------模拟耗时的操作，结束---------------
        }
    };

    //实现耗时操作的线程
    Runnable mStartTraceupRunnable = new Runnable() {

        @Override
        public void run() {
            //----------模拟耗时的操作，开始---------------
            while(mStartTraceupRunning){
                Log.i(TAG, "thread-traceup");
                tracup = new Tracup.Builder(MyApplication.this, "86e08fa0ff47482a42298728b330c460", "a4594ce30e3059e61796585d817efded")
                        .setShouldPlaySounds(false) // 摇一摇截图音乐开关设置 （默认是 false）
                        .setEmailFiledRequied(true) // 反馈页面邮箱字段知否必须填写设置 （默认是 false）
                        .setEmailFiledVisibility(false) // 反馈页面邮箱UI是否显示，如果邮箱设置为必须填写时，此设置无线。(默认是 true)
                        .setShakingThreshold(2000) // 摇一摇灵敏度设置，数值越小，越灵敏，必须大于 0。（默认是350）
                        .setIntroMessageenabled(false) // 提示反馈信息是否自动提示设置。默认应用启动一分钟以后显示提示(默认是 true)
                        .build();
                mStartTraceupRunning = false;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //----------模拟耗时的操作，结束---------------
        }
    };


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(this);

        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();

    }


    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(16);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    //获取单例模式中唯一的MyApplication实例   
    public static MyApplication getInstance() {
        //if (app_instance == null)app_instance = new MyApplication();
        if (app_instance != null){
            return app_instance;
        }else {
            app_instance = new MyApplication();
            return app_instance;
        }
        //return app_instance;
    }

    public static  Context getAppContext(){
        if (mContext == null){
            new MyApplication();
        }
        return mContext;
    }

    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }

    public String getToken() {
        return String.valueOf(token);
    }

    public void setToken(Long token) {
        this.token = token;
    }

}
