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
    float increasedLeft = 0;
    float increasedRight = 150;
    private GestureDetectorCompat gestureDetector;

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

    public void init(){
        blurPaint = new Paint();
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
        blurPaint.setColor(Color.parseColor("#BBF4F7F8"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lines!=null){
            int width = getWidth();
            int height = getHeight();
            int maxY = getMaxY(lines);
            int minY = getMinY(lines);
            for(int k =0; k < lines.size();k++) {
                Line line = lines.get(k);
                if(!line.isHidden()) {
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
            canvas.drawRect(0,0,increasedLeft,getHeight(),blurPaint);
            canvas.drawRect(increasedRight,0,getWidth(),getHeight(),blurPaint);
        }
    }

    boolean increaceCenterClicked = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()<increasedRight && event.getX()>increasedLeft){
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
        for(Line line : lines){
            Paint paint = new Paint();
            paint.setColor(line.getColor());
            paint.setStrokeWidth(4);
            paints.add(paint);
        }
        invalidate();
    }

    public int getMaxY(List<Line> lines){
        int maxY = lines.get(0).getMaxY();
        for(Line line:lines){
            if(maxY < line.getMaxY()){
                maxY = line.getMaxY();
            }
        }
        return maxY;
    }

    public int getMinY(List<Line> lines){
        int minY = lines.get(0).getMinY();
        for(Line line:lines){
            if(minY > line.getMinY()){
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
            Log.e("onScroll", "onScroll");
            if(increaceCenterClicked) {
                float delta = increasedRight - increasedLeft;
                float newLeft = e2.getX()-delta/2;
                float newRight = e2.getX() + delta/2;
                if(newLeft >=0 && newRight <=getWidth()) {
                    increasedLeft = newLeft;
                    increasedRight = newRight;
                }
                if(newLeft<0){
                    increasedLeft = 0;
                    increasedRight = delta;
                }
                if(newRight > getWidth()){
                    increasedRight = getWidth();
                    increasedLeft = getWidth()-delta;
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
}
