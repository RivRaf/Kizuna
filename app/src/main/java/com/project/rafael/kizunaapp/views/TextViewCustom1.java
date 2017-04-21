package com.project.rafael.kizunaapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by RAFAEL on 27/03/2017.
 */

public class TextViewCustom1 extends android.support.v7.widget.AppCompatTextView {
    public TextViewCustom1(Context context) {
        super(context);
        init(context);
    }

    public TextViewCustom1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewCustom1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/CinzelDecorative_Regular.ttf");
        setTypeface(typeface);
    }
}