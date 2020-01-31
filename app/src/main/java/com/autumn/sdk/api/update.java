package com.autumn.sdk.api;

import android.content.Context;

import com.autumn.reptile.R;
import com.autumn.sdk.data.DataUtil;
import com.autumn.sdk.data.DesUtil;
import com.autumn.sdk.data.LogUtil;
import com.autumn.sdk.data.VersionUtil;
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

/**
 * Created by zhang on 2018/6/22.
 */

public class update {

    public static String update(Context context, String app_key, String version) {

        String key = key_manager.key;

        update_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/update.php?app_key="+getURLEncoderString(app_key)+"&version="+getURLEncoderString(version);

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

                                        if (software_version.equals(context.getString(R.string.app_software_version))){
                                            return context.getString(R.string.update_new_version_not_exist);
                                        }else {
                                            if(DataUtil.find_words(context.getString(R.string.app_software_version),"Patch")&&DataUtil.find_words(software_version,"Patch")) {
                                                int beginIndex_now = context.getString(R.string.app_software_version).indexOf("Patch") + 5;
                                                int endIndex_now = context.getString(R.string.app_software_version).lastIndexOf(")");
                                                int patch_now = Integer.parseInt(context.getString(R.string.app_software_version).substring(beginIndex_now, endIndex_now));
                                                //FToastUtils.init().setRoundRadius(30).show(patch_now + "");
                                                int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                int endIndex_new = software_version.lastIndexOf(")");
                                                int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));

                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))){
                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);
                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))){
                                                    if (patch_now < patch_new){

                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }
                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }else {
                                                //FToastUtils.init().setRoundRadius(30).show("null");
                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))) {

                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);

                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))) {

                                                    int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                    int endIndex_new = software_version.lastIndexOf(")");
                                                    int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));
                                                    if (patch_new > 0) {
                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }

                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }
                                        }
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

                                        if (software_version.equals(context.getString(R.string.app_software_version))){
                                            return context.getString(R.string.update_new_version_not_exist);
                                        }else {
                                            if(DataUtil.find_words(context.getString(R.string.app_software_version),"Patch")&&DataUtil.find_words(software_version,"Patch")) {
                                                int beginIndex_now = context.getString(R.string.app_software_version).indexOf("Patch") + 5;
                                                int endIndex_now = context.getString(R.string.app_software_version).lastIndexOf(")");
                                                int patch_now = Integer.parseInt(context.getString(R.string.app_software_version).substring(beginIndex_now, endIndex_now));
                                                //FToastUtils.init().setRoundRadius(30).show(patch_now + "");
                                                int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                int endIndex_new = software_version.lastIndexOf(")");
                                                int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));

                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))){
                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);
                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))){
                                                    if (patch_now < patch_new){

                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }
                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }else {
                                                //FToastUtils.init().setRoundRadius(30).show("null");
                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))) {

                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);

                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))) {

                                                    int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                    int endIndex_new = software_version.lastIndexOf(")");
                                                    int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));
                                                    if (patch_new > 0) {
                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }

                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }
                                        }
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

    public static String beta_update(Context context, String app_key, String version, String user_key) {

        String key = key_manager.key;

        update_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/beta_update.php?app_key="+getURLEncoderString(app_key)+"&version="+getURLEncoderString(version)+"&user_key="+getURLEncoderString(user_key);

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
                                    return context.getString(R.string.update_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.update_new_version_not_exist);
                                }else if(decrypt.equals("2")){

                                    try {
                                        String software_version = DesUtil.decrypt(jsonObject.getString("software_version"),key);
                                        String changelog =DesUtil.decrypt(jsonObject.getString("changelog"),key);
                                        String download_url = DesUtil.decrypt(jsonObject.getString("download_url"),key);
                                        String download_code = DesUtil.decrypt(jsonObject.getString("download_code"),key);
                                        String force_update = DesUtil.decrypt(jsonObject.getString("force_update"),key);
                                        //LogUtil.i(user_key);

                                        if (software_version.equals(context.getString(R.string.app_software_version))){
                                            return context.getString(R.string.update_new_version_not_exist);
                                        }else {
                                            if(DataUtil.find_words(context.getString(R.string.app_software_version),"Patch")&&DataUtil.find_words(software_version,"Patch")) {
                                                int beginIndex_now = context.getString(R.string.app_software_version).indexOf("Patch") + 5;
                                                int endIndex_now = context.getString(R.string.app_software_version).lastIndexOf(")");
                                                int patch_now = Integer.parseInt(context.getString(R.string.app_software_version).substring(beginIndex_now, endIndex_now));
                                                //FToastUtils.init().setRoundRadius(30).show(patch_now + "");
                                                int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                int endIndex_new = software_version.lastIndexOf(")");
                                                int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));

                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))){
                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);
                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))){
                                                    if (patch_now < patch_new){

                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }
                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }else {
                                                //FToastUtils.init().setRoundRadius(30).show("null");
                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))) {

                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);

                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))) {

                                                    int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                    int endIndex_new = software_version.lastIndexOf(")");
                                                    int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));
                                                    if (patch_new > 0) {
                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }

                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }
                                        }
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

                                if (decrypt.equals("-1")) {
                                    return context.getString(R.string.update_error_null);
                                } else if (decrypt.equals("0")) {
                                    return context.getString(R.string.update_new_version_not_exist);
                                } else if (decrypt.equals("2")) {

                                    try {
                                        String software_version = DesUtil.decrypt(jsonObject.getString("software_version"),key);
                                        String changelog =DesUtil.decrypt(jsonObject.getString("changelog"),key);
                                        String download_url = DesUtil.decrypt(jsonObject.getString("download_url"),key);
                                        String download_code = DesUtil.decrypt(jsonObject.getString("download_code"),key);
                                        String force_update = DesUtil.decrypt(jsonObject.getString("force_update"),key);
                                        //LogUtil.i(user_key);

                                        if (software_version.equals(context.getString(R.string.app_software_version))){
                                            return context.getString(R.string.update_new_version_not_exist);
                                        }else {
                                            if(DataUtil.find_words(context.getString(R.string.app_software_version),"Patch")&&DataUtil.find_words(software_version,"Patch")) {
                                                int beginIndex_now = context.getString(R.string.app_software_version).indexOf("Patch") + 5;
                                                int endIndex_now = context.getString(R.string.app_software_version).lastIndexOf(")");
                                                int patch_now = Integer.parseInt(context.getString(R.string.app_software_version).substring(beginIndex_now, endIndex_now));
                                                //FToastUtils.init().setRoundRadius(30).show(patch_now + "");
                                                int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                int endIndex_new = software_version.lastIndexOf(")");
                                                int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));

                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))){
                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);
                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))){
                                                    if (patch_now < patch_new){

                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }
                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }else {
                                                //FToastUtils.init().setRoundRadius(30).show("null");
                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))) {

                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);

                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))) {

                                                    int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                    int endIndex_new = software_version.lastIndexOf(")");
                                                    int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));
                                                    if (patch_new > 0) {
                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }

                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }
                                        }
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

    public static String debug_update(Context context, String app_key, String version, String user_key, String device_code) {

        String key = key_manager.key;

        update_manager.init();

        StringBuffer sb = new StringBuffer();

        String urlString = context.getString(R.string.server_url) + "api/debug_update.php?app_key="+getURLEncoderString(app_key)+"&version="+getURLEncoderString(version)+"&user_key="+getURLEncoderString(user_key)+"&device_code="+getURLEncoderString(device_code);

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
                                    return context.getString(R.string.update_error_null);
                                }else if(decrypt.equals("0")){
                                    return context.getString(R.string.update_new_version_not_exist);
                                }else if(decrypt.equals("3")){

                                    try {
                                        String software_version = DesUtil.decrypt(jsonObject.getString("software_version"),key);
                                        String changelog =DesUtil.decrypt(jsonObject.getString("changelog"),key);
                                        String download_url = DesUtil.decrypt(jsonObject.getString("download_url"),key);
                                        String download_code = DesUtil.decrypt(jsonObject.getString("download_code"),key);
                                        String force_update = DesUtil.decrypt(jsonObject.getString("force_update"),key);
                                        //LogUtil.i(user_key);

                                        if (software_version.equals(context.getString(R.string.app_software_version))){
                                            return context.getString(R.string.update_new_version_not_exist);
                                        }else {
                                            if(DataUtil.find_words(context.getString(R.string.app_software_version),"Patch")&&DataUtil.find_words(software_version,"Patch")) {
                                                int beginIndex_now = context.getString(R.string.app_software_version).indexOf("Patch") + 5;
                                                int endIndex_now = context.getString(R.string.app_software_version).lastIndexOf(")");
                                                int patch_now = Integer.parseInt(context.getString(R.string.app_software_version).substring(beginIndex_now, endIndex_now));
                                                //FToastUtils.init().setRoundRadius(30).show(patch_now + "");
                                                int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                int endIndex_new = software_version.lastIndexOf(")");
                                                int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));

                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))){
                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);
                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))){
                                                    if (patch_now < patch_new){

                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }
                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }else {
                                                //FToastUtils.init().setRoundRadius(30).show("null");
                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))) {

                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);

                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))) {

                                                    int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                    int endIndex_new = software_version.lastIndexOf(")");
                                                    int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));
                                                    if (patch_new > 0) {
                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }

                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                        return "JSON解析失败";
                                    }
                                }else if(decrypt.equals("-2")){
                                    return context.getString(R.string.app_key_not_exist);
                                }else if(decrypt.equals("-3")){
                                    return context.getString(R.string.app_key_outdate);
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
                                    return context.getString(R.string.update_error_null);
                                } else if (decrypt.equals("0")) {
                                    return context.getString(R.string.update_new_version_not_exist);
                                } else if (decrypt.equals("3")) {

                                    try {
                                        String software_version = DesUtil.decrypt(jsonObject.getString("software_version"),key);
                                        String changelog =DesUtil.decrypt(jsonObject.getString("changelog"),key);
                                        String download_url = DesUtil.decrypt(jsonObject.getString("download_url"),key);
                                        String download_code = DesUtil.decrypt(jsonObject.getString("download_code"),key);
                                        String force_update = DesUtil.decrypt(jsonObject.getString("force_update"),key);
                                        //LogUtil.i(user_key);

                                        if (software_version.equals(context.getString(R.string.app_software_version))){
                                            return context.getString(R.string.update_new_version_not_exist);
                                        }else {
                                            if(DataUtil.find_words(context.getString(R.string.app_software_version),"Patch")&&DataUtil.find_words(software_version,"Patch")) {
                                                int beginIndex_now = context.getString(R.string.app_software_version).indexOf("Patch") + 5;
                                                int endIndex_now = context.getString(R.string.app_software_version).lastIndexOf(")");
                                                int patch_now = Integer.parseInt(context.getString(R.string.app_software_version).substring(beginIndex_now, endIndex_now));
                                                //FToastUtils.init().setRoundRadius(30).show(patch_now + "");
                                                int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                int endIndex_new = software_version.lastIndexOf(")");
                                                int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));

                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))){
                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);
                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))){
                                                    if (patch_now < patch_new){

                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }
                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }else {
                                                //FToastUtils.init().setRoundRadius(30).show("null");
                                                if (Integer.valueOf(VersionUtil.build_version(context,software_version))>Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version)))||Integer.valueOf(VersionUtil.sp_version(context,software_version))>Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version)))) {

                                                    update_manager.software_version = software_version;
                                                    update_manager.changelog = changelog;
                                                    update_manager.download_url = download_url;
                                                    update_manager.download_code = download_code;
                                                    update_manager.force_update = force_update;

                                                    LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                    return context.getString(R.string.update_new_version);

                                                }else if (Integer.valueOf(VersionUtil.build_version(context,software_version)).equals(Integer.valueOf(VersionUtil.build_version(context,context.getString(R.string.app_software_version))))||Integer.valueOf(VersionUtil.sp_version(context,software_version)).equals(Integer.valueOf(VersionUtil.sp_version(context,context.getString(R.string.app_software_version))))) {

                                                    int beginIndex_new = software_version.indexOf("Patch") + 5;
                                                    int endIndex_new = software_version.lastIndexOf(")");
                                                    int patch_new = Integer.parseInt(software_version.substring(beginIndex_new, endIndex_new));
                                                    if (patch_new > 0) {
                                                        update_manager.software_version = software_version;
                                                        update_manager.changelog = changelog;
                                                        update_manager.download_url = download_url;
                                                        update_manager.download_code = download_code;
                                                        update_manager.force_update = force_update;

                                                        LogUtil.i("info\n" + software_version + "\n" + changelog + "\n" + download_url + "\n" + download_code + "\n" + force_update);

                                                        return context.getString(R.string.update_new_version);

                                                    }else{
                                                        return context.getString(R.string.update_new_version_not_exist);
                                                    }

                                                }else{
                                                    return context.getString(R.string.update_new_version_not_exist);
                                                }

                                            }
                                        }
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
