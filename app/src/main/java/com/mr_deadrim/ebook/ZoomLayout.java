package com.mr_deadrim.ebook;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.recyclerview.widget.RecyclerView;


public class ZoomLayout extends RecyclerView {

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private float translateX = 0.0f;
    private float translateY = 0.0f;
    private float maxTranslateX;
    private float maxTranslateY;
    private static final float MIN_SCALE_FACTOR = 1.0f;

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
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
            translateX -= distanceX / scaleFactor;
            translateY -= distanceY / scaleFactor;
            clampTranslation();
            setTranslationX(translateX);
            setTranslationY(translateY);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactorChange = detector.getScaleFactor() - 1.0f;
            float newScaleFactor = scaleFactor + scaleFactorChange;
            newScaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(newScaleFactor, 2.0f));
            if (getWidth() * newScaleFactor < getParentWidth() ||
                    getHeight() * newScaleFactor < getParentHeight()) {
                return true;
            }
            float scaleFactorDiff = newScaleFactor / scaleFactor;
            scaleFactor = newScaleFactor;
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
            translateX *= scaleFactorDiff;
            translateY *= scaleFactorDiff;
            clampTranslation();
            setTranslationX(translateX);
            setTranslationY(translateY);
            return true;
        }
    }

    private void clampTranslation() {
        int parentWidth = getParentWidth();
        int parentHeight = getParentHeight();
        maxTranslateX = (getWidth() * scaleFactor - parentWidth) / 2;
        maxTranslateY = (getHeight() * scaleFactor - parentHeight) / 2;
        translateX = Math.max(-maxTranslateX, Math.min(translateX, maxTranslateX));
        translateY = Math.max(-maxTranslateY, Math.min(translateY, maxTranslateY));
    }

    private int getParentWidth() {
        return ((android.view.View) getParent()).getWidth();
    }

    private int getParentHeight() {
        return ((android.view.View) getParent()).getHeight();
    }
}