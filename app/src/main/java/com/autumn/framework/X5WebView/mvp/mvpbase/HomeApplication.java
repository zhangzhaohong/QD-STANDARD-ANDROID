package com.autumn.framework.X5WebView.mvp.mvpbase;
import android.app.Application;
import android.content.Intent;
import com.autumn.framework.X5WebView.tools.X5NetService;

public class HomeApplication extends Application {

    private static HomeApplication homeApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        homeApplication=this;
        preInitX5Core();
    }

    public static HomeApplication getInstance(){
        return homeApplication;
    }

    private void preInitX5Core() {
        //预加载x5内核
        Intent intent = new Intent(this, X5NetService.class);
        startService(intent);
    }
}
