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
    private ArrayList<ArrayList<Float>> strokesX;  // List of X coordinates for all strokes
    private ArrayList<ArrayList<Float>> strokesY;  // List of Y coordinates for all strokes
    private ArrayList<Integer> strokeColors;        // List of colors for each stroke
    private ArrayList<Float> currentStrokeX;        // Current stroke X coordinates
    private ArrayList<Float> currentStrokeY;        // Current stroke Y coordinates
    private int brushColor = Color.BLACK;           // Default color

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

        strokesX = new ArrayList<>();
        strokesY = new ArrayList<>();
        strokeColors = new ArrayList<>();
        currentStrokeX = new ArrayList<>();  // Initialize current stroke X coordinates
        currentStrokeY = new ArrayList<>();  // Initialize current stroke Y coordinates
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw each stroke with its respective color
        for (int i = 0; i < strokesX.size(); i++) {
            paint.setColor(strokeColors.get(i)); // Set color for each stroke
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

        // Draw the current stroke while the user is touching
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
        currentStrokeX.clear();
        currentStrokeY.clear();
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
        brushColor = color; // Update the brush color
        paint.setColor(color); // Apply the color to the paint object
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Start a new stroke
                currentStrokeX = new ArrayList<>();
                currentStrokeY = new ArrayList<>();
                // Add new stroke's color
                strokeColors.add(brushColor);
                // Add the first point of the stroke
                currentStrokeX.add(x);
                currentStrokeY.add(y);
                break;

            case MotionEvent.ACTION_MOVE:
                // Add touch points to the current stroke
                currentStrokeX.add(x);
                currentStrokeY.add(y);
                invalidate(); // Redraw the view
                break;

            case MotionEvent.ACTION_UP:
                // End the current stroke and save it
                strokesX.add(currentStrokeX);
                strokesY.add(currentStrokeY);
                break;
        }

        return true; // Indicate that the touch event was handled
    }
}
