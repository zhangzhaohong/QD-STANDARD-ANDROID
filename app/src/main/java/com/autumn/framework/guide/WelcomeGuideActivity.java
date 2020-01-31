package com.autumn.framework.guide;

/**
 * Created by zhang on 2018/4/1.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.autumn.framework.data.SpUtils;
import com.autumn.framework.update.FindUpdateActivity;
import com.autumn.framework.user.LoginActivity;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.SUtils;
import com.liyi.sutils.utils.ToastUtil;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 欢迎页
 *
 * @author wwj_748
 *
 */
public class WelcomeGuideActivity extends Activity implements OnClickListener {

    private static final String GUIDE_INFO = "Guide_info";
    private static final String IS_NOT_FIRST = "Is_not_first";

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;

    // 引导页图片资源
    private static final int[] pics = { R.layout.guide_view1,
            R.layout.guide_view2, R.layout.guide_view3, R.layout.guide_view4 };

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    private boolean custom_background = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        //内存泄漏
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        setContentView(R.layout.activity_guide);

        //隐藏状态栏和底部虚拟按键
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }

        //SUtils.initialize(getApplication());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ToastUtil.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        views = new ArrayList<View>();

        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            options.placeholder(R.drawable.aio_image_default);
            options.error(R.drawable.empty_photo);

            if (i == 0){
                ImageView guide_image_1 = (ImageView)view.findViewById(R.id.guide_image);
                //guide_image_1.setImageBitmap(BitmapUtil.getImage(BitmapUtil.ReadBitmapById(WelcomeGuideActivity.this,R.drawable.welcome_1)));
                Glide.with(this).load(R.drawable.welcome_1).apply(options).into(guide_image_1);
            }else if(i == 1){
                ImageView guide_image_2 = (ImageView)view.findViewById(R.id.guide_image);
                //guide_image_2.setImageBitmap(BitmapUtil.getImage(BitmapUtil.ReadBitmapById(WelcomeGuideActivity.this,R.drawable.welcome_2)));
                Glide.with(this).load(R.drawable.welcome_2).apply(options).into(guide_image_2);
            }else if(i == 2){
                ImageView guide_image_3 = (ImageView)view.findViewById(R.id.guide_image);
                //guide_image_3.setImageBitmap(BitmapUtil.getImage(BitmapUtil.ReadBitmapById(WelcomeGuideActivity.this,R.drawable.welcome_3)));
                Glide.with(this).load(R.drawable.welcome_3).apply(options).into(guide_image_3);
            }else if(i == 3){
                ImageView guide_image_4 = (ImageView)view.findViewById(R.id.guide_image);
                //guide_image_4.setImageBitmap(BitmapUtil.getImage(BitmapUtil.ReadBitmapById(WelcomeGuideActivity.this,R.drawable.welcome_4)));
                Glide.with(this).load(R.drawable.welcome_4).apply(options).into(guide_image_4);
            }

            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_login);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }

            views.add(view);

        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());
        //vp.setOffscreenPageLimit(0);
        //vp.setOffscreenPageLimit(1);
        //vp.setOffscreenPageLimit(2);
        vp.setOffscreenPageLimit(3);

        initDots();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        //SpUtils.putBoolean(WelcomeGuideActivity.this, AppConstants.FIRST_OPEN, true);
        //finish();

        if(custom_background) {

            custom_background = false;

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
        finish();

    }

    @Override
    protected void onStop() {
        ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        custom_background = true;
        SpUtils.writeBoolean(WelcomeGuideActivity.this, GUIDE_INFO,IS_NOT_FIRST,true);
        Intent intent = new Intent(WelcomeGuideActivity.this,
                LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class PageChangeListener implements OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置

        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

    }
}