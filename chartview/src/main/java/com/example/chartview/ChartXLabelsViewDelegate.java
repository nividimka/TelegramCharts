package com.example.chartview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import com.example.chartview.data.ChartHolder;
import com.example.chartview.data.LeftRightDataHolder;
import com.example.chartview.data.TopBottomDataHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChartXLabelsViewDelegate {

    ChartHolder chartHolder;
    TextPaint textPaint;
    TopBottomDataHolder topBottomDataHolder;
    LeftRightDataHolder leftRightDataHolder;
    SimpleDateFormat format;
    int textPaddingLeft;
    int textPaddingRight;
    float maxWidth;

    Rect rect;

    public final int NO_ANIMATION = 1;
    public final int ANIMATION_CHANGE_RANGE = 2;

    int currentState = NO_ANIMATION;
    View chartView;

    public ChartXLabelsViewDelegate(View view, ChartHolder chartHolder,
                                    Mode mode,
                                    TopBottomDataHolder topBottomDataHolder,
                                    LeftRightDataHolder leftRightDataHolder,
                                    float textSize,
                                    int textPadding){
        this.chartHolder = chartHolder;
        this.chartView = view;
        this.leftRightDataHolder = leftRightDataHolder;
        this.topBottomDataHolder = topBottomDataHolder;
        textPaint = new TextPaint();
        this.textPaddingLeft = textPadding;
        this.textPaddingRight = textPadding;
        textPaint.setColor(mode.gridTextColor);
        textPaint.setTextSize(textSize);
        format = new SimpleDateFormat("MMM d");
        Calendar calendar = Calendar.getInstance();
        maxWidth = 0;
        for(int i = 0; i < 365;i++){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            float width = getTextMaxWidth(format.format(calendar.getTime()), textPaint);
            if(width>=maxWidth){
                maxWidth = width+textPaddingLeft+textPaddingRight;
            }
        }
    }

    public float getLabelWidth(){
        return maxWidth;
    }

    private float getTextMaxWidth(String text, TextPaint paint) {
        return paint.measureText(text);
    }
    public void onDraw(Canvas canvas){
        drawLabels(canvas);
    }


    public void drawLabels(Canvas canvas){
        float widthBetweenPoints = leftRightDataHolder.getWidthBetweenPoints();
        float fullLength = widthBetweenPoints * (chartHolder.getChart().getXLine().getColumns().length-1);
        drawLabels(canvas,0,fullLength);
    }


    private void drawLabels(Canvas canvas, float left,float right) {
        float leftX = left-(leftRightDataHolder.getLeftIndex()+leftRightDataHolder.getPercentToLeftIndex())*leftRightDataHolder.getWidthBetweenPoints();
        float rightX = right-(leftRightDataHolder.getLeftIndex()+leftRightDataHolder.getPercentToLeftIndex())*leftRightDataHolder.getWidthBetweenPoints();
        drawLabel(canvas,leftX);
        drawLabel(canvas,rightX);

        switch (currentState) {
            case ANIMATION_CHANGE_RANGE:
                drawCenterLabelWithAnimation(canvas, left+maxWidth/2, right-maxWidth/2);
                break;
            case NO_ANIMATION:
                drawCenterLabel(canvas, left+maxWidth/2, right-maxWidth/2);
                break;
        }
    }

    private void drawCenterLabel(Canvas canvas, float left, float right) {
        if(right-left>=maxWidth) {
            float centerX = ((right + left) / 2) - (leftRightDataHolder.getLeftIndex() + leftRightDataHolder.getPercentToLeftIndex()) * leftRightDataHolder.getWidthBetweenPoints();
            drawLabel(canvas, centerX);
            drawCenterLabel(canvas, left, (right + left) / 2 - maxWidth / 2);
            drawCenterLabel(canvas, (right + left) / 2 + maxWidth / 2, right);
        }
    }
    private void drawCenterLabelWithAnimation(Canvas canvas, float left, float right){
        if(right-left>=maxWidth) {
            float centerX = ((right + left) / 2) - (leftRightDataHolder.getLeftIndex() + leftRightDataHolder.getPercentToLeftIndex()) * leftRightDataHolder.getWidthBetweenPoints();
            drawLabel(canvas, centerX);
            drawCenterLabelWithAnimation(canvas, left, (right + left) / 2 - maxWidth / 2);
            drawCenterLabelWithAnimation(canvas, (right + left) / 2 + maxWidth / 2, right);
        }else{
            float centerX = ((right + left) / 2) - (leftRightDataHolder.getLeftIndex() + leftRightDataHolder.getPercentToLeftIndex()) * leftRightDataHolder.getWidthBetweenPoints();
            drawLabelWithAnim(canvas, centerX,right-left);
        }
    }

    private void drawLabel(Canvas canvas,float x){
        float labelWidth = maxWidth;
        int index = getSelectedPointIndex(x);
        String text = format.format(new Date(chartHolder.getChart().getXLine().getColumns()[index]));
        float toCenter = (labelWidth - getTextMaxWidth(text, textPaint)) / 2;
        if(x<=leftRightDataHolder.getWidth() && x+labelWidth >=0) {
            textPaint.setAlpha(255);
            canvas.drawText(text, x + toCenter, getAllHeight() - textPaint.getFontMetrics().bottom, textPaint);
        }

    }

    private void drawLabelWithAnim(Canvas canvas,float x,float range){
        float labelWidth = maxWidth;
        int index = getSelectedPointIndex(x);
        String text = format.format(new Date(chartHolder.getChart().getXLine().getColumns()[index]));
        float toCenter = (labelWidth - getTextMaxWidth(text, textPaint)) / 2;
        if(x<=leftRightDataHolder.getWidth() && x+labelWidth >=0) {
            canvas.save();
            canvas.clipRect(x+(labelWidth-range)/2, getAllHeight() + textPaint.getFontMetrics().top, x + range + (labelWidth-range)/2, getAllHeight() - textPaint.getFontMetrics().bottom);
            textPaint.setAlpha((int) (255*range/labelWidth));
            canvas.drawText(text, x + toCenter, getAllHeight() - textPaint.getFontMetrics().bottom, textPaint);
            canvas.restore();
        }
    }

    public int getLabelHeight(){
        return (int) (Math.abs(textPaint.getFontMetrics().top - textPaint.getFontMetrics().bottom));
    }

    public int getAllHeight(){
        return topBottomDataHolder.getHeight();
    }

    public void setMode(Mode mode){
        textPaint.setColor(mode.gridTextColor);
    }

    public int getSelectedPointIndex(float x){
        //0.001 to add some kind of right offset because of .5 value not sure which point should select, with this kind of offset
        // it looks right but in a lot of points this may cause errors
        int selectedPoint = (int) Math.round(x*1. / leftRightDataHolder.getWidthBetweenPoints()+leftRightDataHolder.getLeftIndex() + leftRightDataHolder.getPercentToLeftIndex()+0.001);
        if(selectedPoint<0){
            selectedPoint = 0;
        }
        if (selectedPoint >= chartHolder.getChart().getXLine().getColumns().length) {
            selectedPoint = chartHolder.getChart().getXLine().getColumns().length-1;
        }
        return selectedPoint;
    }

    ValueAnimator animator;

    float offset;
//    public void finishMoveAllAnimation(){
//        offset = 0;
//        currentState = ANIMATION_AFTER_MOVE_ALL;
//        animator = ValueAnimator.ofFloat(0, 1);
//        animator.setDuration(200);
//        animator.setInterpolator(new LinearInterpolator());
//        final float width = maxWidth;
//        final float widthSeparator = (leftRightDataHolder.getWidth() - 5 * width) / 4;
//        offset = (leftRightDataHolder.getWidthBetweenPoints()*(leftOldIndex + percentToLeftOld-leftRightDataHolder.getLeftIndex()-leftRightDataHolder.getPercentToLeftIndex())) % (width + widthSeparator);
//        final float oldOffset = offset;
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                if(oldOffset<=(width+widthSeparator)/2) {
//                    offset = oldOffset * (1 - value);
//                }else{
//                    offset = oldOffset*(1-value) + (width+widthSeparator)*value;
//                }
//                if(value==1){
//                    currentState = NO_ANIMATION;
//                }
//                chartView.invalidate();
//            }
//        });
//        animator.start();
//    }
//
//    public void startMoveRightAnimation() {
//        currentState = ANIMATION_MOVE_RIGHT;
//        rightOldIndex = leftRightDataHolder.getRightIndex();
//        percentToRightOld = leftRightDataHolder.getPercentToRightIndex();
//        leftOldIndex = leftRightDataHolder.getLeftIndex();
//        percentToLeftOld = leftRightDataHolder.getPercentToLeftIndex();
//    }


    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void onRangeChanged(boolean state) {
        if(state){
            currentState = ANIMATION_CHANGE_RANGE;
            chartView.invalidate();
        }else{
            currentState = NO_ANIMATION;
            chartView.invalidate();
        }
    }
}
