package com.autumn.framework.entertainment.manager;

import java.util.Arrays;

public class Data_manager_7_hotMusic {

    private static int number = 10;

    private static int temp_number = 10;

    private static String[] name_all = new String[0];

    private static String[] singer_all = new String[0];

    private static String[] background_all = new String[0];

    private static String[] url_all = new String[0];

    private static String[] name = new String[temp_number];

    private static String[] singer = new String[temp_number];

    private static String[] background = new String[temp_number];

    private static String[] url = new String[temp_number];
    private static int page_num = 0;

    private void setNumber(int number_array){
        number = number_array;
    }

    public void setTemp_number(int number_array){
        temp_number = number_array;
    }

    private void set_name(int num, String name){
        name_all[num] = name;
    }

    private void set_singer(int num, String singer){
        singer_all[num] = singer;
    }

    private void set_background(int num, String background){
        background_all[num] = background;
    }

    private void set_url(int num, String url){
        url_all[num] = url;
    }

    private void setName(int num, String name_temp) {
        name[num] = name_temp;
    }

    private void setSinger(int num, String singer_temp) {
        singer[num] = singer_temp;
    }

    private void setBackground(int num, String background_temp) {
        background[num] = background_temp;
    }

    private void setUrl(int num, String url_temp) {
        url[num] = url_temp;
    }

    public Data_manager_7_hotMusic(int num) {

        setNumber(num);
        name_all = null;
        singer_all = null;
        background_all = null;
        url_all = null;
        name_all = new String[number];
        singer_all = new String[number];
        background_all = new String[number];
        url_all = new String[number];

    }

    public void set_data(int page,String S_name,String S_singer, String S_background, String S_url, int i){
        page_num = page;
        if (page <= 1) {
            set_name(i, S_name);
            set_singer(i, S_singer);
            set_background(i, S_background);
            set_url(i, S_url);
        }else {
            if (i == 0){
                name = null;
                singer = null;
                background = null;
                url = null;
                init_temp_array();
            }
            setName(i, S_name);
            setSinger(i, S_singer);
            setBackground(i, S_background);
            setUrl(i, S_url);
        }
    }

    private void init_array() {

        name_all = new String[number];
        singer_all = new String[number];
        background_all = new String[number];
        url_all = new String[number];

    }

    private void init_temp_array(){
        name = new String[temp_number];
        singer = new String[temp_number];
        background = new String[temp_number];
        url = new String[temp_number];
    }

    public static String[] getName(){
        if (page_num > 1) {
            int length_1 = name_all.length;
            int length_2 = name.length;
            name_all = Arrays.copyOf(name_all, length_1 + length_2);
            System.arraycopy(name, 0 , name_all, length_1, length_2);
        }
        return name_all;
    }

    public static String[] getSinger(){
        if (page_num > 1) {
            int length_1 = singer_all.length;
            int length_2 = singer.length;
            singer_all = Arrays.copyOf(singer_all, length_1 + length_2);
            System.arraycopy(singer, 0 , singer_all, length_1, length_2);
        }
        return singer_all;
    }

    public static String[] getBackground(){
        if (page_num > 1) {
            int length_1 = background_all.length;
            int length_2 = background.length;
            background_all = Arrays.copyOf(background_all, length_1 + length_2);
            System.arraycopy(background, 0 , background_all, length_1, length_2);
        }
        return background_all;
    }

    public static String[] getUrl(){
        if (page_num > 1) {
            int length_1 = url_all.length;
            int length_2 = url.length;
            url_all = Arrays.copyOf(url_all, length_1 + length_2);
            System.arraycopy(url, 0 , url_all, length_1, length_2);
        }
        return url_all;
    }

    public static String[] getName_all(){
        return name_all;
    }

    public static String[] getSinger_all(){
        return singer_all;
    }

    public static String[] getBackground_all(){
        return background_all;
    }

    public static String[] getUrl_all(){
        return url_all;
    }

}
