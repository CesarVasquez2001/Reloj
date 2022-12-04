package com.example.reloj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class AnalogClockView extends View {
    private int mHeight;
    private int mWidth;
    private int mRadius;
    private double mAngle;
    private int mCentreX;
    private int mCentreY;
    private int mPadding;
    private boolean mIsInit;
    private Paint mPaint;
    private Path mPath;
    private Rect mRect;
    private int[] mNumbers;
    private int mMinimum;
    private float mHour;
    private float mMinute;
    private float mSecond;
    private int mHourHandSize;
    private int mHandSize;
    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void init(){
        mHeight = getHeight();
        mWidth = getWidth();
        mPadding = 50;
        mCentreX = mWidth/2;
        mCentreY = mHeight/2;
        mMinimum = Math.min(mHeight, mWidth);
        mRadius = mMinimum/2 - mPadding;
        mAngle = (float) ((Math.PI/30) - (Math.PI/2));
        mPaint = new Paint();
        mPath = new Path();
        mRect = new Rect();
        mHourHandSize = mRadius - mRadius/2;
        mHandSize = mRadius - mRadius/4;
        mNumbers = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        mIsInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsInit) {
            init();
        }
        drawCircle(canvas);
        drawHands(canvas, 0.0F, 0.0F);
        drawNumerals(canvas);
        postInvalidateDelayed(500);
    }

    private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth) {
        mPaint.reset();
        mPaint.setColor(colour);
        mPaint.setStyle(stroke);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.reset();
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE,8);
        canvas.drawCircle(mCentreX,mCentreY,mRadius,mPaint);
    }

    private void drawHands(Canvas canvas,float min,float seg) {
        Calendar calendar = Calendar.getInstance();
        if (seg==0.0){
            seg+=1.0;
        }
        drawMinuteHand(canvas,min);
        drawSecondsHand(canvas,seg);
    }

    private void drawMinuteHand(Canvas canvas, float location) {
        mPaint.reset();
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE,8);
        mAngle = Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(mCentreX,
                mCentreY,
                (float)(mCentreX + Math.cos( mAngle)* mHandSize),
                (float)(mCentreY+Math.sin(mAngle)*mHourHandSize),
                mPaint);
    }

    private void drawSecondsHand(Canvas canvas, float location) {
        mPaint.reset();
        setPaintAttributes(Color.RED, Paint.Style.STROKE,8);
        mAngle = Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(mCentreX,mCentreY,(float)   (mCentreX+Math.cos(mAngle)*mHandSize)
                , (float) (mCentreY+Math.sin(mAngle)*mHourHandSize),mPaint);
    }
    private void drawNumerals(Canvas canvas) {
        for (int number : mNumbers) {
            if (number ==3 ||number==6||number==9||number==12){
                String num = String.valueOf(number);
                mPaint.getTextBounds(num, 0, num.length(), mRect);
                double angle = Math.PI / 6 * (number - 3);
                int x = (int) (mCentreX + Math.cos(angle) * mRadius - mRect.width() / 2);
                int y = (int) (mCentreY + Math.sin(angle) * mRadius + mRect.height() / 2);
                canvas.drawText(num, x, y, mPaint);
            }
        }
    }
}