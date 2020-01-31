package com.autumn.reptile.app_interface;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.reptile.R;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_1;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_2;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_3;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_4;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_404;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_5;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_6;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_7;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_8;
import com.autumn.sdk.manager.content.content_manager;
import com.autumn.sdk.runtime.content.content_runtime;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class EntertainmentFragement extends Fragment {

    private static Context context;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    ArrayList fragmentList = new ArrayList<Fragment>();
    String[] temp = {"每日一句","热门音乐推荐","音乐搜索","抖音小视频","看点","趣图","GIF动图","段子"};
    private int tab_number = temp.length;
    private QMUITipDialog tipDialog;
    private int init_status = 0;
    private MPagerAdapter mPagerAdapter;

    public static EntertainmentFragement newInstance(String param1) {
        EntertainmentFragement fragment = new EntertainmentFragement();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public EntertainmentFragement() {

    }

    public static Context getFragementContext() {
        return context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_2, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        //TextView tv = (TextView)view.findViewById(R.id.container);
        //tv.setText(agrs1);

        context = getActivity();

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewpager = (ViewPager) view.findViewById(R.id.viewPager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fragment中嵌套fragment, Manager需要用(getChildFragmentManager())
        mPagerAdapter = new MPagerAdapter(getChildFragmentManager());
        initFragment();
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initFragment() {

        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create(false);

        tipDialog.show();

        init_status = 0;

        fragmentList.clear();

        viewpager.removeAllViews();

        new Thread(new content_runtime(h1, getActivity(), getActivity().getString(R.string.entertainment_key_6), getActivity().getString(R.string.app_build))).start();

        /*for (int i = 0; i < tab_number; i++) {
            switch (i){
                case 0:
                    fragmentList.add(new Entertainment_fragement_1());
                    break;
                case 1:
                    fragmentList.add(new Entertainment_fragement_2());
                    break;
                case 2:
                    fragmentList.add(new Entertainment_fragement_3());
                    break;
                case 3:
                    fragmentList.add(new Entertainment_fragement_4());
                    break;
                case 4:
                    fragmentList.add(new Entertainment_fragement_5());
                    break;
            }
        }*/
    }


    class MPagerAdapter extends FragmentPagerAdapter {


        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return temp.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        //返回tablayout的标题文字;  @Override
        public CharSequence getPageTitle(int position) {
            return temp[position];
        }
    }

    Handler h1=new Handler()
    {

        private String content = null;

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case content_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    //tipDialog.dismiss();

                    if (EntertainmentFragement.getFragementContext() == null){
                        //this.removeCallbacksAndMessages(null);
                        //break;
                    }else {

                        if (String.valueOf(msg.obj).equals(EntertainmentFragement.getFragementContext().getString(R.string.notice_new_version))) {

                            switch (init_status) {
                                case 0:
                                    content = content_manager.get_app_content();
                                    if (content != null) Host_manager.setDailyHost(content);
                                    fragmentList.add(new Entertainment_fragement_6());
                                    break;
                                case 1:
                                    content = content_manager.get_app_content();
                                    if (content != null) Host_manager.setHotMusicHost(content);
                                    fragmentList.add(new Entertainment_fragement_7());
                                    break;
                                case 2:
                                    content = content_manager.get_app_content();
                                    if (content != null) Host_manager.setMusicHost(content);
                                    fragmentList.add(new Entertainment_fragement_5());
                                    break;
                                case 3:
                                    content = content_manager.get_app_content();
                                    if (content != null) Host_manager.setShortVideoHost(content);
                                    fragmentList.add(new Entertainment_fragement_8());
                                    break;
                                case 4:
                                    fragmentList.add(new Entertainment_fragement_1());
                                    break;
                                case 5:
                                    fragmentList.add(new Entertainment_fragement_2());
                                    break;
                                case 6:
                                    fragmentList.add(new Entertainment_fragement_3());
                                    break;
                                case 7:
                                    fragmentList.add(new Entertainment_fragement_4());
                                    break;
                            }

                            //LogUtil.i(software_version);
                        } else if (String.valueOf(msg.obj).equals(EntertainmentFragement.getFragementContext().getString(R.string.notice_new_version_not_exist))) {
                            fragmentList.add(new Entertainment_fragement_404());
                            //FToastUtils.init().setRoundRadius(30).show("加载服务条款失败，请检查APP是否已经过期！");
                        } else {
                            fragmentList.add(new Entertainment_fragement_404());
                            //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        }

                        init_status++;
                        if (init_status < tab_number) {
                            switch (init_status) {
                                case 0:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_6), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 1:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_7), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 2:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_5), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 3:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_8), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 4:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_1), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 5:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_2), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 6:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_3), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                                case 7:
                                    new Thread(new content_runtime(h1, EntertainmentFragement.getFragementContext(), EntertainmentFragement.getFragementContext().getString(R.string.entertainment_key_4), EntertainmentFragement.getFragementContext().getString(R.string.app_build))).start();
                                    break;
                            }
                        } else {
                            tipDialog.dismiss();
                            init_status = 0;
                            tabLayout.setupWithViewPager(viewpager);
                            viewpager.setAdapter(mPagerAdapter);
                        }

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

}
