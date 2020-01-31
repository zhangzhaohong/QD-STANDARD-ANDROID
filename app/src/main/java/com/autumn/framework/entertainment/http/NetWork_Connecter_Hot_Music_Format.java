package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Host_manager;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class NetWork_Connecter_Hot_Music_Format {

    public static String hot_music_connect(String service, String id, int page) {

        StringBuffer sb = new StringBuffer();

        if (Host_manager.getHotMusicHost() == null) {
            return "域名返回为NULL";
        } else if (Host_manager.getHotMusicHost().equals("")){
            return "域名为空";
        }

        String urlString = Host_manager.getHotMusicHost() + "/" + getURLEncoderString(service) + "/topList?id=" + getURLEncoderString(id) + "&pageSize=10&page=" + getURLEncoderString(String.valueOf(page)) + "&format=1";

        //LogUtil.i(urlString);

        if(urlString.startsWith("https:"))
        {
            try {
                URL url = new URL(urlString);

                try {

                    URLConnection connection = url.openConnection();
                    HttpsURLConnection conn = (HttpsURLConnection) connection;

                    int codes = conn.getResponseCode();
                    if (400 >= codes) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str = "";
                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                            LogUtil.d("NetWork_Connecter_Hot_Music\n" +
                                    "{\n" +
                                    "Url\n[" + urlString +
                                    "]\n" +
                                    "Data\n[" + sb.toString() +
                                    "]\n" +
                                    "}");
                        }

                        try {
                            NetWork_Analysis_Hot_Music.Startprocessing(page, sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return "访问成功";
                    }else {
                        return "无法访问网站" + codes;

                    }
                } catch (IOException e) {
                    return "提交数据失败\n请检查网络是否连接" + "\nio";


                }
            }catch (MalformedURLException e) {
                return "提交数据失败\n请检查网络是否连接" + "\nmalformed";
            }
        }else {

            try {
                URL url = new URL(urlString);

                try {

                    URLConnection connection = url.openConnection();
                    HttpsURLConnection conn = (HttpsURLConnection) connection;

                    int codes = conn.getResponseCode();
                    if (400 >= codes) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str = "";
                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                            LogUtil.d("NetWork_Connecter_Hot_Music\n" +
                                    "{\n" +
                                    "Url\n[" + urlString +
                                    "]\n" +
                                    "Data\n[" + sb.toString() +
                                    "]\n" +
                                    "}");
                        }

                        try {
                            NetWork_Analysis_Hot_Music.Startprocessing(page, sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return "访问成功";
                    }else {
                        return "无法访问网站" + codes;

                    }
                } catch (IOException e) {
                    return "提交数据失败\n请检查网络是否连接" + "\nio";


                }
            } catch (MalformedURLException e) {
                return "提交数据失败\n请检查网络是否连接" + "\nmalformed";
            }
        }
    }

    private static String getKey() {
        return "765929048";
    }

    private static String getHost() {
        return "https://vip.api.bzqll.com";
    }

    public	static String getURLEncoderString(String str) {
        String result = "";
        if(null == str) {
            return"";
        }
        try
        {
            String ENCODE = null;
            result = URLEncoder.encode(str, "UTF8");
        }
        catch
                (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
