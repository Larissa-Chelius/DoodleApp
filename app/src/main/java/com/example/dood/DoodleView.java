package com.example.dood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DoodleView extends View {

    private Paint paint;
    private ArrayList<ArrayList<Float>> strokesX;
    private ArrayList<ArrayList<Float>> strokesY;
    private ArrayList<Integer> strokeColors;
    private ArrayList<Float> strokeOpacities;
    private ArrayList<Float> strokeWidths;
    private ArrayList<Float> currentStrokeX;
    private ArrayList<Float> currentStrokeY;
    private int brushColor = Color.BLACK;
    private int currentAlpha = 255;
    private float currentBrushWidth = 8f;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize Paint for drawing
        paint = new Paint();
        paint.setColor(brushColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(currentBrushWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        strokesX = new ArrayList<>();
        strokesY = new ArrayList<>();
        strokeColors = new ArrayList<>();
        strokeOpacities = new ArrayList<>();
        strokeWidths = new ArrayList<>();
        currentStrokeX = new ArrayList<>();
        currentStrokeY = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < strokesX.size(); i++) {
            paint.setColor(strokeColors.get(i));
            paint.setAlpha((int) (strokeOpacities.get(i) * 255));
            paint.setStrokeWidth(strokeWidths.get(i));
            ArrayList<Float> strokeX = strokesX.get(i);
            ArrayList<Float> strokeY = strokesY.get(i);
            for (int j = 1; j < strokeX.size(); j++) {
                float startX = strokeX.get(j - 1);
                float startY = strokeY.get(j - 1);
                float stopX = strokeX.get(j);
                float stopY = strokeY.get(j);
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }

        paint.setAlpha(currentAlpha);
        paint.setStrokeWidth(currentBrushWidth);
        for (int i = 1; i < currentStrokeX.size(); i++) {
            float startX = currentStrokeX.get(i - 1);
            float startY = currentStrokeY.get(i - 1);
            float stopX = currentStrokeX.get(i);
            float stopY = currentStrokeY.get(i);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    public void clearCanvas() {
        strokesX.clear();
        strokesY.clear();
        strokeColors.clear();
        strokeOpacities.clear();
        strokeWidths.clear();
        currentStrokeX.clear();
        currentStrokeY.clear();
        invalidate();
    }

    public void setBrushSize(float size) {
        currentBrushWidth = size;
        invalidate();
    }

    public void setOpacity(float opacity) {
        currentAlpha = (int) (opacity * 255);
        invalidate();
    }

    public void setBrushColor(int color) {
        brushColor = color;
        paint.setColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentStrokeX = new ArrayList<>();
                currentStrokeY = new ArrayList<>();
                strokeColors.add(brushColor);
                strokeOpacities.add(currentAlpha / 255f);
                strokeWidths.add(currentBrushWidth);
                currentStrokeX.add(x);
                currentStrokeY.add(y);
                break;

            case MotionEvent.ACTION_MOVE:
                currentStrokeX.add(x);
                currentStrokeY.add(y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                strokesX.add(currentStrokeX);
                strokesY.add(currentStrokeY);
                break;
        }

        return true;
    }
}
