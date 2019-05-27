package com.digywood.tms;

import android.app.Application;

import com.digywood.tms.ConnectivityReceiver;
import com.google.android.gms.ads.MobileAds;


public class MyApplication extends Application {
    AppEnvironment appEnvironment;
    UserMode userMode;

    private static MyApplication mInstance;

    //String APP_ID,BANNER_ID,INTERSTITIA_ID,REWARD_ID;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        appEnvironment = AppEnvironment.DEBUG;
        userMode=UserMode.NON_PRIME;

        mInstance = this;
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    /*public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public String getBANNER_ID() {
        return BANNER_ID;
    }

    public void setBANNER_ID(String BANNER_ID) {
        this.BANNER_ID = BANNER_ID;
    }

    public String getINTERSTITIA_ID() {
        return INTERSTITIA_ID;
    }

    public void setINTERSTITIA_ID(String INTERSTITIA_ID) {
        this.INTERSTITIA_ID = INTERSTITIA_ID;
    }

    public String getREWARD_ID() {
        return REWARD_ID;
    }

    public void setREWARD_ID(String REWARD_ID) {
        this.REWARD_ID = REWARD_ID;
    }*/
}
