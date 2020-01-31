package com.autumn.framework.user;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.autumn.framework.Log.CrashHandler;
import com.autumn.framework.Log.DumpUtil;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.View.ClearEditText;
import com.autumn.framework.View.IosAlertDialog;
import com.autumn.framework.View.PasswordEditText;
import com.autumn.framework.data.AES256;
import com.autumn.framework.data.App_info;
import com.autumn.framework.data.Data_editer;
import com.autumn.framework.data.database.DBHelper;
import com.autumn.framework.data.key_manager_data;
import com.autumn.framework.status.login_status_manager;
import com.autumn.framework.update.AppUpdateActivity;
import com.autumn.framework.update.FindUpdateActivity;
import com.autumn.framework.update.Update_release;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.manager.content.content_manager;
import com.autumn.sdk.manager.update.update_manager;
import com.autumn.sdk.manager.user.login_manager;
import com.autumn.sdk.manager.user.sign_manager;
import com.autumn.sdk.runtime.content.content_runtime;
import com.autumn.sdk.runtime.update.beta_update_login_runtime;
import com.autumn.sdk.runtime.update.check_update_version_login_runtime;
import com.autumn.sdk.runtime.update.debug_update_login_runtime;
import com.autumn.sdk.runtime.update.update_runtime;
import com.autumn.sdk.runtime.user.login_runtime;
import com.autumn.sdk.runtime.user.sign_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.KeyboardUtil;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import cn.hotapk.fastandrutils.utils.FAppUtils;
import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    public static boolean BUGLY_INIT_STATUS = false;
    public static boolean FROM_SPLASH = false;
    private String SETTING = "Config";
    private String AUTO_POST = "Auto_post";
    private String AUTO_SAVELOG = "Auto_savelog";
    private String ALLOW_D = "Allow_d";
    private String ALLOW_E = "Allow_e";
    private String ALLOW_I = "Allow_i";
    private String ALLOW_V = "Allow_v";
    private String ALLOW_W = "Allow_w";
    private String ALLOW_WTF = "Allow_wtf";
    private String ALLOW_DEBUG = "Allow_debug";
    private String ALLOW_ALL = "Allow_all";
    private String ALLOW_SETTING = "Allow_setting";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String USER_LEVEL = "User_level";
    public static String user_level;
    public static String user_integrals;
    public static String user_prestige;
    public static String user_grow_integrals;
    public static String force_update;
    private long mExitTime;// 每个页面的Title数据

    private IUiListener userInfoListener;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";
    private static final String IS_AUTO_LOGIN = "Is_auto_login";
    private static final String USER_KEY = "User_key";
    public static String user_key;

    private String type_app = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    //变量
    boolean isServerSideLogin = false;
    public static Tencent mTencent;
    private DBHelper dbHelper;
    private PopupWindow popView;
    private MyAdapter dropDownAdapter;
    private CardView cardView_1;
    private QMUITipDialog tipDialog;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private int importance;
    private String channelName;
    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_login);
        // Set up the login form.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initState();
        //SUtils.initialize(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryDark));
        }
        File_system_init();
        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);
        //LogUtil.i("添加LoginActivity到Stack");

        //LogUtil.d(FAppUtils.getSign(FAppUtils.getAppPackageName()));

        if (!BUGLY_INIT_STATUS){
            Bugly.init(getApplicationContext(), getString(R.string.bugly_app_id), false);
            CrashReport.initCrashReport(getApplicationContext(), getString(R.string.bugly_app_id),false);
        }//else has inited

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "com.autumn.reptile.update";
            channelName = "更新消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            NotificationManager notificationManager;
            NotificationChannel channel;
            notificationManager = (NotificationManager)this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            channelId = "com.autumn.reptile.music";
            channelName = "音乐消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);

            channelId = "com.autumn.reptile.updateNotification";
            channelName = "更新进度通知";
            importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);

            channelId = "com.autumn.reptile.subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }

        cardView_1 = (CardView)findViewById(R.id.cardView_1);
        cardView_1.setRadius(30);//设置图片圆角的半径大小
        cardView_1.setCardElevation(8);//设置阴影部分大小
        cardView_1.setContentPadding(5,5,5,5);//设置图片距离阴影大小

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        login_status_manager.login_status = false;

        String user_name = getValue(USER_INFO,LOGIN_USERNAME);
        String user_password = getValue(USER_INFO,LOGIN_PASSWORD);
        Boolean auto_login = getBoolean(USER_INFO,IS_AUTO_LOGIN);
        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
        //login_username.setText(user_name);
        //login_password.setText(user_password);
        initLoginUserName();

        if (auto_login){
            showProgress(true);
            //showProgress(true);
            //KeyboardUtil.hideSoftInput(LoginActivity.this);
            login_username.setText(user_name);
            login_password.setText(user_password);
            login_username.setInputType(InputType.TYPE_NULL);
            login_password.setInputType(InputType.TYPE_NULL);
            if (FROM_SPLASH) {
                FROM_SPLASH = false;
                new Thread(new content_runtime(h8, LoginActivity.this, getString(R.string.sign_checker_key), getString(R.string.app_build))).start();
            }else {
                new Thread(new login_runtime(h1,LoginActivity.this,user_name,user_password)).start();
            }
        }else{
            //showProgress(false);

            login_username.setInputType(InputType.TYPE_CLASS_NUMBER);
            login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        ImageView dropdown_button = (ImageView)findViewById(R.id.dropdown_button);
        dropdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DBHelper(LoginActivity.this);
                final ClearEditText mUserName = (ClearEditText) findViewById(R.id.username);
                final PasswordEditText mPassword = (PasswordEditText) findViewById(R.id.password);
                if (popView != null) {
                    if (!popView.isShowing()) {
                        popView.showAsDropDown(mUserName);
                    } else {
                        popView.dismiss();
                    }
                } else {
                    // 如果有已经登录过账号
                    if (dbHelper.queryAllUserName().length > 0) {
                        initPopView(dbHelper.queryAllUserName());
                        if (!popView.isShowing()) {
                            popView.showAsDropDown(mUserName);
                        } else {
                            popView.dismiss();
                        }
                    } else {
                        FToastUtils.init().setRoundRadius(30).show("无记录");
                    }
                }
            }
        });

        Button qq_sign_in = (Button)findViewById(R.id.qq_sign_in);
        qq_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginQQ(v);
            }
        });

        TextView findpassword = findViewById(R.id.findpassword);

        findpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, password_reset.class);
                startActivity(intent);

                //overridePendingTransition(R.anim.start, R.anim.splash);
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                AtyTransitionUtil.exitToRight(LoginActivity.this);
            }
        });

        TextView agree = (TextView)findViewById(R.id.agree);

        agree.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {
                service_detail.FROM_LOGIN_ACTIVITY = true;
                Intent intent = new Intent(LoginActivity.this, service_detail.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(LoginActivity.this);
            }
        });

        TextView register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(LoginActivity.this);
            }
        });

        Button login = (Button)findViewById(R.id.sign_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_username.setError(null);
                login_password.setError(null);

                if(login_username.getText().toString().equals("")||login_username.getText().toString().equals(null)||login_password.getText().toString().equals("")||login_password.getText().toString().equals(null)){
                    FToastUtils.init().setRoundRadius(30).show(getString(R.string.login_not_null));
                }else {
                    showProgress(true);
                    login.setEnabled(false);
                    KeyboardUtil.hideSoftInput(LoginActivity.this);
                    if (FROM_SPLASH) {
                        FROM_SPLASH = false;
                        new Thread(new content_runtime(h8, LoginActivity.this, getString(R.string.sign_checker_key), getString(R.string.app_build))).start();
                    }else {
                        new Thread(new login_runtime(h1,LoginActivity.this,login_username.getText().toString(),login_password.getText().toString())).start();
                    }
                }
            }
        });

        userInfoListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if(o == null){
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) o;
                    Log.e("JO:",jo.toString());
                    String level = jo.getString("level");
                    String nickName = jo.getString("nickname");
                    String gender = jo.getString("gender");
                    FToastUtils.init().setRoundRadius(30).show("你好，" + nickName+"\n"+level+"\n"+gender);

                } catch (Exception e) {
                }
            }
            @Override
            public void onError(UiError uiError) {
            }
            @Override
            public void onCancel() {
            }
        };

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出LoginActivity");
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    //点击按钮之后QQ登录
    public void loginQQ(View view){
        //初始化，得到APPID
        mTencent = Tencent.createInstance(getString(R.string.tencent_app_id), this);
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
        }
    }

    //初始化OPENID和TOKEN值（为了得了用户信息）
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

            //Log.e("data:",token+"\n"+expires+"\n"+openId);

            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
        }
    };

    //实现回调
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                FToastUtils.init().setRoundRadius(30).show("返回为空" + "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                FToastUtils.init().setRoundRadius(30).show("返回为空" + "登录失败");
                return;
            }
            FToastUtils.init().setRoundRadius(30).show("登录成功");
            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject)response);

            initOpenidAndToken(jsonResponse);

        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            FToastUtils.init().setRoundRadius(30).show("onError: " + e.errorDetail);
            //PBE.Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            FToastUtils.init().setRoundRadius(30).show("登录取消");
            //PBE.Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }

    //QQ登录后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }

        if (resultCode == -1) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
            Tencent.handleResultData(data, loginListener);
            UserInfo info = new UserInfo(this, mTencent.getQQToken());
            info.getUserInfo(userInfoListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void logout()
    {
        mTencent.logout(this);
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){
            if(keyCode==KeyEvent.KEYCODE_BACK){
                if((System.currentTimeMillis()-mExitTime)>2000){
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime=System.currentTimeMillis();

                }else{
                    int currentVersion = Build.VERSION.SDK_INT;

                    new Thread(new Runnable() {//利用Runnable接口实现线程
                        @Override
                        public void run() {
                            FindUpdateActivity.update_notice = false;
                            //stop_update_notice();
                            if (!FindUpdateActivity.update_notice) {
                                NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                Objects.requireNonNull(manager).cancelAll();
                            }
                        }
                    }).start();
                    ActivtyStack.getScreenManager().popAllActivityExceptOne();

                    if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        System.exit(0);
                    } else {// android2.1
                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        am.restartPackage(getPackageName());
                    }
                }
                return true;
            }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @SuppressLint("InlinedApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (show) {
            //QMUIStatusBarHelper.setStatusBarDarkMode(this);
            //透明状态栏
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            mLoginFormView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
            tipDialog = new QMUITipDialog.Builder(LoginActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在登录")
                    .create(false);
            tipDialog.show();
        }else {
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            initState();
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            tipDialog.dismiss();
            mLoginFormView.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
                mProgressView.setVisibility(View.VISIBLE);
                tipDialog = new QMUITipDialog.Builder(LoginActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在登录")
                        .create();
                tipDialog.show();
            }else {
                tipDialog.dismiss();
            }

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show) {
                tipDialog = new QMUITipDialog.Builder(LoginActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在登录")
                        .create();
                tipDialog.show();
            }else {
                tipDialog.dismiss();
            }
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }*/
    }

    Handler h1=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO: Implement this method
            switch (msg.what) {
                case login_runtime.FINDER_IMAGE_1:

                    //showProgress(true);

                    if (String.valueOf(msg.obj).equals(getString(R.string.login_success))) {
                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);

                        user_key = login_manager.get_user_key();
                        user_level = login_manager.get_user_level();

                        dbHelper = new DBHelper(LoginActivity.this);
                        String userName = login_username.getText().toString();
                        String password = login_password.getText().toString();
                        /*if (mCheckBox.isChecked()) {
                            dbHelper.insertOrUpdate(userName, password, 1);
                        } else {
                            dbHelper.insertOrUpdate(userName, "", 0);
                        }*/
                        String key = key_manager_data.key;
                        try {
                            dbHelper.insertOrUpdate(AES256.aesEncrypt(userName,key), AES256.aesEncrypt(password,key), 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //FToastUtils.init().setRoundRadius(30).show("记录已经保存");


                        writeValue(USER_INFO,LOGIN_USERNAME,userName);
                        writeValue(USER_INFO,LOGIN_PASSWORD,password);
                        writeValue(USER_INFO,USER_KEY,user_key);
                        writeValue(USER_INFO,USER_LEVEL,user_level);
                        writeBoolean(USER_INFO,IS_AUTO_LOGIN,false);
                        //自动分配
                        new Thread(new check_update_version_login_runtime(h6,LoginActivity.this,user_key)).start();

                    }else{

                        writeBoolean(USER_INFO,IS_AUTO_LOGIN,false);

                        final ClearEditText username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText password = (PasswordEditText) findViewById(R.id.password);

                        username.setInputType(InputType.TYPE_CLASS_NUMBER);
                        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        //showProgress(false);
                        if(String.valueOf(msg.obj).equals(getString(R.string.login_error_account_not_allowed))){
                            FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                            Intent intent = new Intent(LoginActivity.this, service_not_allowed.class);
                            startActivity(intent);

                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                        }else {
                            if (String.valueOf(msg.obj).equals(getString(R.string.login_error_password_wrong))) {
                                final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                                login_password.setError(String.valueOf(msg.obj));
                            } else {
                                if (String.valueOf(msg.obj).equals(getString(R.string.login_error_username_not_exist))) {
                                    final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                                    login_username.setError(String.valueOf(msg.obj));
                                } else {
                                    if (String.valueOf(msg.obj).equals(getString(R.string.login_error_account_outdate))) {
                                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                                        act_account.username = login_username.getText().toString();
                                        act_account.password = login_password.getText().toString();
                                        Intent intent = new Intent(LoginActivity.this, act_account.class);
                                        startActivity(intent);

                                        AtyTransitionUtil.exitToRight(LoginActivity.this);
                                    }else {
                                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                                    }
                                }
                            }
                        }

                        showProgress(false);
                        Button login = (Button)findViewById(R.id.sign_in);
                        login.setEnabled(true);
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
                case sign_runtime.FINDER_IMAGE_1:

                    Button login = (Button)findViewById(R.id.sign_in);
                    login.setEnabled(true);
                    //showProgress(true);
                    writeBoolean(USER_INFO,IS_AUTO_LOGIN,true);

                    login_status_manager.login_status = true;

                    user_integrals = sign_manager.get_user_integrals();
                    user_prestige = sign_manager.get_user_prestige();
                    user_grow_integrals = sign_manager.get_user_grow_integrals();

                    if (String.valueOf(msg.obj).equals(getString(R.string.login_sign_success))) {
                        FToastUtils.init().setRoundRadius(30).showLong(getString(R.string.login_success)+"\n"+"今日首次登录获得：\n"+user_integrals+"积分\n"+user_prestige+"威望\n"+user_grow_integrals+"成长值");

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        AtyTransitionUtil.exitToRight(LoginActivity.this);
                    }else{
                        if (String.valueOf(msg.obj).equals(getString(R.string.sign_error_has_login_sign))){
                            FToastUtils.init().setRoundRadius(30).show(getString(R.string.login_success));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                        }else{
                            FToastUtils.init().setRoundRadius(30).show(getString(R.string.login_success)+"\n首次登录奖励获得失败,原因：\n"+String.valueOf(msg.obj));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                        }

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h3=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case update_runtime.FINDER_IMAGE_1:

                    //showProgress(true);

                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){

                        force_update = update_manager.get_force_update();

                        if (force_update.equals("auto")){
                            writeBoolean(USER_INFO,IS_AUTO_LOGIN,false);
                            AppUpdateActivity.from_LoginActivity = true;
                            //AppUpdateActivity.version_release = true;
                            Intent intent = new Intent(LoginActivity.this, AppUpdateActivity.class);
                            startActivity(intent);

                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                        }else{

                            View notice = new View(LoginActivity.this);
                            sendUpdateMsg(notice);

                            final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                            final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                            new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                        }
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                        new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                    }else{

                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                        new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h4=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case beta_update_login_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    //showProgress(true);

                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){

                        force_update = update_manager.get_force_update();

                        if (force_update.equals("auto") && App_info.type.equals("beta")){
                            writeBoolean(USER_INFO,IS_AUTO_LOGIN,false);
                            AppUpdateActivity.from_LoginActivity = true;
                            //AppUpdateActivity.version_release = false;
                            Intent intent = new Intent(LoginActivity.this, AppUpdateActivity.class);
                            startActivity(intent);

                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                        }else{

                            //NotificationUtils notificationUtils = new NotificationUtils(LoginActivity.this);
                            //notificationUtils.sendNotification("发现新版本", "—>请前往检测更新检测新版本并及时更新");
                            View notice = new View(LoginActivity.this);
                            sendUpdateMsg(notice);

                            final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                            final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                            new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                        }
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                        new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                    }else{

                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                        new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h5=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case debug_update_login_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    //showProgress(true);

                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){

                        force_update = update_manager.get_force_update();

                        if (force_update.equals("auto")){
                            writeBoolean(USER_INFO,IS_AUTO_LOGIN,false);
                            AppUpdateActivity.from_LoginActivity = true;
                            //AppUpdateActivity.version_release = false;
                            Intent intent = new Intent(LoginActivity.this, AppUpdateActivity.class);
                            startActivity(intent);

                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                        }else{

                            //NotificationUtils notificationUtils = new NotificationUtils(LoginActivity.this);
                            //notificationUtils.sendNotification("发现新版本", "—>请前往检测更新检测新版本并及时更新");
                            View notice = new View(LoginActivity.this);
                            sendUpdateMsg(notice);

                            final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                            final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                            new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                        }
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){

                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                        new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                    }else{

                        final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                        final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                        new Thread(new sign_runtime(h2, LoginActivity.this,login_username.getText().toString(),login_password.getText().toString(),"login_sign",user_key)).start();

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h6=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case check_update_version_login_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));

                    //showProgress(true);

                    //new Thread(new debug_update_login_runtime(h5,LoginActivity.this,user_key,getUniquePsuedoID()));

                    if (String.valueOf(msg.obj).equals("beta")){
                        if (App_info.type.equals("beta")) {
                            //beta
                            //AppUpdateActivity.version_release = false;
                            new Thread(new beta_update_login_runtime(h4, LoginActivity.this, user_key)).start();
                        }else {
                            //beta
                            //AppUpdateActivity.version_release = false;
                            new Thread(new beta_update_login_runtime(h7, LoginActivity.this, user_key)).start();
                        }
                        //LogUtil.i(software_version);
                    }else{
                        if (App_info.type.equals("beta")){

                            show_notice();

                        }else {
                            //release
                            //AppUpdateActivity.version_release = true;
                            new Thread(new update_runtime(h3, LoginActivity.this)).start();
                        }
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h7=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case beta_update_login_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    //showProgress(true);

                    if (String.valueOf(msg.obj).equals(getString(R.string.update_new_version))){
                        //beta
                        //AppUpdateActivity.version_release = false;
                        new Thread(new beta_update_login_runtime(h4,LoginActivity.this,user_key)).start();
                        //LogUtil.i(software_version);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.update_new_version_not_exist))){
                        //release
                        //AppUpdateActivity.version_release = true;
                        new Thread(new update_runtime(h3, LoginActivity.this)).start();
                    }else{
                        //release
                        //AppUpdateActivity.version_release = true;
                        new Thread(new update_runtime(h3, LoginActivity.this)).start();
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h8 = new Handler()
    {

        private String content = null;

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
                    //tipDialog.dismiss();

                    if (String.valueOf(msg.obj).equals(getString(R.string.notice_new_version))) {

                        String sign = content_manager.get_app_content();

                        if (sign == null){
                            new AlertDialog.Builder(LoginActivity.this).setTitle("错误")//设置对话框标题
                                    .setCancelable(false)
                                    .setMessage("当前为盗版应用，请重新下载安装正版")//设置显示的内容
                                    .setPositiveButton("立即下载最新版本",new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(LoginActivity.this, Update_release.class);
                                            startActivity(intent);
                                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                                        }
                                    }).show();//在按键响应事件中显示此对话框
                        }else if (sign.equals(FAppUtils.getSign(FAppUtils.getAppPackageName()))) {

                            String user_name = getValue(USER_INFO, LOGIN_USERNAME);
                            String user_password = getValue(USER_INFO, LOGIN_PASSWORD);
                            Boolean auto_login = getBoolean(USER_INFO, IS_AUTO_LOGIN);
                            if (auto_login) {
                                new Thread(new login_runtime(h1, LoginActivity.this, user_name, user_password)).start();
                            } else {
                                final ClearEditText login_username = (ClearEditText) findViewById(R.id.username);
                                final PasswordEditText login_password = (PasswordEditText) findViewById(R.id.password);
                                new Thread(new login_runtime(h1, LoginActivity.this, login_username.getText().toString(), login_password.getText().toString())).start();
                            }

                        }else {
                            new AlertDialog.Builder(LoginActivity.this).setTitle("错误")//设置对话框标题
                                    .setCancelable(false)
                                    .setMessage("当前为盗版应用，请重新下载安装正版")//设置显示的内容
                                    .setPositiveButton("立即下载最新版本",new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(LoginActivity.this, Update_release.class);
                                            startActivity(intent);
                                            AtyTransitionUtil.exitToRight(LoginActivity.this);
                                        }
                                    }).show();//在按键响应事件中显示此对话框
                        }

                        //LogUtil.i(software_version);
                    } else {
                        //fragmentList.add(new Entertainment_fragement_404());
                        //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));

                        new AlertDialog.Builder(LoginActivity.this).setTitle("错误")//设置对话框标题
                                .setCancelable(false)
                                .setMessage("当前为盗版应用，请重新下载安装正版")//设置显示的内容
                                .setPositiveButton("立即下载最新版本",new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        // TODO Auto-generated method stub
                                        Intent intent = new Intent(LoginActivity.this, Update_release.class);
                                        startActivity(intent);
                                        AtyTransitionUtil.exitToRight(LoginActivity.this);
                                    }
                                }).show();//在按键响应事件中显示此对话框

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    private void show_notice() {
        new QMUIDialog.MessageDialogBuilder(LoginActivity.this)
                .setTitle("提示")
                .setMessage("您暂无权限体验此版本，请关注正式版本，或前往下载最新正式版！")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addAction("立即前往下载", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        AppUpdateActivity.FindFullUpdateManual = true;
                        Update_release.FindFullUpdateManual = true;
                        Intent intent = new Intent(LoginActivity.this, Update_release.class);
                        startActivity(intent);
                        AtyTransitionUtil.exitToRight(LoginActivity.this);
                    }
                })
                .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        //Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        int currentVersion = Build.VERSION.SDK_INT;
                        if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {

                            //stopPlayMusicService();
                            onDestroy();

                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            System.exit(0);
                        } else {// android2.1

                            //stopPlayMusicService();
                            onDestroy();

                            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                            am.restartPackage(getPackageName());
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
        /*new IosAlertDialog(LoginActivity.this).builder().setCancelable(false).setTitle("提示").setMsg("您暂无权限体验此版本，请关注正式版本")
                .setPositiveButton("退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int currentVersion = Build.VERSION.SDK_INT;
                        if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {

                            //stopPlayMusicService();
                            onDestroy();

                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            System.exit(0);
                        } else {// android2.1

                            //stopPlayMusicService();
                            onDestroy();

                            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                            am.restartPackage(getPackageName());
                        }

                    }
                }).show();*/

    }

    private void show_notice_error() {
        new IosAlertDialog(LoginActivity.this).builder().setCancelable(false).setTitle("提示").setMsg("type异常，请下载最新版本")
                .setPositiveButton("退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int currentVersion = Build.VERSION.SDK_INT;
                        if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {

                            //stopPlayMusicService();
                            onDestroy();

                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            System.exit(0);
                        } else {// android2.1

                            //stopPlayMusicService();
                            onDestroy();

                            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                            am.restartPackage(getPackageName());
                        }

                    }
                }).show();

    }

    //写入数据
    public void writeValue(String file,String name,String value){

        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            value = AES256.aesEncrypt(value, key);
            SharedPreferences activityPreferences = LoginActivity.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = activityPreferences.edit();
            editor.putString(name,value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeBoolean(String file,String name,Boolean value){
        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences activityPreferences = LoginActivity.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = activityPreferences.edit();
            editor.putBoolean(name, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //读取数据
    public String getValue(String file, String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = LoginActivity.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            String Value = AES256.aesDecrypt(pref.getString(name,""), key);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean getBoolean(String file,String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = LoginActivity.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            Boolean Value = pref.getBoolean(name,false);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(
                NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
    }

    public void sendUpdateMsg(View view) {

        FindUpdateActivity.update_notice = true;

        //MainActivity.update_notice = true;


            new Thread(new Runnable() {//利用Runnable接口实现线程
                @Override
                public void run() {

                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = Objects.requireNonNull(manager).getNotificationChannel("com.autumn.reptile.update");
                        if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        if (FindUpdateActivity.update_notice) {

                            Intent intent = new Intent(LoginActivity.this, FindUpdateActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(LoginActivity.this, 0, intent, 0);
                            Notification notification = new NotificationCompat.Builder(getApplicationContext(), "com.autumn.reptile.update")
                                    .setContentTitle("发现新版本")
                                    .setContentText(update_manager.get_software_version())
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText("请点击此通知或手动前往检测更新检测新版本，并及时更新最新版本"))
                                    .setContentIntent(pi)
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                    .setAutoCancel(false)
                                    .build();
                            Objects.requireNonNull(manager).notify(1, notification);

                        }

                    } else {

                        while (FindUpdateActivity.update_notice) {

                            Intent intent = new Intent(LoginActivity.this, FindUpdateActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(LoginActivity.this, 0, intent, 0);
                            Notification notification = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                notification = new Notification.Builder(getApplicationContext())
                                        .setContentTitle("发现新版本")
                                        .setContentText(update_manager.get_software_version())
                                        .setStyle(new Notification.BigTextStyle().bigText("请点击此通知或手动前往检测更新检测新版本，并及时更新最新版本"))
                                        .setContentIntent(pi)
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
                                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                        .setAutoCancel(false)
                                        .build();
                                Objects.requireNonNull(manager).notify(1, notification);
                            }
                        }

                    }
                        //System.out.println("Runnable running");
                    while (!FindUpdateActivity.update_notice) {
                        //NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        Objects.requireNonNull(manager).cancelAll();
                    }

                }
            }).start();

    }

    public void sendSubscribeMsg(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "com.autumn.reptile.subscribe")
                .setContentTitle("发现新版本")
                .setContentText("—>请前往检测更新检测新版本进行更新")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setNumber(1)
                .build();
        manager.notify(2, notification);
    }

    public void File_system_init()
    {
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs");
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash");
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump");
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache");

        CrashHandler.PATH = Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash/";
        LogUtil.LOG_PATH = Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) +  getString(R.string.logs_path) ;
        DumpUtil.LOG_PATH = Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump/";

        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + getString(R.string.files_path) + getString(R.string.logs_path) + getString(R.string.update_path);
        Data_editer.deleteDirectory(path);

        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download");

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
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"), lightStatusBar);
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            }

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

    private void initPopView(String[] usernames) {
        String key = key_manager_data.key;
        final ClearEditText mUserName = (ClearEditText) findViewById(R.id.username);
        final PasswordEditText mPassword = (PasswordEditText) findViewById(R.id.password);
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < usernames.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            try {
                map.put("name", AES256.aesDecrypt(usernames[i],key));
            } catch (Exception e) {
                map.put("name", usernames[i]);
                e.printStackTrace();
            }
            map.put("drawable", R.drawable.ic_cancel);
            list.add(map);
        }
        dropDownAdapter = new MyAdapter(this, list, R.layout.dropdown_item,
                new String[] { "name", "drawable" }, new int[] { R.id.textview,
                R.id.delete });
        ListView listView = new ListView(this);
        listView.setAdapter(dropDownAdapter);

        popView = new PopupWindow(listView, mPassword.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        // popView.showAsDropDown(mUserName);
    }

    class MyAdapter extends SimpleAdapter {

        private List<HashMap<String, Object>> data;

        final ClearEditText mUserName = (ClearEditText) findViewById(R.id.username);
        final PasswordEditText mPassword = (PasswordEditText) findViewById(R.id.password);

        public MyAdapter(Context context, List<HashMap<String, Object>> data,
                         int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            System.out.println(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(LoginActivity.this).inflate(
                        R.layout.dropdown_item, null);
                holder.btn = (ImageButton) convertView.findViewById(R.id.delete);
                holder.tv = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(data.get(position).get("name").toString());
            holder.tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String key = key_manager_data.key;
                    String[] usernames = dbHelper.queryAllUserName();
                    try {
                        mUserName.setText(AES256.aesDecrypt(usernames[position],key));
                    } catch (Exception e) {
                        mUserName.setText(usernames[position]);
                        e.printStackTrace();
                    }
                    try {
                        mPassword.setText(AES256.aesDecrypt(dbHelper
                                .queryPasswordByName(usernames[position]),key));
                    } catch (Exception e) {
                        //mUserName.setText(usernames[position]);
                        mPassword.setText(dbHelper
                                .queryPasswordByName(usernames[position]));
                        e.printStackTrace();
                    }
                    popView.dismiss();
                }
            });
            holder.btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] usernames = dbHelper.queryAllUserName();
                    if (usernames.length > 0) {
                        dbHelper.delete(usernames[position]);
                    }
                    String[] newusernames = dbHelper.queryAllUserName();
                    if (newusernames.length > 0) {
                        popView.dismiss();
                        popView = null;
                        initPopView(newusernames);
                        popView.showAsDropDown(mUserName);
                    } else {
                        popView.dismiss();
                        popView = null;
                    }
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        private TextView tv;
        private ImageButton btn;
    }

    @Override
    protected void onStop() {
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
        dbHelper.cleanup();
    }

    private void initLoginUserName() {
        String key = key_manager_data.key;
        dbHelper = new DBHelper(this);
        final ClearEditText mUserName = (ClearEditText) findViewById(R.id.username);
        final PasswordEditText mPassword = (PasswordEditText) findViewById(R.id.password);
        String[] usernames = dbHelper.queryAllUserName();
        if (usernames.length > 0) {
            String tempName = null;
            try {
                tempName = AES256.aesDecrypt(usernames[usernames.length - 1],key);
            } catch (Exception e) {
                tempName = usernames[usernames.length - 1];
                e.printStackTrace();
            }
            mUserName.setText(tempName);
            mUserName.setSelection(tempName.length());
            tempName = usernames[usernames.length - 1];
            String tempPwd = null;
            try {
                tempPwd = AES256.aesDecrypt(dbHelper.queryPasswordByName(tempName),key);
            } catch (Exception e) {
                tempPwd = dbHelper.queryPasswordByName(tempName);
                e.printStackTrace();
            }
            int checkFlag = dbHelper.queryIsSavedByName(tempName);
            /*if (checkFlag == 0) {
                mCheckBox.setChecked(false);
            } else if (checkFlag == 1) {
                mCheckBox.setChecked(true);
            }*/
            mPassword.setText(tempPwd);
        }
        mUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPassword.setText("");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}

