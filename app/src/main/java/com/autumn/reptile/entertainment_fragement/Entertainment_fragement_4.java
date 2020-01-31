package com.autumn.reptile.entertainment_fragement;

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

import com.autumn.framework.entertainment.adapter.PaletteGridAdapter_4;
import com.autumn.framework.entertainment.runtime.Entertainment_runtime;
import com.autumn.reptile.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class Entertainment_fragement_4 extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private static boolean Analysis_status = false;
    private int mPage;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRvPalette;

    public static Entertainment_fragement_4 newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        Entertainment_fragement_4 fragment = new Entertainment_fragement_4();
        fragment.setArguments(args);
        return fragment;
    }

    public static void Analysis_ok() {

        Analysis_status = true;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_entertainment_4,container,false);
        //TextView textView = (TextView) view.findViewById(R.id.textView);
        //textView.setText("第"+mPage+"页");
        mRefreshLayout = (SmartRefreshLayout)view.findViewById(R.id.pull_to_refresh_view);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.autoRefresh();
        //设置 Footer 为 球脉冲
        //mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRvPalette = (RecyclerView)view.findViewById(R.id.rvPalette);
        mRvPalette.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRvPalette.setAdapter(new PaletteGridAdapter_4());
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Thread(new Entertainment_runtime(h1,"text","10")).start();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                new Thread(new Entertainment_runtime(h1,"text","10")).start();
            }
        });
        return view;
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case Entertainment_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());



                    if (String.valueOf(msg.obj).equals("访问成功")){

                        //访问成功
                        while (Analysis_status == true){
                            Analysis_status = false;
                            mRvPalette.getAdapter().notifyDataSetChanged();
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
