package com.example.baseframe.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.baseframe.utils.CommUtils;


public class ArrowView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint mPaint;
    private Path mPath;
    private ValueAnimator mValueAnimIn, mValueAnimOut;

    public ArrowView(Context context) {
        super(context);
        init();
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLACK);
        //实例化路径
        mPath = new Path();
        initAnim();
    }

    public void AnimToggle(boolean isChecked) {
        if (isChecked) {
            mValueAnimIn.start();
        } else {
            mValueAnimOut.start();
        }
    }

    private void initAnim() {
        mValueAnimIn = ValueAnimator.ofInt(0, -180);
        mValueAnimIn.setDuration(300);
        mValueAnimIn.addUpdateListener((animation) -> {
            int degree = (int) animation.getAnimatedValue();
            setColorSet((float) ((Math.abs(degree) * 0.55)) / 100);
            setRotation(degree);
        });
        mValueAnimOut = ValueAnimator.ofInt(-180, 0);
        mValueAnimOut.setDuration(300);
        mValueAnimOut.addUpdateListener((animation) -> {
            int degree = (int) animation.getAnimatedValue();
            setColorSet((float) (Math.abs(degree) * 0.55) / 100);
            setRotation(degree);
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int mVerticalOffset = 2;
        mPath.moveTo(0, CommUtils.dip2px(mVerticalOffset));
        int x = 8;
        mPath.lineTo(CommUtils.dip2px(x), CommUtils.dip2px(mVerticalOffset));
        int y = 6;
        mPath.lineTo(CommUtils.dip2px(x) >> 1, CommUtils.dip2px(y));
        mPath.close(); // 使这些点构成封闭的三角形
        canvas.drawPath(mPath, mPaint);
    }

    public void setColorSet(float alpha) {
        mPaint.setColor(getCurrentColor(alpha, Color.parseColor("#333333"), Color.parseColor("#5084FC")));
        invalidate();
    }

    /**
     * 根据fraction值来计算当前的颜色。 fraction值范围  0f-1f
     */
    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }
}
