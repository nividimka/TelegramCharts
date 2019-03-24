package com.example.chartview.data;

public class LeftRightDataHolder {
    int leftIndex;
    int rightIndex;
    float percentToLeftIndex;
    float percentToRightIndex;
    private int width;
    int leftPadding;
    int rightPadding;

    public int getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(int leftIndex) {
        this.leftIndex = leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }

    public float getPercentToLeftIndex() {
        return percentToLeftIndex;
    }

    public void setPercentToLeftIndex(float percentToLeftIndex) {
        this.percentToLeftIndex = percentToLeftIndex;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
    }

    public float getPercentToRightIndex() {
        return percentToRightIndex;
    }

    public void setPercentToRightIndex(float percentToRightIndex) {
        this.percentToRightIndex = percentToRightIndex;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getWidthBetweenPoints(){
        float lengthInPoints = rightIndex-leftIndex-percentToRightIndex-percentToLeftIndex;
        return (width -leftPadding-rightPadding)/ lengthInPoints;
    }

    public float getWidthInPoints(){
        float lengthInPoints = rightIndex-leftIndex-percentToRightIndex-percentToLeftIndex;
        return lengthInPoints;
    }

    public int getLeftPadding() {
        return leftPadding;
    }
}
