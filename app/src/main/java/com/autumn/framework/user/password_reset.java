package com.autumn.framework.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.autumn.framework.View.EmailAutoCompleteTextView;
import com.autumn.framework.data.CodeUtils;
import com.autumn.framework.data.DataUtil;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.runtime.user.user_password_reset_check_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.RegexUtil;
import com.liyi.sutils.utils.ToastUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * A login screen that offers login via email/password.
 */
public class password_reset extends BaseActivity {


    private static final String DEVICE_INFO = "Device_info";
    private static final String DEVICE_IMEI = "Device_imei";

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
    private Button password_reset_check;
    private ClearEditText username;
    private EmailAutoCompleteTextView password_reset_email;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_password_reset);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }

        //SUtils.initialize(getApplication());

        initState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        mProgressView = findViewById(R.id.password_reset_progress);
        view_pager = (ViewPager) findViewById(R.id.container_password_reset);

        aList = new ArrayList<View>();
        LayoutInflater view_container = getLayoutInflater();
        view1 = view_container.inflate(R.layout.content_password_reset_check, null,false);
        aList.add(view1);
        //new Register_view(view_container.inflate(R.layout.content_register, null));
        mAdapter = new MyPagerAdapter(aList);
        view_pager.setAdapter(mAdapter);

        code = (ImageView)view1.findViewById(R.id.password_reset_verifycodeview);

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


        password_reset_check = (Button)view1.findViewById(R.id.password_reset_check);

        verfy_code = (ClearEditText)view1.findViewById(R.id.password_reset_verifycode);

        password_reset_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //LogUtil.i(verify_code[0]);

                username = (ClearEditText)view1.findViewById(R.id.password_reset_username);
                password_reset_birthday = (EditText)view1.findViewById(R.id.password_reset_birthday);
                password_reset_email = (EmailAutoCompleteTextView)view1.findViewById(R.id.password_reset_email);


                verfy_code.setError(null);
                username.setError(null);
                password_reset_birthday.setError(null);
                password_reset_email.setError(null);


                if(verfy_code.getText().toString().toLowerCase().equals(verify_code_content.toLowerCase())) {
                    if (username.getText().toString().equals("") || username.getText().toString().equals(null) || password_reset_birthday.getText().toString().equals("") || password_reset_birthday.getText().toString().equals(null) || password_reset_email.getText().toString().equals("") || password_reset_email.getText().toString().equals(null)) {
                        refresh_code();
                        FToastUtils.init().setRoundRadius(30).show(R.string.password_reset_not_null);
                    }else{
                        if (RegexUtil.isEmail(password_reset_email.getText().toString())) {

                            new Thread(new user_password_reset_check_runtime(h1, password_reset.this,username.getText().toString(),password_reset_birthday.getText().toString(),password_reset_email.getText().toString())).start();

                            showProgress(true);
                        }else{
                            refresh_code();
                            password_reset_email.setError(getString(R.string.password_reset_email_not_allowed));
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

                Intent intent = new Intent(password_reset.this, LoginActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(password_reset.this);

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

        password_reset_birthday = (EditText)view1. findViewById(R.id.password_reset_birthday);
        password_reset_birthday.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (password_reset_birthday.getText().toString().equals("")||password_reset_birthday.getText().toString().equals(null)){
                        onYearMonthDayPicker(v);
                        /*new DatePickerDialog(RegisterActivity.this,
                                R.style.MyDatePickerDialogTheme,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        mYear = year;
                                        mMonth = month;
                                        mDay = dayOfMonth;
                                        //register_birthday.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        String choose_date = mYear + "-" + (mMonth + 1) + "-" + mDay;
                                        try {
                                            Date date = formatter.parse(choose_date);
                                            String birthday = formatter.format(date);
                                            RegisterActivity.this.register_birthday.setText(birthday);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                2000,0,1).show();*/
                    }else{
                        String result[] = DataUtil.split_words(password_reset_birthday.getText().toString(),"-");
                        onYearMonthDayPicker(v,Integer.parseInt(result[0]),Integer.parseInt(result[1]),Integer.parseInt(result[2]));
                        //onYearMonthDayPicker(v,mYear,mMonth,mDay);
                        /*new DatePickerDialog(RegisterActivity.this,
                                R.style.MyDatePickerDialogTheme,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        mYear = year;
                                        mMonth = month;
                                        mDay = dayOfMonth;
                                        //register_birthday.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        String choose_date = mYear + "-" + (mMonth + 1) + "-" + mDay;
                                        try {
                                            Date date = formatter.parse(choose_date);
                                            String birthday = formatter.format(date);
                                            RegisterActivity.this.register_birthday.setText(birthday);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                mYear, mMonth, mDay).show();*/
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void refresh_code() {
        final String[] verify_code = {codeUtils.getCode()};
        ImageView code = (ImageView)view1.findViewById(R.id.password_reset_verifycodeview);
        verfy_code = (ClearEditText)view1.findViewById(R.id.password_reset_verifycode);
        verfy_code.setText("");

        Bitmap bitmap = codeUtils.createBitmap();
        code.setImageBitmap(bitmap);

        verify_code[0] = codeUtils.getCode();
        verify_code_content = verify_code[0];
    }

    public void onYearMonthDayPicker(View view) {
        final cn.addapp.pickers.picker.DatePicker picker = new cn.addapp.pickers.picker.DatePicker(password_reset.this);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(1900, 8, 29);
        picker.setRangeEnd(2100, 1, 11);
        picker.setSelectedItem(2000, 1, 1);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(new cn.addapp.pickers.picker.DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                //FToastUtils.init().setRoundRadius(30).show(year + "-" + month + "-" + day);
                password_reset_birthday.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    public void onYearMonthDayPicker(View view, int year, int month, int day) {
        final cn.addapp.pickers.picker.DatePicker picker = new cn.addapp.pickers.picker.DatePicker(password_reset.this);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(1900, 8, 29);
        picker.setRangeEnd(2100, 1, 11);
        picker.setSelectedItem(year, month, day);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(new cn.addapp.pickers.picker.DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                //FToastUtils.init().setRoundRadius(30).show(year + "-" + month + "-" + day);
                password_reset_birthday.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
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
                case user_password_reset_check_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    username = (ClearEditText)view1.findViewById(R.id.password_reset_username);
                    password_reset_birthday = (EditText)view1.findViewById(R.id.password_reset_birthday);
                    password_reset_email = (EmailAutoCompleteTextView)view1.findViewById(R.id.password_reset_email);

                    String username_v1 = username.getText().toString();
                    String password_reset_birthday_v1 = password_reset_birthday.getText().toString();
                    String password_reset_email_v1 = password_reset_email.getText().toString();

                    password_reset_v2.username_v1 = username_v1;
                    password_reset_v2.password_reset_birthday_v1 = password_reset_birthday_v1;
                    password_reset_v2.password_reset_email_v1 = password_reset_email_v1;


                    showProgress(false);
                    if (String.valueOf(msg.obj).equals(getString(R.string.check_success))){

                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        Intent intent = new Intent(password_reset.this, password_reset_v2.class);
                        startActivity(intent);

                        //overridePendingTransition(R.anim.start, R.anim.splash);
                        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                        AtyTransitionUtil.exitToRight(password_reset.this);


                    }else if(String.valueOf(msg.obj).equals(getString(R.string.check_error_username_not_exist))){
                        refresh_code();
                        username.setError(String.valueOf(msg.obj));
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.check_error_data_error))){
                        refresh_code();
                        username.setError(String.valueOf(msg.obj));
                        password_reset_birthday.setError(String.valueOf(msg.obj));
                        password_reset_birthday.setText("");
                        password_reset_email.setError(String.valueOf(msg.obj));
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

            Intent intent = new Intent(password_reset.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(password_reset.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}

