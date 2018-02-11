package com.zj.transform.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zj.transform.activity.SwipeBackActivity;
import com.zj.transform.activity.SwipeBackInterface;
import com.zj.transform.utils.DimenUtils;
import com.zj.transform.utils.ViewGroupChecker;

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

    private View target;
    private ViewGroupChecker swipeCheckers = new ViewGroupChecker();
    private SwipeBackInterface swipeBackInterface;

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

    public void setSwipeBackInterface(SwipeBackInterface swipeBackInterface) {
        this.swipeBackInterface = swipeBackInterface;
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
                onFingerPrepareDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getPointerCount() > 1) {
                    return false;
                }
                intercept = onFingerPrepareMove(ev);
                break;
        }

        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (state == STATE_PENDDING) {
                    state = STATE_SLIDING;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onFingerMove(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                onFingerUp(event, true);
                break;
            case MotionEvent.ACTION_UP:
                onFingerUp(event, false);
                break;
        }
        return true;
    }

    private void onFingerPrepareDown(MotionEvent ev) {
        state = STATE_NONE;
        startX = (int) ev.getRawX();
        startY = (int) ev.getRawY();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            state = STATE_NONE;
            return;
        }

        if (startX < thresHoldForSwipeFinishRegion) {
            target = getChildAt(0);
            if (!checkCheckers(ev)) {
                state = STATE_PENDDING;
            }
        }
    }

    private boolean onFingerPrepareMove(MotionEvent ev) {
        if (state == STATE_PENDDING) {
            int dx = (int) (ev.getRawX() - startX);
            int dy = (int) (ev.getRawY() - startY);
            if (dx > DimenUtils.dp2px(getContext(), 10) && Math.abs(dx) > 2*Math.abs(dy) ) {
                state = STATE_SLIDING;
                return true;
            }
        }
        return false;
    }

    private void onFingerMove(MotionEvent event) {
        if (state == STATE_SLIDING && target != null) {
            int dx = Math.max((int) (event.getRawX() - startX), 0);
            target.setTranslationX(dx);
            if (swipeBackInterface != null) {
                swipeBackInterface.onSwipeBackProgress(dx*100/getWidth());
            }
        }
    }

    private void onFingerUp(MotionEvent event, boolean isCanceled) {
        int dx = Math.max((int) (event.getRawX() - startX), 0);
        if (dx > thresHoldForSwipeFinishRegion && !isCanceled) {
            finish(dx);
        }else {
            state = STATE_NONE;
            resetViewPosition(dx);
        }
    }

    private boolean checkCheckers(MotionEvent event) {
        return target != null && swipeCheckers.check((ViewGroup) target, event);
    }

    private void resetViewPosition(int fromX) {
        if (null != target) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationX", fromX, 0);
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.setDuration(300);
            animator.start();
        }
    }

    private void finish(int fromX) {
        if (null != target) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationX", fromX,
                    getResources().getDisplayMetrics().widthPixels);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (null != target) {
                        target.setVisibility(INVISIBLE);
                        if (target instanceof ViewGroup) {
                            ((ViewGroup) target).removeAllViews();
                        }

                        Activity activity = (Activity) getContext();
                        if (activity instanceof SwipeBackActivity) {
                            ((SwipeBackActivity) activity).finishNoAnim();
                        }else {
                            activity.finish();
                        }
                    }
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float dx = (float) animation.getAnimatedValue();
                    if (swipeBackInterface != null) {
                        swipeBackInterface.onSwipeBackProgress((int) (dx*100/getWidth()));
                    }
                }
            });
            animator.setDuration(300);
            animator.start();
        }
    }

    public void markViewSwipable(View view) {
        swipeCheckers.markViewSwipable(view);
    }

    public void markViewNotSwipable(View view) {
        swipeCheckers.markViewNotSwipable(view);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
