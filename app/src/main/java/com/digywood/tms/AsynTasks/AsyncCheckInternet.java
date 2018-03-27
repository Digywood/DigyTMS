package com.digywood.tms.AsynTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.digywood.tms.INetStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NARESH on 24-12-2016.
 */

public class AsyncCheckInternet extends AsyncTask<Void,String, Boolean> {

    INetStatus listner;
    static Context context;

    public AsyncCheckInternet(Context applicationContext, INetStatus iListner){
        AsyncCheckInternet.context=applicationContext;
        this.listner=iListner;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        boolean res=isInternetWorking();
        return res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean res) {
        super.onPostExecute(res);
        try {
            if (res != null) {
                listner.inetSatus(res);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("InternetCheck----",e.toString());
        }

    }
    public boolean isInternetWorking() {
        boolean success = false;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://www.google.com");
            connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("InternetCheck---",e.toString());
        }
        finally {
            if (connection != null)
            connection.disconnect();
        }
        return success;
    }
}
