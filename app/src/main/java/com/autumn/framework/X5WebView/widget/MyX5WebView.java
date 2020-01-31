package com.autumn.framework.X5WebView.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.X5WebView.Filter.ADFilterTool;
import com.autumn.framework.X5WebView.tools.LogTAG;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

public class MyX5WebView extends WebView {

    private static final String TAG = LogTAG.x5webview;
    private static final int MAX_LENGTH = 8;

    ProgressBar progressBar;
    private TextView tvTitle;
    private ImageView imageView;
    private List<String> newList;


    public MyX5WebView(Context context) {
        super(context);
        initUI();
    }

    public MyX5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MyX5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    public void setShowProgress(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }


    private void initUI() {

        try {
            getX5WebViewExtension().setScrollBarFadingEnabled(false);
        }catch (Exception e){

        }
        setHorizontalScrollBarEnabled(false);//水平不显示小方块
        setVerticalScrollBarEnabled(false); //垂直不显示小方块

//      setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
//      setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示


        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        //progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.color_progressbar));

        addView(progressBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//      加载图 根据自己的需求去集成使用
        imageView.setImageResource(R.mipmap.splash_x5);
        imageView.setVisibility(VISIBLE);
        addView(imageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initWebViewSettings();
    }

    //   基本的WebViewSetting
    public void initWebViewSettings() {
        setBackgroundColor(getResources().getColor(android.R.color.white));
        setWebViewClient(client);
        setWebChromeClient(chromeClient);
        setDownloadListener(downloadListener);
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        //设置自适应屏幕，两者合用
        webSetting.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSetting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //android 默认是可以打开_bank的，是因为它默认设置了WebSettings.setSupportMultipleWindows(false)
        //在false状态下，_bank也会在当前页面打开……
        //而x5浏览器，默认开启了WebSettings.setSupportMultipleWindows(true)，
        // 所以打不开……主动设置成false就可以打开了
        //需要支持多窗体还需要重写WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false);
//        webSetting.setCacheMode(WebSettings.LOAD_NORMAL);
//        getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension

        //启用数据库
        webSetting.setDatabaseEnabled(true);
        String dir =MyApplication.getAppContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSetting.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSetting.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSetting.setDomStorageEnabled(true);

        //兼容视频
        try {
            if (getX5WebViewExtension() != null) {
                Bundle data = new Bundle();
                data.putBoolean("standardFullScreen", false);
                //true表示标准全屏，false表示X5全屏；不设置默认false，
                data.putBoolean("supportLiteWnd", false);
                //false：关闭小窗；true：开启小窗；不设置默认true，
                data.putInt("DefaultVideoScreen", 1);
                //1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
                getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*@Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
        canvas.save();
        Paint paint = new Paint();
        paint.setColor(0x7fff0000);
        paint.setTextSize(24.f);
        paint.setAntiAlias(true);
        if (getX5WebViewExtension() != null) {
            canvas.drawText(this.getContext().getPackageName() + "-pid:"
                    + android.os.Process.myPid(), 10, 50, paint);
            canvas.drawText(
                    "X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
                    100, paint);
        } else {
            canvas.drawText(this.getContext().getPackageName() + "-pid:"
                    + android.os.Process.myPid(), 10, 50, paint);
            canvas.drawText("Sys Core", 10, 100, paint);
        }
        canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
        canvas.drawText(Build.MODEL, 10, 200, paint);
        canvas.restore();
        return ret;
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
            this.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (tvTitle == null || TextUtils.isEmpty(title)) {
                return;
            }
            if (title != null && title.length() > MAX_LENGTH) {
                tvTitle.setText(title.subSequence(0, MAX_LENGTH) + "...");
            } else {
                tvTitle.setText(title);
            }
        }

        //监听进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (progressBar != null && newProgress != 100) {

                //Webview加载没有完成 就显示我们自定义的加载图
                progressBar.setVisibility(VISIBLE);

            } else if (progressBar != null) {

                //Webview加载完成 就隐藏进度条,显示Webview
                progressBar.setVisibility(GONE);
                imageView.setVisibility(GONE);
            }
        }

        /**
         * 当页面中触发了alert(),confirm(),prompt()这三个方法，就会对应回调到下面三个方法中
         * 相当于js调用了安卓的方法，这也算是一种安卓和js的通信,
         * 复写了下面这3方法并且返回true就不会显示页面的弹窗了，而且一定要有确认或者取消的操作，不然会卡死
         * 但能拿到弹出里的所有信息，可以自己写原生弹窗等,如alert,toast等,很灵活
         *
         */

        @Override
        public boolean onJsPrompt(WebView webView, String url, String message, String defalutValue, JsPromptResult result) {
            Log.d(TAG, "onJsPrompt: message:" + message);
            Log.d(TAG, "onJsPrompt: defalutValue:" + message);
            //这里prompt的确认是要传参数的，这个参数会传到js中去，所以这个方法能实现安卓和js的相互通信
            result.confirm("啦啦啦啦，我是安卓原生来的内容");
            return true;
        }

        //配置权限（同样在WebChromeClient中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
            geolocationPermissionsCallback.invoke(s, true, false);
            super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
        }


    };

    private WebViewClient client = new WebViewClient() {

        //当页面加载完成的时候
        @Override
        public void onPageFinished(WebView webView, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            String endCookie = cookieManager.getCookie(url);
            Log.i(TAG, "onPageFinished: endCookie : " + endCookie);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();//同步cookie
            } else {
                CookieManager.getInstance().flush();
            }
            super.onPageFinished(webView, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候控制去WebView打开，
            // 为false调用系统浏览器或第三方浏览器
            if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                return false;
            } else if (url.startsWith("qqmap:")){
                //do nothing
                return true;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    LogUtil.d("异常URL---" + url);
                    Toast.makeText(view.getContext(), "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            //做广告拦截，ADFIlterTool 为广告拦截工具类
            if (!ADFilterTool.hasAd(view.getContext(), url)){
                return super.shouldInterceptRequest(view, url);
            }else {
                return new WebResourceResponse(null,null,null);
            }
            //return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onLoadResource(WebView webView, String s) {
            super.onLoadResource(webView, s);
            String reUrl = webView.getUrl() + "";
//            Log.i(TAG, "onLoadResource: onLoadResource : " + reUrl);
            List<String> urlList = new ArrayList<>();
            urlList.add(reUrl);
            newList = new ArrayList();
            for (String cd : urlList) {
                if (!newList.contains(cd)) {
                    newList.add(cd);
                }
            }
        }


    };

    public void syncCookie(String url, String cookie) {
        CookieSyncManager.createInstance(getContext());
        if (!TextUtils.isEmpty(url)) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();

            //这里的拼接方式是伪代码
            String[] split = cookie.split(";");
            for (String string : split) {
                //为url设置cookie
                // ajax方式下  cookie后面的分号会丢失
                cookieManager.setCookie(url, string);
            }
            String newCookie = cookieManager.getCookie(url);
            Log.i(TAG, "syncCookie: newCookie == " + newCookie);
            //sdk21之后CookieSyncManager被抛弃了，换成了CookieManager来进行管理。
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();//同步cookie
            } else {
                CookieManager.getInstance().flush();
            }
        } else {

        }

    }

    //删除Cookie
    private void removeCookie() {

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

    }

    public String getDoMain(String url) {
        String domain = "";
        int start = url.indexOf(".");
        if (start >= 0) {
            int end = url.indexOf("/", start);
            if (end < 0) {
                domain = url.substring(start);
            } else {
                domain = url.substring(start, end);
            }
        }
        return domain;
    }

    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    };
}
