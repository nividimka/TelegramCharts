package com.example.telegramcharts;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.telegramcharts.data.Chart;
import com.example.telegramcharts.utils.JSONUtils;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends Activity {
    List<Chart> chartList;
    MenuItem checkedMenuItem;
    MenuItem modeMenuItem;
    Mode mode;
    ChartView chartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartView = findViewById(R.id.chart_view);
        JSONArray chartsJson = JSONUtils.loadJSONArrayFromAsset(getBaseContext());
        chartList = JSONUtils.parseCharts(chartsJson);
        invalidateOptionsMenu();
        mode = Mode.DAY_MODE;
        updateModeUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        modeMenuItem = menu.findItem(R.id.mode);
        updateModeState();
        for (int i = 0; i < chartList.size(); i++) {
            Chart chart = chartList.get(i);
            MenuItem menuItem = menu.add(0, i, Menu.NONE, chart.getName()).setCheckable(true);
            if(i==0) {
                menuItem.setChecked(true);
                checkedMenuItem = menuItem;
                selectChart(chartList.get(checkedMenuItem.getItemId()));
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void updateModeState() {
        if(mode.equals(Mode.DAY_MODE)){
            modeMenuItem.setIcon(R.drawable.ic_moon_white);
        }else{
            modeMenuItem.setIcon(R.drawable.ic_moon_white);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()!=modeMenuItem.getItemId()) {
            checkedMenuItem.setChecked(false);
            item.setChecked(true);
            checkedMenuItem = item;
            selectChart(chartList.get(checkedMenuItem.getItemId()));
        }else{
            if (mode.equals(Mode.DAY_MODE)) {
                mode = Mode.NIGHT_MODE;
            } else {
                mode = Mode.DAY_MODE;
            }
            updateModeState();
            updateModeUi();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateModeUi() {
        updateToolbar();
        updateStatusbar();
        updateBackgroundColor();
        chartView.updateMode(mode);
    }

    private void updateBackgroundColor() {
        getWindow().getDecorView().setBackgroundColor(mode.backgroundColor);
    }

    private void updateToolbar(){
        getActionBar().setBackgroundDrawable(new ColorDrawable(mode.toolbarColor));
    }
    private void updateStatusbar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mode.statusbarColor);
        }
    }

    public void selectChart(Chart chart){
        chartView.setChart(chart);
    }
}
