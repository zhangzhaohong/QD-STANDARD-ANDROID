package com.autumn.framework.entertainment.manager;

public class Data_manager_7 {

    private static String[] type_all = new String[0];
    private static String[] url_all = new String[0];
    private static String[] pic_url_all = new String[0];
    private int number = 0;

    public Data_manager_7(int num) {

        setNumber(num);
        type_all = null;
        url_all = null;
        pic_url_all = null;
        type_all = new String[number];
        url_all = new String[number];
        pic_url_all = new String[number];

    }

    private void setNumber(int num) {
        number = num;
    }

    public void set_data(String S_type, String S_Url, String S_picUrl, int i){
        set_type(i,S_type);
        set_url(i, S_Url);
        set_Pic_url(i, S_picUrl);
    }

    private void set_type(int i, String s_type) {
        type_all[i] = s_type;
    }

    private void set_url(int i, String s_url) {
        url_all[i] = s_url;
    }

    private void set_Pic_url(int i, String s_picUrl){
        pic_url_all[i] = s_picUrl;
    }

    public static String[] getType_all(){return type_all;}

    public static String[] getUrl_all(){
        return url_all;
    }

    public static String[] getPicUrl_all(){
        return pic_url_all;
    }

}
