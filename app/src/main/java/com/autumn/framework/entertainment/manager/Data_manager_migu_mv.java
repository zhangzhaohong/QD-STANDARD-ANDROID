package com.autumn.framework.entertainment.manager;

import java.util.Arrays;

public class Data_manager_migu_mv {

    private static int number = 10;

    private static int temp_number = 10;

    private static String[] url_all = new String[0];

    private static String[] url = new String[temp_number];

    private static int page_num = 0;

    private void setNumber(int number_array){
        number = number_array;
    }

    public void setTemp_number(int number_array){
        temp_number = number_array;
    }

    private void set_url(int num, String url){
        url_all[num] = url;
    }

    private void setUrl(int num, String url_temp) {
        url[num] = url_temp;
    }

    public Data_manager_migu_mv(int num) {

        setNumber(num);
        url_all = null;
        url_all = new String[number];

    }

    public void set_data(int page, String S_url, int i){
        page_num = page;
        if (page <= 1) {
            set_url(i, S_url);
        }else {
            if (i == 0){
                url = null;
                init_temp_array();
            }
            setUrl(i, S_url);
        }
    }

    private void init_array() {

        url_all = new String[number];

    }

    private void init_temp_array(){

        url = new String[temp_number];
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

    public static String[] getUrl_all(){
        return url_all;
    }

}
