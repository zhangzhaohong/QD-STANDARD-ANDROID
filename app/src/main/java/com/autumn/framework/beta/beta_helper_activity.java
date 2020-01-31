package com.autumn.framework.beta;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.statusbar.StatusBarCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.AtyTransitionUtil;

import java.lang.reflect.Field;

public class beta_helper_activity extends BaseActivity {

    private ImageView beta_helper_1;
    private ImageView beta_helper_2;
    private ImageView beta_helper_3;
    private ImageView beta_helper_4;
    private CardView cardView_1;
    private CardView cardView_2;
    private CardView cardView_3;
    private CardView cardView_4;
    private CardView cardView_5;
    private CardView cardView_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_beta_helper);

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

        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(R.drawable.aio_image_default);
        options.error(R.drawable.empty_photo);

        beta_helper_1 = (ImageView)findViewById(R.id.beta_helper_1);
        beta_helper_2 = (ImageView)findViewById(R.id.beta_helper_2);
        beta_helper_3 = (ImageView)findViewById(R.id.beta_helper_3);
        beta_helper_4 = (ImageView)findViewById(R.id.beta_helper_4);
        cardView_1 = (CardView)findViewById(R.id.cardView_1);
        cardView_2 = (CardView)findViewById(R.id.cardView_2);
        cardView_3 = (CardView)findViewById(R.id.cardView_3);
        cardView_4 = (CardView)findViewById(R.id.cardView_4);
        cardView_5 = (CardView)findViewById(R.id.cardView_5);
        cardView_6 = (CardView)findViewById(R.id.cardView_6);
        cardView_1 = (CardView) findViewById(R.id.cardView_1);
        cardView_1.setRadius(30);//设置图片圆角的半径大小
        cardView_1.setCardElevation(8);//设置阴影部分大小
        cardView_1.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        cardView_2 = (CardView) findViewById(R.id.cardView_2);
        cardView_2.setRadius(30);//设置图片圆角的半径大小
        cardView_2.setCardElevation(8);//设置阴影部分大小
        cardView_2.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        cardView_3 = (CardView) findViewById(R.id.cardView_3);
        cardView_3.setRadius(30);//设置图片圆角的半径大小
        cardView_3.setCardElevation(8);//设置阴影部分大小
        cardView_3.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        cardView_4 = (CardView) findViewById(R.id.cardView_4);
        cardView_4.setRadius(30);//设置图片圆角的半径大小
        cardView_4.setCardElevation(8);//设置阴影部分大小
        cardView_4.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        cardView_5 = (CardView) findViewById(R.id.cardView_5);
        cardView_5.setRadius(30);//设置图片圆角的半径大小
        cardView_5.setCardElevation(8);//设置阴影部分大小
        cardView_5.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        cardView_6 = (CardView) findViewById(R.id.cardView_6);
        cardView_6.setRadius(30);//设置图片圆角的半径大小
        cardView_6.setCardElevation(8);//设置阴影部分大小
        cardView_6.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        Glide.with(beta_helper_activity.this)
                .load(R.drawable.beta1)
                .apply(options)
                .into(beta_helper_1);
        Glide.with(beta_helper_activity.this)
                .load(R.drawable.beta2)
                .apply(options)
                .into(beta_helper_2);
        Glide.with(beta_helper_activity.this)
                .load(R.drawable.beta3)
                .apply(options)
                .into(beta_helper_3);
        Glide.with(beta_helper_activity.this)
                .load(R.drawable.beta4)
                .apply(options)
                .into(beta_helper_4);

        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                Intent intent = new Intent(beta_helper_activity.this, beta_application_activity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(beta_helper_activity.this);

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

            Intent intent = new Intent(beta_helper_activity.this, beta_application_activity.class);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(beta_helper_activity.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
