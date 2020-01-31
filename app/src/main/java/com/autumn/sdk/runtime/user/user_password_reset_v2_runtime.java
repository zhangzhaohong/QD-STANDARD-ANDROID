package com.autumn.sdk.runtime.user;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.sdk.api.user_password_reset;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.key_manager;

/**
 * Created by zhang on 2018/4/6.
 */

public class user_password_reset_v2_runtime implements Runnable{

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final Context context;
    private final String account;
    private final String user_birthday;
    private final String user_email;
    private final String password;
    private Handler handler = null;

    String key = key_manager.key;

    public user_password_reset_v2_runtime(Handler h1, Context context, String account, String user_birthday, String user_email,String password) {
        this.handler = h1;
        this.context = context;
        this.account = DesUtil.encrypt(account,key);
        this.user_birthday = DesUtil.encrypt(user_birthday,key);
        this.user_email = DesUtil.encrypt(user_email,key);
        this.password = DesUtil.encrypt(DesUtil.encrypt(password,key),key);
    }


    @Override
    public void run() {

        //LogUtil.i(account);
        //LogUtil.i(user_birthday);
        //LogUtil.i(user_email);
        //LogUtil.i(password);

        //LogUtil.i("account="+account+"&password="+password+"&private_name="+private_name+"&user_birthday="+user_birthday+"&user_email="+user_email+"&imei="+user_imei);
        String result = null;
        result = user_password_reset.password_reset_v2(context,account,user_birthday,user_email,password);
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
