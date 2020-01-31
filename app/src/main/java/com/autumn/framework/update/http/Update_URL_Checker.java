package com.autumn.framework.update.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.update.data.Update_url_manager;

import org.json.JSONException;
import org.json.JSONObject;

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

public class Update_URL_Checker{

    private static String url_302;
    private static JSONObject json;

    public static String check_avaliable(String urlString) {

        int code;
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
                        if (codes == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String str = "";
                            while ((str = br.readLine()) != null) {
                                sb.append(str);
                                LogUtil.d("Update_URL_Checker\n" +
                                        "{\n" +
                                        "Url\n[" + urlString +
                                        "]\n" +
                                        "Data\n[" + sb.toString() +
                                        "]\n" +
                                        "}");
                            }
                            //JSONObject json= null;
                            try {
                                json = new JSONObject(sb.toString());
                                code = json.getInt("code");
                                if (code == 200){
                                    url_302 = json.getString("downUrl");
                                    new Update_url_manager(url_302);
                                }else {
                                    return json.getString("msg");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //code = json.getInt("code");
                        } else
                            return "地址无效！" + codes;
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
                        if (codes == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String str = "";
                            while ((str = br.readLine()) != null) {
                                sb.append(str);
                                LogUtil.d("Update_URL_Checker\n" +
                                        "{\n" +
                                        "Url\n[" + urlString +
                                        "]\n" +
                                        "Data\n[" + sb.toString() +
                                        "]\n" +
                                        "}");
                            }
                            //JSONObject json= null;
                            try {
                                json = new JSONObject(sb.toString());
                                code = json.getInt("code");
                                if (code == 200){
                                    url_302 = json.getString("downUrl");
                                    new Update_url_manager(url_302);
                                }else {
                                    return json.getString("msg");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //code = json.getInt("code");
                        } else
                            return "地址无效！" + codes;
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

    public static String check_url_avaliable() {

        String urlString = Update_url_manager.getUrl();
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

