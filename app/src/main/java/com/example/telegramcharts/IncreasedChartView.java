package com.example.telegramcharts;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.telegramcharts.data.Chart;
import com.example.telegramcharts.data.Line;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import static com.example.telegramcharts.FullChartView.getMaxY;
import static com.example.telegramcharts.FullChartView.getMinY;

public class IncreasedChartView extends View implements FullChartView.OnRangeChangeListener {
    int leftIndex;
    int rightIndex;
    float percentToLeftIndex;
    float percentToRightIndex;
    Chart chart;
    List<Paint> paints;
    Paint backgroundPaint;
    Mode mode;
    GestureDetectorCompat gestureDetector;
    int currentMinY;
    int currentMaxY;

    public IncreasedChartView(Context context) {
        super(context);
        init();
    }

    public IncreasedChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IncreasedChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mode = Mode.DAY_MODE;
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(mode.viewBackgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (chart != null) {
            int width = getWidth();
            int height = getHeight();
            int maxY = currentMaxY;
            int minY = currentMinY;
            //-2 because we don't want to include left and right point, just percents
            float lengthInPoints = rightIndex-leftIndex-percentToRightIndex-percentToLeftIndex;
            float widthBetweenPoints = width / lengthInPoints;
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
            if(selectedPoint){
                for (int k = 0; k < chart.getYLines().size(); k++) {
                    Line line = chart.getYLines().get(k);
                    if (!line.isHidden()) {
                        int y0 = line.getColumns()[selectedPointIndex];
                        int y0Scaled = (int) (((y0 - maxY)*1.0/ (minY - maxY)) * height);
                        int x0 = (int) (widthBetweenPoints * (selectedPointIndex-leftIndex-percentToLeftIndex));
                        canvas.drawCircle(x0,y0Scaled,15,paints.get(k));
                        canvas.drawCircle(x0,y0Scaled,10,backgroundPaint);
                    }
                }
            }
        }
    }

    @Override
    public void onRangeChange(int leftIndex, int rightIndex, float percentToLeftIndex, float percentToRightIndex) {
        Log.e("onRangeChange", "onRangeChange");
        assert leftIndex >= 0;
        assert rightIndex < chart.getYLines().get(0).getColumns().length;
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
    public void initChart(Chart chart) {
        setChart(chart);
        currentMaxY = getMaxY(chart.getYLines());
        currentMinY = getMinY(chart.getYLines());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean val = gestureDetector.onTouchEvent(event);
        return val;
    }

    boolean selectedPoint = false;
    int selectedPointIndex;

    private int getNearestIndexPoint(int width, float point, int count) {
        float i = point * (count-1)/ width;
        return (int) Math.round(i);
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("onShowPress", e.toString());
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            selectedPoint = !selectedPoint;
            if(selectedPoint) {
                if (rightIndex - leftIndex == 1) {
                    return false;
                }
                int index = leftIndex+getNearestIndexPoint(getWidth(), e.getX(), rightIndex - leftIndex);
                if (index == leftIndex & percentToLeftIndex >= 0) {
                    index = 1;
                } else if (index == rightIndex & percentToRightIndex >= 0) {
                    index = rightIndex - 1;
                }
                selectedPointIndex = index;
            }
            invalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    public void setMode(Mode mode) {
        backgroundPaint.setColor(mode.viewBackgroundColor);
        invalidate();
    }
    ValueAnimator animator;
    public void changeMinMax(Chart chart) {
        if(animator!=null) animator.cancel();
        setChart(chart);
        if(currentMaxY!=Integer.MIN_VALUE && getMaxY(chart.getYLines())!=Integer.MIN_VALUE) {
            final int oldMax = currentMaxY;
            final int oldMin = currentMinY;
            final int maxY = getMaxY(chart.getYLines());
            final int minY = getMinY(chart.getYLines());
            animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    currentMaxY = (int) (oldMax + (maxY-oldMax)*value);
                    currentMinY = (int) (oldMin + (minY-oldMin) * value);
                    invalidate();
                }
            });
            animator.start();
        }else{
            currentMaxY = getMaxY(chart.getYLines());
            currentMinY = getMinY(chart.getYLines());
            invalidate();
        }
    }
}
