package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Host_manager;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class NetWork_Connecter_Music {

    public static String music_connect(String service, String content, String type, int page) {

        StringBuffer sb = new StringBuffer();

        if (Host_manager.getMusicHost() == null) {
            return "域名返回为NULL";
        } else if (Host_manager.getMusicHost().equals("")){
            return "域名为空";
        }

        String urlString = Host_manager.getMusicHost() + "/" + getURLEncoderString(service) + "/search?keyword=" + getURLEncoderString(content) + "&type=" + getURLEncoderString(type) + "&pageSize=10&page=" + getURLEncoderString(String.valueOf(page));

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
                            LogUtil.d("NetWork_Connecter_Music\n" +
                                    "{\n" +
                                    "Url\n[" + urlString +
                                    "]\n" +
                                    "Data\n[" + sb.toString() +
                                    "]\n" +
                                    "}");
                        }
                        try {
                            switch (service){
                                case "netease":
                                    NetWork_Analysis_Music_Netease.Startprocessing(page, sb.toString());
                                    break;
                                case "tencent":
                                    NetWork_Analysis_Music_Tencent.Startprocessing(page, sb.toString());
                                    break;
                                case "kugou":
                                    NetWork_Analysis_Music_Kugou.Startprocessing(page, sb.toString());
                                    break;
                                case "kuwo":
                                    NetWork_Analysis_Music_Kuwo.Startprocessing(page, sb.toString());
                                    break;
                                case "migu":
                                    NetWork_Analysis_Music_Migu_MV.Startprocessing(page, sb.toString());
                                    break;
                                case "baidu":
                                    NetWork_Analysis_Music_Baidu.Startprocessing(page, sb.toString());
                                    break;
                            }
                            //NetWork_Analysis_Music.Startprocessing(page, sb.toString());
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
                    HttpURLConnection conn = (HttpURLConnection) connection;

                    int codes = conn.getResponseCode();
                    if (400 >= codes) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str = "";
                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                            LogUtil.d("NetWork_Connecter_Music\n" +
                                    "{\n" +
                                    "Url\n[" + urlString +
                                    "]\n" +
                                    "Data\n[" + sb.toString() +
                                    "]\n" +
                                    "}");
                        }
                        try {
                            switch (service){
                                case "netease":
                                    NetWork_Analysis_Music_Netease.Startprocessing(page, sb.toString());
                                    break;
                                case "tencent":
                                    NetWork_Analysis_Music_Tencent.Startprocessing(page, sb.toString());
                                    break;
                                case "kugou":
                                    NetWork_Analysis_Music_Kugou.Startprocessing(page, sb.toString());
                                    break;
                                case "kuwo":
                                    NetWork_Analysis_Music_Kuwo.Startprocessing(page, sb.toString());
                                    break;
                                case "migu":
                                    NetWork_Analysis_Music_Migu_MV.Startprocessing(page, sb.toString());
                                    break;
                                case "baidu":
                                    NetWork_Analysis_Music_Baidu.Startprocessing(page, sb.toString());
                                    break;
                            }
                            //NetWork_Analysis_Music.Startprocessing(page, sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return "访问成功";
                    } else {
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
