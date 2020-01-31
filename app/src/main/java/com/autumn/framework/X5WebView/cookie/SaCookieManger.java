package com.autumn.framework.X5WebView.cookie;

import android.content.Context;

import com.autumn.framework.X5WebView.tools.LogTAG;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by tzw on 2018/3/12.
 *
 */

public class SaCookieManger implements CookieJar {

    private static final String TAG = LogTAG.cookie;
    private static Context mContext;

    public SaCookieManger(Context context) {
        mContext = context;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        SaasCookieManager.loadCookie(cookies,url.host());
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return new ArrayList<>();
    }
}
