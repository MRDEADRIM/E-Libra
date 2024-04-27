package com.mr_deadrim.ebook;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ZoomLayout extends RecyclerView {

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private float translateX = 0.0f;
    private float translateY = 0.0f;
    private float maxTranslateX;
    private float maxTranslateY;

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
            ViewCompat.setTranslationX(ZoomLayout.this, translateX);
            ViewCompat.setTranslationY(ZoomLayout.this, translateY);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private static final float MIN_SCALE_FACTOR = 1.0f; // Minimum scale factor to prevent zooming out beyond original size

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactorChange = detector.getScaleFactor() - 1.0f;
            float newScaleFactor = scaleFactor + scaleFactorChange; // Apply the change
            newScaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(newScaleFactor, 2.0f));
            if (getWidth() * newScaleFactor < ((View) getParent()).getWidth() ||
                    getHeight() * newScaleFactor < ((View) getParent()).getHeight()) {
                return true;
            }
            float scaleFactorDiff = newScaleFactor / scaleFactor;
            scaleFactor = newScaleFactor;
            ViewCompat.setScaleX(ZoomLayout.this, scaleFactor);
            ViewCompat.setScaleY(ZoomLayout.this, scaleFactor);
            translateX *= scaleFactorDiff;
            translateY *= scaleFactorDiff;
            clampTranslation();
            ViewCompat.setTranslationX(ZoomLayout.this, translateX);
            ViewCompat.setTranslationY(ZoomLayout.this, translateY);
            return true;
        }
    }

    private void clampTranslation() {
        View parent = (View) getParent();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        maxTranslateX = (getWidth() * scaleFactor - parentWidth) / 2;
        maxTranslateY = (getHeight() * scaleFactor - parentHeight) / 2;
        translateX = Math.max(-maxTranslateX, Math.min(translateX, maxTranslateX));
        translateY = Math.max(-maxTranslateY, Math.min(translateY, maxTranslateY));
    }
}