package com.autumn.reptile.entertainment_fragement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.X5WebView.ui.X5WebGameActivity;
import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.entertainment.activity.Music_list_activity;
import com.autumn.framework.entertainment.adapter.PaletteGridAdapter_7;
import com.autumn.framework.entertainment.manager.Data_manager_7;
import com.autumn.framework.entertainment.manager.Music_player_manager;
import com.autumn.framework.entertainment.runtime.Entertainment_Hot_Music_runtime;
import com.autumn.framework.entertainment.runtime.Entertainment_Music_Banner_runtime;
import com.autumn.reptile.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import cn.hotapk.fastandrutils.utils.FToastUtils;

import static android.app.Activity.RESULT_OK;

public class Entertainment_fragement_7 extends Fragment implements OnBannerListener {
    public static final String ARGS_PAGE = "args_page";
    private static RelativeLayout Hot_list;
    private static boolean Analysis_failed_status_HotMusic = false;
    private static boolean Analysis_status_HotMusic = false;
    private static boolean Analysis_status = false;
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private String[] url;
    private String[] pic_url;
    private CardView cardView_1;
    private static int page = 0;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRvPalette;
    private LinearLayout hot_title_music;
    private NestedScrollView hot_music_page;
    private boolean onRefresh = false;
    private String[] type;

    public static void Analysis_ok() {

        Analysis_status = true;

    }

    public static void Analysis_failed(){

        Analysis_status = false;

    }

    public static void Analysis_ok_HotMusic(int page_num) {

        page = page_num;
        Analysis_status_HotMusic = true;

    }

    public static void Analysis_failed_HotMusic(int page_num) {
        page = page_num;
        Analysis_failed_status_HotMusic = true;
    }

