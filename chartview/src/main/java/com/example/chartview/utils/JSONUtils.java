package com.example.chartview.utils;

import android.content.Context;
import android.graphics.Color;

import com.example.chartview.data.Chart;
import com.example.chartview.data.Line;
import com.example.chartview.data.XLine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Chart> parseCharts(JSONArray chartsJson){
        List<Chart> chartList = new ArrayList<>();
        if (chartsJson != null) {
            try {
                for (int i = 0; i < chartsJson.length(); i++) {
                    JSONObject chartJson = chartsJson.getJSONObject(i);

                    Chart chart = new Chart();
                    chart.setName("Chart" + (i + 1));
                    JSONArray columnsJson = chartJson.getJSONArray("columns");
                    JSONObject typesJson = chartJson.getJSONObject("types");
                    JSONObject colorsJson = chartJson.getJSONObject("colors");
                    JSONObject namesJson = chartJson.getJSONObject("names");
                    for(int j = 0; j < columnsJson.length();j++){
                        JSONArray jsonLines = columnsJson.getJSONArray(j);
                        String lineId = jsonLines.getString(0);
                        switch (typesJson.getString(lineId)){
                            case XLine.ID:
                                XLine xLine = new XLine();
                                long[] columns = new long[jsonLines.length() - 1];
                                for(int k = 1;k < jsonLines.length();k++){
                                    columns[k - 1] = jsonLines.getLong(k);
                                }
                                xLine.setColumns(columns);
                                chart.setXLine(xLine);
                                break;
                            case Line.ID:
                                Line yLine = new Line();
                                int[] yColumns = new int[jsonLines.length() - 1];
                                int minY = jsonLines.getInt(1);
                                int maxY = jsonLines.getInt(1);
                                for(int k = 1;k < jsonLines.length();k++){
                                    yColumns[k - 1] = jsonLines.getInt(k);
                                    if(jsonLines.getInt(k) < minY){
                                        minY = jsonLines.getInt(k);
                                    }
                                    if(jsonLines.getInt(k) > maxY){
                                        maxY = jsonLines.getInt(k);
                                    }
                                }
                                yLine.setColumns(yColumns);
                                yLine.setMaxY(maxY);
                                yLine.setMinY(minY);
                                yLine.setName(namesJson.getString(lineId));
                                yLine.setColor(Color.parseColor(colorsJson.getString(lineId)));
                                chart.addYLine(yLine);
                                break;
                        }
                    }
                    chartList.add(chart);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return chartList;
    }

}
