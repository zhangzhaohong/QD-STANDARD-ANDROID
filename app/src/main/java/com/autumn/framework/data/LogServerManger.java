package com.autumn.framework.data;

public class LogServerManger {
    private static boolean ServerStatus = false;

    public static void setLogServerStatus(boolean b) {
        ServerStatus = b;
    }

    public static boolean getLogServerStatus() {
        return ServerStatus;
    }
}
