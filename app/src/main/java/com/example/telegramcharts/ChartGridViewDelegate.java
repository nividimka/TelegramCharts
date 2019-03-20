package com.example.telegramcharts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;

import com.example.telegramcharts.data.ChartHolder;
import com.example.telegramcharts.data.LeftRightDataHolder;
import com.example.telegramcharts.data.Line;
import com.example.telegramcharts.data.TopBottomDataHolder;

import java.util.ArrayList;
import java.util.List;

public class ChartGridViewDelegate {
    Rect rect;
    Paint horizontalGridPaint;
    TextPaint gridTextPaint;
    int horizontalRange;
    Mode mode;
    ChartHolder chartHolder;
    LeftRightDataHolder leftRightDataHolder;
    TopBottomDataHolder minMaxDataHolder;
    Paint backgroundPaint;

    public ChartGridViewDelegate(Mode mode, ChartHolder chartHolder,LeftRightDataHolder leftRightDataHolder, int horizontalRange, float textSize, TopBottomDataHolder minMaxDataHolder){
        rect = new Rect();
        this.chartHolder = chartHolder;
        this.leftRightDataHolder = leftRightDataHolder;
        this.minMaxDataHolder = minMaxDataHolder;
        this.mode = mode;
        this.horizontalRange = horizontalRange;
        horizontalGridPaint = new Paint();
        horizontalGridPaint.setStrokeWidth(4);
        horizontalGridPaint.setColor(mode.horizontalGridColor);
        gridTextPaint = new TextPaint();
        gridTextPaint.setColor(mode.gridTextColor);
        gridTextPaint.setTextSize(textSize);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(mode.viewBackgroundColor);
    }

    public void onDraw(Canvas canvas){
        if(!startAnimation) {
            for (int i = 0; i < 6; i++) {
                int height = getHeight() - i * horizontalRange;
                int currentY = calculateYFromHeight(height,getHeight());
                gridTextPaint.setAlpha(255);
                canvas.drawLine(0, height, getWidth(), height, horizontalGridPaint);
                canvas.drawText(String.valueOf(currentY), 0, height - 10, gridTextPaint);
            }
        }else{
            for (int i = 0; i < 6; i++) {
                int height = calculateHeightFromY(oldValues.get(i), getHeight());
                gridTextPaint.setAlpha((int) (255-(animationPercent)*255));
                horizontalGridPaint.setAlpha((int) (255-(animationPercent)*255));
                canvas.drawLine(0, height, getWidth(), height, horizontalGridPaint);
                canvas.drawText(String.valueOf(oldValues.get(i)), 0, height - 10, gridTextPaint);
            }
            for (int i = 0; i < 6; i++) {
                int nextHeight = getHeight() - i * horizontalRange;
                int nextYFromHeight = calculateNextYFromHeight(nextHeight,getHeight());
                int height = calculateHeightFromY(nextYFromHeight, getHeight());
                if(oldValues.get(i)!=nextYFromHeight) {
                    gridTextPaint.setAlpha((int) ((animationPercent) * 255));
                    horizontalGridPaint.setAlpha((int) ((animationPercent) * 255));
                }else{
                    gridTextPaint.setAlpha((int) (255));
                    horizontalGridPaint.setAlpha((int) (255));
                }
//                Rect background = getTextBackgroundSize(0, height-10, String.valueOf(nextYFromHeight), gridTextPaint);
//                canvas.drawRect(background, backgroundPaint);
                canvas.drawLine(0, height, getWidth(), height, horizontalGridPaint);
                canvas.drawText(String.valueOf(nextYFromHeight), 0, height - 10, gridTextPaint);

//                canvas.drawText(String.valueOf(currentY), 0, height - 10, gridTextPaint);
            }
        }
    }
    private Rect getTextBackgroundSize(float x, float y, String text, TextPaint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float halfTextLength = paint.measureText(text) / 2 + 5;
        return new Rect((int) (x), (int) (y + fontMetrics.top), (int) (x + 2*halfTextLength), (int) (y + fontMetrics.bottom));
    }
    public int getHeight(){
        return Math.abs(rect.top - rect.bottom);
    }
    public int getWidth(){
        return Math.abs(rect.left - rect.right);
    }

    public int calculateHeightFromY(int y,int allHeight){
        return (int) (allHeight-(y - minMaxDataHolder.getCurrentMinY()) * allHeight *1.0/ (minMaxDataHolder.getCurrentMaxY() - minMaxDataHolder.getCurrentMinY()));
    }
    public int calculateYFromHeight(int height,int allHeight){
        return (int) (minMaxDataHolder.getCurrentMinY() + (minMaxDataHolder.getCurrentMaxY() - minMaxDataHolder.getCurrentMinY()) * ((allHeight-height) * 1.0 / allHeight));
    }

    public int calculateNextYFromHeight(int height,int allHeight){
        return (int) (getMinY(chartHolder.getChart().getYLines()) + (getMaxY(chartHolder.getChart().getYLines()) - getMinY(chartHolder.getChart().getYLines())) * ((allHeight-height) * 1.0 / allHeight));
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
    public void setLayoutBoards(int left, int top, int right, int bottom) {
        rect.bottom = bottom;
        rect.top = top;
        rect.right = right;
        rect.left = left;
    }

    public void setMode(Mode mode) {
        horizontalGridPaint.setColor(mode.horizontalGridColor);
        gridTextPaint.setColor(mode.gridTextColor);
    }
    boolean startAnimation = false;

    public void animate(float value) {
        animationPercent = value;
        if(value==1){
            startAnimation = false;
        }
    }

    List<Integer> oldValues;
    public float animationPercent;

    public void startAnimate() {
        startAnimation = true;
        oldValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int height = getHeight() - i * horizontalRange;
            int currentY = calculateYFromHeight(height,getHeight());
            oldValues.add(currentY);
        }
    }
}
