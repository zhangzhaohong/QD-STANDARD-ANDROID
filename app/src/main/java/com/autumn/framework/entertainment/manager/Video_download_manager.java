package com.autumn.framework.entertainment.manager;

public class Video_download_manager {
    private static Boolean status = false;

    public static Boolean getStatus() {
        return status;
    }

    public static void setStatus(boolean b) {
        status = b;
    }
}