    public static Entertainment_fragement_7 newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        Entertainment_fragement_7 fragment = new Entertainment_fragement_7();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_entertainment_7,container,false);

        //banner区域
        banner = (Banner) view.findViewById(R.id.banner);
        cardView_1 = (CardView)view.findViewById(R.id.cardView_1);
        mRefreshLayout = (SmartRefreshLayout)view.findViewById(R.id.pull_to_refresh_view);
        hot_title_music = (LinearLayout)view.findViewById(R.id.hot_title_music);
        Hot_list = (RelativeLayout)view.findViewById(R.id.Hot_list);
        hot_music_page = (NestedScrollView)view.findViewById(R.id.hot_music_page);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Hot_list.setNestedScrollingEnabled(false);
        }*/
        //解决卡顿
        //mRvPalette.setHasFixedSize(true);
        //mRvPalette.setNestedScrollingEnabled(false);

        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setEnableRefresh(false);
        if (page < 1){
            mRefreshLayout.setEnableLoadmore(false);
        }else {
            mRefreshLayout.setEnableLoadmore(true);
        }
        //mRefreshLayout.autoRefresh();
        //设置 Footer 为 球脉冲
        //mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRvPalette = (RecyclerView)view.findViewById(R.id.rvPalette);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);

        mRvPalette.setLayoutManager(gridLayoutManager);
        mRvPalette.setHasFixedSize(true);
        mRvPalette.setNestedScrollingEnabled(false);
        //mRvPalette.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRvPalette.setVisibility(View.GONE);
        //PaletteGridAdapter_5.initData();
        mRvPalette.setAdapter(new PaletteGridAdapter_7());

        mRefreshLayout.setNestedScrollingEnabled(false);

        hot_music_page.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LogUtil.d("Entainment7[PageINFO]" +
                        "\nHeight:" + v.getMeasuredHeight() + "\n" +
                        "Width:" + v.getMeasuredWidth() + "\n" +
                        "ScrollX:" + scrollX + "\n" +
                        "ScrollY:" + scrollY + "\n" +
                        "oldScrollX:" + oldScrollX + "\n" +
                        "oldScrollY:" + oldScrollY);
                if (scrollY > oldScrollY) {
                    // 向下滑动
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                    banner.requestFocus();
                    banner.requestDisallowInterceptTouchEvent(true);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //FToastUtils.init().show("bottom");
                    // 上拉刷新实现
                    if (page >= 1 && !onRefresh){
                        mRefreshLayout.autoLoadmore(0);
                        //onRefresh = true;
                    }else {
                        FToastUtils.init().show("正在刷新中，请稍候重试");
                    }
                }
            }
        });

        if (NetworkChecker.isVpnUsed()){

            //vpn
            FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
            Intent intent = VpnService.prepare(getActivity());
            if (intent != null) {
                startActivityForResult(intent, 0);
            } else {
                onActivityResult(0, RESULT_OK, null);
            }

            for (int i = 0; i <= 10; i ++){
                if (NetworkChecker.isVpnUsed()){
                    i ++;
                }else {

                    new Thread(new Entertainment_Music_Banner_runtime(h1, "tencent")).start();
                    onRefresh = true;
                    page = 1;
                    new Thread(new Entertainment_Hot_Music_runtime(h2, "tencent", "26", page)).start();

                    break;
                }
                if (i == 10){
                    FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                    break;
                }
            }

        }else if (NetworkChecker.isWifiProxy(getActivity())){
            FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
        }else {

            new Thread(new Entertainment_Music_Banner_runtime(h1, "tencent")).start();
            onRefresh = true;
            page = 1;
            new Thread(new Entertainment_Hot_Music_runtime(h2, "tencent", "26", page)).start();

        }

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (NetworkChecker.isVpnUsed()){

                    //vpn
                    FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
                    Intent intent = VpnService.prepare(getActivity());
                    if (intent != null) {
                        startActivityForResult(intent, 0);
                    } else {
                        onActivityResult(0, RESULT_OK, null);
                    }

                    for (int i = 0; i <= 10; i ++){
                        if (NetworkChecker.isVpnUsed()){
                            i ++;
                        }else {

                            //new Thread(new Entertainment_Music_Banner_runtime(h1, "tencent")).start();
                            //page = 1;
                            onRefresh = true;
                            page += 1;
                            new Thread(new Entertainment_Hot_Music_runtime(h2, "tencent", "26", page)).start();

                            break;
                        }
                        if (i == 10){
                            FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                            break;
                        }
                    }

                }else if (NetworkChecker.isWifiProxy(getActivity())){
                    FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                }else {

                    //new Thread(new Entertainment_Music_Banner_runtime(h1, "tencent")).start();
                    //page = 1;
                    onRefresh = true;
                    page += 1;
                    new Thread(new Entertainment_Hot_Music_runtime(h2, "tencent", "26", page)).start();

                }

            }
        });

        //initView();

        return view;
    }

    private void initData() {

        //list_path.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1004138305,3574267912&fm=26&gp=0.jpg");
        //list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=269417505,1119922751&fm=26&gp=0.jpg");
        //list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1214742538,3795549008&fm=26&gp=0.jpg");
        //list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=644298988,1798734310&fm=26&gp=0.jpg");
        //list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1565029830,4214558184&fm=26&gp=0.jpg");
        //list_path.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=400872882,2453478033&fm=26&gp=0.jpg");
        //list_path.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3167642956,1020780379&fm=26&gp=0.jpg");
        for (int i = 0; i < pic_url.length; i++){
            list_path.add(pic_url[i]);
        }
        for (int i = 0; i < list_path.size(); i++){
            list_title.add("每日推荐");
        }
        //list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530616822341&di=127291d0d27161f38da3da5a00ac14de&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01d881579dc3620000018c1b430c4b.JPG%403000w_1l_2o_100sh.jpg");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        list_path = new ArrayList<>();
        list_title = new ArrayList<>();
        initData();
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new MyLoader());
        banner.setBannerAnimation(Transformer.ScaleInOut);
        banner.setBannerTitles(list_title);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(list_path)
                .setOnBannerListener(this)
                .start();
        banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //LogUtil.d("Entertainment_fragement_7-Banner Touched");
                //banner.requestDisallowInterceptTouchEvent(true);
                int ea=event.getAction();
