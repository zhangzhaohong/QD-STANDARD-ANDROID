package com.autumn.framework.View;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DisplayCutoutView {

    private Activity mAc;

    public DisplayCutoutView(Activity ac) {
        mAc = ac;
    }


    //在使用LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES的时候，状态栏会显示为白色，这和主内容区域颜色冲突,
    //所以我们要开启沉浸式布局模式，即真正的全屏模式,以实现状态和主体内容背景一致
    public void openFullScreenModel(){
        mAc.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = mAc.getWindow().getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        mAc.getWindow().setAttributes(lp);
        View decorView = mAc.getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;
        mAc.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

}
