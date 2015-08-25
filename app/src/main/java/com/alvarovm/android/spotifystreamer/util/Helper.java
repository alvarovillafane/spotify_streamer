package com.alvarovm.android.spotifystreamer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

/**
 * Created by root on 29/06/15.
 */
public class Helper {

    public static boolean isValidURL(String uriStr) {
        if (uriStr != null && Patterns.WEB_URL.matcher(uriStr).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static int millisecondsToSeconds(int millis){
        return millis/1000;
    }
}
