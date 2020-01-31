package com.autumn.framework.Log;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.autumn.reptile.MyApplication;
import com.autumn.framework.data.AES256;
import com.autumn.framework.data.key_manager_data;

public class CrashApplication extends Application
{
    private String SETTING = "Config";

    private String AUTO_POST = "Auto_post";

    @Override  
    public void onCreate() {  
        super.onCreate();  
		boolean is_auto = false;
		//测试
		//writeBoolean(SETTING,AUTO_ON,true);
		
		//is_auto = getBoolean(SETTING,AUTO_POST);
        CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());  
		//crashHandler.Auto_post = is_auto;

    }

    //写入数据
    public void writeValue(String file,String name,String value){

        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            value = AES256.aesEncrypt(value, key);
            SharedPreferences.Editor editor = MyApplication.getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE).edit();
            editor.putString(name,value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void writeBoolean(String file,String name,Boolean value){
        String key = key_manager_data.key;

        String encrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences.Editor editor = MyApplication.getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE).edit();
            editor.putBoolean(name,value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //读取数据
    public String getValue(String file, String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE);
            String Value = AES256.aesDecrypt(pref.getString(name,""), key);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean getBoolean(String file,String name){
        String key = key_manager_data.key;

        String decrypt = null;
        try {
            //file = AES256.aesEncrypt(file, key);
            name = AES256.aesEncrypt(name, key);
            SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(file,Context.MODE_PRIVATE);
            Boolean Value = pref.getBoolean(name,false);
            return Value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	
}  
