package com.autumn.framework.status;

public class login_status_manager {

    public static boolean login_status = false;

    public static void init(){
        login_status = false;
    }

    public static Boolean get_login_status(){
        return login_status;
    }

}
