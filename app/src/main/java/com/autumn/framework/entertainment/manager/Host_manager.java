package com.autumn.framework.entertainment.manager;

public class Host_manager {
    private static String ShortVideo_host = null;
    private static String Hot_Music_host = null;
    private static String Daily_host = null;
    private static String Music_host = null;

    public static void setDailyHost(String content) {
        Daily_host = content;
    }

    public static String getDailyHost() {
        return Daily_host;
    }

    public static void setMusicHost(String content) {
        Music_host = content;
    }

    public static String getMusicHost() {
        return Music_host;
    }

    public static void setHotMusicHost(String content) {
        Hot_Music_host = content;
    }

    public static String getHotMusicHost() {
        return Hot_Music_host;
    }

    public static void setShortVideoHost(String content) {
        ShortVideo_host = content;
    }

    public static String getShortVideoHost() {
        return ShortVideo_host;
    }
}
