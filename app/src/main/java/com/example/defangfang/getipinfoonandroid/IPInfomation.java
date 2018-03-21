package com.example.defangfang.getipinfoonandroid;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


/**
 * Created by defangfang on 2017/3/29.
 */

public class IPInfomation {
    private  WifiManager mWifiManager;
    private  WifiInfo mWifiInfo;

    public IPInfomation(Context mContext){

        //获取wifi服务
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        mWifiInfo = mWifiManager.getConnectionInfo();
    }
    public  String getWIFILocalIpAdress() {
        int ipAddress = mWifiInfo.getIpAddress();
        return formatIpAddress(ipAddress);
    }
    private static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF ) + "." +
                ((ipAdress >> 8 ) & 0xFF) + "." +
                ((ipAdress >> 16 ) & 0xFF) + "." +
                ( ipAdress >> 24 & 0xFF) ;
    }
    public  String getMacAddress() {
        return mWifiInfo.getMacAddress();
    }


}
