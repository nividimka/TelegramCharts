package com.example.telegramcharts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import androidx.core.widget.CompoundButtonCompat;

public class ChartView extends FrameLayout {
    public ChartView(Context context) {
        super(context);
        initCheckboxes();
    }

    public ChartView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        initCheckboxes();
    }

    public ChartView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCheckboxes();
    }

    public void init(){
        initCheckboxes();
    }

    public void initCheckboxes(){
        CheckBox checkbox = new CheckBox(getContext());
        checkbox.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        CompoundButtonCompat.setButtonTintList(checkbox, ColorStateList.valueOf(Color.GREEN));
        addView(checkbox);
    }
}
