package com.autumn.sdk.runtime.update;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.reptile.R;
import com.autumn.sdk.api.check_update_version;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.SpUtil;
import com.autumn.sdk.data.key_manager;

/**
 * Created by zhang on 2018/4/5.
 */

public class check_update_version_login_runtime implements Runnable {

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final Context context;
    private final String app_key;
    private final String user_key;
    private Handler handler = null;

    String key = key_manager.key;

    SpUtil rw = new SpUtil();
    private static final String USER_INFO = "User_info";
    private static final String USER_KEY = "User_key";

    public check_update_version_login_runtime(Handler h1, Context context, String user_key) {
        this.handler = h1;
        this.context = context;
        this.app_key =  DesUtil.encrypt(DesUtil.encrypt(context.getString(R.string.app_key),key),key);
        this.user_key = DesUtil.encrypt(DesUtil.encrypt(user_key,key),key);

        //LogUtil.i(context.getString(R.string.app_key)+"\n"+context.getString(R.string.app_build));

        //LogUtil.i(app_key+"\n"+version);
    }


    @Override
    public void run() {

        //LogUtil.i("account="+account+"&password="+password);
        String result = null;
        result = check_update_version.check_update_version(context, app_key, user_key);
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

