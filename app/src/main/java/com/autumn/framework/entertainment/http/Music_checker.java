package com.autumn.framework.entertainment.http;

import com.autumn.framework.entertainment.manager.Music_player_manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Music_checker {

    private static String url_302;

    public static String music_check_avaliable(String urlString) {

        StringBuffer sb = new StringBuffer();

        if (urlString == null){
            return "链接无效";
        }else if (urlString.equals(""))
            return "链接无效";
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
                        if (codes == 302 || codes == 301) {
                            url_302 = conn.getHeaderField("Location");
                            if (url_302 == null) {
                                url_302 = conn.getHeaderField("location"); //临时重定向和永久重定向location的大小写有区分
                                if (url_302 == null)
                                    return "链接异常";
                                else if (url_302.equals(""))
                                    return "链接异常";
                                else
                                    Music_player_manager.setMusicUrl(url_302);
                            }else if (url_302.equals("")){
                                url_302 = conn.getHeaderField("location"); //临时重定向和永久重定向location的大小写有区分
                                if (url_302 == null)
                                    return "链接异常";
                                else if (url_302.equals(""))
                                    return "链接异常";
                                else
                                    Music_player_manager.setMusicUrl(url_302);
                            }else
                                Music_player_manager.setMusicUrl(url_302);
                        } else
                            return "地址无效！";
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
                        if (codes == 302 || codes == 301) {
                            url_302 = conn.getHeaderField("Location");
                            if (url_302 == null) {
                                url_302 = conn.getHeaderField("location"); //临时重定向和永久重定向location的大小写有区分
                                if (url_302 == null)
                                    return "链接异常";
                                else if (url_302.equals(""))
                                    return "链接异常";
                                else
                                    Music_player_manager.setMusicUrl(url_302);
                            }else if (url_302.equals("")){
                                url_302 = conn.getHeaderField("location"); //临时重定向和永久重定向location的大小写有区分
                                if (url_302 == null)
                                    return "链接异常";
                                else if (url_302.equals(""))
                                    return "链接异常";
                                else
                                    Music_player_manager.setMusicUrl(url_302);
                            }else
                                Music_player_manager.setMusicUrl(url_302);
                        } else
                            return "地址无效！";
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

    public static String music_check_url_avaliable() {

        String urlString = Music_player_manager.getUrl();
        StringBuffer sb = new StringBuffer();

        if (urlString == null){
            return "链接无效";
        }else if (urlString.equals(""))
            return "链接无效";
        //LogUtil.i(urlString);

        if(urlString.startsWith("https:"))
        {
            try {
                URL url = new URL(urlString);

                try {

                    URLConnection connection = url.openConnection();
                    HttpsURLConnection conn = (HttpsURLConnection) connection;

                    int codes = conn.getResponseCode();
                    if (300 > codes) {
                        //success
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
                    if (300 > codes) {
                        //success
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
