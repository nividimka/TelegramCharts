package com.example.telegramcharts;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.telegramcharts.data.Chart;
import com.example.telegramcharts.data.Line;
import com.example.telegramcharts.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Chart> chartList;
    public static final String X = "x";
    public static final String LINE = "line";
    MenuItem checkedMenuItem;
    ChartView chartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartView = findViewById(R.id.chart_view);
        JSONArray chartsJson = JSONUtils.loadJSONArrayFromAsset(getBaseContext());
        chartList = new ArrayList<>();
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
                            case X:
                                Line<Long> xLine = new Line<>();
                                Long[] columns = new Long[jsonLines.length() - 1];
                                for(int k = 1;k < jsonLines.length();k++){
                                    columns[k - 1] = jsonLines.getLong(k);
                                }
                                xLine.setColumns(columns);
                                chart.setXLine(xLine);
                                break;
                            case LINE:
                                Line<Integer> yLine = new Line<>();
                                Integer[] yColumns = new Integer[jsonLines.length() - 1];
                                for(int k = 1;k < jsonLines.length();k++){
                                    yColumns[k - 1] = jsonLines.getInt(k);
                                }
                                yLine.setColumns(yColumns);
                                yLine.setName(namesJson.getString(lineId));
                                yLine.setColor(Color.parseColor(colorsJson.getString(lineId)));
                                chart.addYLine(yLine);
                                break;
                        }
                    }
                    chartList.add(chart);
                }
                invalidateOptionsMenu();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        for (int i = 0; i < chartList.size(); i++) {
            Chart chart = chartList.get(i);
            MenuItem menuItem = menu.add(0, i, Menu.NONE, chart.getName()).setCheckable(true);
            if(i==0) {
                menuItem.setChecked(true);
                checkedMenuItem = menuItem;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        checkedMenuItem.setChecked(false);
        item.setChecked(true);
        checkedMenuItem = item;
        Toast.makeText(getBaseContext(), "clicked " + chartList.get(item.getItemId()).getName(), Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
