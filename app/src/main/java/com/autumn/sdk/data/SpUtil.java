package com.autumn.sdk.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhang on 2018/4/28.
 */

public class SpUtil {

    //写入数据
    public void writeValue(Context context, String file, String name, String value){

        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            value = AES256.aesEncrypt(value, key);
            SharedPreferences activityPreferences = context.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = activityPreferences.edit();
            editor.putString(name,value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void writeBoolean(Context context,String file,String name,Boolean value){
        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences activityPreferences = context.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = activityPreferences.edit();
            editor.putBoolean(name, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //读取数据
    public String getValue(Context context,String file, String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = context.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            String Value = AES256.aesDecrypt(pref.getString(name,""), key);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();

            //LogUtil.e(e.toString());
        }
        return null;
    }
    public boolean getBoolean(Context context,String file,String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = context.getSharedPreferences(
                    file, Context.MODE_PRIVATE);
            Boolean Value = pref.getBoolean(name,false);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
