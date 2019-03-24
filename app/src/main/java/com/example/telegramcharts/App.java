package com.example.telegramcharts;

import android.app.Application;

public class App extends Application {
    private PreferenceHelper preferenceHelper;
    private static App INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        preferenceHelper = new PreferenceHelper(getBaseContext());
    }

    public static App getInstance(){
        return INSTANCE;
    }

    public PreferenceHelper getPreferenceHelper() {
        return preferenceHelper;
    }
}
