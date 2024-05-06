package com.mr_deadrim.ebook;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;

import androidx.recyclerview.widget.RecyclerView;

public class PdfLayout extends RecyclerView {

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private Matrix matrix;
    private float[] matrixValues;
    private float scaleFactor = 1.0f;
    private static final float MIN_SCALE_FACTOR = 1.0f;
    private static final float MAX_SCALE_FACTOR = 2.0f;
    private float translationSensitivity = 1.2f;
    private static final float HORIZONTAL_TRANSLATION_SENSITIVITY = 0.5f;
    private static final float VERTICAL_TRANSLATION_SENSITIVITY = 1.2f;

    public PdfLayout(Context context) {
        super(context);
        init(context);
    }

    public PdfLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PdfLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
        matrix = new Matrix();
        matrixValues = new float[9];
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                translationSensitivity = HORIZONTAL_TRANSLATION_SENSITIVITY;
            } else {
                translationSensitivity = VERTICAL_TRANSLATION_SENSITIVITY;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.concat(matrix);
        super.onDraw(canvas);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            matrix.postTranslate(-distanceX * translationSensitivity, -distanceY * translationSensitivity);
            clampTranslation();
            invalidate();
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactorChange = detector.getScaleFactor();
            float newScaleFactor = scaleFactor * scaleFactorChange;

            // Limit zoom out until the window border and Limit zoom in
            if (newScaleFactor < MIN_SCALE_FACTOR) {
                scaleFactorChange = MIN_SCALE_FACTOR / scaleFactor;
            }else if (newScaleFactor > MAX_SCALE_FACTOR) {
                scaleFactorChange = MAX_SCALE_FACTOR / scaleFactor;
            }

            scaleFactor *= scaleFactorChange;
            matrix.postScale(scaleFactorChange, scaleFactorChange, detector.getFocusX(), detector.getFocusY());
            clampTranslation();
            invalidate();
            return true;
        }
    }

    private void clampTranslation() {
        matrix.getValues(matrixValues);
        float[] values = matrixValues;
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];
        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float minX = -viewWidth * (scaleX - 1);
        float minY = -viewHeight * (scaleY - 1);
        float maxX = 0;
        float maxY = 0;

        float adjustedTransX = Math.min(Math.max(minX, transX), maxX);
        float adjustedTransY = Math.min(Math.max(minY, transY), maxY);

        matrix.postTranslate(adjustedTransX - transX, adjustedTransY - transY);
    }
}