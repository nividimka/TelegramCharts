package com.example.telegramcharts;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "PREFERENCES";
    private static final String CHART_ID = "CHART_ID";
    private static final String MODE_ID = "MODE_ID";

    public PreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void saveChartId(int chartId){
        sharedPreferences.edit().putInt(CHART_ID, chartId).apply();
    }
    public int getChartId(){
        return sharedPreferences.getInt(CHART_ID, 0);
    }
    public void saveModeId(int modeId){
        sharedPreferences.edit().putInt(MODE_ID, modeId).apply();
    }
    public int getModeId(){
        return sharedPreferences.getInt(MODE_ID, 0);
    }
}
