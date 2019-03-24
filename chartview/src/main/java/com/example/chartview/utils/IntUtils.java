package com.example.chartview.utils;

import java.util.Locale;

public class IntUtils {

    public static String formatInt(int value){
        if(value/1000000>0){
            return String.format(Locale.ENGLISH,"%.2fM",value *1.0/ 1000000);
        }
        if(value/1000>0){
            return String.format(Locale.ENGLISH,"%.2fK",value *1.0/ 1000);
        }
        return String.valueOf(value);
    }
}