//                   Log.i("TAG", "Touch:"+ea);
                switch(ea)
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        Log.v("-onTouchListener-","------ACTION_DOWN-----");
                        break;
//                       return true;
                    }
                    case MotionEvent.ACTION_MOVE:
                    {
                        Log.v("-onTouchListener-","------ACTION_MOVE-----");
                        break;
//                       return true;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        Log.v("-onTouchListener-","------ACTION_UP-----");
                        break;
//                       return true;
                    }

                }
                return false;
            }
        });

    }

    /**
     * 轮播监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        if (url[position].startsWith("https://y.qq.com/")){
            Intent intent = new Intent(getActivity(), X5WebGameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("key", url[position]);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if (type == null){
            FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候再试");
        }else if (type.length == 0){
            FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候再试");
        }else if (type[position].equals("10002")){
            Intent intent = new Intent(getActivity(), Music_list_activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id",url[position]);
            intent.putExtras(bundle);
            startActivity(intent);
            AtyTransitionUtil.exitToRight(getActivity());
        }else {
            FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候再试");
        }
        //FToastUtils.init().setRoundRadius(30).show(url[position]);
        //FToastUtils.init().setRoundRadius(30).show( "你点了第" + (position + 1) + "张轮播图");
    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            options.placeholder(R.drawable.aio_image_default);
            options.error(R.drawable.empty_photo);
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .apply(options)
                    .into(imageView);
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
                case Entertainment_Music_Banner_runtime.FINDER_IMAGE_1:
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
                            banner.setVisibility(View.VISIBLE);
                            banner.requestFocus();
                            banner.requestDisallowInterceptTouchEvent(true);
                            //cardView_1.setVisibility(View.VISIBLE);
                            //banner.setVisibility(View.VISIBLE);
                            type = Data_manager_7.getType_all();
                            url = Data_manager_7.getUrl_all();
                            pic_url = Data_manager_7.getPicUrl_all();

                            initView();

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

                        //访问失败
                        banner.setVisibility(View.GONE);
                        banner.clearFocus();
                        //cardView_1.setVisibility(View.GONE);
                        //banner.setVisibility(View.GONE);
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
                case Entertainment_Hot_Music_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals("访问成功")){


                        Music_player_manager.setHotMusicService(String.valueOf(1));
                        //Music_player_manager.setMusicService(String.valueOf(1));

                        mRvPalette.setVisibility(View.VISIBLE);
                        mRefreshLayout.setEnableLoadmore(true);
                        //mRvPalette.setAdapter(new PaletteGridAdapter_5());
                        //访问成功
                        while (Analysis_status_HotMusic == true){
                            Analysis_status_HotMusic = false;
                            hot_title_music.setVisibility(View.VISIBLE);
                            if (page <= 1)
                                PaletteGridAdapter_7.initData();
                            PaletteGridAdapter_7.refreshData();
                            mRvPalette.getAdapter().notifyDataSetChanged();
                            //解决卡顿
                            //mRvPalette.setHasFixedSize(true);
                            //mRvPalette.setNestedScrollingEnabled(false);
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
                            mRefreshLayout.finishRefresh();
                            mRefreshLayout.finishLoadmore();
                            onRefresh = false;
                            break;
                        }

                        while (Analysis_failed_status_HotMusic == true){
                            Analysis_failed_status_HotMusic = false;
                            //mRvPalette.getAdapter().notifyDataSetChanged();
                            FToastUtils.init().setRoundRadius(30).show("暂无更多数据");
                            mRefreshLayout.finishRefresh();
                            mRefreshLayout.finishLoadmore();
                            onRefresh = false;
                            break;
                        }

                    }else{

                        //访问失败
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        hot_title_music.setVisibility(View.GONE);
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();

                        onRefresh = false;

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

}
