package com.example.asonictrackerreceiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.util.AttributeSet;

import java.util.jar.Attributes;

public class TrackView extends View{
//    private Bitmap bitmap;
//    private Canvas canvas;
//    private Paint bitmapPaint;
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean startPoint = true;
    private int count = 1;
    private Rect bounds = new Rect();

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
//        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
//        paint.setTextSize(30);
//        String text = String.valueOf(count);
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        float textWidth = bounds.width();
//        float textHeight = bounds.height();
//        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 - textHeight / 2, paint);
    }

    public void drawPath(float x, float y) {
        Log.i("draw", "here");
        if (startPoint) {
            startPoint = false;
            path.moveTo(x, y);
        } else {
            path.lineTo(x, y);
        }
        count += 1;
        invalidate();
    }


//    public TrackView(Context context) {
//        super(context);
//
////        bitmap = Bitmap.createBitmap(context.get, Bitmap.Config.ARGB_8888);
//        bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(bitmap);
//
//        bitmapPaint = new Paint(Paint.DITHER_FLAG);
//        paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeJoin(Paint.Join.ROUND);
//        paint.setStrokeCap(Paint.Cap.SQUARE);
//        paint.setStrokeWidth(8);
//        paint.setColor(0xFF2145FF);
//        startPoint = true;
//    }
//
//    public void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.TRANSPARENT);
//        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
//        if (path != null) {
//            canvas.drawPath(path, paint);
//        }
//    }
//
//    public void drawPath(float x, float y) {
//        if (startPoint) {
//            startPoint = false;
//            path.moveTo(x, y);
//        } else {
//            path.lineTo(x, y);
//        }
//        canvas.drawPath(path, paint);
//        invalidate();
//    }
}
