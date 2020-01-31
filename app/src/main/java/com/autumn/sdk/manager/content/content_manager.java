package com.autumn.sdk.manager.content;

public class content_manager {
    public static String app_content_version = null;
    public static String app_content = null;

    public static void init(){
        app_content_version = null;
        app_content = null;
    }

    public static String get_app_content_version(){
        return app_content_version;
    }

    public static String get_app_content(){
        return app_content;
    }
}

