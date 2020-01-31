package com.autumn.sdk.runtime.user;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.sdk.api.user_available_date;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.key_manager;

/**
 * Created by zhang on 2018/4/6.
 */

public class get_available_date_runtime implements Runnable{

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final Context context;
    private final String account;
    private Handler handler = null;

    String key = key_manager.key;


    public get_available_date_runtime(Handler h1, Context context,String username) {
        this.handler = h1;
        this.context = context;
        this.account = DesUtil.encrypt(username,key);
        //LogUtil.i(account);
    }


    @Override
    public void run() {


        String result = null;
        result = user_available_date.get_available_date(context,account);
        //LogUtil.i(result);
        SendMsg(FINDER_IMAGE_1, result);


    }

    public int percent(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(1);
            str = nf.format(p3);
            String ssw = str.replaceAll("([0-9]+)\\.[^\\.]*", "$1");
            return Integer.parseInt(ssw);
        }
        return 0;
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private void SendMsg(int what, Object obj) {
        handler.obtainMessage(what, obj).sendToTarget();
    }

}