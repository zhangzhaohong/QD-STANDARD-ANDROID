package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.key_manager;
import com.autumn.sdk.manager.user.user_info_manager;

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

public class user_info {

    public static String info(Context context, String account_id) {

        String key = key_manager.key;

        user_info_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/users_info.php?account="+getURLEncoderString(account_id);

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

                            JSONObject jsonObject = new JSONObject(DesUtil.decrypt(DesUtil.decrypt(sb.toString(),key),key));
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code, key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                if (decrypt.equals("-1")) {
                                    return context.getString(R.string.info_error_null);
                                } else if (decrypt.equals("0")) {

                                    try {
                                        String account = DesUtil.decrypt(jsonObject.getString("account"),key);
                                        String private_name =DesUtil.decrypt(jsonObject.getString("private_name"),key);
                                        String user_birthday = DesUtil.decrypt(jsonObject.getString("user_birthday"),key);
                                        String user_email = DesUtil.decrypt(jsonObject.getString("user_email"),key);
                                        String user_level = DesUtil.decrypt(jsonObject.getString("user_level"),key);
                                        String log_level = DesUtil.decrypt(jsonObject.getString("log_level"),key);
                                        String user_available_date =DesUtil.decrypt(jsonObject.getString("user_available_date"),key);
                                        String integrals = DesUtil.decrypt(jsonObject.getString("integrals"),key);
                                        String prestige = DesUtil.decrypt(jsonObject.getString("prestige"),key);
                                        String grow_integrals = DesUtil.decrypt(jsonObject.getString("grow_integrals"),key);
                                        String last_login = DesUtil.decrypt(jsonObject.getString("last_login"),key);
                                        String last_sign =DesUtil.decrypt(jsonObject.getString("last_sign"),key);
                                        String keep_sign_times = DesUtil.decrypt(jsonObject.getString("keep_sign_times"),key);
                                        String vip_endtime = DesUtil.decrypt(jsonObject.getString("vip_endtime"),key);
                                        String svip_endtime = DesUtil.decrypt(jsonObject.getString("svip_endtime"),key);

                                        user_info_manager.account = account;
                                        user_info_manager.private_name = private_name;
                                        user_info_manager.user_birthday = user_birthday;
                                        user_info_manager.user_email = user_email;
                                        user_info_manager.user_level = user_level;
                                        user_info_manager.log_level = log_level;
                                        user_info_manager.user_available_date = user_available_date;
                                        user_info_manager.integrals = Integer.parseInt(integrals);
                                        user_info_manager.prestige = Integer.parseInt(prestige);
                                        user_info_manager.grow_integrals = Integer.parseInt(grow_integrals);
                                        user_info_manager.last_login = last_login;
                                        user_info_manager.last_sign = last_sign;
                                        user_info_manager.keep_sign_times = keep_sign_times;
                                        user_info_manager.vip_endtime = vip_endtime;
                                        user_info_manager.svip_endtime = svip_endtime;


                                        //LogUtil.i(user_key);

                                        return context.getString(R.string.info_full);

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

                            JSONObject jsonObject = new JSONObject(DesUtil.decrypt(DesUtil.decrypt(sb.toString(),key),key));
                            String code = jsonObject.getString("code");
                            LogUtil.i(code);

                            String decrypt = null;
                            try {
                                decrypt = DesUtil.decrypt(code, key);
                                //decrypt = AES256.aesDecrypt(account,key);

                                if (decrypt.equals("-1")) {
                                    return context.getString(R.string.info_error_null);
                                } else if (decrypt.equals("0")) {

                                    try {
                                        String account = DesUtil.decrypt(jsonObject.getString("account"),key);
                                        String private_name =DesUtil.decrypt(jsonObject.getString("private_name"),key);
                                        String user_birthday = DesUtil.decrypt(jsonObject.getString("user_birthday"),key);
                                        String user_email = DesUtil.decrypt(jsonObject.getString("user_email"),key);
                                        String user_level = DesUtil.decrypt(jsonObject.getString("user_level"),key);
                                        String log_level = DesUtil.decrypt(jsonObject.getString("log_level"),key);
                                        String user_available_date =DesUtil.decrypt(jsonObject.getString("user_available_date"),key);
                                        String integrals = DesUtil.decrypt(jsonObject.getString("integrals"),key);
                                        String prestige = DesUtil.decrypt(jsonObject.getString("prestige"),key);
                                        String grow_integrals = DesUtil.decrypt(jsonObject.getString("grow_integrals"),key);
                                        String last_login = DesUtil.decrypt(jsonObject.getString("last_login"),key);
                                        String last_sign =DesUtil.decrypt(jsonObject.getString("last_sign"),key);
                                        String keep_sign_times = DesUtil.decrypt(jsonObject.getString("keep_sign_times"),key);
                                        String vip_endtime = DesUtil.decrypt(jsonObject.getString("vip_endtime"),key);
                                        String svip_endtime = DesUtil.decrypt(jsonObject.getString("svip_endtime"),key);

                                        user_info_manager.account = account;
                                        user_info_manager.private_name = private_name;
                                        user_info_manager.user_birthday = user_birthday;
                                        user_info_manager.user_email = user_email;
                                        user_info_manager.user_level = user_level;
                                        user_info_manager.log_level = log_level;
                                        user_info_manager.user_available_date = user_available_date;
                                        user_info_manager.integrals = Integer.parseInt(integrals);
                                        user_info_manager.prestige = Integer.parseInt(prestige);
                                        user_info_manager.grow_integrals = Integer.parseInt(grow_integrals);
                                        user_info_manager.last_login = last_login;
                                        user_info_manager.last_sign = last_sign;
                                        user_info_manager.keep_sign_times = keep_sign_times;
                                        user_info_manager.vip_endtime = vip_endtime;
                                        user_info_manager.svip_endtime = svip_endtime;

                                        //LogUtil.i(user_key);

                                        return context.getString(R.string.info_full);

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
