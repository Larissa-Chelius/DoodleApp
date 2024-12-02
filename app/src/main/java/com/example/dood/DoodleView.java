package com.example.dood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DoodleView extends View {

    private Paint paint;
    private ArrayList<Float> pointsX;
    private ArrayList<Float> pointsY;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize Paint for drawing
        paint = new Paint();
        paint.setColor(0xFF000000); // Default color: black
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        // Initialize points lists
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Draw all points as lines
        for (int i = 1; i < pointsX.size(); i++) {
            float startX = pointsX.get(i - 1);
            float startY = pointsY.get(i - 1);
            float stopX = pointsX.get(i);
            float stopY = pointsY.get(i);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    public void clearCanvas() {
        pointsX.clear();
        pointsY.clear();
        invalidate(); // Redraw the view
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    public void setOpacity(float opacity) {
        paint.setAlpha((int) (opacity * 255));
    }

    public void setBrushColor(int color) {
        paint.setColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Add touch points to the list
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            pointsX.add(x);
            pointsY.add(y);
            invalidate(); // Redraw the view
        }

        return true; // Indicate that the touch event was handled
    }
}

