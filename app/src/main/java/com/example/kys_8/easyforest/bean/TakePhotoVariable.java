package com.example.kys_8.easyforest.bean;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;


import com.example.kys_8.easyforest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by kys-29 on 2017/10/3.
 */

public class TakePhotoVariable {
    public static final String TAG = TakePhotoVariable.class.getName();

    /**
     * 选取图片界面参数
     */
    public static final int MAX_SELECT_PHOTO_COUNT = 9;
    public static final int SPAN_COUNT = 3;


    public static String getAddBitmapPath(Context context){
        String addBitmaoPath = context.getFilesDir() + File.separator + "add_1.png";
        setDrawableBitmap(context,addBitmaoPath);
        return addBitmaoPath;
    }

    private static boolean setDrawableBitmap(Context context,String addBitmaoPath) {
        BitmapDrawable d = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.add_1);
        Bitmap img = d.getBitmap();
        try {
            OutputStream os = new FileOutputStream(addBitmaoPath);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
