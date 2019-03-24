package com.example.chartview.data;

public class TopBottomDataHolder {
    private int currentMinY;
    private int currentMaxY;
    private int height;
    private int topPadding;
    private int bottomPadding;

    public int getCurrentMinY() {
        return currentMinY;
    }

    public void setCurrentMinY(int currentMinY) {
        this.currentMinY = currentMinY;
    }

    public int getCurrentMaxY() {
        return currentMaxY;
    }

    public void setCurrentMaxY(int currentMaxY) {
        this.currentMaxY = currentMaxY;
    }

    public int getHeight() {
        return height;
    }
    public int getChartHeight(){
        return height-topPadding-bottomPadding;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public int getScaledY(int y){
        return (int) (((y - currentMaxY)*1.0/ (currentMinY - currentMaxY)) * getChartHeight()+topPadding);
    }
}
