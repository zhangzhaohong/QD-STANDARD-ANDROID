package com.autumn.framework.data;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Donview on 2018/7/18.
 */

public class HttpUtil {
    public final static int CONNECT_TIMEOUT = 3;
    public final static int READ_TIMEOUT = 3;
    public final static int WRITE_TIMEOUT = 3;
    public static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();
    /**
     * Get方法，获取数据
     * 携带seesionKey、无传递数据
     */
    public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback,String sessionKey) {
        Request request = new Request.Builder()
                .header("Grpc-Metadata-sessionkey",sessionKey)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * Post方法，发送数据、接收数据
     * 无携带seesionKey、有传递数据
     */
    public static void postJson(final String address,String json, final okhttp3.Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .post(body)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 有携带seesionKey、有传递数据
     */
    public static void postWithSession(final String address,String json, final okhttp3.Callback callback,String sessionKey) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .header("Grpc-Metadata-sessionkey",sessionKey)
                .post(body)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
