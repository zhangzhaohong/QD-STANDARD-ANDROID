package com.autumn.reptile.entertainment_fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autumn.framework.View.ClearEditText;
import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.entertainment.adapter.PaletteGridAdapter_5;
import com.autumn.framework.entertainment.manager.Music_player_manager;
import com.autumn.framework.entertainment.runtime.Entertainment_Music_runtime;
import com.autumn.reptile.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.hotapk.fastandrutils.utils.FToastUtils;

import static android.app.Activity.RESULT_OK;

public class Entertainment_fragement_5 extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private static boolean Analysis_status = false;
    private static boolean Analysis_failed_status = false;
    private int mPage;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRvPalette;
    private static int page = 0;
    private ClearEditText music_search_name;
    private Button music_search;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String choose_service = "0";

    public static Entertainment_fragement_5 newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        Entertainment_fragement_5 fragment = new Entertainment_fragement_5();
        fragment.setArguments(args);
        return fragment;
    }

    public static void Analysis_ok(int page_num) {

        page = page_num;
        Analysis_status = true;

    }

    public static void Analysis_failed(int page_num) {
        page = page_num;
        Analysis_failed_status = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_entertainment_5,container,false);
        //TextView textView = (TextView) view.findViewById(R.id.textView);
        //textView.setText("第"+mPage+"页");
        mRefreshLayout = (SmartRefreshLayout)view.findViewById(R.id.pull_to_refresh_view);
        music_search = (Button)view.findViewById(R.id.music_search);
        music_search_name = (ClearEditText)view.findViewById(R.id.music_search_name);
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
        mRvPalette.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRvPalette.setVisibility(View.GONE);
        //PaletteGridAdapter_5.initData();
        mRvPalette.setAdapter(new PaletteGridAdapter_5());
        music_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //page = 0;
                if (music_search_name.getText().toString().equals("")){
                    FToastUtils.init().setRoundRadius(30).show("数据不能为空！");
                }else {
                    page = 1;
                    showSingleChoiceDialog(music_search_name.getText().toString());
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //new Thread(new Entertainment_runtime(h1,"image","10")).start();
            }
        });
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

                            page += 1;
                            String content = music_search_name.getText().toString();
                            switch (choose_service){
                                case "0":
                                    //choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "netease", content, "song", page)).start();
                                    break;
                                case "1":
                                    //choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "tencent", content, "song", page)).start();
                                    break;
                                case "2":
                                    //choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "kugou", content, "song", page)).start();
                                    break;
                                case "3":
                                    new Thread(new Entertainment_Music_runtime(h1, "kuwo", content, "song", page)).start();
                                    break;
                                case "4":
                                    new Thread(new Entertainment_Music_runtime(h1, "migu", content, "song", page)).start();
                                    break;
                                case "5":
                                    new Thread(new Entertainment_Music_runtime(h1, "baidu", content, "song", page)).start();
                                    break;
                            }

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
                    page += 1;
                    String content = music_search_name.getText().toString();
                    switch (choose_service){
                        case "0":
                            //choose_service = String.valueOf(which);
                            new Thread(new Entertainment_Music_runtime(h1, "netease", content, "song", page)).start();
                            break;
                        case "1":
                            //choose_service = String.valueOf(which);
                            new Thread(new Entertainment_Music_runtime(h1, "tencent", content, "song", page)).start();
                            break;
                        case "2":
                            //choose_service = String.valueOf(which);
                            new Thread(new Entertainment_Music_runtime(h1, "kugou", content, "song", page)).start();
                            break;
                        case "3":
                            new Thread(new Entertainment_Music_runtime(h1, "kuwo", content, "song", page)).start();
                            break;
                        case "4":
                            new Thread(new Entertainment_Music_runtime(h1, "migu", content, "song", page)).start();
                            break;
                        case "5":
                            new Thread(new Entertainment_Music_runtime(h1, "baidu", content, "song", page)).start();
                            break;
                    }
                }
                //new Thread(new Entertainment_runtime(h1,"image","10")).start();
            }
        });
        return view;
    }

    private void showSingleChoiceDialog(String content) {
        final String[] items = new String[]{"网易云音乐", "qq音乐", "酷狗音乐", "酷我音乐", "咪咕音乐", "百度音乐"};
        final int checkedIndex = 0;
        page = 1;
        choose_service = String.valueOf(checkedIndex);
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PaletteGridAdapter_5.initData();
                        mRvPalette.getAdapter().notifyDataSetChanged();
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
                                    switch (String.valueOf(which)){
                                        case "0":
                                            choose_service = String.valueOf(which);
                                            new Thread(new Entertainment_Music_runtime(h1, "netease", content, "song", page)).start();
                                            break;
                                        case "1":
                                            choose_service = String.valueOf(which);
                                            new Thread(new Entertainment_Music_runtime(h1, "tencent", content, "song", page)).start();
                                            break;
                                        case "2":
                                            choose_service = String.valueOf(which);
                                            new Thread(new Entertainment_Music_runtime(h1, "kugou", content, "song", page)).start();
                                            break;
                                        case "3":
                                            choose_service = String.valueOf(which);
                                            new Thread(new Entertainment_Music_runtime(h1, "kuwo", content, "song", page)).start();
                                            break;
                                        case "4":
                                            choose_service = String.valueOf(which);
                                            new Thread(new Entertainment_Music_runtime(h1, "migu", content, "song", page)).start();
                                            break;
                                        case "5":
                                            choose_service = String.valueOf(which);
                                            new Thread(new Entertainment_Music_runtime(h1, "baidu", content, "song", page)).start();
                                            break;
                                    }
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
                            switch (String.valueOf(which)){
                                case "0":
                                    choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "netease", content, "song", page)).start();
                                    break;
                                case "1":
                                    choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "tencent", content, "song", page)).start();
                                    break;
                                case "2":
                                    choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "kugou", content, "song", page)).start();
                                    break;
                                case "3":
                                    choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "kuwo", content, "song", page)).start();
                                    break;
                                case "4":
                                    choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "migu", content, "song", page)).start();
                                    break;
                                case "5":
                                    choose_service = String.valueOf(which);
                                    new Thread(new Entertainment_Music_runtime(h1, "baidu", content, "song", page)).start();
                                    break;
                            }
                        }
                        //Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        //PaletteGridAdapter_5.initData();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case Entertainment_Music_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals("访问成功")){

                        //Music_player_manager.setSearchMusicService(choose_service);
                        Music_player_manager.setMusicService(choose_service);

                        mRvPalette.setVisibility(View.VISIBLE);
                        mRefreshLayout.setEnableLoadmore(true);
                        //mRvPalette.setAdapter(new PaletteGridAdapter_5());
                        //访问成功
                        while (Analysis_status == true){
                            Analysis_status = false;
                            if (page <= 1)
                                PaletteGridAdapter_5.initData();
                            PaletteGridAdapter_5.refreshData();
                            mRvPalette.getAdapter().notifyDataSetChanged();
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
                        }

                        while (Analysis_failed_status == true){
                            Analysis_failed_status = false;
                            //mRvPalette.getAdapter().notifyDataSetChanged();
                            FToastUtils.init().setRoundRadius(30).show("暂无更多数据");
                            mRefreshLayout.finishRefresh();
                            mRefreshLayout.finishLoadmore();
                        }

                    }else{

                        //访问失败
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };
}
