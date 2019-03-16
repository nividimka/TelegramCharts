package com.example.telegramcharts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.telegramcharts.data.Chart;
import com.example.telegramcharts.data.Line;

import androidx.core.widget.CompoundButtonCompat;

public class ChartView extends LinearLayout {
    Chart chart;
    ViewGroup checkboxesView;
    FullChartView fullChartView;
    IncreasedChartView increasedChartView;
    Mode mode;

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.chart_view, this);
        checkboxesView = findViewById(R.id.checkboxes_view);
        fullChartView = findViewById(R.id.full_chart_view);
        increasedChartView = findViewById(R.id.increased_chart_view);
        fullChartView.setOnRangeChangeListener(increasedChartView);
        mode = Mode.DAY_MODE;
        updateBackgroundColor();
        initCheckboxes();
        initFullChart();
    }
    public void updateMode(Mode mode){
        this.mode = mode;
        updateBackgroundColor();
        initCheckboxes();
        fullChartView.setMode(mode);
        increasedChartView.setMode(mode);
        invalidate();
    }

    private void updateBackgroundColor(){
        setBackgroundColor(mode.viewBackgroundColor);
    }

    private void initFullChart() {
        if(chart!=null) {
            increasedChartView.initChart(chart);
            fullChartView.initChart(chart);
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void initCheckboxes(){
        if(chart!=null) {
            checkboxesView.removeAllViews();
            for (int i = 0; i < chart.getYLines().size();i++) {
                final Line line = chart.getYLines().get(i);
                CheckBox checkbox = new CheckBox(getContext());
                checkbox.setText(line.getName());
                checkbox.setTypeface(Typeface.SANS_SERIF);
                checkbox.setTextColor(mode.textColor);
                checkbox.setPadding(40,0,0,0);
                checkbox.setChecked(!line.isHidden());
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        line.setHidden(!isChecked);
                        changeMinMax();
                    }
                });
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(30,20,20,20);
                checkbox.setLayoutParams(params);
                CompoundButtonCompat.setButtonTintList(checkbox, ColorStateList.valueOf(line.getColor()));
                checkboxesView.addView(checkbox);
                View separator = new View(getContext());
                separator.setBackgroundColor(mode.separatorColor);
                LayoutParams separatorParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
                separatorParams.setMargins(150,0,40,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    separator.setElevation(0);
                }
                if(i!=chart.getYLines().size()-1) {
                    separator.setLayoutParams(separatorParams);
                    checkboxesView.addView(separator);
                }
            }
        }
    }

    private void changeMinMax() {
        if(chart!=null){
            increasedChartView.changeMinMax(chart);
            fullChartView.changeMinMax(chart);
        }
    }

    public void setChart(Chart chart){
        this.chart = chart;
        initCheckboxes();
        initFullChart();
    }


}
