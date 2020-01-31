package com.autumn.framework.X5WebView.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.autumn.framework.X5WebView.mvp.mvpbase.BaseActivity;
import com.autumn.framework.X5WebView.tools.LogTAG;
import com.autumn.framework.X5WebView.tools.Logger;
import com.autumn.framework.X5WebView.widget.MyX5WebView;
import com.autumn.reptile.R;


public class X5WebGameActivity extends BaseActivity{

    private MyX5WebView myX5WebView;
    private String webUrl;
    private long exitTime = 0;
    private Bundle extras;
    public final String TAG = LogTAG.x5webview;
    public static final String DATA = "确定退出当前页面吗？";
    @Override
    public int getLayoutId() {
        extras = getIntent().getExtras();
        webUrl = extras.getString("key");
        Logger.i(TAG,"load url : "+ webUrl );
        full(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return R.layout.layout_x5;
    }

    @Override
    public void initViews() {
        myX5WebView = $(R.id.main_web);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //兼容视频
        try {
            if (myX5WebView.getX5WebViewExtension() != null) {
                Bundle data = new Bundle();
                data.putBoolean("standardFullScreen", false);
                //true表示标准全屏，false表示X5全屏；不设置默认false，
                data.putBoolean("supportLiteWnd", false);
                //false：关闭小窗；true：开启小窗；不设置默认true，
                data.putInt("DefaultVideoScreen", 2);
                //1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
                myX5WebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        myX5WebView.loadUrl(webUrl);
    }

    @Override
    public void processClick(View v) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            /*if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(this,DATA,Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }*/
            if (webUrl == null){
                finish();
            }else if (webUrl.equals("http://debugtbs.qq.com/")){
                finish();
            }else {
                if (myX5WebView.canGoBack()) {
                    myX5WebView.goBack();
                } else {
                    finish();
                }
            }
            /*if (myX5WebView.canGoBack()){
                myX5WebView.goBack();
            }else {
                finish();
            }*/
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void full(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

}
