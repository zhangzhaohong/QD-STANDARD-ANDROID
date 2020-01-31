package com.autumn.framework.entertainment.manager;

public class Data_manager_6 {
    private static String pic_url;
    private static String volume;
    private static String post_date;
    private static String forward;
    private static String words_info;

    public Data_manager_6(String pic_url, String volume, String post_date, String forward, String words_info) {
        this.pic_url = pic_url;
        this.volume = volume;
        this.post_date = post_date;
        this.forward = forward;
        this.words_info = words_info;
    }

    public static String getPic_url() {
        return pic_url;
    }

    public static String getVolume() {
        return volume;
    }

    public static String getPost_date() {
        return post_date;
    }

    public static String getForward() {
        return forward;
    }

    public static String getWords_info() {
        return words_info;
    }
}
