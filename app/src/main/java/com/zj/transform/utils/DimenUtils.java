package com.zj.transform.utils;

import android.content.Context;

/**
 * Created by zhangjun on 2018/2/11.
 */

public class DimenUtils {

    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
