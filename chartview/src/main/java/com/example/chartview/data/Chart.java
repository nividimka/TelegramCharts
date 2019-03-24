package com.example.chartview.data;

import java.util.ArrayList;
import java.util.List;

public class Chart {
    private String name;
    private XLine xLine;
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

    public XLine getXLine() {
        return xLine;
    }

    public void setXLine(XLine xLine) {
        this.xLine = xLine;
    }

}
