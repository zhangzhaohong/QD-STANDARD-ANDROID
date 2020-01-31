package com.autumn.framework.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.entertainment.adapter.PaletteGridAdapter_Music_List;
import com.autumn.framework.entertainment.runtime.Entertainment_Music_List_runtime;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.statusbar.StatusBarCompat;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Field;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class Music_list_activity extends BaseActivity {

    private static boolean Analysis_status = false;
    private static Context context;
    private RecyclerView mRvPalette;
    private String url = "";
    private Bundle extras;
    private QMUITipDialog tipDialog;

    public static Context getAppContext() {
        if (context == null)
            new Music_list_activity();
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_music_list);

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

        context = this;

        extras = getIntent().getExtras();
        //id = extras.getInt("id");
        url = extras.getString("id");

        showProgress(true);

        mRvPalette = (RecyclerView)findViewById(R.id.rvPalette);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Music_list_activity.this, 1);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);

        mRvPalette.setLayoutManager(gridLayoutManager);
        mRvPalette.setHasFixedSize(true);
        mRvPalette.setNestedScrollingEnabled(false);
        //mRvPalette.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRvPalette.setVisibility(View.GONE);
        //PaletteGridAdapter_5.initData();
        mRvPalette.setAdapter(new PaletteGridAdapter_Music_List());

        if (NetworkChecker.isVpnUsed()){

            //vpn
            FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
            Intent intent = VpnService.prepare(Music_list_activity.this);
            if (intent != null) {
                startActivityForResult(intent, 0);
            } else {
                onActivityResult(0, RESULT_OK, null);
            }

            for (int i = 0; i <= 10; i ++){
                if (NetworkChecker.isVpnUsed()){
                    i ++;
                }else {

                    new Thread(new Entertainment_Music_List_runtime(h1, "tencent", url)).start();
                    //new Thread(new Entertainment_Music_Banner_runtime(h1, "tencent")).start();
                    //onRefresh = true;
                    //page = 1;
                    //new Thread(new Entertainment_Hot_Music_runtime(h2, "tencent", "26", page)).start();

                    break;
                }
                if (i == 10){
                    FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                    break;
                }
            }

        }else if (NetworkChecker.isWifiProxy(Music_list_activity.this)){
            FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
        }else {

            //new Thread(new Entertainment_Music_Banner_runtime(h1, "tencent")).start();
            //onRefresh = true;
            //page = 1;
            //new Thread(new Entertainment_Hot_Music_runtime(h2, "tencent", "26", page)).start();

            new Thread(new Entertainment_Music_List_runtime(h1, "tencent", url)).start();

        }

        ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

                Music_list_activity.super.onBackPressed();

                //Intent intent = new Intent(Music_list_activity.this, beta_application_activity.class);
                //startActivity(intent);
                //AtyTransitionUtil.exitToRight(Music_list_activity.this);

            }
        });

    }

    public static void Analysis_ok() {

        Analysis_status = true;

    }

    public static void Analysis_failed(){

        Analysis_status = false;

    }

    private void showProgress(Boolean status){
        if (status){
            tipDialog = new QMUITipDialog.Builder(Music_list_activity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在加载")
                    .create(false);
            tipDialog.show();
            //player_play.setEnabled(false);
            //player_stop.setEnabled(false);
            //FToastUtils.init().setRoundRadius(30).show("正在加载音乐，请稍候！");
        }else {
            if (tipDialog.isShowing())
                tipDialog.dismiss();
            //player_play.setEnabled(true);
            //player_stop.setEnabled(true);
            //FToastUtils.init().setRoundRadius(30).show("加载完成！");
        }
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

            super.onBackPressed();
            //Intent intent = new Intent(Music_list_activity.this, beta_application_activity.class);
            //startActivity(intent);
            //AtyTransitionUtil.exitToRight(Music_list_activity.this);

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case Entertainment_Music_List_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals("访问成功")){

                        //Music_player_manager.setMusicService(choose_service);

                        //mRvPalette.setVisibility(View.VISIBLE);
                        //mRefreshLayout.setEnableLoadmore(true);
                        //mRvPalette.setAdapter(new PaletteGridAdapter_5());
                        //访问成功
                        while (Analysis_status == true){
                            Analysis_status = false;
                            //cardView_1.setVisibility(View.VISIBLE);
                            //banner.setVisibility(View.VISIBLE);
                            //type = Data_manager_7.getType_all();
                            //url = Data_manager_7.getUrl_all();
                            //pic_url = Data_manager_7.getPicUrl_all();

                            //initView();

                            mRvPalette.setVisibility(View.VISIBLE);
                            PaletteGridAdapter_Music_List.initData();
                            PaletteGridAdapter_Music_List.refreshData();
                            mRvPalette.getAdapter().notifyDataSetChanged();

                            //if (page <= 1)
                            //    PaletteGridAdapter_5.initData();
                            //PaletteGridAdapter_5.refreshData();
                            //mRvPalette.getAdapter().notifyDataSetChanged();
                            /*if (page >= 1){
                                PaletteGridAdapter_5.refreshData();
                                //mRvPalette.scrollToPosition(page * 10 + 1);
                                mRvPalette.getAdapter().notifyDataSetChanged();
                            }else {
                                PaletteGridAdapter_5.refreshData();
                                mRvPalette.setAdapter(new PaletteGridAdapter_5());
                                mRvPalette.getAdapter().notifyDataSetChanged();
                            }*/
                            //mRvPalette.getAdapter().notifyDataSetChanged();
                            //mRefreshLayout.finishRefresh();
                            //mRefreshLayout.finishLoadmore();
                        }

                    }else{

                        if (String.valueOf(msg.obj).equals("域名返回为NULL") || String.valueOf(msg.obj).equals("域名为空")){
                            FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                            Music_list_activity.super.onBackPressed();
                        }else {
                            //访问失败
                            //cardView_1.setVisibility(View.GONE);
                            //banner.setVisibility(View.GONE);
                            FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                            mRvPalette.setVisibility(View.GONE);
                        }
                    }

                    showProgress(false);

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

}
