package com.example.telegramcharts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.telegramcharts.data.Chart;
import com.example.telegramcharts.data.Line;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.example.telegramcharts.FullChartView.getMaxY;
import static com.example.telegramcharts.FullChartView.getMinY;

public class IncreasedChartView extends View implements FullChartView.OnRangeChangeListener {
    int leftIndex;
    int rightIndex;
    float percentToLeftIndex;
    float percentToRightIndex;
    Chart chart;
    List<Paint> paints;

    public IncreasedChartView(Context context) {
        super(context);
    }

    public IncreasedChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IncreasedChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("onDraw", "onDraw");
        if (chart != null) {
            int width = getWidth();
            int height = getHeight();
            int maxY = getMaxY(chart.getYLines());
            int minY = getMinY(chart.getYLines());
            //-2 because we don't want to include left and right point, just percents
            float lengthInPoints = ((1-percentToRightIndex)+(1-percentToLeftIndex)+rightIndex-leftIndex-2);
//            if(percentToLeftIndex==0){
//                lengthInPoints = 1;
//            }
//            if(percentToRightIndex!=0){
//                lengthInPoints -= 1;
//            }
            float widthBetweenPoints = width / lengthInPoints;
            Log.e("widthBetweenPoints", "lengthInPoints"+((1-percentToRightIndex)+(1-percentToLeftIndex)+rightIndex-leftIndex-2));
            Log.e("widthBetweenPoints", "width" + widthBetweenPoints);
            for (int k = 0; k < chart.getYLines().size(); k++) {
                Line line = chart.getYLines().get(k);
                if (!line.isHidden()) {
                    for (int i = leftIndex; i < rightIndex; i++) {
                        int y0 = line.getColumns()[i];
                        int y1 = line.getColumns()[i + 1];
                        int y0Scaled = (int) (((y0 - maxY)*1.0/ (minY - maxY)) * height);
                        int y1Scaled = (int) (((y1 - maxY)*1.0/(minY - maxY)) * height);
                        int x0 = (int) (widthBetweenPoints * (i-leftIndex-percentToLeftIndex));
                        int x1 = (int) (widthBetweenPoints * (i+1-leftIndex-percentToLeftIndex));
                        canvas.drawLine(x0, y0Scaled, x1, y1Scaled, paints.get(k));
                    }
                }
            }
        }
    }

    @Override
    public void onRangeChange(int leftIndex, int rightIndex, float percentToLeftIndex, float percentToRightIndex) {
        Log.e("onRangeChange", "onRangeChange");
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.percentToLeftIndex = percentToLeftIndex;
        this.percentToRightIndex = percentToRightIndex;
        invalidate();
    }

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
}
