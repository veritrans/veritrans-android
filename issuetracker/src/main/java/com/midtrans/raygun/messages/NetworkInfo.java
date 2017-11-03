package com.midtrans.raygun.messages;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkInfo {
    private List<String> iPAddress = new ArrayList<String>();
    private String networkConnectivityState;

    public NetworkInfo(Context context) {
        readIPAddress();
        networkConnectivityState = readNetworkConnectivityState(context);
    }

    public List<String> getiPAddress() {
        return iPAddress;
    }

    public void setiPAddress(List<String> iPAddress) {
        this.iPAddress = iPAddress;
    }

    private String readNetworkConnectivityState(Context context) {
        String result = "Not connected";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            if (info.isConnected()) {
                result = "Connected - ";

                int type = info.getType();

                switch (type) {
                    case ConnectivityManager.TYPE_WIFI:
                        result += "WiFi";
                        break;
                    case ConnectivityManager.TYPE_WIMAX:
                        result += "WiMax";
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                    case ConnectivityManager.TYPE_MOBILE_DUN:
                    case ConnectivityManager.TYPE_MOBILE_HIPRI:
                    case ConnectivityManager.TYPE_MOBILE_MMS:
                    case ConnectivityManager.TYPE_MOBILE_SUPL:
                        result += "Mobile - ";

                        int subtype = info.getSubtype();
                        switch (subtype) {
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                                result += "1xRTT";
                                break;
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                                result += "CDMA";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                                result += "EDGE";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                result += "EVDO_0";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                result += "EVDO_A";
                                break;
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                                result += "GPRS";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                                result += "HSDPA";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                                result += "HSPA";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                                result += "HSUPA";
                                break;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                                result += "UMTS";
                                break;
                            case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                                result += "IDEN";
                                break;
                            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            default:
                                result += "subtype unknown/EVDO_B/EHRPD/LTE/HSPAP or similar";
                        }
                        break;
                    default:
                        result += "unknown type";
                }
            }
        }

        return result;
    }

    private void readIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());

                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = isIpV4(sAddr);

                        if (isIPv4) {
                            if (!iPAddress.contains(sAddr)) {
                                iPAddress.add(sAddr);
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                String delimited = delim < 0 ? sAddr : sAddr.substring(0, delim);

                                if (!iPAddress.contains(delimited)) {
                                    iPAddress.add(delimited);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public boolean isIpV4(String ipAddress) {
        try {

            InetAddress address = InetAddress.getByName(ipAddress);

            if (address instanceof Inet4Address) {
                return true;
            }

        } catch (UnknownHostException e) {
            Log.e("isIpV4", "ip:" + e.getMessage());
        }
        return false;
    }
}
