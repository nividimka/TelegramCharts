package com.example.chartview.data;

import android.content.Context;
import android.graphics.Paint;

import com.example.chartview.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ChartHolder {
    private Chart chart;
    private List<Paint> paints;
    List<OnUpdateListener> listeners = new ArrayList<>();

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart, Context context) {
        this.chart = chart;
        for(OnUpdateListener listener : listeners)listener.updateChart(chart);
        paints = new ArrayList<>();
        for (Line line : chart.getYLines()) {
            Paint paint = new Paint();
            paint.setColor(line.getColor());
            paint.setTextSize(ViewUtils.toPx(context,20));
            paint.setStrokeWidth(4);
            paints.add(paint);
        }
    }

    public List<Paint> getPaints() {
        return paints;
    }

    public void removeListener(OnUpdateListener listener) {
        listeners.remove(listener);
    }

    public void addListener(OnUpdateListener listener) {
        listeners.add(listener);
    }

    public interface OnUpdateListener{
        public void updateChart(Chart chart);
    }
}
