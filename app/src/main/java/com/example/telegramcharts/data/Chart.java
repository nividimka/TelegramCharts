package com.example.telegramcharts.data;

import java.util.ArrayList;
import java.util.List;

public class Chart {
    private String name;
    private Line xLine;
    private List<Line> yLines = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Line> getYLines() {
        return yLines;
    }

    public void addYLine(Line yLine) {
        this.yLines.add(yLine);
    }

    public Line getXLine() {
        return xLine;
    }

    public void setXLine(Line xLine) {
        this.xLine = xLine;
    }

}
