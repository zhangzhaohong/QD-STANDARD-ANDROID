package com.autumn.framework.entertainment.manager;

public class Data_manager_1 {

    private static int number = 10;

    private static String[] name_all = new String[number];

    private static String[] background_all = new String[number];

    private static String[] url_all = new String[number];

    private void set_name(int num, String name){
        name_all[num] = name;
    }

    private void set_background(int num, String background){
        background_all[num] = background;
    }

    private void set_url(int num, String url){
        url_all[num] = url;
    }

    public Data_manager_1() {
        //init_array();
    }

    private void init_array() {

        name_all = new String[number];
        background_all = new String[number];
        url_all = new String[number];

    }

    public void setData(String name, String background, String url, int i){
        set_name(i,name);
        set_background(i,background);
        set_url(i,url);
    }

    public static String[] getName(){
        return name_all;
    }

    public static String[] getBackground(){
        return background_all;
    }

    public static String[] getUrl(){
        return url_all;
    }

}
