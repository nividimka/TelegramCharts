package com.example.telegramcharts.data;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class ChartHolder {
    private Chart chart;
    private List<Paint> paints;

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
        paints = new ArrayList<>();
        for (Line line : chart.getYLines()) {
            Paint paint = new Paint();
            paint.setColor(line.getColor());
            paint.setStrokeWidth(4);
            paints.add(paint);
        }
    }

    public List<Paint> getPaints() {
        return paints;
    }

}
