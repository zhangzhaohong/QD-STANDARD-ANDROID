package com.autumn.framework.entertainment.manager;

public class Data_manager_Music_List {

    private static String[] name_all = new String[0];
    private static String[] singer_all = new String[0];
    private static String[] url_all = new String[0];
    private static String[] pic_url_all = new String[0];
    private int number = 0;

    public Data_manager_Music_List(int num) {

        setNumber(num);
        name_all = null;
        singer_all = null;
        url_all = null;
        pic_url_all = null;
        name_all = new String[number];
        singer_all = new String[number];
        url_all = new String[number];
        pic_url_all = new String[number];

    }

    private void setNumber(int num) {
        number = num;
    }

    private void set_name(int i, String s_name) {
        name_all[i] = s_name;
    }

    private void set_singer(int i, String s_singer){
        singer_all[i] = s_singer;
    }

    private void set_url(int i, String s_url) {
        url_all[i] = s_url;
    }

    private void set_Pic_url(int i, String s_picUrl){
        pic_url_all[i] = s_picUrl;
    }

    public static String[] getName_all(){return name_all;}

    public static String[] getSinger_all(){return singer_all;}

    public static String[] getUrl_all(){
        return url_all;
    }

    public static String[] getPicUrl_all(){
        return pic_url_all;
    }

    public void set_data(String name, String singer, String background, String url, int i) {
        set_name(i, name);
        set_singer(i, singer);
        set_Pic_url(i, background);
        set_url(i, url);
    }
}
