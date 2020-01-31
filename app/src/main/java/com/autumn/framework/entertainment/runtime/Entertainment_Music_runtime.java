package com.autumn.framework.entertainment.runtime;

import android.icu.text.NumberFormat;
import android.os.Handler;

import com.autumn.framework.entertainment.http.NetWork_Connecter_Music;
import com.autumn.framework.entertainment.http.NetWork_Connecter_Music_Format;

/**
 * Created by zhang on 2018/3/26.
 */

public class Entertainment_Music_runtime implements Runnable {

    public static final int FINDER_IMAGE_1 = 0;
    public static final int FINDER_IMAGE_2 = 1;
    public static final int FINDER_IMAGE_3 = 2;
    public static final int FINDER_IMAGE_4 = 3;
    public static final int FINDER_IMAGE_5 = 4;
    public static final int FINDER_IMAGE_6 = 5;
    private final String type;
    private final String service;
    private final String content;
    private final int page;
    private Handler handler = null;


    public Entertainment_Music_runtime(Handler h1, String service, String content, String type, int page) {
        this.handler = h1;
        this.service = service;
        this.content = content;
        this.type = type;
        this.page = page;
    }


    @Override
    public void run() {


        String result = "";
        switch (service) {
            case "netease":
                result = NetWork_Connecter_Music.music_connect(service, content, type, page);
                SendMsg(FINDER_IMAGE_1, result);
                break;
            case "tencent":
                result = NetWork_Connecter_Music.music_connect(service, content, type, page);
                SendMsg(FINDER_IMAGE_1, result);
                break;
            case "kugou":
                result = NetWork_Connecter_Music.music_connect(service, content, type, page);
                SendMsg(FINDER_IMAGE_1, result);
                break;
            case "kuwo":
                result = NetWork_Connecter_Music_Format.music_connect(service, content, type, page);
                SendMsg(FINDER_IMAGE_1, result);
                break;
            case "migu":
                //result = NetWork_Connecter_Music.music_connect(service, content, type, page);
                result = NetWork_Connecter_Music_Format.music_connect(service, content, type, page);
                /*if (result.equals("访问成功")) {
                    result = NetWork_Connecter_Music_Format.music_connect(service, content, type, page);
                    //result = NetWork_Connecter_Music.music_connect(service, content, type, page);
                    SendMsg(FINDER_IMAGE_1, result);
                }else*/
                SendMsg(FINDER_IMAGE_1, result);
                break;
            case "baidu":
                result = NetWork_Connecter_Music_Format.music_connect(service, content, type, page);
                SendMsg(FINDER_IMAGE_1, result);
                break;
        }
        //result = NetWork_Connecter_Music.music_connect(service, content, type, page);
        //SendMsg(FINDER_IMAGE_1, result);


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
