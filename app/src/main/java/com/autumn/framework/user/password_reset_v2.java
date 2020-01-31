package com.autumn.framework.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.autumn.framework.View.ClearEditText;
import com.autumn.framework.data.CodeUtils;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.runtime.user.user_password_reset_v2_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * A login screen that offers login via email/password.
 */
public class password_reset_v2 extends BaseActivity {


    private static final String DEVICE_INFO = "Device_info";
    private static final String DEVICE_IMEI = "Device_imei";
    public static String username_v1;
    public static String password_reset_birthday_v1;
    public static String password_reset_email_v1;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    private int mYear;
    private int mMonth;
    private int mDay;
    private CodeUtils codeUtils;
    private ViewPager view_pager;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private View view1;
    private LinearLayout keyboard_height;
    private View mProgressView;
    private EditText password_reset_birthday;
    private String verify_code_content;
    private ClearEditText verfy_code;
    private ImageView code;
    private Button password_reset_complete;
    private ClearEditText password_reset_password1_v2;
    private ClearEditText password_reset_password2_v2;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_password_reset_v2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initState();

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }*/

        mProgressView = findViewById(R.id.password_reset_progress_v2);
        view_pager = (ViewPager) findViewById(R.id.container_password_reset_v2);

        aList = new ArrayList<View>();
        LayoutInflater view_container = getLayoutInflater();
        view1 = view_container.inflate(R.layout.content_password_reset_v2, null,false);
        aList.add(view1);
        //new Register_view(view_container.inflate(R.layout.content_register, null));
        mAdapter = new MyPagerAdapter(aList);
        view_pager.setAdapter(mAdapter);

        code = (ImageView)view1.findViewById(R.id.password_reset_verifycodeview_v2);

        codeUtils = CodeUtils.getInstance();
        //Bitmap bitmap = codeUtils.createBitmap();
        //code.setImageBitmap(bitmap);

        refresh_code();

        //final String[] verify_code = {codeUtils.getCode()};

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_code();
            }
        });


        password_reset_complete = (Button)view1.findViewById(R.id.password_reset_complete);

        verfy_code = (ClearEditText)view1.findViewById(R.id.password_reset_verifycode_v2);

        password_reset_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //LogUtil.i(verify_code[0]);

                password_reset_password1_v2 = (ClearEditText)view1.findViewById(R.id.password_reset_password1_v2);

                password_reset_password2_v2 = (ClearEditText)view1.findViewById(R.id.password_reset_password2_v2);



                verfy_code.setError(null);
                password_reset_password1_v2.setError(null);
                password_reset_password2_v2.setError(null);



                if(verfy_code.getText().toString().toLowerCase().equals(verify_code_content.toLowerCase())) {
                    if (password_reset_password1_v2.getText().toString().equals("") || password_reset_password1_v2.getText().toString().equals(null) || password_reset_password2_v2.getText().toString().equals("") || password_reset_password2_v2.getText().toString().equals(null)) {
                        refresh_code();
                        FToastUtils.init().setRoundRadius(30).show(R.string.password_reset_not_null_v2);
                    }else{
                        if (password_reset_password1_v2.getText().toString().equals(password_reset_password2_v2.getText().toString())) {

                            new Thread(new user_password_reset_v2_runtime(h1, password_reset_v2.this,username_v1,password_reset_birthday_v1,password_reset_email_v1,password_reset_password1_v2.getText().toString())).start();

                            showProgress(true);
                        }else{

                            refresh_code();
                            password_reset_password1_v2.setError(getString(R.string.password_reset_not_equal_v2));
                            password_reset_password2_v2.setError(getString(R.string.password_reset_not_equal_v2));

                        }
                    }
                    //FToastUtils.init().setRoundRadius(30).show("正确");

                }else{

                    refresh_code();
                    //verfy_code.setText("");
                    //FToastUtils.init().setRoundRadius(30).show(R.string.verfy_wrong);
                    verfy_code.setError(getString(R.string.verfy_wrong));

                }
            }
        });


        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(password_reset_v2.this, LoginActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(password_reset_v2.this);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void refresh_code() {
        final String[] verify_code = {codeUtils.getCode()};
        ImageView code = (ImageView)view1.findViewById(R.id.password_reset_verifycodeview_v2);
        verfy_code = (ClearEditText)view1.findViewById(R.id.password_reset_verifycode_v2);
        verfy_code.setText("");

        Bitmap bitmap = codeUtils.createBitmap();
        code.setImageBitmap(bitmap);

        verify_code[0] = codeUtils.getCode();
        verify_code_content = verify_code[0];
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

            view_pager.setVisibility(show ? View.GONE : View.VISIBLE);
            view_pager.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view_pager.setVisibility(show ? View.GONE : View.VISIBLE);
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
            view_pager.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> viewLists;

        public MyPagerAdapter() {
        }

        public MyPagerAdapter(ArrayList<View> viewLists) {
            super();
            this.viewLists = viewLists;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewLists.get(position));
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }
    }






    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initState() {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case user_password_reset_v2_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    showProgress(false);
                    if (String.valueOf(msg.obj).equals(getString(R.string.reset_success))){

                        FToastUtils.init().setRoundRadius(30).show(getString(R.string.user_reset_success));
                        Intent intent = new Intent(password_reset_v2.this, LoginActivity.class);
                        startActivity(intent);

                        //overridePendingTransition(R.anim.start, R.anim.splash);
                        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                        AtyTransitionUtil.exitToRight(password_reset_v2.this);

                    }else{
                        refresh_code();
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    public class VerticalScrollView extends ScrollView {

        private float xDistance, yDistance, xLast, yLast;

        public VerticalScrollView(Context context) {
            super(context);
        }

        public VerticalScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public VerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xDistance = yDistance = 0f;
                    xLast = ev.getX();
                    yLast = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float curX = ev.getX();
                    final float curY = ev.getY();

                    xDistance += Math.abs(curX - xLast);
                    yDistance += Math.abs(curY - yLast);
                    xLast = curX;
                    yLast = curY;

                    if (xDistance > yDistance) {
                        return false;
                    }
            }
            return super.onInterceptTouchEvent(ev);
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

            Intent intent = new Intent(password_reset_v2.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(password_reset_v2.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}

