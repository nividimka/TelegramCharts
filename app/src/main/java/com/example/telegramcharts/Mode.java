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
                int textColor) {
        this.separatorColor = separatorColor;
        this.backgroundColor = backgroundColor;
        this.toolbarColor = toolbarColor;
        this.statusbarColor = statusbarColor;
        this.viewBackgroundColor = viewBackgroundColor;
        this.blurColor = blurColor;
        this.frameColor = frameColor;
        this.textColor = textColor;
    }
    int frameColor;
    int viewBackgroundColor;
    int separatorColor;
    int backgroundColor;
    int toolbarColor;
    int statusbarColor;
    int blurColor;
    int textColor;
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
            Color.parseColor("#FDFDFE")
            );

    public static Mode DAY_MODE = new Mode(
            Color.parseColor("#DFDFDF"),
            Color.parseColor("#EFEFEF"),
            Color.parseColor("#517DA1"),
            Color.parseColor("#426381"),
            Color.parseColor("#FDFDFE"),
            Color.parseColor("#BAF0F4F6"),
            Color.parseColor("#324B80B4"),
            Color.parseColor("#222222")
            );

}