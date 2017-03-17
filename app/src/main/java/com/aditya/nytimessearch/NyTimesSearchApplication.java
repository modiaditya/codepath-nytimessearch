package com.aditya.nytimessearch;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * Created by amodi on 3/16/17.
 */

public class NyTimesSearchApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
