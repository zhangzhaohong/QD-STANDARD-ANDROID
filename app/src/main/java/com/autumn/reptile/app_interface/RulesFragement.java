package com.autumn.reptile.app_interface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autumn.reptile.R;
import com.autumn.reptile.rules_fragement.Rules_fragement_1;
import com.autumn.reptile.rules_fragement.Rules_fragement_2;
import com.autumn.reptile.rules_fragement.Rules_fragement_3;

import java.util.ArrayList;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class RulesFragement extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewpager;
    ArrayList fragmentList = new ArrayList<Fragment>();
    String[] temp = {"普通用户","vip用户","svip用户"};
    private int tab_number = 3;
    //private QMUITipDialog tipDialog;
    //private int init_status = 0;
    private MPagerAdapter mPagerAdapter;

    public static RulesFragement newInstance(String param1) {
        RulesFragement fragment = new RulesFragement();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public RulesFragement() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_3, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        //TextView tv = (TextView)view.findViewById(R.id.container);
        //tv.setText(agrs1);

        //context = getActivity();

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

        //init_status = 0;

        fragmentList.clear();

        viewpager.removeAllViews();

        //new Thread(new content_runtime(h1, getActivity(), getActivity().getString(R.string.entertainment_key_6), getActivity().getString(R.string.app_build))).start();

        for (int i = 0; i < tab_number; i++) {
            switch (i){
                case 0:
                    fragmentList.add(new Rules_fragement_1());
                    break;
                case 1:
                    fragmentList.add(new Rules_fragement_2());
                    break;
                case 2:
                    fragmentList.add(new Rules_fragement_3());
                    break;
            }
        }

        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(mPagerAdapter);

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

}
