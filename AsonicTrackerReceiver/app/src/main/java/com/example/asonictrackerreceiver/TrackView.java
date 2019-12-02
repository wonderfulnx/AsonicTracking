package com.example.asonictrackerreceiver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import android.util.AttributeSet;

import java.util.jar.Attributes;

public class TrackView extends View{
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean startPoint = true;

    public TrackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TrackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrackView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
    }

    public void drawPath(float x, float y) {
        Log.i("draw", "here");
        if (startPoint) {
            startPoint = false;
            path.moveTo(x, y);
        } else {
            path.lineTo(x, y);
        }
        invalidate();
    }

}
