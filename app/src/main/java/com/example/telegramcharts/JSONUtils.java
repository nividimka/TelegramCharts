package com.example.telegramcharts;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class JSONUtils {

    public static JSONArray loadJSONArrayFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("chart_data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            return new JSONArray(json);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
