package com.example.kys_8.easyforest.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by shahroz on 1/8/2016.
 */
public class DpUtil {

    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static void showSnackBarMessage(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public static float vDpToPx(Context context, float dp) {
        float px = getAbsValue(context, dp, TypedValue.COMPLEX_UNIT_DIP);
        return px;
    }

    private static float getAbsValue(Context context, float value, int unit) {
        return TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
    }

    public static float spToPx(Context context, float sp) {
        float px = getAbsValue(context, sp, TypedValue.COMPLEX_UNIT_SP);
        return px;
    }

    public static int getRvWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int marginWidth = (int) (density*42);
        return width - marginWidth;
    }

}
