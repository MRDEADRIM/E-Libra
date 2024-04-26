package com.mr_deadrim.ebook;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class ZoomLayout extends RecyclerView {
    private ScaleGestureDetector scaleGestureDetector;

    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;

    private float offsetX = 0.0f;
    private float offsetY = 0.0f;

    public ZoomLayout(Context context) {
        super(context);
        setup(context);
    }

    public ZoomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public ZoomLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new MoveListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private static final float MIN_SCALE_FACTOR = 0.0f;
        private static final float MAX_SCALE_FACTOR = 2.0f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float previousScaleFactor = scaleFactor;
            scaleFactor *= detector.getScaleFactor();
            // Limit the scale factor
            scaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(scaleFactor, MAX_SCALE_FACTOR));

            // Calculate the new dimensions of the RecyclerView
            int width = getWidth();
            int height = getHeight();
            float newWidth = width * scaleFactor;
            float newHeight = height * scaleFactor;

            // Calculate the maximum allowed offset to keep the content within the RecyclerView's borders
            float maxOffsetX = (newWidth - width) / 2;
            float maxOffsetY = (newHeight - height) / 2;

            // Adjust the offset to keep the zoom centered
            offsetX *= scaleFactor / previousScaleFactor;
            offsetY *= scaleFactor / previousScaleFactor;

            // Ensure that the zoom does not exceed the borders of the PDF
            if (newWidth <= width) { // If the zoomed content fits within or is smaller than the RecyclerView
                scaleFactor = width / (float) getWidth(); // Set scale factor to fit content within RecyclerView
                offsetX = 0.0f; // Center the content horizontally
            } else {
                offsetX = Math.max(-maxOffsetX, Math.min(offsetX, maxOffsetX));
            }

            if (newHeight <= height) { // If the zoomed content fits within or is smaller than the RecyclerView
                scaleFactor = height / (float) getHeight(); // Set scale factor to fit content within RecyclerView
                offsetY = 0.0f; // Center the content vertically
            } else {
                offsetY = Math.max(-maxOffsetY, Math.min(offsetY, maxOffsetY));
            }

            // Apply scale transformation to the RecyclerView
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);

            // Apply the offset to move the content
            setTranslationX(offsetX);
            setTranslationY(offsetY);

            return true;
        }
    }

    private class MoveListener extends GestureDetector.SimpleOnGestureListener {
        private static final float SCROLL_SPEED = 1.2f;
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            distanceX *= SCROLL_SPEED; // Adjust scroll speed here
            distanceY *= SCROLL_SPEED; // Adjust scroll speed here
            // Calculate the new offset based on the scroll distance
            Log.d("distance_x", String.valueOf(distanceX));
            Log.d("distance_y", String.valueOf(distanceY));
            offsetX -= distanceX;
            offsetY -= distanceY;

            // Get the dimensions of the RecyclerView
            int width = getWidth();
            int height = getHeight();

            // Calculate the maximum allowed offset
            float maxOffsetX = (getWidth() * scaleFactor - width) / 2;
            float maxOffsetY = (getHeight() * scaleFactor - height) / 2;

            // Apply bounds checking to prevent moving above the PDF page border
            offsetX = Math.max(-maxOffsetX, Math.min(offsetX, maxOffsetX));
            offsetY = Math.max(-maxOffsetY, Math.min(offsetY, maxOffsetY));

            // Apply the offset to move the content
            setTranslationX(offsetX);
            setTranslationY(offsetY);

            return true;
        }
    }
}


