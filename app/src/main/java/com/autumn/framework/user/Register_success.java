package com.autumn.framework.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.autumn.framework.View.IosAlertDialog;
import com.autumn.framework.data.database.DBHelper;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.sdk.data.SpUtil;
import com.liyi.sutils.utils.AtyTransitionUtil;

public class Register_success extends AppCompatActivity {


    public static String username;
    public static String password;
    public static String private_name;
    public static String birthday;
    public static String register_email;

    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";
    private static final String IS_AUTO_LOGIN = "Is_auto_login";
    private static final String USER_KEY = "User_key";
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_register_success);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        TextView info_username = (TextView)findViewById(R.id.register_success_TextView1);
        TextView info_password = (TextView)findViewById(R.id.register_success_TextView2);
        TextView info_private_name = (TextView)findViewById(R.id.register_success_TextView3);
        TextView info_birthday = (TextView)findViewById(R.id.register_success_TextView4);
        TextView info_register_email = (TextView)findViewById(R.id.register_success_TextView5);

        Button register_complete = (Button)findViewById(R.id.btn_register_complete);

        info_username.setText(username);
        info_password.setText(password);
        info_private_name.setText(private_name);
        info_birthday.setText(birthday);
        info_register_email.setText(register_email);

        dbHelper = new DBHelper(this);
        dbHelper.insertOrUpdate(username, password, 1);
        SpUtil rw = new SpUtil();
        rw.writeValue(Register_success.this,USER_INFO,LOGIN_USERNAME,username);
        rw.writeValue(Register_success.this,USER_INFO,LOGIN_PASSWORD,password);

        register_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new IosAlertDialog(Register_success.this).builder().setCancelable(false).setTitle("注意").setMsg("是否退出此账号？")
                        .setNegativeButton("稍后", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                rw.writeBoolean(Register_success.this,USER_INFO,IS_AUTO_LOGIN,false);

                                // TODO Auto-generated method stub
                                Intent intent = new Intent(Register_success.this, LoginActivity.class);
                                startActivity(intent);
                                AtyTransitionUtil.exitToLeft(Register_success.this);
                            }
                        }).setPositiveButton("立即登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        rw.writeBoolean(Register_success.this,USER_INFO,IS_AUTO_LOGIN,true);

                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Register_success.this, LoginActivity.class);
                        startActivity(intent);
                        AtyTransitionUtil.exitToLeft(Register_success.this);
                    }
                }).show();
            }
        });
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
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            //LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            //linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            //int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            //params.height = statusHeight;
            //linear_bar.setLayoutParams(params);
        }
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

            Intent intent = new Intent(Register_success.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(Register_success.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}
