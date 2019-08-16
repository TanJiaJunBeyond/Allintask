package com.allintask.lingdao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by TanJiaJun on 2018/1/25.
 */

public class IPAddressUtils {

    public static String getIPAddress(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        String IPAddress = null;

        if (null != networkInfo && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements(); ) {
                        NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();

                        for (Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses(); inetAddressEnumeration.hasMoreElements(); ) {
                            InetAddress inetAddress = inetAddressEnumeration.nextElement();

                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                IPAddress = intIPToStringIP(wifiInfo.getIpAddress());
            }
        }
        return IPAddress;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param IP
     * @return
     */
    public static String intIPToStringIP(int IP) {
        return (IP & 0xFF) + "." + ((IP >> 8) & 0xFF) + "." + ((IP >> 16) & 0xFF) + "." + (IP >> 24 & 0xFF);
    }

}
