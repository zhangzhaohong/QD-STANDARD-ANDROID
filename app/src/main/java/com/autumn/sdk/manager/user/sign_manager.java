package com.autumn.sdk.manager.user;

public class sign_manager {
    public static String user_integrals = null;
    public static String user_prestige = null;
    public static String user_grow_integrals = null;
    public static String user_keep_sign_times = null;
    public static boolean sign_status = false;

    public static void init(){
        user_integrals = null;
        user_prestige = null;
        user_grow_integrals = null;
        user_keep_sign_times = null;
        sign_status = false;
    }

    public static String get_user_integrals(){
        return user_integrals;
    }

    public static String get_user_prestige(){
        return user_prestige;
    }

    public static String get_user_grow_integrals(){
        return user_grow_integrals;
    }

    public static String get_user_keep_sign_times(){
        return user_keep_sign_times;
    }

    public static Boolean get_sign_status(){
        return sign_status;
    }
}
