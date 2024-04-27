package com.mr_deadrim.ebook;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ZoomLayout extends RecyclerView {

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    public ZoomLayout(Context context) {
        super(context);
        setup(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private static final float SCALE_THRESHOLD = 0.02f; // Adjust as needed
        private float initialScaleFactor = 1.0f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initialScaleFactor = getScaleX(); // Store the initial scale factor
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float newScaleFactor = initialScaleFactor * scaleFactor;
            newScaleFactor = Math.max(0.1f, Math.min(newScaleFactor, 2.0f));
            float deltaScaleFactor = newScaleFactor / initialScaleFactor;
            int recyclerViewWidth = getWidth();
            int recyclerViewHeight = getHeight();
            View parent = (View) getParent();
            int parentWidth = parent.getWidth();
            int parentHeight = parent.getHeight();
            float minScaleX = (float) parentWidth / recyclerViewWidth;
            float minScaleY = (float) parentHeight / recyclerViewHeight;
            float minScale = Math.min(minScaleX, minScaleY);
            float newScaleX = getScaleX() * deltaScaleFactor;
            float newScaleY = getScaleY() * deltaScaleFactor;
            if (Math.abs(newScaleFactor - initialScaleFactor) > SCALE_THRESHOLD) {
                newScaleX = Math.max(newScaleX, minScale);
                newScaleY = Math.max(newScaleY, minScale);
                setScaleX(newScaleX);
                setScaleY(newScaleY);
            }
            return true;
        }
    }
}

