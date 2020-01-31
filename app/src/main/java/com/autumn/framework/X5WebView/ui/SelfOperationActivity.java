package com.autumn.framework.X5WebView.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.X5WebView.widget.MyX5WebView;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;

public class SelfOperationActivity extends BaseActivity {
    //private String url="http://www.suomusic.com/dryCargoInfo.htm?mt=show&id=2";  //测试网址选择1
    //private String url="https://v.qq.com/index.html";//测试网址选择2腾讯视频
    private String url="https://data.meternity.cn";//测试网址选择2腾讯视频
    private MyX5WebView main_web;
    private LinearLayout mProgressView;
    private LinearLayout mWebFormView;
    private QMUITipDialog tipFailDialog;
    private QMUITipDialog tipDialog;
    private QMUITipDialog tipSuccessDialog;
    //private String url="http://zh.84000.com.cn/cas/bus.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_webview_self_operation);

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

        //main_web = (MyX5WebView)findViewById(R.id.main_web);
        mProgressView = (LinearLayout)findViewById(R.id.progress_view);
        mWebFormView = (LinearLayout)findViewById(R.id.web_view);

        showProgress(true);
        initX5();


        //APIWebviewTBS.getAPIWebview().initTBSActivity(this);   //api借口注册二次封装
        //loadWebvieUrl(url);
        //mProgressView.setVisibility(View.GONE);
        //mWebFormView.setVisibility(View.VISIBLE);
        //main_web.setVisibility(View.VISIBLE);
        //main_web.loadUrl("https://www.baidu.com");
        //full(true);
        //new MyX5WebView(this).loadUrl(url);

        ImageView imageButton1 = (ImageView)findViewById(R.id.imageButton1);
        ImageView imageButton2 = (ImageView)findViewById(R.id.imageButton2);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelfOperationActivity.this, MainActivity.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(SelfOperationActivity.this);

                //webView.destroy();

                //finish();

            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_web.reload();
            }
        });

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mWebFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mWebFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mWebFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            if (show) {
                tipDialog = new QMUITipDialog.Builder(SelfOperationActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在加载内核")
                        .create();
                tipDialog.show();
                //initX5();
            }else {
                tipDialog.dismiss();
            }

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show) {
                tipDialog = new QMUITipDialog.Builder(SelfOperationActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在加载内核")
                        .create();
                tipDialog.show();
                //initX5();
            }else {
                tipDialog.dismiss();
            }
            mWebFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void show_success_message(final Boolean show){
        if (show) {
            showProgress(false);
            tipSuccessDialog = new QMUITipDialog.Builder(SelfOperationActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord("加载成功")
                    .create();
            tipSuccessDialog.show();
            init_view();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tipSuccessDialog.dismiss();
        }else {
            tipSuccessDialog.dismiss();
        }
    }

    private void show_fail_message(final Boolean show){
        if (show) {
            showProgress(false);
            tipFailDialog = new QMUITipDialog.Builder(SelfOperationActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("加载失败")
                    .create();
            tipFailDialog.show();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tipFailDialog.dismiss();
        }else {
            tipFailDialog.dismiss();
        }
    }

    private void init_view(){
        main_web = new MyX5WebView(this);
        //main_web.initWebViewSettings();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebFormView.addView(main_web, layoutParams);
        //mWebFormView.addView(main_web);
        //main_web.loadUrl("https://www.baidu.com");
        WebSettings webSettings = main_web.getSettings();
        //设置支持javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //main_web.loadUrl("http://debugtbs.qq.com/");
        //main_web.loadUrl("http://soft.imtt.qq.com/browser/tes/feedback.html");
        //main_web.loadUrl("https://www.baidu.com");
        main_web.loadUrl(url);
    }

    /**
     * 初始化x5内核并加载
     */
    private void initX5() {

        //非wifi网络下是否允许下载内核，默认是false
        QbSdk.setDownloadWithoutWifi(true);

        //QbSdk.isTbsCoreInited()用来判断x5内核是否已经加载了
        if (QbSdk.isTbsCoreInited()) {
            //如果已经加载
            //Log.d(TAG, "QbSdk.isTbsCoreInited: true 已经加载x5内核");
            LogUtil.d("SelfOperationActivity" +
                    "\n{" +
                    "\nStatus" +
                    "\n[" +
                    "\nMessage[QbSdk.isTbsCoreInited: true 已经加载x5内核]" +
                    "\n]" +
                    "\n}");
            //progressBar.setVisibility(View.INVISIBLE);
            //textview.setText("x5内核初始化成功");
            show_success_message(true);
        } else {
            //还没加载,就要初始化内核并加载
            //Log.d(TAG, "QbSdk.isTbsCoreInited: false 还没加载x5内核");
            LogUtil.d("SelfOperationActivity" +
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
                    LogUtil.d("SelfOperationActivity" +
                            "\n{" +
                            "\nStatus" +
                            "\n[" +
                            "\nX5加载状态[" + b + "]" +
                            "\n]" +
                            "\n}");
                    if (b == true) {
                        show_success_message(true);
                    } else {
                        //X5内核初始化失败
                        show_fail_message(true);
                    }
                }
            });
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

//    private void initdata() {//获取手机版本信息
//        int tbsVersion = QbSdk.getTbsVersion(this);
//        String TID = QbSdk.getTID();
//        String qBVersion = QbSdk.getMiniQBVersion(this);
//        tvStatus.setText("TbsVersion:" + tbsVersion + "\nTID:" + TID + "\nMiniQBVersion:" + qBVersion);
//    }

    @Override
    protected void onResume() {
        //激活WebView为活跃状态，能正常执行网页的响应
        if (main_web != null){
            main_web.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        if (main_web != null)
            main_web.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (main_web != null) {
            //先让webview加载null内容
            main_web.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            main_web.clearHistory();

            //父布局移除webview
            ((ViewGroup) main_web.getParent()).removeView(main_web);
            //最后webview在销毁
            main_web.destroy();
            main_web = null;
        }
        // 退出时弹出stack
        //LogUtil.i("Stack弹出AppUpdateActivity");
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //判断是否可以后退网页，但是不是后退网页的动作，后退网页的动作是mWebview.goBack()
        //这个方法的返回值是boolean，当不是第一个页面的时候，
        // 就是又点进其他页面，调用的话就返回true,如果就是在第一个页面就返回false
        if (main_web.canGoBack()) {
            main_web.goBack();  //后退网页
        } else {
            Intent intent = new Intent(SelfOperationActivity.this, MainActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(SelfOperationActivity.this);
            super.onBackPressed();
        }
    }

    /*@Override
    protected void onStop() {
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }*/

}
