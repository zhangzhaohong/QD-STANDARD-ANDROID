package com.autumn.sdk.manager.update;

public class update_manager {
    public static String software_version = null;
    public static String changelog = null;
    public static String download_url = null;
    public static String download_code = null;
    public static String force_update = null;

    public static void init(){
        software_version = null;
        changelog = null;
        download_url = null;
        download_code = null;
        force_update = null;
    }

    public static String get_software_version(){
        return software_version;
    }

    public static String get_changelog(){
        return changelog;
    }

    public static String get_download_url(){
        return download_url;
    }

    public static String get_download_code(){
        return download_code;
    }

    public static String get_force_update(){
        return force_update;
    }
}
