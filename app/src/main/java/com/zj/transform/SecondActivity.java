package com.zj.transform;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zj.transform.activity.SwipeBackActivity;

/**
 * Created by zhangjun on 2018/2/11.
 */

public class SecondActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSwipeBackLayout().setEnabled(true);
    }
}
