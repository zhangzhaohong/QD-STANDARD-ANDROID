package com.autumn.sdk.manager.beta;

public class beta_manager {


    public static String beta_private_key = null;

    public static void init(){
        beta_private_key = null;
    }

    public static String get_private_key(){
        return beta_private_key;
    }
}
