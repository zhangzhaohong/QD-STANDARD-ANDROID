package com.autumn.framework.X5WebView.mvp.mvpbase;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.X5WebView.tools.SystemBarTintManager;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by tzw on 2018/3/13.
 * 简单的AppCompatActivity封装
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private SparseArray<View> mViews;

    //  获取布局id(setContentView)
    public abstract int getLayoutId();
    //  初始化view
    public abstract void initViews();
    //  初始化点击事件
    public abstract void initListener();
    //  初始化数据
    public abstract void initData();
    //  处理具体的点击事件
    public abstract void processClick(View v);

    public void onClick(View v) {
        processClick(v);
    }
//   设置Activit的方向 true == 横屏  false == 竖屏
    public boolean setScreenOrientation(){
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }*/

        /**
         *第一种：始终隐藏，触摸屏幕时也不出现
         *解决办法：同时设置以下两个参数
         *View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
         *View.SYSTEM_UI_FLAG_IMMERSIVE
         *在需要隐藏虚拟键Navigation Bar的Activity的onCreate方法中
         *添加如下代码：
         * 隐藏pad底部虚拟键
         */
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);

        mViews = new SparseArray<>();

        if (setScreenOrientation()){
            //强制为竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            //强制为横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        setContentView( getLayoutId() );
        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);
        //ActivityUtils.getInstance().attach(this);

        initX5();

        initViews();
        initListener();
        initData();

    }

    /**
     * 初始化x5内核并加载
     */
    private void initX5() {

        QbSdk.setDownloadWithoutWifi(true);

        //QbSdk.isTbsCoreInited()用来判断x5内核是否已经加载了
        if (QbSdk.isTbsCoreInited()) {
            //如果已经加载
            //Log.d(TAG, "QbSdk.isTbsCoreInited: true 已经加载x5内核");
            LogUtil.d("X5_BaseActivity" +
                    "\n{" +
                    "\nStatus" +
                    "\n[" +
                    "\nMessage[QbSdk.isTbsCoreInited: true 已经加载x5内核]" +
                    "\n]" +
                    "\n}");
            //progressBar.setVisibility(View.INVISIBLE);
            //textview.setText("x5内核初始化成功");
            //show_success_message(true);
        } else {
            //还没加载,就要初始化内核并加载
            //Log.d(TAG, "QbSdk.isTbsCoreInited: false 还没加载x5内核");
            LogUtil.d("X5_BaseActivity" +
                    "\n{" +
                    "\nStatus" +
                    "\n[" +
                    "\nMessage[QbSdk.isTbsCoreInited: false 还没加载x5内核]" +
                    "\n]" +
                    "\n}");
            //初始化x5内核
            QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {

                @Override
                public void onCoreInitFinished() {

                }

                @Override
                public void onViewInitFinished(boolean b) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    //Log.d(TAG, "onViewInitFinished: x5内核初始化:" + b);
                    LogUtil.d("X5_BaseActivity" +
                            "\n{" +
                            "\nStatus" +
                            "\n[" +
                            "\nX5加载状态[" + b + "]" +
                            "\n]" +
                            "\n}");
                }
            });
        }
    }

    public <E extends View> E $(int viewId) {
        E view = (E) mViews.get(viewId);
        if (view == null) {
            view = (E) findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }


    public  <E extends View> void setOnClick(E view){
        view.setOnClickListener(this);
    }


    /** 子类可以重写改变状态栏颜色 */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /** 子类可以重写决定是否使用透明状态栏 */
    protected boolean translucentStatusBar() {
        return false;
    }


    /** 设置状态栏颜色 */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具类，有些4.4的手机有问题，这里为演示方便，不使用沉浸式
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(setStatusBarColor());
        }
    }

    /** 获取主题色 */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /** 获取深主题色 */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /** 初始化 Toolbar */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    /** 初始化 Toolbar  字体设置居中需要在toolbar里面包一层*/
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled) {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }


    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }


    @Override
    protected void onDestroy() {
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
        //ActivityUtils.getInstance().detach(this);
    }

}
