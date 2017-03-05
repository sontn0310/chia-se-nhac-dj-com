package com.bteam.sharedj.networks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.bteam.sharedj.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class UtilsNetwork {

    public static JSONObject getJSonObject(InputStream in) {
        JSONObject result = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8192);
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            result = new JSONObject(sb.toString());
        } catch (JSONException e) {
            LogUtils.error("JSonObject :" + e.getMessage());
        } catch (Exception e) {
            LogUtils.error("JSonObject :" + e.getMessage());
        }

        return result;
    }

    public static JSONArray getJSonArray(InputStream in) {
        JSONArray result = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8192);
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            result = new JSONArray(sb.toString());
        } catch (JSONException e) {
            LogUtils.error("JSonObject :" + e.getMessage());
        } catch (Exception e) {
            LogUtils.error("JSonObject :" + e.getMessage());
        }

        return result;
    }

    public static String getResult(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8192);
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (sb.toString());
    }
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return 0;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return 1;
        }
        return 2;
    }
}
