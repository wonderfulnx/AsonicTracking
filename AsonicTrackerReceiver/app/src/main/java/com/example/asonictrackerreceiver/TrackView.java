package com.example.asonictrackerreceiver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import java.util.jar.Attributes;

public class TrackView extends View{
    private Paint paint = new Paint();
    private Paint axePaint = new Paint();
    private Path path = new Path();
    private boolean isStartPoint = true;
    private int margin = 50;
    private static final int LINE_NUM = 20;
    private String[] scaleX = {"0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5"};
    private String[] scaleY = {"", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5"};

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

        float startX = margin;
        float startY = getHeight() - margin;

        axePaint.setStyle(Paint.Style.STROKE);
        axePaint.setAntiAlias(true);
        axePaint.setColor(Color.GRAY);
        axePaint.setTextSize(20);

        for (int i = 0; i < LINE_NUM; ++i) {
            if (i % 2 == 0) {
                axePaint.setColor(Color.BLACK);
                axePaint.setStrokeWidth(2);
            } else {
                axePaint.setColor(Color.GRAY);
                axePaint.setStrokeWidth(1);
            }
            canvas.drawLine(startX, startY - i * startY / LINE_NUM, getWidth(), startY - i * startY / LINE_NUM, axePaint);
            canvas.drawLine(startX + i * (getWidth() - margin) / LINE_NUM, 0, startX + i * (getWidth() - margin) / LINE_NUM, startY, axePaint);
        }
        axePaint.setColor(Color.BLACK);
        for (int i = 0; i <= LINE_NUM / 2; ++i) {
            canvas.drawText(scaleX[i], startX - 10 + i * 2 * (getWidth() - margin) / LINE_NUM, getHeight() - 25, axePaint);
            canvas.drawText(scaleY[i], 20, startY - i * 2 * startY / LINE_NUM, axePaint);
        }



        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
    }

    public void drawPath(float x, float y) {
        x = x * (float) (getWidth() - margin);
        y = (1 - y) * (float) (this.getHeight() - margin);
        if (isStartPoint) {
            isStartPoint = false;
            path.moveTo(x, y);
        } else {
            path.lineTo(x, y);
//            Log.i("point:", String.format("x: %.1f, y:%.1f", x, y));
        }
        invalidate();
    }

}
