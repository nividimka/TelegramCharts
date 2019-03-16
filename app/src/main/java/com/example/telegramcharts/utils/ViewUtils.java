package com.example.telegramcharts.utils;

import android.content.Context;

public class ViewUtils {

    public static int toPx(Context context, int dp){
        return (int) (dp * (context.getResources().getDisplayMetrics().densityDpi / 160.0));
    }
    public static int toDp(Context context, int px){
        return (int) (px / (context.getResources().getDisplayMetrics().densityDpi / 160.0));
    }
}
