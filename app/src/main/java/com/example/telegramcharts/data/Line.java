package com.example.telegramcharts.data;

import android.graphics.Color;

public class Line<T> {
    T[] columns;
    String name;
    int color;
    boolean hidden;

    public T[] getColumns() {
        return columns;
    }

    public void setColumns(T[] columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
