package com.example.chartview;

import android.graphics.Color;

import java.util.Objects;

public class Mode {
    public Mode(int separatorColor,
                int backgroundColor,
                int toolbarColor,
                int statusbarColor,
                int viewBackgroundColor,
                int blurColor,
                int frameColor,
                int textColor,
                int horizontalGridColor,
                int verticalGridColor,
                int gridTextColor,
                int summaryBoarderColor,
                int summaryTextColor) {
        this.separatorColor = separatorColor;
        this.backgroundColor = backgroundColor;
        this.toolbarColor = toolbarColor;
        this.statusbarColor = statusbarColor;
        this.viewBackgroundColor = viewBackgroundColor;
        this.blurColor = blurColor;
        this.frameColor = frameColor;
        this.textColor = textColor;
        this.horizontalGridColor = horizontalGridColor;
        this.verticalGridColor = verticalGridColor;
        this.gridTextColor = gridTextColor;
        this.summaryBoarderColor = summaryBoarderColor;
        this.summaryTextColor = summaryTextColor;
    }
    int frameColor;
    int viewBackgroundColor;
    int summaryBoarderColor;
    int summaryTextColor;
    int separatorColor;
    int backgroundColor;
    int toolbarColor;
    int statusbarColor;
    int blurColor;
    int textColor;
    int horizontalGridColor;
    int verticalGridColor;
    int gridTextColor;
    public int getSeparatorColor() {
        return separatorColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mode mode = (Mode) o;
        return separatorColor == mode.separatorColor &&
                backgroundColor == mode.backgroundColor;
    }

    public int getFrameColor() {
        return frameColor;
    }

    public void setFrameColor(int frameColor) {
        this.frameColor = frameColor;
    }

    public int getViewBackgroundColor() {
        return viewBackgroundColor;
    }

    public void setViewBackgroundColor(int viewBackgroundColor) {
        this.viewBackgroundColor = viewBackgroundColor;
    }

    public int getSummaryBoarderColor() {
        return summaryBoarderColor;
    }

    public void setSummaryBoarderColor(int summaryBoarderColor) {
        this.summaryBoarderColor = summaryBoarderColor;
    }

    public int getSummaryTextColor() {
        return summaryTextColor;
    }

    public void setSummaryTextColor(int summaryTextColor) {
        this.summaryTextColor = summaryTextColor;
    }

    public void setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public int getStatusbarColor() {
        return statusbarColor;
    }

    public void setStatusbarColor(int statusbarColor) {
        this.statusbarColor = statusbarColor;
    }

    public int getBlurColor() {
        return blurColor;
    }

    public void setBlurColor(int blurColor) {
        this.blurColor = blurColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getHorizontalGridColor() {
        return horizontalGridColor;
    }

    public void setHorizontalGridColor(int horizontalGridColor) {
        this.horizontalGridColor = horizontalGridColor;
    }

    public int getVerticalGridColor() {
        return verticalGridColor;
    }

    public void setVerticalGridColor(int verticalGridColor) {
        this.verticalGridColor = verticalGridColor;
    }

    public int getGridTextColor() {
        return gridTextColor;
    }

    public void setGridTextColor(int gridTextColor) {
        this.gridTextColor = gridTextColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(separatorColor, backgroundColor);
    }

    public static Mode NIGHT_MODE = new Mode(
            Color.parseColor("#0B131E"),
            Color.parseColor("#151E27"),
            Color.parseColor("#212D3B"),
            Color.parseColor("#1A242E"),
            Color.parseColor("#1D2733"),
            Color.parseColor("#C117222D"),
            Color.parseColor("#3462ACDF"),
            Color.parseColor("#FDFDFE"),
            Color.parseColor("#161F2B"),
            Color.parseColor("#131C26"),
            Color.parseColor("#506372"),
            Color.parseColor("#506372"),
            Color.parseColor("#506372")
            );

    public static Mode DAY_MODE = new Mode(
            Color.parseColor("#DFDFDF"),
            Color.parseColor("#EFEFEF"),
            Color.parseColor("#517DA1"),
            Color.parseColor("#426381"),
            Color.parseColor("#FDFDFE"),
            Color.parseColor("#BAF0F4F6"),
            Color.parseColor("#324B80B4"),
            Color.parseColor("#222222"),
            Color.parseColor("#F0F0F1"),
            Color.parseColor("#E4EAEE"),
            Color.parseColor("#95A109"),
            Color.parseColor("#E3E3E3"),
            Color.parseColor("#222222")
            );

}