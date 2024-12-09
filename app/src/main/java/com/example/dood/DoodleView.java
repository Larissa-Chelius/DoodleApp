package com.example.dood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    // Stack for undo/redo functionality
    private ArrayList<StrokeData> undoStack = new ArrayList<>();
    private ArrayList<StrokeData> redoStack = new ArrayList<>();

    private static class StrokeData {
        ArrayList<Float> strokeX;
        ArrayList<Float> strokeY;
        int color;
        float opacity;
        float width;

        StrokeData(ArrayList<Float> strokeX, ArrayList<Float> strokeY, int color, float opacity, float width) {
            this.strokeX = strokeX;
            this.strokeY = strokeY;
            this.color = color;
            this.opacity = opacity;
            this.width = width;
        }
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        strokesX = new ArrayList<>();
        strokesY = new ArrayList<>();
        strokeColors = new ArrayList<>();
        strokeOpacities = new ArrayList<>();
        strokeWidths = new ArrayList<>();
        currentStrokeX = new ArrayList<>();
        currentStrokeY = new ArrayList<>();

        paint.setColor(brushColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(currentBrushWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < strokesX.size(); i++) {
            paint.setColor(strokeColors.get(i));
            paint.setAlpha((int) (strokeOpacities.get(i) * 255));
            paint.setStrokeWidth(strokeWidths.get(i));
            ArrayList<Float> strokeXList = strokesX.get(i);
            ArrayList<Float> strokeYList = strokesY.get(i);
            for (int j = 1; j < strokeXList.size(); j++) {
                float startX = strokeXList.get(j - 1);
                float startY = strokeYList.get(j - 1);
                float stopX = strokeXList.get(j);
                float stopY = strokeYList.get(j);
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }

        // Draw the current stroke
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
                currentStrokeX.add(x);
                currentStrokeY.add(y);
                break;

            case MotionEvent.ACTION_MOVE:
                currentStrokeX.add(x);
                currentStrokeY.add(y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                // Add the stroke to the list of strokes
                strokesX.add(currentStrokeX);
                strokesY.add(currentStrokeY);
                strokeColors.add(brushColor);
                strokeOpacities.add(currentAlpha / 255f);
                strokeWidths.add(currentBrushWidth);

                // Save the current stroke to the undo stack
                undoStack.add(new StrokeData(currentStrokeX, currentStrokeY, brushColor, currentAlpha / 255f, currentBrushWidth));
                // Clear the redo stack
                redoStack.clear();

                currentStrokeX = new ArrayList<>();
                currentStrokeY = new ArrayList<>();
                invalidate();
                break;
        }

        return true;
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            // Pop the last stroke from the undo stack and remove it from the main lists
            StrokeData lastStroke = undoStack.remove(undoStack.size() - 1);
            strokesX.remove(strokesX.size() - 1);
            strokesY.remove(strokesY.size() - 1);
            strokeColors.remove(strokeColors.size() - 1);
            strokeOpacities.remove(strokeOpacities.size() - 1);
            strokeWidths.remove(strokeWidths.size() - 1);

            // Push the stroke onto the redo stack for future redo
            redoStack.add(lastStroke);

            invalidate();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            // Pop the last stroke from the redo stack and add it back to the main lists
            StrokeData lastStroke = redoStack.remove(redoStack.size() - 1);
            strokesX.add(lastStroke.strokeX);
            strokesY.add(lastStroke.strokeY);
            strokeColors.add(lastStroke.color);
            strokeOpacities.add(lastStroke.opacity);
            strokeWidths.add(lastStroke.width);

            // Push the stroke onto the undo stack for future undo
            undoStack.add(lastStroke);

            invalidate();
        }
    }

    public void saveDrawing() {
        try {
            JSONObject drawingData = new JSONObject();
            JSONArray strokesArray = new JSONArray();

            for (int i = 0; i < strokesX.size(); i++) {
                JSONObject stroke = new JSONObject();
                stroke.put("color", strokeColors.get(i));
                stroke.put("opacity", strokeOpacities.get(i));
                stroke.put("width", strokeWidths.get(i));
                stroke.put("xPoints", new JSONArray(strokesX.get(i)));
                stroke.put("yPoints", new JSONArray(strokesY.get(i)));
                strokesArray.put(stroke);
            }

            drawingData.put("strokes", strokesArray);

            FileOutputStream fos = getContext().openFileOutput("doodle.json", Context.MODE_PRIVATE);
            fos.write(drawingData.toString().getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDrawing() {
        try {
            FileInputStream fis = getContext().openFileInput("doodle.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String jsonData = new String(data);
            JSONObject drawingData = new JSONObject(jsonData);
            JSONArray strokesArray = drawingData.getJSONArray("strokes");

            strokesX.clear();
            strokesY.clear();
            strokeColors.clear();
            strokeOpacities.clear();
            strokeWidths.clear();

            for (int i = 0; i < strokesArray.length(); i++) {
                JSONObject stroke = strokesArray.getJSONObject(i);

                ArrayList<Float> xPoints = new ArrayList<>();
                ArrayList<Float> yPoints = new ArrayList<>();
                JSONArray xArray = stroke.getJSONArray("xPoints");
                JSONArray yArray = stroke.getJSONArray("yPoints");

                for (int j = 0; j < xArray.length(); j++) {
                    xPoints.add((float) xArray.getDouble(j));
                    yPoints.add((float) yArray.getDouble(j));
                }

                strokesX.add(xPoints);
                strokesY.add(yPoints);
                strokeColors.add(stroke.getInt("color"));
                strokeOpacities.add((float) stroke.getDouble("opacity"));
                strokeWidths.add((float) stroke.getDouble("width"));
            }

            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
