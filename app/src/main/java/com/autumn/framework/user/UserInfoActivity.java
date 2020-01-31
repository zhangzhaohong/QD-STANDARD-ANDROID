package com.autumn.framework.user;

/**
 * Created by zhang on 2018/7/2.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.autumn.framework.View.IosAlertDialog;
import com.autumn.framework.View.RefreshableView;
import com.autumn.framework.X5WebView.ui.X5WebGameActivity;
import com.autumn.framework.data.DataUtil;
import com.autumn.framework.data.SpUtil;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.manager.user.sign_manager;
import com.autumn.sdk.manager.user.user_info_manager;
import com.autumn.sdk.runtime.user.check_sign_runtime;
import com.autumn.sdk.runtime.user.manual_sign_runtime;
import com.autumn.sdk.runtime.user.user_info_runtime;
import com.autumn.statusbar.StatusBarCompat;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hotapk.fastandrutils.utils.FToastUtils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class UserInfoActivity extends BaseActivity {

    private SmartRefreshLayout mRefreshLayout;
    private ListView mList;
    private Button mStop;
    private View mProgressView;

    private RefreshableView mRefreshableView;
    private Context mContext;
    public static String account;
    public static String private_name;
    public static String user_birthday;
    public static String user_email;
    public static String user_level;
    public static String log_level;
    public static String user_available_date;
    public static int integrals;
    public static int prestige;
    public static int grow_integrals;
    public static String last_login;
    public static String last_sign;
    public static String keep_sign_times;
    public static String vip_endtime;
    public static String svip_endtime;
    public static String user_integrals;
    public static String user_prestige;
    public static String user_grow_integrals;
    public static String user_keep_sign_times;
    public static boolean sign_status = false;
    private boolean wallet_is_open = false;
    private boolean level_is_open = false;
    private boolean date_is_open = false;
    private boolean member_is_open = false;
    private boolean manual_refresh = false;
    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";
    private static final String IS_AUTO_LOGIN = "Is_auto_login";
    private static final String USER_KEY = "User_key";

    private PullToRefreshScrollView refresh_view;

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            //mRefreshLayout.finishRefreshing();
            //mRefreshLayout.onRefreshComplete();
            mRefreshLayout.finishRefresh(1000);
            FToastUtils.init().setRoundRadius(30).show("刷新成功！");
        };
    };
    private CardView cardView;
    private String MESSAGE_SIGN = null;
    private QMUIPopup mNormalPopup;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardView = (CardView)findViewById(R.id.cardView);
        cardView.setRadius(30);//设置图片圆角的半径大小
        cardView.setCardElevation(8);//设置阴影部分大小
        cardView.setContentPadding(5,5,5,5);//设置图片距离阴影大小

        //Animation enterAnimation = new AlphaAnimation(0f, 1f);
        //enterAnimation.setDuration(600);
        //enterAnimation.setFillAfter(true);

        //Animation exitAnimation = new AlphaAnimation(1f, 0f);
        //exitAnimation.setDuration(600);
        //exitAnimation.setFillAfter(true);

        mProgressView = findViewById(R.id.user_info_progress);

        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.pull_to_refresh_view);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        //设置 Footer 为 球脉冲
        //mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //mList = (ListView) findViewById(R.id.list);

        initState();

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);
        //SUtils.initialize(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(UserInfoActivity.this, R.color.colorPrimaryDark));
        }
        /*boolean lightStatusBar = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.colorPrimaryDark), lightStatusBar);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }*/

        //showProgress(true);

        mRefreshLayout.autoRefresh();

        SpUtil rw = new SpUtil();
        if (rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME).equals("")||rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME) == null){
            FToastUtils.init().setRoundRadius(30).show("登录失效，即将重新登录！");
            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(UserInfoActivity.this);
        }else {
            new Thread(new user_info_runtime(h1, UserInfoActivity.this, rw.getValue(UserInfoActivity.this, USER_INFO, LOGIN_USERNAME))).start();
        }

        final ImageView user_available_date_show = (ImageView)findViewById(R.id.user_available_date_show);
        final ImageView user_available_date_disable_show = (ImageView)findViewById(R.id.user_available_date_disable_show);
        final ImageView user_member_show = (ImageView)findViewById(R.id.user_member_show);
        final ImageView user_member_disable_show = (ImageView)findViewById(R.id.user_member_disable_show);
        final LinearLayout divider_date = (LinearLayout)findViewById(R.id.divider_date);
        final LinearLayout divider_member = (LinearLayout)findViewById(R.id.divider_member);
        final TextView integrals = (TextView)findViewById(R.id.integrals);
        final TextView prestige = (TextView)findViewById(R.id.prestige);
        final TextView grow_integrals = (TextView)findViewById(R.id.grow_integrals);
        final ImageButton sign = (ImageButton)findViewById(R.id.sign);
        final ImageButton have_signed = (ImageButton)findViewById(R.id.have_signed);
        final TextView private_name = (TextView)findViewById(R.id.private_name_info);
        final TextView user_birthday_info = (TextView)findViewById(R.id.user_birthday_info);
        final TextView user_email_info = (TextView)findViewById(R.id.user_email_info);
        final ImageView keep_sign = (ImageView)findViewById(R.id.keep_sign);
        final ImageView not_keep_sign = (ImageView)findViewById(R.id.not_keep_sign);

        keep_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*MESSAGE_SIGN = "您已经连续签到超过七天，记得不要断呦！";
                initNormalPopupIfNeed();
                mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
                mNormalPopup.show(v);*/
                FToastUtils.init().setRoundRadius(30).show("您已经连续签到超过七天，记得不要断呦！");
            }
        });

        not_keep_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*MESSAGE_SIGN = "连续签到七天以上即可点亮！";
                initNormalPopupIfNeed();
                mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
                mNormalPopup.show(v);*/
                FToastUtils.init().setRoundRadius(30).show("连续签到七天以上即可点亮！");
            }
        });

        user_birthday_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessagePositiveDialog();
                //FToastUtils.init().setRoundRadius(30).show("生日不支持手动修改，如若注册时写错，请联系qq544901005后台进行改正，每个账号只有一次修改机会",12000);
            }
        });

        user_email_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessagePositiveDialog();
                //FToastUtils.init().setRoundRadius(30).show("邮箱不支持手动修改，如若注册时写错，请联系qq544901005后台进行改正",12000);
            }
        });

        private_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPrivateName.private_name = private_name.getText().toString();
                Intent intent = new Intent(UserInfoActivity.this, EditPrivateName.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(UserInfoActivity.this);
            }
        });
        user_available_date_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout available_date_data = (LinearLayout)findViewById(R.id.available_date_data);
                available_date_data.setVisibility(View.VISIBLE);
                user_available_date_show.setVisibility(View.GONE);
                user_available_date_disable_show.setVisibility(View.VISIBLE);
                divider_date.setVisibility(View.VISIBLE);
                date_is_open=true;
                //showProgress(true);
                SpUtil rw = new SpUtil();
                new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
            }
        });
        user_available_date_disable_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout available_date_data = (LinearLayout)findViewById(R.id.available_date_data);
                available_date_data.setVisibility(View.GONE);
                user_available_date_disable_show.setVisibility(View.GONE);
                user_available_date_show.setVisibility(View.VISIBLE);
                divider_date.setVisibility(View.GONE);
                date_is_open=false;
            }
        });
        LinearLayout date = (LinearLayout)findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_is_open){
                    LinearLayout available_date_data = (LinearLayout)findViewById(R.id.available_date_data);
                    available_date_data.setVisibility(View.GONE);
                    user_available_date_disable_show.setVisibility(View.GONE);
                    user_available_date_show.setVisibility(View.VISIBLE);
                    divider_date.setVisibility(View.GONE);
                    date_is_open=false;
                }else{
                    LinearLayout available_date_data = (LinearLayout)findViewById(R.id.available_date_data);
                    available_date_data.setVisibility(View.VISIBLE);
                    user_available_date_show.setVisibility(View.GONE);
                    user_available_date_disable_show.setVisibility(View.VISIBLE);
                    divider_date.setVisibility(View.VISIBLE);
                    date_is_open=true;
                    //showProgress(true);
                    SpUtil rw = new SpUtil();
                    new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
                }
            }
        });
        user_member_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout member_data = (LinearLayout)findViewById(R.id.member_data);
                member_data.setVisibility(View.VISIBLE);
                user_member_show.setVisibility(View.GONE);
                user_member_disable_show.setVisibility(View.VISIBLE);
                divider_member.setVisibility(View.VISIBLE);
                member_is_open=true;
                //showProgress(true);
                SpUtil rw = new SpUtil();
                new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
            }
        });
        user_member_disable_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout member_data = (LinearLayout)findViewById(R.id.member_data);
                member_data.setVisibility(View.GONE);
                user_member_disable_show.setVisibility(View.GONE);
                user_member_show.setVisibility(View.VISIBLE);
                divider_member.setVisibility(View.GONE);
                member_is_open=false;
            }
        });
        LinearLayout member_status = (LinearLayout)findViewById(R.id.member_status);
        member_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(member_is_open){
                    LinearLayout member_data = (LinearLayout)findViewById(R.id.member_data);
                    member_data.setVisibility(View.GONE);
                    user_member_disable_show.setVisibility(View.GONE);
                    user_member_show.setVisibility(View.VISIBLE);
                    divider_member.setVisibility(View.GONE);
                    member_is_open=false;
                }else{
                    LinearLayout member_data = (LinearLayout)findViewById(R.id.member_data);
                    member_data.setVisibility(View.VISIBLE);
                    user_member_show.setVisibility(View.GONE);
                    user_member_disable_show.setVisibility(View.VISIBLE);
                    divider_member.setVisibility(View.VISIBLE);
                    member_is_open=true;
                    //showProgress(true);
                    SpUtil rw = new SpUtil();
                    new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
                }
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgress(true);
                SpUtil rw = new SpUtil();
                new Thread(new manual_sign_runtime(h3,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME),rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_PASSWORD),rw.getValue(UserInfoActivity.this,USER_INFO,USER_KEY))).start();
            }
        });
        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(UserInfoActivity.this);
            }
        });
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        //mList.setAdapter(adapter);

        //refresh_view =(PullToRefreshScrollView)findViewById(R.id.pull_to_refresh_view);

        /*mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

                manual_refresh = true;
                SpUtil rw = new SpUtil();
                new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(USER_INFO,LOGIN_USERNAME))).start();

            }
        });*/

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                manual_refresh = true;
                SpUtil rw = new SpUtil();
                new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                manual_refresh = true;
                SpUtil rw = new SpUtil();
                new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
            }
        });

        /*mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        // do something when refresh starts

                    }

                    @Override
                    public void completeRefresh() {
                        // do something when refresh complete
                    }
                });*/

    }

    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new QMUIPopup(UserInfoActivity.this, QMUIPopup.DIRECTION_NONE);
            TextView textView = new TextView(UserInfoActivity.this);
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(
                    QMUIDisplayHelper.dp2px(UserInfoActivity.this, 250),
                    WRAP_CONTENT
            ));
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(UserInfoActivity.this, 4), 1.0f);
            int padding = QMUIDisplayHelper.dp2px(UserInfoActivity.this, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(MESSAGE_SIGN);
            textView.setTextColor(ContextCompat.getColor(UserInfoActivity.this, R.color.colorArc));
            mNormalPopup.setContentView(textView);
            mNormalPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //mActionButton1.setText(getContext().getResources().getString(R.string.popup_normal_action_button_text_show));
                }
            });
        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){

        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        startActivity(intent);

        AtyTransitionUtil.exitToRight(UserInfoActivity.this);

        return super.onKeyDown(keyCode, event);
    }

    private String intChange2Str(int number) {
        String str = "";
        if (number <= 0) {
            str = "";
        } else if (number < 10000) {
            str = number + "";
        } else {
            double d = (double) number;
            double num = d / 10000;//1.将数字转换成以万为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            str = f1 + "万";
        }
        return str;
    }

    private String date_manager(String date){
        if (date == null){
            //FToastUtils.init().setRoundRadius(30).show("参数异常");
            date = "尚未开通";
        }else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
            //获取当前时间
            Date date_now = new Date(System.currentTimeMillis());
            String date_now_system = simpleDateFormat.format(date_now);
            String date_mamber = DataUtil.replace_words("-",date,"");

            if (date.equals("0000-00-00")) {
                date = "尚未开通";
            }else if (Integer.parseInt(date_now_system) > Integer.parseInt(date_mamber)){
                date = "已过期";
            }else{
                try {
                    int leave_time = daysBetween(simpleDateFormat.parse(date_now_system),simpleDateFormat.parse(date_mamber));
                    if (leave_time > 2 && leave_time <= 10){
                        date = leave_time + "天后过期";
                    }else if (leave_time == 2){
                        date = "将于后天过期";
                    }else if (leave_time == 1){
                        date = "将于明天过期";
                    }else if (leave_time == 0){
                        date = "将于今天过期";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return date;
    }

    public static final int daysBetween(Date early, Date late) {

        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;

        return days;
    }

    private String level_manager(String level){
        ImageView vip = (ImageView)findViewById(R.id.vip_view);
        ImageView svip = (ImageView)findViewById(R.id.svip_view);

        if (level == null){
            //FToastUtils.init().setRoundRadius(30).show("参数异常");
            level = "普通用户  (慢速成长中)";
            svip.setVisibility(View.GONE);
            vip.setVisibility(View.GONE);
        }else {
            if (level.equals("svip")) {
                level = "svip  (3倍加速成长中)";
                vip.setVisibility(View.GONE);
                svip.setVisibility(View.VISIBLE);
            }
            if (level.equals("vip")) {
                level = "vip  (2倍加速成长中)";
                svip.setVisibility(View.GONE);
                vip.setVisibility(View.VISIBLE);
            }
            if (level.equals("普通用户")) {
                level = "普通用户  (慢速成长中)";
                svip.setVisibility(View.GONE);
                vip.setVisibility(View.GONE);
            }
        }
        return level;
    }

    public void bxx_manager(){
        ImageView level_bxx = (ImageView)findViewById(R.id.level_bxx);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_bxx(String.valueOf(grow_integrals)) == 0){
            level_bxx.setVisibility(View.GONE);
        }else{
            level_bxx.setVisibility(View.VISIBLE);
        }
    }

    public void qxx_manager(){
        ImageView level_qxx1 = (ImageView)findViewById(R.id.level_qxx1);
        ImageView level_qxx2 = (ImageView)findViewById(R.id.level_qxx2);
        ImageView level_qxx3 = (ImageView)findViewById(R.id.level_qxx3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_qxx(String.valueOf(grow_integrals)) == 0){
            level_qxx1.setVisibility(View.GONE);
            level_qxx2.setVisibility(View.GONE);
            level_qxx3.setVisibility(View.GONE);
        }else if (level_show_manager.get_qxx(String.valueOf(grow_integrals)) == 1){
            level_qxx1.setVisibility(View.VISIBLE);
            level_qxx2.setVisibility(View.GONE);
            level_qxx3.setVisibility(View.GONE);
        }else if (level_show_manager.get_qxx(String.valueOf(grow_integrals)) == 2){
            level_qxx1.setVisibility(View.VISIBLE);
            level_qxx2.setVisibility(View.VISIBLE);
            level_qxx3.setVisibility(View.GONE);
        }else{
            level_qxx1.setVisibility(View.GONE);
            level_qxx2.setVisibility(View.GONE);
            level_qxx3.setVisibility(View.GONE);
        }
    }

    public void yl_manager(){
        ImageView level_yl1 = (ImageView)findViewById(R.id.level_yl1);
        ImageView level_yl2 = (ImageView)findViewById(R.id.level_yl2);
        ImageView level_yl3 = (ImageView)findViewById(R.id.level_yl3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_yl(String.valueOf(grow_integrals)) == 0){
            level_yl1.setVisibility(View.GONE);
            level_yl2.setVisibility(View.GONE);
            level_yl3.setVisibility(View.GONE);
        }else if (level_show_manager.get_yl(String.valueOf(grow_integrals)) == 1){
            level_yl1.setVisibility(View.VISIBLE);
            level_yl2.setVisibility(View.GONE);
            level_yl3.setVisibility(View.GONE);
        }else if (level_show_manager.get_yl(String.valueOf(grow_integrals)) == 2){
            level_yl1.setVisibility(View.VISIBLE);
            level_yl2.setVisibility(View.VISIBLE);
            level_yl3.setVisibility(View.GONE);
        }else{
            level_yl1.setVisibility(View.GONE);
            level_yl2.setVisibility(View.GONE);
            level_yl3.setVisibility(View.GONE);
        }
    }

    public void ty_manager(){
        ImageView level_ty1 = (ImageView)findViewById(R.id.level_ty1);
        ImageView level_ty2 = (ImageView)findViewById(R.id.level_ty2);
        ImageView level_ty3 = (ImageView)findViewById(R.id.level_ty3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_ty(String.valueOf(grow_integrals)) == 0){
            level_ty1.setVisibility(View.GONE);
            level_ty2.setVisibility(View.GONE);
            level_ty3.setVisibility(View.GONE);
        }else if (level_show_manager.get_ty(String.valueOf(grow_integrals)) == 1){
            level_ty1.setVisibility(View.VISIBLE);
            level_ty2.setVisibility(View.GONE);
            level_ty3.setVisibility(View.GONE);
        }else if (level_show_manager.get_ty(String.valueOf(grow_integrals)) == 2){
            level_ty1.setVisibility(View.VISIBLE);
            level_ty2.setVisibility(View.VISIBLE);
            level_ty3.setVisibility(View.GONE);
        }else{
            level_ty1.setVisibility(View.GONE);
            level_ty2.setVisibility(View.GONE);
            level_ty3.setVisibility(View.GONE);
        }
    }

    public void hg_manager(){
        ImageView level_hg1 = (ImageView)findViewById(R.id.level_hg1);
        ImageView level_hg2 = (ImageView)findViewById(R.id.level_hg2);
        ImageView level_hg3 = (ImageView)findViewById(R.id.level_hg3);
        //LogUtil.i(String.valueOf(grow_integrals));
        //LogUtil.i(String.valueOf(level_show_manager.get_bxx(String.valueOf(grow_integrals))));
        if (level_show_manager.get_hg(String.valueOf(grow_integrals)) == 0){
            level_hg1.setVisibility(View.GONE);
            level_hg2.setVisibility(View.GONE);
            level_hg3.setVisibility(View.GONE);
        }else if (level_show_manager.get_hg(String.valueOf(grow_integrals)) == 1){
            level_hg1.setVisibility(View.VISIBLE);
            level_hg2.setVisibility(View.GONE);
            level_hg3.setVisibility(View.GONE);
        }else if (level_show_manager.get_hg(String.valueOf(grow_integrals)) == 2){
            level_hg1.setVisibility(View.VISIBLE);
            level_hg2.setVisibility(View.VISIBLE);
            level_hg3.setVisibility(View.GONE);
        }else{
            level_hg1.setVisibility(View.GONE);
            level_hg2.setVisibility(View.GONE);
            level_hg3.setVisibility(View.GONE);
        }
    }

    private void keep_sign_manager() {

        ImageView keep_sign = (ImageView)findViewById(R.id.keep_sign);
        ImageView not_keep_sign = (ImageView)findViewById(R.id.not_keep_sign);

        if (keep_sign_times == null){
            //FToastUtils.init().setRoundRadius(30).show("参数异常");
            keep_sign.setVisibility(View.GONE);
            not_keep_sign.setVisibility(View.GONE);
        }else {
            if (!keep_sign_times.equals("")) {
                if (Integer.parseInt(keep_sign_times) > 7) {
                    keep_sign.setVisibility(View.VISIBLE);
                    not_keep_sign.setVisibility(View.GONE);
                } else {
                    keep_sign.setVisibility(View.GONE);
                    not_keep_sign.setVisibility(View.VISIBLE);
                }
            } else {
                keep_sign.setVisibility(View.GONE);
                not_keep_sign.setVisibility(View.GONE);
            }
        }

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

            mRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mRefreshLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case user_info_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.info_full))) {
                        //showProgress(false);
                        SpUtil rw = new SpUtil();
                        if (rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME).equals("")||rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME) == null){
                            FToastUtils.init().setRoundRadius(30).show("登录失效，即将重新登录！");
                            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                            startActivity(intent);
                            AtyTransitionUtil.exitToRight(UserInfoActivity.this);
                        }else {
                            //SpUtil rw = new SpUtil();
                            new Thread(new check_sign_runtime(h2, UserInfoActivity.this, rw.getValue(UserInfoActivity.this,USER_INFO, LOGIN_USERNAME), rw.getValue(UserInfoActivity.this,USER_INFO, LOGIN_PASSWORD), rw.getValue(UserInfoActivity.this,USER_INFO, USER_KEY))).start();
                        }
                        }else{
                        showProgress(false);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
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
                case check_sign_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.do_not_sign))) {
                        final ImageButton sign = (ImageButton)findViewById(R.id.sign);
                        final ImageButton have_signed = (ImageButton)findViewById(R.id.have_signed);
                        sign.setVisibility(View.VISIBLE);
                        have_signed.setVisibility(View.GONE);
                        showProgress(false);
                    }else if(String.valueOf(msg.obj).equals(getString(R.string.already_sign))){
                        final ImageButton sign = (ImageButton)findViewById(R.id.sign);
                        final ImageButton have_signed = (ImageButton)findViewById(R.id.have_signed);
                        sign.setVisibility(View.GONE);
                        have_signed.setVisibility(View.VISIBLE);
                        showProgress(false);
                    }else{
                        showProgress(false);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }
                    final TextView username_info = (TextView)findViewById(R.id.username_info);
                    final TextView private_name_info = (TextView)findViewById(R.id.private_name_info);
                    final TextView user_birthday_info = (TextView)findViewById(R.id.user_birthday_info);
                    final TextView user_email_info = (TextView)findViewById(R.id.user_email_info);
                    final TextView integrals_data = (TextView)findViewById(R.id.integrals);
                    final TextView prestige_data = (TextView)findViewById(R.id.prestige);
                    final TextView grow_integrals_data = (TextView)findViewById(R.id.grow_integrals);
                    final TextView user_level_info = (TextView)findViewById(R.id.user_level_info);
                    final TextView available_date_info = (TextView)findViewById(R.id.available_date_info);
                    final TextView vip = (TextView)findViewById(R.id.vip);
                    final TextView svip = (TextView)findViewById(R.id.svip);

                    user_level = user_info_manager.get_user_level();
                    integrals = user_info_manager.get_integrals();
                    prestige = user_info_manager.get_prestige();
                    grow_integrals = user_info_manager.get_grow_integrals();
                    keep_sign_times = user_info_manager.get_keep_sign_times();

                    username_info.setText(user_info_manager.get_account());
                    private_name_info.setText(user_info_manager.get_private_name());
                    user_birthday_info.setText(user_info_manager.get_user_birthday());
                    user_email_info.setText(user_info_manager.get_user_email());
                    integrals_data.setText(intChange2Str(user_info_manager.get_integrals()));
                    prestige_data.setText(intChange2Str(user_info_manager.get_prestige()));
                    grow_integrals_data.setText(intChange2Str(user_info_manager.get_grow_integrals()));
                    user_level_info.setText(level_manager(user_info_manager.get_user_level()));
                    available_date_info.setText(user_info_manager.get_user_available_date());
                    vip.setText(date_manager(user_info_manager.get_vip_endtime()));
                    svip.setText(date_manager(user_info_manager.get_svip_endtime()));

                    keep_sign_manager();
                    bxx_manager();
                    qxx_manager();
                    yl_manager();
                    ty_manager();
                    hg_manager();

                    if (manual_refresh) {
                        handler.sendEmptyMessageDelayed(1, 2000);
                        //mRefreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
                        //mRefreshLayout.finishRefreshing();
                        manual_refresh = false;
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
                case manual_sign_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    if (String.valueOf(msg.obj).equals(getString(R.string.login_sign_success))) {

                        user_integrals = sign_manager.get_user_integrals();
                        user_prestige = sign_manager.get_user_prestige();
                        user_grow_integrals = sign_manager.get_user_grow_integrals();
                        user_keep_sign_times = sign_manager.get_user_keep_sign_times();

                        final ImageButton sign = (ImageButton)findViewById(R.id.sign);
                        final ImageButton have_signed = (ImageButton)findViewById(R.id.have_signed);
                        sign.setVisibility(View.GONE);
                        have_signed.setVisibility(View.VISIBLE);
                        show_notice();
                        showProgress(false);
                    }else{
                        showProgress(false);
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                    }
                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    private void show_notice() {
        new IosAlertDialog(UserInfoActivity.this).builder().setCancelable(false).setTitle("签到成功").setMsg("获得如下签到奖励：\n"+user_integrals+"积分\n"+user_prestige+"威望\n"+user_grow_integrals+"成长值\n【连续签到"+user_keep_sign_times+"天】")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showProgress(true);
                        //integrals.setText(intChange2Str(1000));
                        //prestige.setText(intChange2Str(10000));
                        //grow_integrals.setText(intChange2Str(25000));
                        SpUtil rw = new SpUtil();
                        new Thread(new user_info_runtime(h1,UserInfoActivity.this,rw.getValue(UserInfoActivity.this,USER_INFO,LOGIN_USERNAME))).start();
                    }
                }).show();

    }

    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("电子邮箱和生日信息仅支持填写问卷后，由管理员审核后，方可在后台修改，暂不提供用户自行修改通道，感谢您的支持与配合！")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("立即前往填写问卷", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Intent intent = new Intent(UserInfoActivity.this, X5WebGameActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "https://survey.meternity.cn/983834?lang=zh-Hans");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .create(mCurrentDialogStyle).show();
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

}
