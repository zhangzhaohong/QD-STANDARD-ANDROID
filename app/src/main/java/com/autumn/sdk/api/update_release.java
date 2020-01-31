package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.key_manager;
import com.autumn.sdk.manager.update.update_manager;

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

public class update_release{

        public static String update(Context context, String package_name) {

            String key = key_manager.key;

            update_manager.init();

            StringBuffer sb = new StringBuffer();

            String urlString = context.getString(R.string.server_url) + "api/update_release.php?package_name="+getURLEncoderString(package_name);

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

                                JSONObject jsonObject = new JSONObject(DesUtil.decrypt(sb.toString(),key));
                                String code = jsonObject.getString("code");
                                LogUtil.i(code);

                                String decrypt = null;
                                try {
                                    decrypt = DesUtil.decrypt(code,key);
                                    //decrypt = AES256.aesDecrypt(account,key);

                                    if(decrypt.equals("-1")){
                                        return context.getString(R.string.update_error_null);
                                    }else if(decrypt.equals("0")){
                                        return context.getString(R.string.update_new_version_not_exist);
                                    }else if(decrypt.equals("1")){

                                        try {
                                            String software_version = DesUtil.decrypt(jsonObject.getString("software_version"),key);
                                            String changelog =DesUtil.decrypt(jsonObject.getString("changelog"),key);
                                            String download_url = DesUtil.decrypt(jsonObject.getString("download_url"),key);
                                            String download_code = DesUtil.decrypt(jsonObject.getString("download_code"),key);
                                            String force_update = DesUtil.decrypt(jsonObject.getString("force_update"),key);
                                            //LogUtil.i(user_key);

                                            //FindUpdateActivity.software_version = software_version;
                                            update_manager.software_version = software_version;
                                            update_manager.changelog = changelog;
                                            update_manager.download_url = download_url;
                                            update_manager.download_code = download_code;
                                            update_manager.force_update = force_update;
                                            //LoginActivity.force_update = force_update;

                                            LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                            return context.getString(R.string.update_new_version);

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

                                JSONObject jsonObject = new JSONObject(DesUtil.decrypt(sb.toString(),key));
                                String code = jsonObject.getString("code");
                                LogUtil.i(code);

                                String decrypt = null;
                                try {
                                    decrypt = DesUtil.decrypt(code, key);
                                    //decrypt = AES256.aesDecrypt(account,key);

                                    if (decrypt.equals("-1")) {
                                        return context.getString(R.string.update_error_null);
                                    } else if (decrypt.equals("0")) {
                                        return context.getString(R.string.update_new_version_not_exist);
                                    } else if (decrypt.equals("1")) {

                                        try {
                                            String software_version = DesUtil.decrypt(jsonObject.getString("software_version"),key);
                                            String changelog =DesUtil.decrypt(jsonObject.getString("changelog"),key);
                                            String download_url = DesUtil.decrypt(jsonObject.getString("download_url"),key);
                                            String download_code = DesUtil.decrypt(jsonObject.getString("download_code"),key);
                                            String force_update = DesUtil.decrypt(jsonObject.getString("force_update"),key);
                                            //LogUtil.i(user_key);

                                            //FindUpdateActivity.software_version = software_version;
                                            update_manager.software_version = software_version;
                                            update_manager.changelog = changelog;
                                            update_manager.download_url = download_url;
                                            update_manager.download_code = download_code;
                                            update_manager.force_update = force_update;
                                            //LoginActivity.force_update = force_update;

                                            LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                            return context.getString(R.string.update_new_version);

                                        }catch (JSONException e) {
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
