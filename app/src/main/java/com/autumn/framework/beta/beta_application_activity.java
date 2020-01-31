package com.autumn.framework.beta;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.data.SpUtil;
import com.autumn.framework.update.FindUpdateActivity;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.manager.content.content_manager;
import com.autumn.sdk.runtime.beta.beta_application_runtime;
import com.autumn.sdk.runtime.content.content_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Field;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class beta_application_activity extends BaseActivity {

    public static String beta_private_key;
    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";
    private static final String IS_AUTO_LOGIN = "Is_auto_login";
    private static final String USER_KEY = "User_key";
    private View mProgressView;
    private View mBetaApplicationView;
    private QMUITipDialog tipDialog;
    private int beta_status = 0;
    private String app_content_version;
    private String app_content;
    private TextView service_version;
    private TextView service_content;
    private ScrollView service_info;
    private LinearLayout user_operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_beta_application);

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

        //SUtils.initialize(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(beta_application_activity.this, R.color.colorPrimaryDark));
        }

        mBetaApplicationView = findViewById(R.id.beta_application);
        mProgressView = findViewById(R.id.beta_application_progress);

        tipDialog = new QMUITipDialog.Builder(beta_application_activity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        new Thread(new content_runtime(h2, beta_application_activity.this, getString(R.string.beta_content_key), getString(R.string.app_build))).start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity rw = new MainActivity();
                //String user_name = rw.getValue(USER_INFO,LOGIN_USERNAME);
                //String user_password = rw.getValue(USER_INFO,LOGIN_PASSWORD);
                //new Thread(new beta_application_runtime(h1, beta_application_activity.this,user_name,user_password,getString(R.string.app_key))).start();

                //showProgress(true);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        Button agree_rules = (Button)findViewById(R.id.agree_rules);
        final CheckBox agree_user_rules = (CheckBox)findViewById(R.id.agree_user_rules);
        TextView read_already = (TextView)findViewById(R.id.read_already);

        read_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agree_user_rules.isChecked()){
                    agree_user_rules.setChecked(false);
                }else {
                    agree_user_rules.setChecked(true);
                }
            }
        });

        agree_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agree_user_rules.isChecked()){

                    SpUtil rw = new SpUtil();
                    String user_name = rw.getValue(beta_application_activity.this,USER_INFO,LOGIN_USERNAME);
                    String user_password = rw.getValue(beta_application_activity.this,USER_INFO,LOGIN_PASSWORD);
                    new Thread(new beta_application_runtime(h1, beta_application_activity.this,user_name,user_password,getString(R.string.app_key))).start();

                    showProgress(true);

                }else{
                    FToastUtils.init().setRoundRadius(30).show("您尚未同意以上规定，请先阅读以上规定！");
                }
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                Intent intent = new Intent(beta_application_activity.this, MainActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(beta_application_activity.this);



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
                case beta_application_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.beta_application_success))){
                        LogUtil.i("beta_application_activity" +
                                "\n{" +
                                "\nStatus" +
                                "\n[" +
                                "\nTime[" + beta_status + "]" +
                                "\nMessage[" + String.valueOf(msg.obj) + "]" +
                                "\n]" +
                                "\nBetaStatus[true]" +
                                "\n}");

                        //String key = key_manager.key;

                        //beta_private_key = beta_manager.get_private_key();

                        showProgress(false);

                        new AlertDialog.Builder(beta_application_activity.this).setTitle("申请成功")//设置对话框标题

                                .setMessage("请前往检测更新检测更新")//设置显示的内容

                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮

                                    @Override

                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                        // TODO Auto-generated method stub
                                        //String key = key_manager.key;
                                        //ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        // 将文本内容放到系统剪贴板里。
                                        //cm.setText(beta_private_key);
                                        //FToastUtils.init().setRoundRadius(30).show("请前往自助授权进行激活！");
                                        dialog.dismiss();
                                        //showProgress(true);
                                        //beta_status_manager.act_beta = true;
                                        Intent intent = new Intent(beta_application_activity.this, FindUpdateActivity.class);
                                        startActivity(intent);
                                        AtyTransitionUtil.exitToRight(beta_application_activity.this);
                                    }


                                }).show();//在按键响应事件中显示此对话框

                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }else{
                        if (beta_status < 5 && beta_status >= 0){
                            beta_status += 1;
                            LogUtil.i("beta_application_activity" +
                                    "\n{" +
                                    "\nStatus" +
                                    "\n[" +
                                    "\nTime[" + beta_status + "]" +
                                    "\nMessage[" + String.valueOf(msg.obj) + "]" +
                                    "\n]" +
                                    "\nBetaStatus[retry]" +
                                    "\n}");
                            SpUtil rw = new SpUtil();
                            String user_name = rw.getValue(beta_application_activity.this,USER_INFO,LOGIN_USERNAME);
                            String user_password = rw.getValue(beta_application_activity.this,USER_INFO,LOGIN_PASSWORD);
                            new Thread(new beta_application_runtime(h1, beta_application_activity.this,user_name,user_password,getString(R.string.app_key))).start();
                        }else{
                            beta_status = 0;
                            LogUtil.i("beta_application_activity" +
                                    "\n{" +
                                    "\nStatus" +
                                    "\n[" +
                                    "\nTime[" + beta_status + "]" +
                                    "\nMessage[" + String.valueOf(msg.obj) + "]" +
                                    "\n]" +
                                    "\nBetaStatus[false]" +
                                    "\n}");
                            showProgress(false);
                            FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        }
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
                case content_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    tipDialog.dismiss();

                    if (String.valueOf(msg.obj).equals(getString(R.string.notice_new_version))){

                        app_content_version = content_manager.get_app_content_version();
                        app_content = content_manager.get_app_content();

                        user_operation = (LinearLayout)findViewById(R.id.user_operation);
                        user_operation.setVisibility(View.VISIBLE);
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
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (show) {
            tipDialog = new QMUITipDialog.Builder(beta_application_activity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在申请")
                    .create(false);
            tipDialog.show();
        }else {
            tipDialog.dismiss();
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mBetaApplicationView.setVisibility(show ? View.GONE : View.VISIBLE);
            mBetaApplicationView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBetaApplicationView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mBetaApplicationView.setVisibility(show ? View.GONE : View.VISIBLE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //四个参数的含义。1，group的id,2,item的id,3,是否排序，4，将要显示的内容
        menu.add(0,1,0,getString(R.string.title_activity_beta_helper));
        //menu.add(0,2,0,"菜单二");
        //menu.add(0,3,0,"菜单三");
        //menu.add(0,4,0,"菜单四");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                //Toast.makeText(FindUpdateActivity.this,"菜单一",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(beta_application_activity.this, beta_helper_activity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(beta_application_activity.this);
                break;
        }
        return true;
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
        //LogUtil.i("Stack弹出FindUpdateActivity");
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

            Intent intent = new Intent(beta_application_activity.this, MainActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(beta_application_activity.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}
