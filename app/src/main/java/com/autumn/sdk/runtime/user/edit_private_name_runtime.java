package com.autumn.sdk.runtime.user;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.sdk.api.edit_private_name;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.SpUtil;
import com.autumn.sdk.data.key_manager;

/**
 * Created by zhang on 2018/4/5.
 */

public class edit_private_name_runtime implements Runnable {

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final Context context;
    private final String account;
    private final String password;
    private final String new_private_name;
    private Handler handler = null;

    String key = key_manager.key;

    SpUtil rw = new SpUtil();
    private static final String USER_INFO = "User_info";
    private static final String USER_KEY = "User_key";
    private static final String LOGIN_USERNAME = "Login_username";
    private static final String LOGIN_PASSWORD = "Login_password";


    public edit_private_name_runtime(Handler h1, Context context, String new_private_name) {
        this.handler = h1;
        this.context = context;
        this.account = DesUtil.encrypt(rw.getValue(context,USER_INFO,LOGIN_USERNAME),key);
        this.password = DesUtil.encrypt(DesUtil.encrypt(rw.getValue(context,USER_INFO,LOGIN_PASSWORD),key),key);
        this.new_private_name = DesUtil.encrypt(new_private_name,key);

    }


    @Override
    public void run() {

        //LogUtil.i("account="+account+"&password="+password);
        String result = null;
        result = edit_private_name.edit_private_name(context,account,password,new_private_name);
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
