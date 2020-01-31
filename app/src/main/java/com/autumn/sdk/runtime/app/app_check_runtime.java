package com.autumn.sdk.runtime.app;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.sdk.api.app_check;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.key_manager;

/**
 * Created by zhang on 2018/4/6.
 */

public class app_check_runtime implements Runnable{

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final Context context;
    private final String app_name;
    private final String app_package_name;
    private final String app_key;
    private Handler handler = null;

    String key = key_manager.key;

    public app_check_runtime(Handler h1, Context context, String app_name, String app_package_name, String app_key) {
        this.handler = h1;
        this.context = context;
        this.app_name = DesUtil.encrypt(app_name,key);
        this.app_package_name = DesUtil.encrypt(app_package_name,key);
        this.app_key = DesUtil.encrypt(app_key,key);
    }


    @Override
    public void run() {

        //LogUtil.i("account="+account+"&password="+password+"&private_name="+private_name+"&user_birthday="+user_birthday+"&user_email="+user_email+"&imei="+user_imei);
        String result = null;
        result = app_check.app_key_check(context,app_name,app_package_name,app_key);
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
