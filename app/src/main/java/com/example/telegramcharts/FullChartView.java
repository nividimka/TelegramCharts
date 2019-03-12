package com.example.telegramcharts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.telegramcharts.data.Line;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

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
    private GestureDetectorCompat gestureDetector;
    private OnRangeChangeListener onRangeChangeListener;

    public FullChartView(Context context) {
        super(context);
        init();
    }

    public FullChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mode = Mode.DAY_MODE;
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
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
            int maxY = getMaxY(lines);
            int minY = getMinY(lines);
            for (int k = 0; k < lines.size(); k++) {
                Line line = lines.get(k);
                if (!line.isHidden()) {
                    int count = line.getColumns().length;
                    for (int i = 0; i < count - 1; i++) {
                        int y0 = line.getColumns()[i];
                        int y1 = line.getColumns()[i + 1];
                        int y0Scaled = (y0 - maxY) * height / (minY - maxY);
                        int y1Scaled = (y1 - maxY) * height / (minY - maxY);
                        int x0 = width * i / count;
                        int x1 = width * (i + 1) / count;
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

    boolean increaceCenterClicked = false;

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
                if (event.getX() < increasedRight && event.getX() > increasedLeft) {
                    increaceCenterClicked = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                increaceCenterClicked = false;
                break;
        }
        boolean val = gestureDetector.onTouchEvent(event);
        return val;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
        paints = new ArrayList<>();
        for (Line line : lines) {
            Paint paint = new Paint();
            paint.setColor(line.getColor());
            paint.setStrokeWidth(4);
            paints.add(paint);
        }
        updateIncreasedView();
        invalidate();
    }

    public static int getMaxY(List<Line> lines) {
        int maxY = lines.get(0).getMaxY();
        for (Line line : lines) {
            if (maxY < line.getMaxY()) {
                maxY = line.getMaxY();
            }
        }
        return maxY;
    }

    public static int getMinY(List<Line> lines) {
        int minY = lines.get(0).getMinY();
        for (Line line : lines) {
            if (minY > line.getMinY()) {
                minY = line.getMinY();
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
            if (increaceCenterClicked) {
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

    interface OnRangeChangeListener {
        void onRangeChange(int leftIndex, int rightIndex, float percentToLeftIndex, float percentToRightIndex);
    }

    private int getNearestLeftIndexPoint(int width, float leftFrame, int count) {
        float i = leftFrame * count / width;
        return (int) Math.floor(i);
    }

    private int getNearestRightIndexPoint(int width, float rightFrame, int count) {
        float i = rightFrame * count / width;
        return (int) Math.ceil(i);
    }

    private float percentToRightIndex(int rightIndex, int width, int count) {
        float length = 1f * width / count;
        return (1f * rightIndex * width / count - increasedRight) / length;
    }

    private float percentToLeftIndex(int leftIndex, int width, int count) {
        float length = 1f * width / count;
        return (increasedLeft - 1f * leftIndex * width / count) / length;
    }
}
