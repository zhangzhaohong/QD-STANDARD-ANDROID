package com.autumn.framework.data;

public class Page_manager {
    private static int LastPositon = 0;

    public static void setLastPosition(int position) {
        LastPositon = position;
    }

    public static int getLastPosition() {
        return LastPositon;
    }
}
