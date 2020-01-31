package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.key_manager;
import com.autumn.sdk.manager.user.sign_manager;

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
 * Created by zhang on 2018/4/30.
 */

public class user_sign {

    public static String sign(Context context, String account, String password, String type, String user_key) {

        String key = key_manager.key;

        sign_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/sign.php?account="+getURLEncoderString(account)+"&password="+getURLEncoderString(password)+"&type="+getURLEncoderString(type)+"&key="+getURLEncoderString(user_key);

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
                                    return context.getString(R.string.login_sign_error_null);
                                }else if(decrypt.equals("0")){
                                    try {
                                        String integrals = jsonObject.getString("integrals");
                                        String prestige = jsonObject.getString("prestige");
                                        String grow_integrals = jsonObject.getString("grow_integrals");
                                        //LogUtil.i(user_key);

                                        String user_integrals = DesUtil.decrypt(integrals, key);
                                        String user_prestige =DesUtil.decrypt(prestige,key);
                                        String user_grow_integrals =DesUtil.decrypt(grow_integrals,key);

                                        sign_manager.user_integrals = user_integrals;
                                        sign_manager.user_prestige = user_prestige;
                                        sign_manager.user_grow_integrals = user_grow_integrals;

                                        return context.getString(R.string.login_sign_success);

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.login_sign_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.login_sign_error_password_wrong);
                                }else if(decrypt.equals("-4")){
                                    return context.getString(R.string.login_sign_error_account_not_allowed);
                                }else if(decrypt.equals("-5")){
                                    return context.getString(R.string.login_sign_error_account_outdate);
                                }else if(decrypt.equals("-6")){
                                    return context.getString(R.string.login_sign_error_type_key_not_null);
                                }else if(decrypt.equals("-7")){
                                    return context.getString(R.string.login_sign_error_key_error);
                                }else if(decrypt.equals("-8")){
                                    return context.getString(R.string.login_sign_error_data_error);
                                }else if(decrypt.equals("-9")){
                                    return context.getString(R.string.sign_error_has_login_sign);
                                }else if(decrypt.equals("-10")){
                                    return context.getString(R.string.sign_error_has_do_sign);
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

                                if (decrypt.equals("-1")) {
                                    return context.getString(R.string.login_sign_error_null);
                                } else if (decrypt.equals("0")) {
                                    try {
                                        String integrals = jsonObject.getString("integrals");
                                        String prestige = jsonObject.getString("prestige");
                                        String grow_integrals = jsonObject.getString("grow_integrals");
                                        //LogUtil.i(user_key);

                                        String user_integrals = DesUtil.decrypt(integrals, key);
                                        String user_prestige = DesUtil.decrypt(prestige, key);
                                        String user_grow_integrals = DesUtil.decrypt(grow_integrals, key);

                                        sign_manager.user_integrals = user_integrals;
                                        sign_manager.user_prestige = user_prestige;
                                        sign_manager.user_grow_integrals = user_grow_integrals;

                                        return context.getString(R.string.login_sign_success);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                } else if (decrypt.equals("-2")) {
                                    return context.getString(R.string.login_sign_error_username_not_exist);
                                } else if (decrypt.equals("-3")) {
                                    return context.getString(R.string.login_sign_error_password_wrong);
                                } else if (decrypt.equals("-4")) {
                                    return context.getString(R.string.login_sign_error_account_not_allowed);
                                } else if (decrypt.equals("-5")) {
                                    return context.getString(R.string.login_sign_error_account_outdate);
                                } else if (decrypt.equals("-6")) {
                                    return context.getString(R.string.login_sign_error_type_key_not_null);
                                } else if (decrypt.equals("-7")) {
                                    return context.getString(R.string.login_sign_error_key_error);
                                } else if (decrypt.equals("-8")) {
                                    return context.getString(R.string.login_sign_error_data_error);
                                } else if (decrypt.equals("-9")) {
                                    return context.getString(R.string.sign_error_has_login_sign);
                                } else if (decrypt.equals("-10")) {
                                    return context.getString(R.string.sign_error_has_do_sign);
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

    public static String manual_sign(Context context, String account, String password, String type, String user_key) {

        String key = key_manager.key;

        sign_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/sign.php?account="+getURLEncoderString(account)+"&password="+getURLEncoderString(password)+"&type="+getURLEncoderString(type)+"&key="+getURLEncoderString(user_key);

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
                                    return context.getString(R.string.login_sign_error_null);
                                }else if(decrypt.equals("0")){
                                    try {
                                        String integrals = jsonObject.getString("integrals");
                                        String prestige = jsonObject.getString("prestige");
                                        String grow_integrals = jsonObject.getString("grow_integrals");
                                        String keep_sign_times = jsonObject.getString("keep_sign_times");
                                        //LogUtil.i(user_key);

                                        String user_integrals = DesUtil.decrypt(integrals, key);
                                        String user_prestige =DesUtil.decrypt(prestige,key);
                                        String user_grow_integrals =DesUtil.decrypt(grow_integrals,key);
                                        String user_keep_sign_times =DesUtil.decrypt(keep_sign_times,key);

                                        sign_manager.user_integrals = user_integrals;
                                        sign_manager.user_prestige = user_prestige;
                                        sign_manager.user_grow_integrals = user_grow_integrals;
                                        sign_manager.user_keep_sign_times = user_keep_sign_times;

                                        return context.getString(R.string.login_sign_success);

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.login_sign_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.login_sign_error_password_wrong);
                                }else if(decrypt.equals("-4")){
                                    return context.getString(R.string.login_sign_error_account_not_allowed);
                                }else if(decrypt.equals("-5")){
                                    return context.getString(R.string.login_sign_error_account_outdate);
                                }else if(decrypt.equals("-6")){
                                    return context.getString(R.string.login_sign_error_type_key_not_null);
                                }else if(decrypt.equals("-7")){
                                    return context.getString(R.string.login_sign_error_key_error);
                                }else if(decrypt.equals("-8")){
                                    return context.getString(R.string.login_sign_error_data_error);
                                }else if(decrypt.equals("-9")){
                                    return context.getString(R.string.sign_error_has_login_sign);
                                }else if(decrypt.equals("-10")){
                                    return context.getString(R.string.sign_error_has_do_sign);
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

                                if (decrypt.equals("-1")) {
                                    return context.getString(R.string.login_sign_error_null);
                                } else if (decrypt.equals("0")) {
                                    try {
                                        String integrals = jsonObject.getString("integrals");
                                        String prestige = jsonObject.getString("prestige");
                                        String grow_integrals = jsonObject.getString("grow_integrals");
                                        String keep_sign_times = jsonObject.getString("keep_sign_times");
                                        //LogUtil.i(user_key);

                                        String user_integrals = DesUtil.decrypt(integrals, key);
                                        String user_prestige =DesUtil.decrypt(prestige,key);
                                        String user_grow_integrals =DesUtil.decrypt(grow_integrals,key);
                                        String user_keep_sign_times =DesUtil.decrypt(keep_sign_times,key);

                                        sign_manager.user_integrals = user_integrals;
                                        sign_manager.user_prestige = user_prestige;
                                        sign_manager.user_grow_integrals = user_grow_integrals;
                                        sign_manager.user_keep_sign_times = user_keep_sign_times;

                                        return context.getString(R.string.login_sign_success);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                } else if (decrypt.equals("-2")) {
                                    return context.getString(R.string.login_sign_error_username_not_exist);
                                } else if (decrypt.equals("-3")) {
                                    return context.getString(R.string.login_sign_error_password_wrong);
                                } else if (decrypt.equals("-4")) {
                                    return context.getString(R.string.login_sign_error_account_not_allowed);
                                } else if (decrypt.equals("-5")) {
                                    return context.getString(R.string.login_sign_error_account_outdate);
                                } else if (decrypt.equals("-6")) {
                                    return context.getString(R.string.login_sign_error_type_key_not_null);
                                } else if (decrypt.equals("-7")) {
                                    return context.getString(R.string.login_sign_error_key_error);
                                } else if (decrypt.equals("-8")) {
                                    return context.getString(R.string.login_sign_error_data_error);
                                } else if (decrypt.equals("-9")) {
                                    return context.getString(R.string.sign_error_has_login_sign);
                                } else if (decrypt.equals("-10")) {
                                    return context.getString(R.string.sign_error_has_do_sign);
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

    public static String check_sign(Context context, String account, String password, String type, String user_key) {

        String key = key_manager.key;

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/sign.php?account="+getURLEncoderString(account)+"&password="+getURLEncoderString(password)+"&type="+getURLEncoderString(type)+"&key="+getURLEncoderString(user_key);

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
                                    return context.getString(R.string.login_sign_error_null);
                                }else if(decrypt.equals("1")){
                                    sign_manager.sign_status = false;
                                    return context.getString(R.string.do_not_sign);
                                }else if(decrypt.equals("2")){
                                    sign_manager.sign_status = true;
                                    return context.getString(R.string.already_sign);
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.login_sign_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.login_sign_error_password_wrong);
                                }else if(decrypt.equals("-4")){
                                    return context.getString(R.string.login_sign_error_account_not_allowed);
                                }else if(decrypt.equals("-5")){
                                    return context.getString(R.string.login_sign_error_account_outdate);
                                }else if(decrypt.equals("-6")){
                                    return context.getString(R.string.login_sign_error_type_key_not_null);
                                }else if(decrypt.equals("-7")){
                                    return context.getString(R.string.login_sign_error_key_error);
                                }else if(decrypt.equals("-8")){
                                    return context.getString(R.string.login_sign_error_data_error);
                                }else if(decrypt.equals("-9")){
                                    return context.getString(R.string.sign_error_has_login_sign);
                                }else if(decrypt.equals("-10")){
                                    return context.getString(R.string.sign_error_has_do_sign);
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
                                    return context.getString(R.string.login_sign_error_null);
                                }else if(decrypt.equals("1")){
                                    sign_manager.sign_status = false;
                                    return context.getString(R.string.do_not_sign);
                                }else if(decrypt.equals("2")){
                                    sign_manager.sign_status = true;
                                    return context.getString(R.string.already_sign);
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.login_sign_error_username_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.login_sign_error_password_wrong);
                                }else if(decrypt.equals("-4")){
                                    return context.getString(R.string.login_sign_error_account_not_allowed);
                                }else if(decrypt.equals("-5")){
                                    return context.getString(R.string.login_sign_error_account_outdate);
                                }else if(decrypt.equals("-6")){
                                    return context.getString(R.string.login_sign_error_type_key_not_null);
                                }else if(decrypt.equals("-7")){
                                    return context.getString(R.string.login_sign_error_key_error);
                                }else if(decrypt.equals("-8")){
                                    return context.getString(R.string.login_sign_error_data_error);
                                }else if(decrypt.equals("-9")){
                                    return context.getString(R.string.sign_error_has_login_sign);
                                }else if(decrypt.equals("-10")){
                                    return context.getString(R.string.sign_error_has_do_sign);
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
