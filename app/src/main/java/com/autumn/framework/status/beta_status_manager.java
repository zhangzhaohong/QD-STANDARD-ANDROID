package com.autumn.framework.status;

public class beta_status_manager {

    public static boolean act_beta = false;

    public static void init(){
        act_beta = false;
    }

    public static Boolean get_beta(){
        return act_beta;
    }

}
