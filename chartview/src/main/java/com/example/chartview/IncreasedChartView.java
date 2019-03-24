package com.example.chartview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.chartview.data.Chart;
import com.example.chartview.data.ChartHolder;
import com.example.chartview.data.LeftRightDataHolder;
import com.example.chartview.data.Line;
import com.example.chartview.data.TopBottomDataHolder;
import com.example.chartview.utils.ViewUtils;


import java.util.List;


public class IncreasedChartView extends View implements FullChartView.OnRangeChangeListener, FullChartView.OnMinMaxChangeListener {
    Paint verticalGridPaint;
    Mode mode;
    GestureDetector gestureDetector;
    int leftPadding;
    int rightPadding;
    int topPadding;
    int bottomPadding;
    ChartGridViewDelegate chartGridViewDelegate;
    ChartSelectedPointViewDelegate chartSelectedPointViewDelegate;
    TopBottomDataHolder topBottomDataHolder;
    LeftRightDataHolder leftRightDataHolder;
    ChartXLabelsViewDelegate chartXLabelsViewDelegate;
    ChartHolder chartHolder;

    public IncreasedChartView(Context context) {
        super(context);
        init();
    }

    public IncreasedChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IncreasedChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        chartHolder = new ChartHolder();
        topBottomDataHolder = new TopBottomDataHolder();
        leftRightDataHolder = new LeftRightDataHolder();
        mode = Mode.DAY_MODE;
        chartGridViewDelegate = new ChartGridViewDelegate(mode,
                chartHolder,
                leftRightDataHolder,
                ViewUtils.toPx(getContext(),60),
                ViewUtils.toPx(getContext(),12),
                topBottomDataHolder);
        chartSelectedPointViewDelegate = new ChartSelectedPointViewDelegate(getContext(),topBottomDataHolder,
                leftRightDataHolder,
                chartHolder,
                mode,
                ViewUtils.toPx(getContext(),12));
        chartXLabelsViewDelegate = new ChartXLabelsViewDelegate(this,chartHolder, mode, topBottomDataHolder, leftRightDataHolder,
                ViewUtils.toPx(getContext(), 12),
                ViewUtils.toPx(getContext(),10));
        leftPadding = ViewUtils.toPx(getContext(), 20);
        rightPadding = ViewUtils.toPx(getContext(), 20);
        topPadding = ViewUtils.toPx(getContext(), 20);
        bottomPadding = ViewUtils.toPx(getContext(), 20);
        topBottomDataHolder.setTopPadding(topPadding);
        topBottomDataHolder.setBottomPadding(bottomPadding);
        leftRightDataHolder.setLeftPadding(leftPadding);
        leftRightDataHolder.setRightPadding(rightPadding);
        gestureDetector = new GestureDetector(getContext(), gestureListener);
        verticalGridPaint = new Paint();
        verticalGridPaint.setColor(mode.verticalGridColor);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                chartXLabelsViewDelegate.setRect(new Rect(0, getHeight()-chartXLabelsViewDelegate.getLabelHeight(), getWidth(), getHeight()));
                leftPadding = (int) (chartXLabelsViewDelegate.getLabelWidth()/2);
                rightPadding = (int) (chartXLabelsViewDelegate.getLabelWidth() / 2);
                leftRightDataHolder.setLeftPadding(leftPadding);
                leftRightDataHolder.setRightPadding(rightPadding);
                chartGridViewDelegate.setLayoutBoards(left, top, right, bottom);
                leftRightDataHolder.setWidth(Math.abs(left-right));
                topBottomDataHolder.setHeight(Math.abs(top-bottom));
                invalidate();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (chartHolder.getChart() != null) {
            float widthBetweenPoints = leftRightDataHolder.getWidthBetweenPoints();
            chartGridViewDelegate.onDraw(canvas);
            chartXLabelsViewDelegate.onDraw(canvas);
            for (int k = 0; k < chartHolder.getChart().getYLines().size(); k++) {
                Line line = chartHolder.getChart().getYLines().get(k);
                if (!line.isHidden()) {
                    drawLeftMissingPoints(canvas,line,widthBetweenPoints,chartHolder.getPaints().get(k));
                    drawPoints(canvas,line,widthBetweenPoints,chartHolder.getPaints().get(k));
                    drawRightMissingPoints(canvas,line,widthBetweenPoints,chartHolder.getPaints().get(k));
                }
            }

            chartSelectedPointViewDelegate.onDraw(canvas);

        }
    }


    private void drawPoints(Canvas canvas,Line line,float widthBetweenPoints, Paint paint) {
        for (int i = leftRightDataHolder.getLeftIndex(); i < leftRightDataHolder.getRightIndex(); i++) {
            int y0 = line.getColumns()[i];
            int y1 = line.getColumns()[i + 1];
            int y0Scaled = topBottomDataHolder.getScaledY(y0);
            int y1Scaled = topBottomDataHolder.getScaledY(y1);
            int x0 = (int) (widthBetweenPoints * (i-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftPadding;
            int x1 = (int) (widthBetweenPoints * (i+1-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftPadding;
            canvas.drawLine(x0, y0Scaled, x1, y1Scaled, paint);
        }
    }

    private void drawLeftMissingPoints(Canvas canvas,Line line, float widthBetweenPoints, Paint paint) {
        int currentLeftPointIndex = leftRightDataHolder.getLeftIndex();
        int x0 = 0;
        while (currentLeftPointIndex>0 && x0 >= 0){
            int y0 = line.getColumns()[currentLeftPointIndex-1];
            int y1 = line.getColumns()[currentLeftPointIndex];
            int y0Scaled = topBottomDataHolder.getScaledY(y0);
            int y1Scaled = topBottomDataHolder.getScaledY(y1);
            x0 = (int) (widthBetweenPoints * (currentLeftPointIndex-1-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftPadding;
            int x1 = (int) (widthBetweenPoints * (currentLeftPointIndex-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftPadding;
            canvas.drawLine(x0, y0Scaled, x1, y1Scaled, paint);
            currentLeftPointIndex--;
        }
    }

    private void drawRightMissingPoints(Canvas canvas,Line line, float widthBetweenPoints, Paint paint) {
        int currentRightPointIndex = leftRightDataHolder.getRightIndex();
        int x1 = getWidth();
        int size = line.getColumns().length;
        while ((currentRightPointIndex+1<=size-1) && x1 <= getWidth()){
            int y0 = line.getColumns()[currentRightPointIndex];
            int y1 = line.getColumns()[currentRightPointIndex+1];
            int y0Scaled = topBottomDataHolder.getScaledY(y0);
            int y1Scaled = topBottomDataHolder.getScaledY(y1);
            int x0 = (int) (widthBetweenPoints * (currentRightPointIndex-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftPadding;
            x1 = (int) (widthBetweenPoints * (currentRightPointIndex+1-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftPadding;
            canvas.drawLine(x0, y0Scaled, x1, y1Scaled, paint);
            currentRightPointIndex++;
        }
    }

    @Override
    public void onRangeChange(int leftIndex, int rightIndex, float percentToLeftIndex, float percentToRightIndex) {
        Log.e("onRangeChange", "onRangeChange" +leftIndex + " " + rightIndex + " " + percentToLeftIndex + " " + percentToRightIndex);
        assert leftIndex >= 0;
        if(rightIndex >= chartHolder.getChart().getYLines().get(0).getColumns().length){
            rightIndex = chartHolder.getChart().getYLines().get(0).getColumns().length-1;
            percentToRightIndex = 0;
            percentToLeftIndex = 0;
        }
        this.leftRightDataHolder.setLeftIndex(leftIndex);
        this.leftRightDataHolder.setRightIndex(rightIndex);
        this.leftRightDataHolder.setPercentToLeftIndex(percentToLeftIndex);
        this.leftRightDataHolder.setPercentToRightIndex(percentToRightIndex);
        invalidate();
    }


    @Override
    public void setLeftOrRightClicked(boolean state) {
        chartXLabelsViewDelegate.onRangeChanged(state);
    }

    public void setChart(Chart chart) {
        chartHolder.setChart(chart,getContext());
    }
    public void initChart(Chart chart) {
        setChart(chart);
        leftRightDataHolder.setLeftIndex(0);
        leftRightDataHolder.setRightIndex(0);
        topBottomDataHolder.setCurrentMaxY(getMaxY(chartHolder.getChart().getYLines()));
        topBottomDataHolder.setCurrentMinY(getMinY(chartHolder.getChart().getYLines()));
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                chartSelectedPointViewDelegate.updateSelectedPoint(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                chartSelectedPointViewDelegate.resetSelectedPointClicked();
                invalidate();
                break;
        }
        boolean val = gestureDetector.onTouchEvent(event);
        return val;
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
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            chartSelectedPointViewDelegate.onScroll(e2.getX(),e2.getY());
            invalidate();
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
        chartGridViewDelegate.setMode(mode);
        chartSelectedPointViewDelegate.setMode(mode);
        verticalGridPaint.setColor(mode.verticalGridColor);
        chartXLabelsViewDelegate.setMode(mode);
        invalidate();
    }
    ValueAnimator animator;
    public void changeMinMax(Chart chart) {
        setChart(chart);
        animateMinMax();
    }


    public int getMaxY(List<Line> lines) {
        int maxY = Integer.MIN_VALUE;
        for (Line line : lines) {
            for(int i = leftRightDataHolder.getLeftIndex(); i <= leftRightDataHolder.getRightIndex();i++) {
                if (!line.isHidden() && maxY < line.getColumns()[i]) {
                    maxY = line.getColumns()[i];
                }
            }
        }
        return maxY;
    }

    public int getMinY(List<Line> lines) {
        int minY = Integer.MAX_VALUE;
        for (Line line : lines) {
            for(int i = leftRightDataHolder.getLeftIndex(); i <= leftRightDataHolder.getRightIndex();i++) {
                if (!line.isHidden() && minY > line.getColumns()[i]) {
                    minY = line.getColumns()[i];
                }
            }
        }
        return minY;
    }

    @Override
    public void animateMinMax() {
        if(animator!=null) animator.cancel();
        if(topBottomDataHolder.getCurrentMaxY()!=Integer.MIN_VALUE && getMaxY(chartHolder.getChart().getYLines())!=Integer.MIN_VALUE) {
            final int oldMax = topBottomDataHolder.getCurrentMaxY();
            final int oldMin = topBottomDataHolder.getCurrentMinY();
            final int maxY = getMaxY(chartHolder.getChart().getYLines());
            final int minY = getMinY(chartHolder.getChart().getYLines());
            animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            chartGridViewDelegate.startAnimate();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    chartGridViewDelegate.animate(value);
                    topBottomDataHolder.setCurrentMaxY((int) (oldMax + (maxY - oldMax) * value));
                    topBottomDataHolder.setCurrentMinY((int) (oldMin + (minY - oldMin) * value));
                    invalidate();
                }
            });
            animator.start();
        }else{
            topBottomDataHolder.setCurrentMaxY(getMaxY(chartHolder.getChart().getYLines()));
            topBottomDataHolder.setCurrentMinY(getMinY(chartHolder.getChart().getYLines()));
            invalidate();
        }
    }

    @Override
    public void changeMinMax() {
        topBottomDataHolder.setCurrentMaxY(getMaxY(chartHolder.getChart().getYLines()));
        topBottomDataHolder.setCurrentMinY(getMinY(chartHolder.getChart().getYLines()));
        invalidate();
    }
}
