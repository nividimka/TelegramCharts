package com.example.telegramcharts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.example.telegramcharts.data.ChartHolder;
import com.example.telegramcharts.data.LeftRightDataHolder;
import com.example.telegramcharts.data.Line;
import com.example.telegramcharts.data.TopBottomDataHolder;

public class ChartSelectedPointViewDelegate {
    boolean selectedPoint = false;
    int selectedPointIndex;
    TopBottomDataHolder topBottomDataHolder;
    LeftRightDataHolder leftRightDataHolder;
    Paint backgroundPaint;
    Paint summaryBoardPaint;
    ChartHolder chartHolder;
    float selectedPointClickedX;
    float selectedPointClickedY;
    boolean isSelectedPointClicked = false;


    public ChartSelectedPointViewDelegate(TopBottomDataHolder topBottomDataHolder, LeftRightDataHolder leftRightDataHolder, ChartHolder chartHolder, Mode mode) {
        this.topBottomDataHolder = topBottomDataHolder;
        this.chartHolder = chartHolder;
        this.leftRightDataHolder = leftRightDataHolder;
        backgroundPaint = new Paint();
        backgroundPaint.setColor(mode.viewBackgroundColor);
        summaryBoardPaint = new Paint();
        summaryBoardPaint.setStrokeWidth(3);
        summaryBoardPaint.setColor(mode.summaryBoarderColor);
    }

    public void onDraw(Canvas canvas) {
        if(selectedPoint){
            for (int k = 0; k < chartHolder.getChart().getYLines().size(); k++) {
                Line line = chartHolder.getChart().getYLines().get(k);
                if (!line.isHidden()) {
                    int y0 = line.getColumns()[selectedPointIndex];
                    int y0Scaled = (int) (((y0 - topBottomDataHolder.getCurrentMaxY())*1.0/ (topBottomDataHolder.getCurrentMinY() - topBottomDataHolder.getCurrentMaxY())) * topBottomDataHolder.getHeight())+topBottomDataHolder.getTopPadding();
                    int x0 = (int) (leftRightDataHolder.getWidthBetweenPoints() * (selectedPointIndex-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex()))+leftRightDataHolder.getLeftPadding();

                    canvas.drawCircle(x0,y0Scaled,15,chartHolder.getPaints().get(k));
                    canvas.drawCircle(x0,y0Scaled,10,backgroundPaint);
                }
            }
            drawSelectedSummary(canvas);
        }
    }

    private void drawSelectedSummary(Canvas canvas) {
        RectF summaryRect = new RectF();
        int offset = 100;
        if(selectedPointClickedX>= leftRightDataHolder.getWidth()/2+leftRightDataHolder.getLeftPadding()){
            summaryRect.right = (int) (selectedPointClickedX - offset);
            summaryRect.left = (int) (selectedPointClickedX - offset-leftRightDataHolder.getWidth()/2.5);
            summaryRect.top = (int) (selectedPointClickedY);
            summaryRect.bottom = (int) (selectedPointClickedY + 200);
        }else{
            summaryRect.left = (int) (selectedPointClickedX + offset);
            summaryRect.right = (int) (selectedPointClickedX + offset+leftRightDataHolder.getWidth()/2.5);
            summaryRect.top = (int) (selectedPointClickedY);
            summaryRect.bottom = (int) (selectedPointClickedY + 200);
        }
        backgroundPaint.setStrokeWidth(5);
        canvas.drawRoundRect(summaryRect.left-4,summaryRect.top-4,summaryRect.right+4,summaryRect.bottom+4,10,10,summaryBoardPaint);
        canvas.drawRoundRect(summaryRect,10,10,backgroundPaint);
    }

    public void setMode(Mode mode) {
        backgroundPaint.setColor(mode.viewBackgroundColor);
    }


    public int getSelectedPointIndex(float x){
        int selectedPoint = Math.round((x - leftRightDataHolder.getLeftPadding()) / leftRightDataHolder.getWidthBetweenPoints()+leftRightDataHolder.getLeftIndex() + leftRightDataHolder.getPercentToLeftIndex());
        if(selectedPoint<0){
            selectedPoint = 0;
        }
        if (selectedPoint >= chartHolder.getChart().getYLines().get(0).getColumns().length) {
            selectedPoint = chartHolder.getChart().getYLines().get(0).getColumns().length-1;
        }
        return selectedPoint;
    }

    public void updateSelectedPoint(float x, float y) {
        selectedPoint = !selectedPoint;
        if(selectedPoint) {
            selectedPointClickedX = x;
            selectedPointClickedY = y;
            selectedPointIndex = getSelectedPointIndex(x);
            isSelectedPointClicked = true;
        }
    }

    public boolean onScroll(float x, float y) {
        if(isSelectedPointClicked){
            if(selectedPoint) {
                if (leftRightDataHolder.getRightIndex() - leftRightDataHolder.getLeftIndex() == 1) {
                    return false;
                }
                selectedPointClickedX = x;
                selectedPointClickedY = y;
                selectedPointIndex = getSelectedPointIndex(x);
            }
        }
        return true;
    }

    public void resetSelectedPointClicked() {
        isSelectedPointClicked = false;
    }
}
