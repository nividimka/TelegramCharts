package com.example.telegramcharts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.telegramcharts.data.Line;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class FullChartView extends View {
    List<Line> lines;
    List<Paint> paints;
    public FullChartView(Context context) {
        super(context);
    }

    public FullChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FullChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lines!=null){
            int width = getWidth();
            int height = getHeight();
            //0*B+c = maxY
            //height*B + c = minY
            //(2-1) height*B = minY-maxY
            // B = (minY-maxY)/height
            // C = maxY
            //? = y-c/B
            //yCoord = (y-maxY)*height/(minY-maxY)
            int maxY = getMaxY(lines);
            int minY = getMinY(lines);
            for(int k =0; k < lines.size();k++) {
                Line line = lines.get(k);
                if(!line.isHidden()) {
                    int count = line.getColumns().length;
                    for (int i = 0; i < count - 1; i++) {
                        int y0 = line.getColumns()[i];
                        int y1 = line.getColumns()[i + 1];
                        int y0Scaled = (y0 - maxY) * height / (minY - maxY);
                        int y1Scaled = (y1 - maxY) * height / (minY - maxY);
                        int x0 = width * i / count;
                        int x1 = width * (i + 1) / count;
                        canvas.drawLine(x0, y0Scaled, x1, y1Scaled, paints.get(k));
                    }
                }
            }
        }
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
        paints = new ArrayList<>();
        for(Line line : lines){
            Paint paint = new Paint();
            paint.setColor(line.getColor());
            paint.setStrokeWidth(4);
            paints.add(paint);
        }
        invalidate();
    }

    public int getMaxY(List<Line> lines){
        int maxY = lines.get(0).getMaxY();
        for(Line line:lines){
            if(maxY < line.getMaxY()){
                maxY = line.getMaxY();
            }
        }
        return maxY;
    }

    public int getMinY(List<Line> lines){
        int minY = lines.get(0).getMinY();
        for(Line line:lines){
            if(minY > line.getMinY()){
                minY = line.getMinY();
            }
        }
        return minY;
    }
}
