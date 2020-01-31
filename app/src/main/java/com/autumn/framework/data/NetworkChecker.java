package com.autumn.framework.data;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.autumn.framework.Log.LogUtil;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkChecker {

    /**
     * 是否使用代理(WiFi状态下的,避免被抓包)
     */
    public static boolean isWifiProxy(Context context){
        final boolean is_ics_or_later = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (is_ics_or_later) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portstr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));
            System.out.println(proxyAddress + "~");
            System.out.println("port = " + proxyPort);
        }else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
            LogUtil.e("address = " + proxyAddress);
            LogUtil.e("port = " + proxyPort);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }
    /**
     * 是否正在使用VPN
     */


    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    LogUtil.d("isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


}
