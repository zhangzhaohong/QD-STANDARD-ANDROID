package com.autumn.sdk.manager.user;

public class login_manager {
    public static String user_key = null;
    public static String user_level = null;

    public static void init(){
        user_key = null;
        user_level = null;
    }

    public static String get_user_key(){
        return user_key;
    }

    public static String get_user_level(){
        return user_level;
    }
}
