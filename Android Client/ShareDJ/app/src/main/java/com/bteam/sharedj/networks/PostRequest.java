package com.bteam.sharedj.networks;

import android.os.AsyncTask;
import android.util.Log;


import com.bteam.sharedj.listeners.INetworkCallback;
import com.bteam.sharedj.models.Data;
import com.bteam.sharedj.utils.Config;
import com.bteam.sharedj.utils.LogUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class PostRequest extends AsyncTask<List<NameValuePair>, Integer, Data> {

    private INetworkCallback mNetWorkCallBack;
    private Data.Typer typer = Data.Typer.NODE;
    private boolean isRun = false;
    private String url;

    public PostRequest() {
        isRun = true;
    }

    public void setURL(String url) {
        this.url = url;
    }

    @Override
    protected Data doInBackground(List<NameValuePair>... arg0) {
        List<NameValuePair> list = arg0[0];
        InputStream is = null;

        if (list == null)
            return null;
        if (list.size() < 1)
            return null;
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android_id:" + Config.android_id);
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));


            HttpResponse httpResponse = httpclient.execute(httppost);
            is = httpResponse.getEntity().getContent();
            JSONObject obj = UtilsNetwork.getJSonObject(is);
            Data result = new Data();

            if (typer != Data.Typer.NODE) {
                result.mJson = obj;
                result.mTyper = typer;
                LogUtils.debug("The response is: " + result.mJson.toString());

            } else {
                Log.d("TAG", "Error: Bạn chưa set Typer cho connection nay");
            }
            return result;
        } catch (Exception e) {
            Log.e("Log", e.getLocalizedMessage(), e);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mNetWorkCallBack != null) {
            mNetWorkCallBack.onLoadStart();
        }
    }

    @Override
    protected void onPostExecute(Data result) {
        super.onPostExecute(result);
        if (result != null) {
            if (mNetWorkCallBack != null) {
                mNetWorkCallBack.onResultData(result);
            }
        } else {
            if (mNetWorkCallBack != null) {
                mNetWorkCallBack.onErrorData();
            }
        }
    }

    public Data.Typer getTyper() {
        return typer;
    }

    public void setTyper(Data.Typer typer) {
        this.typer = typer;
    }

    public void setCallBackListener(INetworkCallback callBack) {
        mNetWorkCallBack = callBack;
    }


    private long totalSize;

}
