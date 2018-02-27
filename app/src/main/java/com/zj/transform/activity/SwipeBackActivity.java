package com.zj.transform.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zj.transform.view.SwipeBackLayout;

/**
 * Created by zhangjun on 2018/2/11.
 */

public class SwipeBackActivity extends FragmentActivity implements SwipeBackContainer, SwipeBackInterface{
    private SwipeBackLayout swipeBackLayout;
    private ImageView imgShadow;
    private View view;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getContainer());
        view = LayoutInflater.from(this).inflate(layoutResID, null);
        swipeBackLayout.addView(view);
    }

    public void finishNoAnim() {
        finish();
        overridePendingTransition(0, 0);
    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        imgShadow = new ImageView(this);
        imgShadow.setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(imgShadow, params);
        container.addView(swipeBackLayout, params);
        swipeBackLayout.setSwipeBackInterface(this);
        return container;
    }

    private void setRootPaddingTop(int statusBarHeight) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = statusBarHeight;
        swipeBackLayout.setLayoutParams(params);
        imgShadow.setLayoutParams(params);
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (swipeBackLayout != null && swipeBackLayout.isSwiping()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }


    public void disableBackgroundWhite() {
        view.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    public void markViewSwipable(View view) {
        swipeBackLayout.markViewSwipable(view);
    }

    public void markViewNotSwipable(View view) {
        swipeBackLayout.markViewNotSwipable(view);
    }


    @Override
    public void onSwipeBackProgress(int percent) {
        if (percent>=0 && percent<=100) {
            imgShadow.getBackground().setAlpha(100-percent);
        }
    }
}
