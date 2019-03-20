package com.example.telegramcharts;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.telegramcharts.data.Chart;
import com.example.telegramcharts.data.Line;

import java.util.ArrayList;
import java.util.List;


public class FullChartView extends View {
    List<Line> lines;
    List<Paint> paints;
    Paint blurPaint;
    Paint framePaint;
    Mode mode;
    float increasedLeft = 0;
    float increasedRight = 150;
    float frameHeight = 5;
    float frameWidth = 10;
    private GestureDetector gestureDetector;
    private OnRangeChangeListener onRangeChangeListener;
    private OnMinMaxAnimateListener onMinMaxAnimateListener;
    int currentMax;
    int currentMin;

    public FullChartView(Context context) {
        super(context);
        init();
    }

    public FullChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mode = Mode.DAY_MODE;
        gestureDetector = new GestureDetector(getContext(), gestureListener);
        blurPaint = new Paint();
        blurPaint.setColor(mode.blurColor);
        framePaint = new Paint();
        framePaint.setColor(mode.frameColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lines != null) {
            int width = getWidth();
            int height = getHeight();
            int maxY = currentMax;
            int minY = currentMin;
            for (int k = 0; k < lines.size(); k++) {
                Line line = lines.get(k);
                if (!line.isHidden()) {
                    int count = line.getColumns().length;
                    for (int i = 0; i < count - 1; i++) {
                        int y0 = line.getColumns()[i];
                        int y1 = line.getColumns()[i + 1];
                        int y0Scaled = (int) (((y0 - maxY)*1.0/ (minY - maxY)) * height);
                        int y1Scaled = (int) (((y1 - maxY)*1.0/(minY - maxY)) * height);
                        int x0 = width * i / (count-1);
                        int x1 = width * (i + 1) / (count-1);
                        canvas.drawLine(x0, y0Scaled, x1, y1Scaled, paints.get(k));
                    }
                }
            }
            canvas.drawRect(0, 0, increasedLeft, getHeight(), blurPaint);
            canvas.drawRect(increasedLeft, 0, increasedLeft + frameWidth, getHeight(), framePaint);
            canvas.drawRect(increasedRight - frameWidth, 0, increasedRight, getHeight(), framePaint);
            canvas.drawRect(increasedLeft + frameWidth, 0, increasedRight - frameWidth, frameHeight, framePaint);
            canvas.drawRect(increasedLeft + frameWidth, getHeight() - frameHeight, increasedRight - frameWidth, getHeight(), framePaint);
            canvas.drawRect(increasedRight, 0, getWidth(), getHeight(), blurPaint);
        }
    }

    boolean increaseCenterClicked = false;
    boolean increaseRightClicked = false;
    boolean increaseLeftClicked = false;

    public void setMode(Mode mode) {
        this.mode = mode;
        blurPaint.setColor(mode.blurColor);
        framePaint.setColor(mode.frameColor);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < (increasedRight - 30) && event.getX() > (increasedLeft+30)) {
                    increaseCenterClicked = true;
                }
                if (event.getX() >= increasedRight-30 && event.getX() <= increasedRight+30){
                    increaseRightClicked = true;
                }
                if (event.getX() >= increasedLeft-30 && event.getX() <= increasedLeft+30){
                    increaseLeftClicked = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                onMinMaxAnimateListener.animateMinMax();
                increaseCenterClicked = false;
                increaseRightClicked = false;
                increaseLeftClicked = false;
                break;
        }
        boolean val = gestureDetector.onTouchEvent(event);
        return val;
    }

    public void setChart(Chart chart){
        this.lines = chart.getYLines();
        paints = new ArrayList<>();
        for (Line line : lines) {
            Paint paint = new Paint();
            paint.setColor(line.getColor());
            paint.setStrokeWidth(4);
            paints.add(paint);
        }
    }

    public void initChart(Chart chart) {
        setChart(chart);
        currentMax = getMaxY(chart.getYLines());
        currentMin = getMinY(chart.getYLines());
        updateIncreasedView();
        onMinMaxAnimateListener.animateMinMax();
        invalidate();
    }

    public static int getMaxY(List<Line> lines) {
        int maxY = Integer.MIN_VALUE;
        for (Line line : lines) {
            for(int i = 0; i < line.getColumns().length;i++) {
                if (!line.isHidden() && maxY < line.getColumns()[i]) {
                    maxY = line.getColumns()[i];
                }
            }
        }
        return maxY;
    }

    public static int getMinY(List<Line> lines) {
        int minY = Integer.MAX_VALUE;
        for (Line line : lines) {
            for(int i = 0; i < line.getColumns().length;i++) {
                if (!line.isHidden() && minY > line.getColumns()[i]) {
                    minY = line.getColumns()[i];
                }
            }
        }
        return minY;
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
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (increaseCenterClicked) {
                float delta = increasedRight - increasedLeft;
                float newLeft = e2.getX() - delta / 2;
                float newRight = e2.getX() + delta / 2;
                if (newLeft >= 0 && newRight <= getWidth() && (increasedLeft != newLeft || increasedRight != newRight)) {
                    increasedLeft = newLeft;
                    increasedRight = newRight;
                    updateIncreasedView();
                }
                if (newLeft < 0 && increasedLeft != 0) {
                    increasedLeft = 0;
                    increasedRight = delta;
                    updateIncreasedView();
                }
                if (newRight > getWidth() && increasedRight != getWidth()) {
                    increasedRight = getWidth();
                    increasedLeft = getWidth() - delta;
                    updateIncreasedView();
                }
                invalidate();
            }
            if(increaseRightClicked){
                float newRight = e2.getX();
                float delta = newRight - increasedLeft;
                if(delta>=150){
                    if(newRight> getWidth()){
                        increasedRight = getWidth();
                    }else {
                        increasedRight = newRight;
                    }
                }else{
                    increasedRight = increasedLeft + 150;
                }
                invalidate();
                updateIncreasedView();
            }
            if(increaseLeftClicked){
                float newLeft = e2.getX();
                float delta = increasedRight - newLeft;
                if(delta>=150){
                    if(newLeft< 0){
                        increasedLeft = 0;
                    }else {
                        increasedLeft = newLeft;
                    }
                }else{
                    increasedLeft = increasedRight - 150;
                }
                invalidate();
                updateIncreasedView();
            }
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

    private void updateIncreasedView() {
        int pointCount = lines.get(0).getColumns().length;
        int leftIndex = getNearestLeftIndexPoint(getWidth(), increasedLeft, pointCount);
        int rightIndex = getNearestRightIndexPoint(getWidth(), increasedRight, pointCount);
        float percentToRightIndex = percentToRightIndex(rightIndex, getWidth(), pointCount);
        float percentToLeftIndex = percentToLeftIndex(leftIndex, getWidth(), pointCount);
        if (onRangeChangeListener != null) {
            onRangeChangeListener.onRangeChange(leftIndex, rightIndex, percentToLeftIndex, percentToRightIndex);
        }
    }

    public OnRangeChangeListener getOnRangeChangeListener() {
        return onRangeChangeListener;
    }

    public void setOnRangeChangeListener(OnRangeChangeListener onRangeChangeListener) {
        this.onRangeChangeListener = onRangeChangeListener;
    }

    public OnMinMaxAnimateListener getOnMinMaxAnimateListener() {
        return onMinMaxAnimateListener;
    }

    public void setOnMinMaxAnimateListener(OnMinMaxAnimateListener onMinMaxAnimateListener) {
        this.onMinMaxAnimateListener = onMinMaxAnimateListener;
    }

    ValueAnimator animator;
    public void changeMinMax(Chart chart) {
        if(animator!=null) animator.cancel();
        setChart(chart);
        if(currentMax!=Integer.MIN_VALUE && getMaxY(chart.getYLines())!=Integer.MIN_VALUE) {
            final int oldMax = currentMax;
            final int oldMin = currentMin;
            final int maxY = getMaxY(chart.getYLines());
            final int minY = getMinY(chart.getYLines());
            animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    currentMax = (int) (oldMax + (maxY-oldMax)*value);
                    currentMin = (int) (oldMin + (minY-oldMin) * value);
                    invalidate();
                }
            });
            animator.start();
        }else{
            currentMax = getMaxY(chart.getYLines());
            currentMin = getMinY(chart.getYLines());
            invalidate();
        }
    }


    interface OnRangeChangeListener {
        void onRangeChange(int leftIndex, int rightIndex, float percentToLeftIndex, float percentToRightIndex);
    }

    interface OnMinMaxAnimateListener {
        void animateMinMax();
    }

    private int getNearestLeftIndexPoint(int width, float leftFrame, int count) {
        float i = leftFrame * (count-1) / width;
        return (int) Math.floor(i);
    }

    private int getNearestRightIndexPoint(int width, float rightFrame, int count) {
        float i = rightFrame * (count-1)/ width;
        return (int) Math.ceil(i);
    }

    private float percentToRightIndex(int rightIndex, int width, int count) {
        float length = 1f * width / (count-1);
        return (1f * rightIndex * width / (count-1) - increasedRight) / length;
    }

    private float percentToLeftIndex(int leftIndex, int width, int count) {
        float length = 1f * width / (count-1);
        return (increasedLeft - 1f * leftIndex * width / (count-1)) / length;
    }
}
