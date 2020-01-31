package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.key_manager;

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

public class user_act_account {

    public static String act_account(Context context, String account, String password, String password_new) {

        String key = key_manager.key;

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/act_account.php?account="+getURLEncoderString(account)+"&password="+getURLEncoderString(password)+"&password_new="+getURLEncoderString(password_new);

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
                                    return context.getString(R.string.act_account_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.act_account_success);
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.act_account_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.act_account_error_old_password_not_correct);
                                } else if(decrypt.equals("-4")){
                                    return context.getString(R.string.act_account_error_account_not_allowed);
                                } else if(decrypt.equals("-5")){
                                    return context.getString(R.string.act_account_error_account);
                                } else if(decrypt.equals("-6")){
                                    return context.getString(R.string.act_account_error_password_not_equal_old);
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
                                    return context.getString(R.string.act_account_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.act_account_success);
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.act_account_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.act_account_error_old_password_not_correct);
                                } else if(decrypt.equals("-4")){
                                    return context.getString(R.string.act_account_error_account_not_allowed);
                                } else if(decrypt.equals("-5")){
                                    return context.getString(R.string.act_account_error_account);
                                } else if(decrypt.equals("-6")){
                                    return context.getString(R.string.act_account_error_password_not_equal_old);
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
