package com.example.chartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.example.chartview.data.Chart;
import com.example.chartview.data.ChartHolder;
import com.example.chartview.data.LeftRightDataHolder;
import com.example.chartview.data.Line;
import com.example.chartview.data.TopBottomDataHolder;
import com.example.chartview.utils.IntUtils;
import com.example.chartview.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartSelectedPointViewDelegate implements ChartHolder.OnUpdateListener {
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
    TextPaint dateTextPaint;
    SimpleDateFormat summaryDateFormat;
    List<TextPaint> paintCounts = new ArrayList<>();
    List<TextPaint> paintLabels = new ArrayList<>();
    Context context;

    public ChartSelectedPointViewDelegate(Context context, TopBottomDataHolder topBottomDataHolder, LeftRightDataHolder leftRightDataHolder, ChartHolder chartHolder,
                                          Mode mode,
                                          int textSize) {
        this.topBottomDataHolder = topBottomDataHolder;
        this.chartHolder = chartHolder;
        this.context = context;
        chartHolder.addListener(this);
        this.leftRightDataHolder = leftRightDataHolder;
        dateTextPaint = new TextPaint();
        dateTextPaint.setColor(mode.summaryTextColor);
        dateTextPaint.setTextSize(textSize);
        dateTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(mode.viewBackgroundColor);
        summaryBoardPaint = new Paint();
        summaryBoardPaint.setStrokeWidth(3);
        summaryBoardPaint.setColor(mode.summaryBoarderColor);
        summaryDateFormat = new SimpleDateFormat("EEE, MMM d");
        initChartPaints();
    }

    private void initChartPaints() {
        paintCounts.clear();
        paintLabels.clear();
        Chart chart = chartHolder.getChart();
        if(chart!=null) {
            for (Line line : chart.getYLines()) {
                TextPaint countPaint = new TextPaint();
                countPaint.setColor(line.getColor());
                countPaint.setTextSize(ViewUtils.toPx(context, 16));
                paintCounts.add(countPaint);

                TextPaint labelPaint = new TextPaint();
                labelPaint.setColor(line.getColor());
                labelPaint.setTextSize(ViewUtils.toPx(context, 12));
                paintLabels.add(labelPaint);
            }
        }
    }


    public void onDraw(Canvas canvas) {
        if(selectedPoint){
            for (int k = 0; k < chartHolder.getChart().getYLines().size(); k++) {
                Line line = chartHolder.getChart().getYLines().get(k);
                if (!line.isHidden()) {
                    int y0 = line.getColumns()[selectedPointIndex];
                    int y0Scaled = (int) (((y0 - topBottomDataHolder.getCurrentMaxY())*1.0/ (topBottomDataHolder.getCurrentMinY() - topBottomDataHolder.getCurrentMaxY())) * topBottomDataHolder.getChartHeight())+topBottomDataHolder.getTopPadding();
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
        if(selectedPointClickedX>= leftRightDataHolder.getWidth()/2){
            summaryRect.right = (int) (selectedPointClickedX - offset);
            summaryRect.left = (int) (selectedPointClickedX - offset-leftRightDataHolder.getWidth()/3);
            summaryRect.top = (int) (selectedPointClickedY);
            summaryRect.bottom = (int) (selectedPointClickedY + 200);
        }else{
            summaryRect.left = (int) (selectedPointClickedX + offset);
            summaryRect.right = (int) (selectedPointClickedX + offset+leftRightDataHolder.getWidth()/3);
            summaryRect.top = (int) (selectedPointClickedY);
            summaryRect.bottom = (int) (selectedPointClickedY + 200);
        }
        backgroundPaint.setStrokeWidth(5);
        canvas.drawRoundRect(summaryRect.left-4,summaryRect.top-4,summaryRect.right+4,summaryRect.bottom+4,10,10,summaryBoardPaint);
        canvas.drawRoundRect(summaryRect,10,10,backgroundPaint);

        canvas.drawText(summaryDateFormat.format(new Date(chartHolder.getChart().getXLine().getColumns()[selectedPointIndex])),summaryRect.left+10, summaryRect.top+Math.abs(dateTextPaint.getFontMetrics().top)+10,dateTextPaint);
        for(int i = 0;i <chartHolder.getChart().getYLines().size();i++){
            Line line = chartHolder.getChart().getYLines().get(i);
            int point = line.getColumns()[selectedPointIndex];
            canvas.save();
            canvas.drawText(IntUtils.formatInt(point), summaryRect.left+10 + (i%2!=0?(summaryRect.right-summaryRect.left)/2:0), summaryRect.top+Math.abs(dateTextPaint.getFontMetrics().top)+getLabelHeight(paintCounts.get(i))+ (i/2)*70, paintCounts.get(i));
            canvas.drawText(String.valueOf(line.getName()), summaryRect.left+10 + (i%2!=0?(summaryRect.right-summaryRect.left)/2:0), summaryRect.top+Math.abs(dateTextPaint.getFontMetrics().top)+getLabelHeight(paintCounts.get(i))+ (i/2)*70+30, paintLabels.get(i));
        }
    }

    public int getLabelHeight(Paint textPaint){
        return (int) (Math.abs(textPaint.getFontMetrics().top - textPaint.getFontMetrics().bottom));
    }

    public void setMode(Mode mode) {
        backgroundPaint.setColor(mode.viewBackgroundColor);
        summaryBoardPaint.setColor(mode.getSummaryBoarderColor());
        dateTextPaint.setColor(mode.getSummaryTextColor());
    }


    public int getSelectedPointIndex(float x){
        int selectedPoint = Math.round((x - leftRightDataHolder.getLeftPadding()) / leftRightDataHolder.getWidthBetweenPoints()+leftRightDataHolder.getLeftIndex() + leftRightDataHolder.getPercentToLeftIndex());
        if(selectedPoint<0){
            selectedPoint = 0;
        }
        if (selectedPoint >= chartHolder.getChart().getXLine().getColumns().length) {
            selectedPoint = chartHolder.getChart().getXLine().getColumns().length-1;
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

    @Override
    public void updateChart(Chart chart) {
        selectedPoint = false;
        initChartPaints();
    }
}
