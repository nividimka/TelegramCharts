package com.example.telegramcharts;

import android.graphics.Color;

import java.util.Objects;

class Mode {
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
                int summaryBoarderColor) {
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
    }
    int frameColor;
    int viewBackgroundColor;
    int summaryBoarderColor;
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
            Color.parseColor("#E3E3E3")
            );

}