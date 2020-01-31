package com.autumn.framework.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.autumn.framework.View.ClearEditText;
import com.autumn.framework.View.EmailAutoCompleteTextView;
import com.autumn.framework.View.PasswordEditText;
import com.autumn.framework.data.AES256;
import com.autumn.framework.data.CodeUtils;
import com.autumn.framework.data.DataUtil;
import com.autumn.framework.data.key_manager_data;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.runtime.user.register_runtime;
import com.autumn.sdk.runtime.user.user_register_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.RegexUtil;
import com.liyi.sutils.utils.ToastUtil;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity {

    private static final String DEVICE_INFO = "Device_info";
    private static final String DEVICE_IMEI = "Device_imei";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private EditText register_birthday;
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
    private String verify_code_content;
    private ClearEditText verfy_code;
    private ImageView code;
    private Button register;
    private EditText username;
    private PasswordEditText register_password1;
    private PasswordEditText register_password2;
    private ClearEditText register_private_name;
    private EmailAutoCompleteTextView register_email;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_register);

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

        mProgressView = findViewById(R.id.register_progress);
        view_pager = (ViewPager) findViewById(R.id.container_register);

        showProgress(true);

        new Thread(new register_runtime(h1, RegisterActivity.this)).start();

        aList = new ArrayList<View>();
        LayoutInflater view_container = getLayoutInflater();
        view1 = view_container.inflate(R.layout.content_register, null,false);
        aList.add(view1);
        //new Register_view(view_container.inflate(R.layout.content_register, null));
        mAdapter = new MyPagerAdapter(aList);
        view_pager.setAdapter(mAdapter);

        code = (ImageView)view1.findViewById(R.id.register_verifycodeview);

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

        TextView login_hasaccount = (TextView)view1.findViewById(R.id.login_hasaccount);

        login_hasaccount.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(RegisterActivity.this);

            }

        });

        TextView agree = (TextView)view1.findViewById(R.id.agree);

        agree.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                service_detail.FROM_LOGIN_ACTIVITY = false;
                Intent intent = new Intent(RegisterActivity.this, service_detail.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(RegisterActivity.this);

            }

        });

        register = (Button)view1.findViewById(R.id.register);

        verfy_code = (ClearEditText)view1.findViewById(R.id.register_verifycode);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //LogUtil.i(verify_code[0]);

                username = (EditText)view1.findViewById(R.id.register_username);
                register_password1 = (PasswordEditText)view1.findViewById(R.id.register_password1);
                register_password2 = (PasswordEditText)view1.findViewById(R.id.register_password2);
                register_private_name = (ClearEditText)view1.findViewById(R.id.register_private_name);
                register_birthday = (EditText)view1.findViewById(R.id.register_birthday);
                register_email = (EmailAutoCompleteTextView)view1.findViewById(R.id.register_email);

                register_password1.setError(null);
                register_password2.setError(null);
                register_private_name.setError(null);
                //register_email.setError(null);
                verfy_code.setError(null);


                if(verfy_code.getText().toString().toLowerCase().equals(verify_code_content.toLowerCase())) {

                    if(username.getText().toString().equals("")||username.getText().toString().equals(null)){
                        new Thread(new register_runtime(h1, RegisterActivity.this)).start();
                    }else {

                        if (register_password1.getText().toString().equals("") || register_password1.getText().toString().equals(null) || register_password2.getText().toString().equals("") || register_password2.getText().toString().equals(null) || register_private_name.getText().toString().equals("") || register_private_name.getText().toString().equals(null) || register_birthday.getText().toString().equals("") || register_birthday.getText().toString().equals(null) || register_email.getText().toString().equals("") || register_email.getText().toString().equals(null)) {
                            refresh_code();
                            FToastUtils.init().setRoundRadius(30).show(R.string.register_not_null);
                        }else{

                            //int private_name_disabled_1 = et_content.getText().toString().indexOf("sb");
                            //int private_name_disabled_2 = et_content.getText().toString().indexOf("傻逼");
                            //int private_name_disabled_3 = et_content.getText().toString().indexOf("操你妈");
                            //int private_name_disabled_4 = et_content.getText().toString().indexOf("草泥马");
                            //int private_name_disabled_5 = et_content.getText().toString().indexOf("脑残");

                            String new_private_name = register_private_name.getText().toString();

                            if (com.autumn.sdk.data.DataUtil.find_words(new_private_name,"sb")|| com.autumn.sdk.data.DataUtil.find_words(new_private_name,"傻逼") || com.autumn.sdk.data.DataUtil.find_words(new_private_name,"操你妈") || com.autumn.sdk.data.DataUtil.find_words(new_private_name,"草泥马") || com.autumn.sdk.data.DataUtil.find_words(new_private_name,"脑残")) {

                                refresh_code();
                                register_private_name.setError(getString(R.string.register_private_name_disabled));

                            }else{

                                if (RegexUtil.isEmail(register_email.getText().toString())) {

                                    if (register_password1.getText().toString().equals(register_password2.getText().toString())) {

                                        new Thread(new user_register_runtime(h2, RegisterActivity.this, username.getText().toString(), register_password1.getText().toString(), register_private_name.getText().toString(), register_birthday.getText().toString(), register_email.getText().toString(), getValue(DEVICE_INFO, DEVICE_IMEI))).start();

                                    } else {

                                        refresh_code();
                                        //FToastUtils.init().setRoundRadius(30).show(R.string.register_password_not_equal);
                                        register_password1.setError(getString(R.string.register_password_not_equal));
                                        register_password2.setError(getString(R.string.register_password_not_equal));

                                    }

                                } else {

                                    refresh_code();
                                    register_email.setError(getString(R.string.register_email_disabled));

                                }

                            }

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

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(RegisterActivity.this);

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

        register_birthday = (EditText)view1. findViewById(R.id.register_birthday);
        register_birthday.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (register_birthday.getText().toString().equals("")||register_birthday.getText().toString().equals(null)){
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
                        String result[] = DataUtil.split_words(register_birthday.getText().toString(),"-");
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

        final TextView private_name_num;// 用来显示剩余字数
        final int num = 20;//限制的最大字数
        private_name_num = (TextView) view1.findViewById(R.id.private_name_num);
        register_private_name = (ClearEditText)view1.findViewById(R.id.register_private_name);

        private_name_num.setVisibility(View.GONE);
        private_name_num.setText("20");

        register_private_name.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                private_name_num.setVisibility(View.VISIBLE);
                int number = num - s.length();
                private_name_num.setText("" + number);
                selectionStart = register_private_name.getSelectionStart();
                selectionEnd = register_private_name.getSelectionEnd();
                //System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    register_private_name.setText(s);
                    register_private_name.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

    }

    private void refresh_code() {
        final String[] verify_code = {codeUtils.getCode()};
        ImageView code = (ImageView)view1.findViewById(R.id.register_verifycodeview);
        verfy_code = (ClearEditText)view1.findViewById(R.id.register_verifycode);
        verfy_code.setText("");

        Bitmap bitmap = codeUtils.createBitmap();
        code.setImageBitmap(bitmap);

        verify_code[0] = codeUtils.getCode();
        verify_code_content = verify_code[0];
    }

    public void onYearMonthDayPicker(View view) {
        final cn.addapp.pickers.picker.DatePicker picker = new cn.addapp.pickers.picker.DatePicker(RegisterActivity.this);
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
                register_birthday.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    public void onYearMonthDayPicker(View view, int year, int month, int day) {
        final cn.addapp.pickers.picker.DatePicker picker = new cn.addapp.pickers.picker.DatePicker(RegisterActivity.this);
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
                register_birthday.setText(year + "-" + month + "-" + day);
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


    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String choose_date = year + "-" + monthOfYear + "-" + dayOfMonth;
                try {
                    Date date = formatter.parse(choose_date);
                    String birthday = formatter.format(date);
                    RegisterActivity.this.register_birthday.setText(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    public void getDate(View view){
        //实例化Calendar对象，通过getInstance()获得时间操作类型
        Calendar c=Calendar.getInstance();
        //获得时间的年月日
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_YEAR);
        //DatePickerDialog中的参数为：
        //  1、this
        //  2、new DatePickerDialog.OnDateSetListener()
        //          2.1重写onDateSet方法，其中的参数打印出的就是年月日
        //  3、年月日
        //
        DatePickerDialog dpd=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //LogUtil.i("当前日期："+year+"-"+month+"-"+dayOfMonth);
            }
        },year,month,day);
        //调用日期（一定要记得）
        dpd.show();

    }

    //读取数据
    public String getValue(String file, String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = RegisterActivity.this.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            String Value = AES256.aesDecrypt(pref.getString(name,""), key);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            //boolean lightStatusBar = false;
            //StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.colorPrimary), lightStatusBar);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            //LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            //linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            //int statusHeight = getStatusBarHeight(RegisterActivity.this);
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

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case register_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    username = (EditText)view1.findViewById(R.id.register_username);
                    username.setText(String.valueOf(msg.obj));

                    if (String.valueOf(msg.obj).matches("[0-9]+")) {
                        showProgress(false);
                    }else{
                        new Thread(new register_runtime(h1, RegisterActivity.this)).start();
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
                case register_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    //EditText username = (EditText)view1.findViewById(R.id.register_username);
                    //username.setText(String.valueOf(msg.obj));
                    FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));

                    if (String.valueOf(msg.obj).equals(getString(R.string.register_success))){
                        username = (EditText)view1.findViewById(R.id.register_username);
                        register_password1 = (PasswordEditText)view1.findViewById(R.id.register_password1);
                        register_password2 = (PasswordEditText)view1.findViewById(R.id.register_password2);
                        register_private_name = (ClearEditText)view1.findViewById(R.id.register_private_name);
                        register_birthday = (EditText)view1.findViewById(R.id.register_birthday);
                        register_email = (EmailAutoCompleteTextView)view1.findViewById(R.id.register_email);
                        Register_success.username = username.getText().toString();
                        Register_success.password = register_password1.getText().toString();
                        Register_success.private_name = register_private_name.getText().toString();
                        Register_success.birthday = register_birthday.getText().toString();
                        Register_success.register_email = register_email.getText().toString();

                        Intent intent = new Intent(RegisterActivity.this, Register_success.class);
                        startActivity(intent);

                        //overridePendingTransition(R.anim.start, R.anim.splash);
                        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                        AtyTransitionUtil.exitToLeft(RegisterActivity.this);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.register_error_username_wrong))){

                        showProgress(true);

                        new Thread(new register_runtime(h1, RegisterActivity.this)).start();

                        username = (EditText)view1.findViewById(R.id.register_username);
                        register_password1 = (PasswordEditText)view1.findViewById(R.id.register_password1);
                        register_password2 = (PasswordEditText)view1.findViewById(R.id.register_password2);
                        register_private_name = (ClearEditText)view1.findViewById(R.id.register_private_name);
                        register_birthday = (EditText)view1.findViewById(R.id.register_birthday);
                        register_email = (EmailAutoCompleteTextView)view1.findViewById(R.id.register_email);

                        final String[] verify_code = {codeUtils.getCode()};

                        verfy_code = (ClearEditText)view1.findViewById(R.id.register_verifycode);

                        if(verfy_code.getText().toString().toLowerCase().equals(verify_code[0].toLowerCase())) {

                            if(username.getText().toString().equals("")||username.getText().toString().equals(null)){
                                new Thread(new register_runtime(h1, RegisterActivity.this)).start();
                            }else {

                                if (register_password1.getText().toString().equals("") || register_password1.getText().toString().equals(null) || register_password2.getText().toString().equals("") || register_password2.getText().toString().equals(null) || register_private_name.getText().toString().equals("") || register_private_name.getText().toString().equals(null) || register_birthday.getText().toString().equals("") || register_birthday.getText().toString().equals(null) || register_email.getText().toString().equals("") || register_email.getText().toString().equals(null)) {
                                    refresh_code();
                                    FToastUtils.init().setRoundRadius(30).show(R.string.register_not_null);
                                }else{

                                    //int private_name_disabled_1 = et_content.getText().toString().indexOf("sb");
                                    //int private_name_disabled_2 = et_content.getText().toString().indexOf("傻逼");
                                    //int private_name_disabled_3 = et_content.getText().toString().indexOf("操你妈");
                                    //int private_name_disabled_4 = et_content.getText().toString().indexOf("草泥马");
                                    //int private_name_disabled_5 = et_content.getText().toString().indexOf("脑残");

                                    String new_private_name = register_private_name.getText().toString();

                                    if (com.autumn.sdk.data.DataUtil.find_words(new_private_name,"sb")|| com.autumn.sdk.data.DataUtil.find_words(new_private_name,"傻逼") || com.autumn.sdk.data.DataUtil.find_words(new_private_name,"操你妈") || com.autumn.sdk.data.DataUtil.find_words(new_private_name,"草泥马") || com.autumn.sdk.data.DataUtil.find_words(new_private_name,"脑残")) {

                                        refresh_code();
                                        register_private_name.setError(getString(R.string.register_private_name_disabled));

                                    }else{

                                        if (RegexUtil.isEmail(register_email.getText().toString())) {

                                            if (register_password1.getText().toString().equals(register_password2.getText().toString())) {

                                                new Thread(new user_register_runtime(h2, RegisterActivity.this, username.getText().toString(), register_password1.getText().toString(), register_private_name.getText().toString(), register_birthday.getText().toString(), register_email.getText().toString(), getValue(DEVICE_INFO, DEVICE_IMEI))).start();

                                            } else {

                                                refresh_code();
                                                //FToastUtils.init().setRoundRadius(30).show(R.string.register_password_not_equal);
                                                register_password1.setError(getString(R.string.register_password_not_equal));
                                                register_password2.setError(getString(R.string.register_password_not_equal));

                                            }

                                        } else {

                                            refresh_code();
                                            register_email.setError(getString(R.string.register_email_disabled));

                                        }

                                    }

                                }

                            }

                            //FToastUtils.init().setRoundRadius(30).show("正确");

                        }else{

                            refresh_code();
                            //verfy_code.setText("");

                            //FToastUtils.init().setRoundRadius(30).show(R.string.verfy_wrong);
                            verfy_code.setError(getString(R.string.verfy_wrong));

                        }

                    }else if (String.valueOf(msg.obj).equals(getString(R.string.register_error_email))){
                        refresh_code();
                        register_email = (EmailAutoCompleteTextView)view1.findViewById(R.id.register_email);
                        register_email.setError(getString(R.string.register_error_email));
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

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(RegisterActivity.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}

