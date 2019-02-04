package com.digywood.tms;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;


public class MyApplication extends Application {
    AppEnvironment appEnvironment;
    UserMode userMode;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        appEnvironment = AppEnvironment.PRODUCTION;
        userMode=UserMode.NON_PRIME;
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
}
