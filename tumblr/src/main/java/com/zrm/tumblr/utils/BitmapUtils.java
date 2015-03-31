package com.zrm.tumblr.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by zhangrm on 2015/3/31 0031.
 */
public class BitmapUtils {

    public static Bitmap scaleWithWidth(Bitmap bm,int newWidth){
        int width = bm.getWidth();
        int height = bm.getHeight();

        int newHeight = newWidth*height/width;

        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
    }


}
