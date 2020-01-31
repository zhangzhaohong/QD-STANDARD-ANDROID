package com.autumn.framework.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.manager.content.content_manager;
import com.autumn.sdk.runtime.content.content_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Field;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class service_detail extends BaseActivity {

    public static boolean FROM_LOGIN_ACTIVITY = true;
    private String app_content_version;
    private String app_content;
    private QMUITipDialog tipDialog;
    private TextView service_version;
    private TextView service_content;
    private ScrollView service_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_service_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        tipDialog = new QMUITipDialog.Builder(service_detail.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create(false);
        tipDialog.show();

        new Thread(new content_runtime(h1, service_detail.this, getString(R.string.service_content_key), getString(R.string.app_build))).start();

        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                if (FROM_LOGIN_ACTIVITY) {
                    Intent intent = new Intent(service_detail.this, LoginActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(service_detail.this);
                }else {
                    FROM_LOGIN_ACTIVITY = true;
                    Intent intent = new Intent(service_detail.this, RegisterActivity.class);
                    startActivity(intent);
                    AtyTransitionUtil.exitToRight(service_detail.this);
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
                case content_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    tipDialog.dismiss();

                    if (String.valueOf(msg.obj).equals(getString(R.string.notice_new_version))){

                        app_content_version = content_manager.get_app_content_version();
                        app_content = content_manager.get_app_content();

                        service_info = (ScrollView)findViewById(R.id.service_info);
                        service_info.setVisibility(View.VISIBLE);
                        service_version = (TextView)findViewById(R.id.service_version);
                        service_content = (TextView)findViewById(R.id.service_content);

                        service_version.setText(app_content_version);

                        if (app_content == null || app_content.equals("")){
                            FToastUtils.init().setRoundRadius(30).show("数据配置错误，请联系管理员" +
                                    "\n邮箱：544901005@qq.com");
                        }else{
                            service_content.setText(app_content.replaceAll("\\[换行]", "\n"));
                        }

                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.notice_new_version_not_exist))){
                        FToastUtils.init().setRoundRadius(30).show("加载服务条款失败，请检查APP是否已经过期！");
                    }else{
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };


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
        //LogUtil.i("Stack弹出AppUpdateActivity");
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (FROM_LOGIN_ACTIVITY) {
                Intent intent = new Intent(service_detail.this, LoginActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(service_detail.this);
            }else {
                FROM_LOGIN_ACTIVITY = true;
                Intent intent = new Intent(service_detail.this, RegisterActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(service_detail.this);
            }
            
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}
