package com.digywood.tms.AsynTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.digywood.tms.INetStatus;
import com.digywood.tms.URLClass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class AsyncCheckInternet_WithOutProgressBar extends AsyncTask<Void,String, Boolean> {
    //private ProgressDialog dialog;
    INetStatus listner;
    static Context context;

    public AsyncCheckInternet_WithOutProgressBar(Context applicationContext, INetStatus iListner){
        /*dialog = new ProgressDialog(applicationContext);
        dialog.setCanceledOnTouchOutside(false)*/;
        AsyncCheckInternet_WithOutProgressBar.context=applicationContext;
        this.listner=iListner;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        boolean res=isInternetWorking();
        return res;
    }

    @Override
    protected void onPreExecute() {
        /*dialog.setMessage("Checking Internet Connection...");
        dialog.show();*/
    }

    @Override
    protected void onPostExecute(Boolean res) {
        super.onPostExecute(res);

        if (res != null) {
            listner.inetSatus(res);
        }

        /*try {
            if ((dialog !=null) && (dialog.isShowing())) {
                dialog.dismiss();
                if (res != null) {
                    dialog.dismiss();
                    listner.inetSatus(res);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("InternetCheck----",e.toString());
        }*/

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
