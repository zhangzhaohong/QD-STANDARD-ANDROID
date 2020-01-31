package com.autumn.sdk.manager.notice;

public class notice_manager {
    public static String app_notice_version = null;
    public static String app_notice_content = null;

    public static void init(){
        app_notice_version = null;
        app_notice_content = null;
    }

    public static String get_app_notice_version(){
        return app_notice_version;
    }

    public static String get_app_notice_content(){
        return app_notice_content;
    }
}
