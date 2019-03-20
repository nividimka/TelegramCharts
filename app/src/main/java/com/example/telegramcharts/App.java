package com.example.telegramcharts;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        TinyDancer.create().show(this);

        //or customazed config
        TinyDancer.create()
                .redFlagPercentage(.1f) // set red indicator for 10%
                .startingGravity(Gravity.TOP)
                .startingXPosition(200)
                .startingYPosition(600)
                .show(this);
    }
}
