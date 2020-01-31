package com.autumn.framework.update.data;

public class Update_url_manager {
    private static String url_302 = null;

    public Update_url_manager(String url_302){
        Update_url_manager.url_302 = url_302;
    }

    public static String getUrl() {
        return url_302;
    }

    public static void setUrl(String url_302) {
        Update_url_manager.url_302 = url_302;
    }
}
