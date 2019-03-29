package com.digywood.tms;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;

import java.util.Date;

public class AdColonUtility {

    static final private String APP_ID = "app08c4ed552f48483597";
    static final private String TEST_ZONE_ID = "vz3d3dc16c08cb49d9b5";
    static final private String PRODUCTION_ZONE_ID = "vze1ab5c474a744b819d";
    static final private String TAG = "AdColonUtility";
    //private ProgressBar progress;
    static AdColonyInterstitial ad;
    static AdColonyInterstitialListener listener;
    static AdColonyAdOptions adOptions;

    // Your user's consent String. In this case, the user has given consent to store
    // and process personal information.
    static String consent = "1";
    static AppEnvironment appEnvironment;
    static UserMode userMode;

    public static void PlayInterstitialAds(Activity activity){

        final String ZONE_ID;
        // Construct optional app options object to be sent with configure
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID(""+new Date())
                .setKeepScreenOn(true)
                .setGDPRConsentString(consent)
                .setGDPRRequired(true);

        appEnvironment = ((MyApplication) activity.getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) activity.getApplication()).getUserMode();//getting User Mode

        if(appEnvironment.debug()){
            ZONE_ID=TEST_ZONE_ID;
        }else {
            ZONE_ID=PRODUCTION_ZONE_ID;
        }

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(activity, appOptions, APP_ID, ZONE_ID);

        // Optional user metadata sent with the ad options in each request
        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(26)
                .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                .setUserGender(AdColonyUserMetadata.USER_MALE);

        // Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions().setUserMetadata(metadata);

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.

        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                AdColonUtility.ad = ad;
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                Log.d( TAG, "onRequestNotFilled " + zone.getZoneID()+"zone"+zone);
                Log.d(TAG, "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring

                AdColony.requestInterstitial(ZONE_ID, this, adOptions);
                Log.d(TAG, "onExpiring");
            }
        };

        AdColony.requestInterstitial(ZONE_ID, listener,adOptions);

        if(ad!=null)
            ad.show();


    }


    public static void PlayRewardedInterstitialAds(Activity activity){

        final String ZONE_ID;
        appEnvironment = ((MyApplication) activity.getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) activity.getApplication()).getUserMode();//getting User Mode

        if(appEnvironment.debug()){
            ZONE_ID=TEST_ZONE_ID;
        }else {
            ZONE_ID=PRODUCTION_ZONE_ID;
        }

        // Construct optional app options object to be sent with configure
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID(""+new Date())
                .setKeepScreenOn(true)
                .setGDPRConsentString(consent)
                .setGDPRRequired(true);

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(activity, appOptions, APP_ID, ZONE_ID);

        // Optional user metadata sent with the ad options in each request
        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(26)
                .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                .setUserGender(AdColonyUserMetadata.USER_MALE);

        // Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions()
                .enableConfirmationDialog(true)
                .enableResultsDialog(true)
                .setUserMetadata(metadata);

        // Create and set a reward listener
        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                // Query reward object for info here
                Log.d( TAG, "onReward" );
            }
        });

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                AdColonUtility.ad = ad;
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                Log.d(TAG, "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
                AdColony.requestInterstitial(ZONE_ID, this, adOptions);
                Log.d(TAG, "onExpiring");
            }
        };

        if(ad!=null)
            ad.show();

    }

    public static void requestInterstitial(Activity activity){

        final String ZONE_ID;
        appEnvironment = ((MyApplication) activity.getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) activity.getApplication()).getUserMode();//getting User Mode

        if(appEnvironment.debug()){
            ZONE_ID=TEST_ZONE_ID;
        }else {
            ZONE_ID=PRODUCTION_ZONE_ID;
        }
        // It's somewhat arbitrary when your ad request should be made. Here we are simply making
        // a request if there is no valid ad available onResume, but really this can be done at any
        // reasonable time before you plan on showing an ad.
        if (ad == null || ad.isExpired()) {
            // Optionally update location info in the ad options for each request:
            // LocationManager locationManager =
            //     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Location location =
            //     locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // adOptions.setUserMetadata(adOptions.getUserMetadata().setUserLocation(location));
            AdColony.requestInterstitial(ZONE_ID, listener, adOptions);
        }
    }
}
