package com.autumn.framework.entertainment.manager;

public class Data_manager_4 {

    private static int number = 10;

    private static String[] content_all = new String[number];

    private void set_content(int num, String content){
        content_all[num] = content;
    }

    public Data_manager_4() {

    }

    private void init_array() {

        content_all = new String[number];

    }

    public void setData(String content, int i) {
        set_content(i,content);
    }

    public static String[] getContent(){
        return content_all;
    }

}
