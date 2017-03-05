package com.bteam.sharedj.networks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.bteam.sharedj.listeners.INetworkCallback;
import com.bteam.sharedj.models.Data;
import com.bteam.sharedj.utils.Config;
import com.bteam.sharedj.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetRequest extends AsyncTask<String, Void, Data> {

    private INetworkCallback iNetWorkCallback;
    private Data.Typer mTyper = Data.Typer.NODE;
    private Context mContext;

    public GetRequest(Context mContext) {
        this.mContext = mContext;
    }

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

    }

    @Override
    protected Data doInBackground(String... params) {
        Data result = null;
        JSONArray arr = null;
        JSONObject obj = null;
        InputStream is = null;
        try {
            String myUrl = Config.EndpointService + params[0];
            URL url = new URL(myUrl);
            LogUtils.error("url: " + myUrl);
            LogUtils.error("params: " + params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            LogUtils.error("Response: " + is.toString());

            if (mTyper != Data.Typer.NODE) {
                obj = UtilsNetwork.getJSonObject(is);
                result = new Data();
                result.mJson = obj;
                LogUtils.debug("The response is: " + result.mJson.toString());
                result.mTyper = mTyper;

            } else {
                LogUtils.infor("Error: Bạn chưa set Typer cho connection này");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Data result) {
        // if (progressDialog.isShowing()) {
        // progressDialog.dismiss();
        // }

        if (iNetWorkCallback != null) {
            if (result == null) {
                iNetWorkCallback.onErrorData();
            } else {
                iNetWorkCallback.onResultData(result);
            }
        }
    }

    public void dismisProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void setOnNetWorkCallBack(INetworkCallback inetwork) {
        this.iNetWorkCallback = inetwork;
    }

    public void setTyper(Data.Typer ty) {
        this.mTyper = ty;
    }
}
