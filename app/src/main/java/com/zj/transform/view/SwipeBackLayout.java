package com.zj.transform.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by zhangjun on 2018/2/11.
 */

public class SwipeBackLayout extends FrameLayout {
    public static final int STATE_NONE = 111;
    public static final int STATE_SLIDING = 222;
    public static final int STATE_PENDDING = 333;

    private int thresHoldForSwipeBackRegion;
    private int thresHoldForSwipeFinishRegion;

    private int startX;
    private int startY;
    private int state = STATE_NONE;

    private boolean enabled = false;

    public SwipeBackLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        thresHoldForSwipeBackRegion = getResources().getDisplayMetrics().widthPixels;
        thresHoldForSwipeFinishRegion = getResources().getDisplayMetrics().widthPixels/4;
    }

    public boolean isSwiping() {
        return state == STATE_SLIDING;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!enabled) {
            return false;
        }

        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getPointerCount() > 1) {
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getPointerCount() > 1) {
                    return false;
                }
                break;
        }

        return intercept;
    }
}
