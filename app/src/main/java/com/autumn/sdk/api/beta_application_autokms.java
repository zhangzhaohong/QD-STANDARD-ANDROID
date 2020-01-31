package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.key_manager;
import com.autumn.sdk.manager.beta.beta_manager;

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
 * Created by zhang on 2018/4/5.
 */

public class beta_application_autokms {

    public static String beta_application(Context context, String account, String password, String appkey) {

        String key = key_manager.key;

        beta_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/beta_application_AutoKms.php?account="+getURLEncoderString(account)+"&password="+getURLEncoderString(password)+"&app_key="+getURLEncoderString(appkey);

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

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.beta_application_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.beta_application_success);
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.login_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.beta_application_error_password_wrong);
                                }else if(decrypt.equals("-4")){
                                    return context.getString(R.string.login_error_account_not_allowed);
                                }else if(decrypt.equals("-5")){
                                    return context.getString(R.string.login_error_account_outdate);
                                }else if(decrypt.equals("-6")){
                                    return context.getString(R.string.beta_application_error_app_key_not_null);
                                }else if(decrypt.equals("-7")){
                                    return context.getString(R.string.beta_application_error_app_key_not_exist);
                                }else if(decrypt.equals("-8")){
                                    return context.getString(R.string.beta_application_error_app_beta_not_start);
                                }else if(decrypt.equals("-9")){
                                    return context.getString(R.string.beta_application_error_app_beta_not_start_application);
                                }else if(decrypt.equals("-10")){
                                    return context.getString(R.string.beta_application_error_app_beta_twice);
                                }else if(decrypt.equals("-11")){
                                    return context.getString(R.string.beta_application_error_app_beta_exhaust);
                                }else if (decrypt.equals("-12")){
                                    return context.getString(R.string.beta_application_data_error);
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
        }else{
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
                                decrypt = DesUtil.decrypt(code,key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                if(decrypt.equals("-1")){
                                    return context.getString(R.string.beta_application_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.beta_application_success);
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.login_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.beta_application_error_password_wrong);
                                }else if(decrypt.equals("-4")){
                                    return context.getString(R.string.login_error_account_not_allowed);
                                }else if(decrypt.equals("-5")){
                                    return context.getString(R.string.login_error_account_outdate);
                                }else if(decrypt.equals("-6")){
                                    return context.getString(R.string.beta_application_error_app_key_not_null);
                                }else if(decrypt.equals("-7")){
                                    return context.getString(R.string.beta_application_error_app_key_not_exist);
                                }else if(decrypt.equals("-8")){
                                    return context.getString(R.string.beta_application_error_app_beta_not_start);
                                }else if(decrypt.equals("-9")){
                                    return context.getString(R.string.beta_application_error_app_beta_not_start_application);
                                }else if(decrypt.equals("-10")){
                                    return context.getString(R.string.beta_application_error_app_beta_twice);
                                }else if(decrypt.equals("-11")){
                                    return context.getString(R.string.beta_application_error_app_beta_exhaust);
                                }else if (decrypt.equals("-12")){
                                    return context.getString(R.string.beta_application_data_error);
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
