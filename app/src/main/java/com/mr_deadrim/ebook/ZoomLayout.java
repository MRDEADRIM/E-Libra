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
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private float translateX = 0.0f;
    private float translateY = 0.0f;

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
        gestureDetector = new GestureDetector(context, new GestureListener());
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Translate the view based on scroll distance
            translateX -= distanceX / scaleFactor;
            translateY -= distanceY / scaleFactor;
            // Clamp translation to stay within bounds
            clampTranslation();
            // Apply translation
            setTranslationX(translateX);
            setTranslationY(translateY);
            return true;
        }
    }

    private void clampTranslation() {
        // Get parent dimensions
        View parent = (View) getParent();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        // Calculate maximum translation
        float maxTranslateX = (getWidth() * scaleFactor - parentWidth) / 2;
        float maxTranslateY = (getHeight() * scaleFactor - parentHeight) / 2;
        // Clamp translation
        translateX = Math.max(-maxTranslateX, Math.min(translateX, maxTranslateX));
        translateY = Math.max(-maxTranslateY, Math.min(translateY, maxTranslateY));
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private static final float MIN_SCALE_FACTOR = 1.0f; // Minimum scale factor to prevent zooming out beyond original size

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactorChange = detector.getScaleFactor() - 1.0f; // Calculate the change in scale factor
            float newScaleFactor = scaleFactor + scaleFactorChange; // Apply the change
            newScaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(newScaleFactor, 2.0f)); // Clamp the new scale factor

            // Check if the new scale factor would make the view smaller than the parent
            if (getWidth() * newScaleFactor < ((View) getParent()).getWidth() ||
                    getHeight() * newScaleFactor < ((View) getParent()).getHeight()) {
                return true; // Prevent further scaling
            }

            // Calculate the difference between the new and current scale factors
            float scaleFactorDiff = newScaleFactor / scaleFactor;
            scaleFactor = newScaleFactor; // Update the current scale factor
            // Apply scaling
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
            // Adjust translation to keep the zoom centered
            translateX *= scaleFactorDiff;
            translateY *= scaleFactorDiff;
            // Clamp translation after scaling
            clampTranslation();
            // Apply translation
            setTranslationX(translateX);
            setTranslationY(translateY);
            return true;
        }
    }
}
