package com.autumn.framework.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Iflytek_dsw on 2017/6/20.
 */
public class HWLoadingView extends View {
    private PathMeasure pathMeasure;
    /**当前绘制圆弧的路径*/
    private Path pathCircle;
    /**测量路径圆的半径*/
    private Path pathMeasureCircle;
    /** 绘制圆圈的坐标x、y */
    private float x,y;
    /**圆圈的半径*/
    private static final int radius = 10;
    private static int strokeWidth = 4;
    /**圆圈的颜色*/
    private int circleColor = Color.parseColor("#28C0C6");
    /**圆弧的颜色*/
    private int circlePathColor = Color.parseColor("#C3C3C3");
    /**圆圈的画笔*/
    private Paint mCirclePaint;
    /**圆弧的画笔*/
    private Paint mCirclePathPaint;
    /**当前位置的坐标*/
    private float[] currentLocation = new float[2];
    private float[] currentTan = new float[2];
    public HWLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePathPaint = new Paint();
        mCirclePathPaint.setStrokeWidth(strokeWidth);
        mCirclePathPaint.setColor(circlePathColor);
        mCirclePathPaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //此处创建Path外围圆的路径
        pathCircle = new Path();
        pathMeasureCircle = new Path();
        pathCircle.addArc(new RectF(radius , radius, w/2 - radius, h/2 -radius), -90, 360);
        pathMeasureCircle.addArc(new RectF(radius , radius, w/2 - radius, h/2 -radius), -90, 359);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(pathCircle, mCirclePathPaint);
        if(x == 0 && y == 0){
            startAnimation();
        }else{
            canvas.drawCircle(x ,y, radius, mCirclePaint);
        }
    }

    /**
     * 开始绘制变换动画
     */
    private void startAnimation(){
        /**创建一个绑定路径Path的PathMeasure对象*/
        pathMeasure = new PathMeasure(pathMeasureCircle, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前计算的值
                float len = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(len, currentLocation,currentTan);
                x = currentLocation[0];
                y = currentLocation[1];
                invalidate();
            }
        });
        valueAnimator.start();
    }
}