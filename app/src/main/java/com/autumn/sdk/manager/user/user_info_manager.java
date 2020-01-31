package com.autumn.sdk.manager.user;

public class user_info_manager {
    public static String account = null;
    public static String private_name = null;
    public static String user_birthday = null;
    public static String user_email = null;
    public static String user_level = null;
    public static String log_level = null;
    public static String user_available_date = null;
    public static int integrals = 0;
    public static int prestige = 0;
    public static int grow_integrals = 0;
    public static String last_login = null;
    public static String last_sign = null;
    public static String keep_sign_times = null;
    public static String vip_endtime = null;
    public static String svip_endtime = null;

    public static void init(){
        account = null;
        private_name = null;
        user_birthday = null;
        user_email = null;
        user_level = null;
        log_level = null;
        user_available_date = null;
        integrals = 0;
        prestige = 0;
        grow_integrals = 0;
        last_login = null;
        last_sign = null;
        keep_sign_times = null;
        vip_endtime = null;
        svip_endtime = null;
    }

    public static String get_account(){
        return account;
    }

    public static String get_private_name(){
        return private_name;
    }

    public static String get_user_birthday(){
        return user_birthday;
    }

    public static String get_user_email(){
        return user_email;
    }

    public static String get_user_level(){
        return user_level;
    }

    public static String get_log_level(){
        return log_level;
    }

    public static String get_user_available_date(){
        return user_available_date;
    }

    public static int get_integrals(){
        return integrals;
    }

    public static int get_prestige(){
        return prestige;
    }

    public static int get_grow_integrals(){
        return grow_integrals;
    }

    public static String get_last_login(){
        return last_login;
    }

    public static String get_last_sign(){
        return last_sign;
    }

    public static String get_keep_sign_times(){
        return keep_sign_times;
    }

    public static String get_vip_endtime(){
        return vip_endtime;
    }

    public static String get_svip_endtime(){
        return svip_endtime;
    }
}
