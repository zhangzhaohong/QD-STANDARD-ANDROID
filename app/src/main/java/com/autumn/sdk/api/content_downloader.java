package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.key_manager;
import com.autumn.sdk.manager.content.content_manager;

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

/**
 * Created by zhang on 2018/6/22.
 */

public class content_downloader {

    public static String content_download(Context context, String app_key, String version) {

        String key = key_manager.key;

        content_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/notice.php?app_key="+getURLEncoderString(app_key)+"&version="+getURLEncoderString(version);

        LogUtil.i(urlString);

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
                            LogUtil.i(sb.toString());
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code,key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                LogUtil.i(decrypt);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.update_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.update_new_version_not_exist);
                                }else if(decrypt.equals("1")){

                                    try {
                                        String app_content_version = DesUtil.decrypt(jsonObject.getString("version"),key);
                                        String app_content =DesUtil.decrypt(jsonObject.getString("notice"),key);
                                        //LogUtil.i(user_key);

                                        content_manager.app_content_version = app_content_version;
                                        content_manager.app_content = app_content;


                                        LogUtil.i("info\n" + app_content_version + "\n" + app_content);

                                        return context.getString(R.string.notice_new_version);

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }

                                return  decrypt;
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                                return  "数据解密失败";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                            return "JSON解析失败";
                        }
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
                            LogUtil.i(sb.toString());
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code, key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.notice_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.notice_new_version_not_exist);
                                }else if(decrypt.equals("1")){

                                    try {
                                        String app_content_version = DesUtil.decrypt(jsonObject.getString("version"),key);
                                        String app_content =DesUtil.decrypt(jsonObject.getString("notice"),key);
                                        //LogUtil.i(user_key);

                                        content_manager.app_content_version = app_content_version;
                                        content_manager.app_content = app_content;


                                        LogUtil.i("info\n" + app_content_version + "\n" + app_content);

                                        return context.getString(R.string.notice_new_version);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }

                                return decrypt;
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                                return "数据解密失败";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                            return "JSON解析失败";
                        }
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

    public static String beta_content_download(Context context, String app_key, String version, String user_key) {

        String key = key_manager.key;

        content_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/beta_notice.php?app_key="+getURLEncoderString(app_key)+"&version="+getURLEncoderString(version)+"&user_key="+getURLEncoderString(user_key);

        LogUtil.i(urlString);

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
                            LogUtil.i(sb.toString());
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code,key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                LogUtil.i(decrypt);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.update_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.update_new_version_not_exist);
                                }else if(decrypt.equals("2")){

                                    try {
                                        String app_content_version = DesUtil.decrypt(jsonObject.getString("version"),key);
                                        String app_content =DesUtil.decrypt(jsonObject.getString("notice"),key);
                                        //LogUtil.i(user_key);

                                        content_manager.app_content_version = app_content_version;
                                        content_manager.app_content = app_content;


                                        LogUtil.i("info\n" + app_content_version + "\n" + app_content);

                                        return context.getString(R.string.notice_new_version);

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }

                                return  decrypt;
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                                return  "数据解密失败";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                            return "JSON解析失败";
                        }
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
                            LogUtil.i(sb.toString());
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code, key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.notice_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.notice_new_version_not_exist);
                                }else if(decrypt.equals("2")){

                                    try {
                                        String app_content_version = DesUtil.decrypt(jsonObject.getString("version"),key);
                                        String app_content =DesUtil.decrypt(jsonObject.getString("notice"),key);
                                        //LogUtil.i(user_key);

                                        content_manager.app_content_version = app_content_version;
                                        content_manager.app_content = app_content;


                                        LogUtil.i("info\n" + app_content_version + "\n" + app_content);

                                        return context.getString(R.string.notice_new_version);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }

                                return decrypt;
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                                return "数据解密失败";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                            return "JSON解析失败";
                        }
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

    public static String debug_content_download(Context context, String app_key, String version, String user_key, String device_code) {

        String key = key_manager.key;

        content_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/debug_notice.php?app_key="+getURLEncoderString(app_key)+"&version="+getURLEncoderString(version)+"&user_key="+getURLEncoderString(user_key)+"&device_code="+getURLEncoderString(device_code);

        LogUtil.i(urlString);

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
                            LogUtil.i(sb.toString());
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code,key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                LogUtil.i(decrypt);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.update_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.update_new_version_not_exist);
                                }else if(decrypt.equals("3")){

                                    try {
                                        String app_content_version = DesUtil.decrypt(jsonObject.getString("version"),key);
                                        String app_content =DesUtil.decrypt(jsonObject.getString("notice"),key);
                                        //LogUtil.i(user_key);

                                        content_manager.app_content_version = app_content_version;
                                        content_manager.app_content = app_content;


                                        LogUtil.i("info\n" + app_content_version + "\n" + app_content);

                                        return context.getString(R.string.notice_new_version);

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }

                                return  decrypt;
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                                return  "数据解密失败";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                            return "JSON解析失败";
                        }
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
                            LogUtil.i(sb.toString());
                        }
                        try {

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code, key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.notice_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.notice_new_version_not_exist);
                                }else if(decrypt.equals("3")){

                                    try {
                                        String app_content_version = DesUtil.decrypt(jsonObject.getString("version"),key);
                                        String app_content =DesUtil.decrypt(jsonObject.getString("notice"),key);
                                        //LogUtil.i(user_key);

                                        content_manager.app_content_version = app_content_version;
                                        content_manager.app_content = app_content;


                                        LogUtil.i("info\n" + app_content_version + "\n" + app_content);

                                        return context.getString(R.string.notice_new_version);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }

                                return decrypt;
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                                return "数据解密失败";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                            return "JSON解析失败";
                        }
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

