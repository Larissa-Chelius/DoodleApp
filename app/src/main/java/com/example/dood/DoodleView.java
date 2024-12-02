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
    private ArrayList<Float> pointsX;
    private ArrayList<Float> pointsY;
    private int brushColor = Color.BLACK; // Default color

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize Paint for drawing
        paint = new Paint();
        paint.setColor(brushColor); // Set default color
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        // Initialize points lists
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
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

    // Method to set the brush color
    public void setBrushColor(int color) {
        brushColor = color;
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


