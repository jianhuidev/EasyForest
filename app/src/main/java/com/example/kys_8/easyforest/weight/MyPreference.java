package com.example.kys_8.easyforest.weight;

import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kys-8 on 2018/10/1.
 */

public class MyPreference extends Preference {

    public MyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        if (view instanceof TextView){
            TextView tv = (TextView) view;
            tv.setTextColor(Color.RED);
        }
    }
}
