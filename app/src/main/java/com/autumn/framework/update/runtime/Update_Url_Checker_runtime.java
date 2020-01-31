package com.autumn.framework.update.runtime;

import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.framework.update.http.Update_URL_Checker;

/**
 * Created by zhang on 2018/3/26.
 */

public class Update_Url_Checker_runtime implements Runnable {

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final String url;
    private Handler handler = null;


    public Update_Url_Checker_runtime(Handler h1, String url) {
        this.handler = h1;
        this.url = url;
    }


    @Override
    public void run() {


        String result = "";
        result =Update_URL_Checker.check_avaliable(url);
        /*if (result.equals("访问成功"))
            result = Update_URL_Checker.check_url_avaliable();*/
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

