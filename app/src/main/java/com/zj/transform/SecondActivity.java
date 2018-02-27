package com.zj.transform;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zj.transform.activity.SwipeBackActivity;
import com.zj.transform.utils.AnimationUtils;
import com.zj.transform.utils.AnimatorAdapter;
import com.zj.transform.utils.TransitionAdapter;
import com.zj.transform.utils.TransitionUtils;

/**
 * Created by zhangjun on 2018/2/11.
 */

public class SecondActivity extends SwipeBackActivity {
    public final static String EXTRA_MOVIE_POSITION         = "movie_position";
    public final static String SHARED_ELEMENT_COVER         = "cover";

    private TextView mTitle;
    private ImageButton mFabButton;
    private LinearLayout mMovieDescriptionContainer;
    private ImageView mCoverImageView;
    private ScrollView mObservableScrollView;
    private int[] mViewLastLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSwipeBackLayout().setEnabled(true);
        initView();
        initializeStartAnimation();
    }

    private void initView() {
        mTitle = findViewById(R.id.activity_detail_title);
        mFabButton = findViewById(R.id.activity_detail_fab);
        mMovieDescriptionContainer = findViewById(R.id.activity_detail_book_info);
        mCoverImageView = findViewById(R.id.item_movie_cover);
        mObservableScrollView = findViewById(R.id.activity_detail_scroll);
    }

    private void initializeStartAnimation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (true) {

                AnimationUtils.makeTheStatusbarTranslucent(this);
            }
            configureEnterTransition ();

        } else {

            mViewLastLocation = getIntent().getIntArrayExtra(
                    MainActivity.EXTRA_MOVIE_LOCATION);

            configureEnterAnimation ();
        }
    }

    private void configureEnterAnimation() {

        if (true) {

            AnimationUtils.startScaleAnimationFromPivotY(
                    mViewLastLocation[0], mViewLastLocation[1],
                    mObservableScrollView, new AnimatorAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            super.onAnimationEnd(animation);
                            AnimationUtils.showViewByScale(mFabButton);
                        }
                    }
            );

            animateElementsByScale();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void configureEnterTransition() {

        getWindow().setSharedElementEnterTransition(
                TransitionUtils.makeSharedElementEnterTransition(this));

        postponeEnterTransition();

        int moviePosition = getIntent().getIntExtra(
                EXTRA_MOVIE_POSITION, 0);

        mCoverImageView.setTransitionName(SHARED_ELEMENT_COVER + moviePosition);
        mObservableScrollView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {

                        mObservableScrollView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);

                        startPostponedEnterTransition();
                        return true;
                    }
                }
        );

        getWindow().getSharedElementEnterTransition().addListener(
                new TransitionAdapter() {

                    @Override
                    public void onTransitionEnd(Transition transition) {

                        super.onTransitionEnd(transition);
                        animateElementsByScale();
                    }
                }
        );
    }

    private void animateElementsByScale() {

        AnimationUtils.showViewByScale(mFabButton);
        AnimationUtils.showViewByScaleY(mTitle, new AnimatorAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);
                AnimationUtils.showViewByScale(mMovieDescriptionContainer);
            }
        });
    }
}
