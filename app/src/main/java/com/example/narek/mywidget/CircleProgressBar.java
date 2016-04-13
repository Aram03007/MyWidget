/*
 * Copyright (C) 2015 Pedramrn@gmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.narek.mywidget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


public class CircleProgressBar extends View {


    private float strokeWidth = 4;
    private float progress = 0;
    private int min = 0;
    private int max = 100;

    private int startAngle = 180;
    private int color = Color.DKGRAY;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    Paint textPaint;
    Path path;
    String lines = "";
    String volume = "";


    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        backgroundPaint.setStrokeWidth(strokeWidth);
        foregroundPaint.setStrokeWidth(strokeWidth);
        invalidate();
        requestLayout();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        int res = (int) progress;
        volume = "Volume " + res;
        invalidate();
    }


    public void setColor(int color) {
        this.color = color;
        backgroundPaint.setColor(adjustAlpha(color, 0.3f));
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }


    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        getVolume();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressBar,
                0, 0);
        try {
            strokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progressBarThickness, strokeWidth);
            progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress, progress);
            color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color);
            min = typedArray.getInt(R.styleable.CircleProgressBar_min, min);
            max = typedArray.getInt(R.styleable.CircleProgressBar_max, max);
        } finally {
            typedArray.recycle();
        }


        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, 0.3f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GREEN);

        path = new Path();


        for (int i = 1; i <= 10; i++) {

            lines += "|            ";


        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textPaint.setTextSize((float) ((rectF.right - rectF.left) * 0.1));
        path.addOval(rectF, Path.Direction.CW);

        canvas.drawText(volume, (float) (canvas.getWidth() * 0.3), (float) (canvas.getHeight() * 0.45), textPaint);
        canvas.drawTextOnPath(lines, path, 0, (float) ((rectF.right - rectF.left) * 0.1), textPaint);


        canvas.drawOval(rectF, backgroundPaint);
        canvas.drawText("Check Volume", (float) (rectF.left + ((rectF.centerX() - rectF.left) * 0.3)),
                (float) (rectF.centerY() + textPaint.getTextSize() * 0.5), textPaint);

        float angle = 360 * progress / max;

        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);

    }

    private void getVolume() {
        for (int i = 0; i < 10; i++) {
            if (getProgress() / 36 == i) {
                volume = "Volume " + i;
                invalidate();
            }
        }


    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }


    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public void setProgressWithAnimation(float progress) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);

        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();


    }
}




















