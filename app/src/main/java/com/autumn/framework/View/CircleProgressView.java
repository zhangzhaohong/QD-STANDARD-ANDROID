package com.autumn.framework.View;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Mysim on 2016/10/14.
 * Github: https://github.com/micadalee/miclee-fir-demo
 */
public class CircleProgressView extends View {


    private int[] RATE_COLORS = {0xFFbb59ff,0xFF44dcfc};
    private float mMaxProgress;
    private float mProgress;
    private float mSweepAngle;
    private String mLevel;
    private final int mCircleLineStrokeWidth = 50;
    private final int mTxtStrokeWidth = 10;

    // 画圆所在的距形区域
    private  RectF mRectF;
    private  Paint mPaint;
    private  Paint mTxtPaint;
    private  Context mContext;

    private float mWidth;
    private float mHeight;
    private float mSection;
    private CustomAnimation anim;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
        mTxtPaint = new Paint();
        mSection = 0;
        anim = new CustomAnimation();
        anim.setFillAfter(true);
        anim.setDuration(2000);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = this.getMeasuredWidth();
        mHeight = this.getMeasuredHeight();

        setLayerType(LAYER_TYPE_SOFTWARE,null);

        mSweepAngle = mProgress / mMaxProgress;

        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2 + 100; // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2 + 100; // 左上角y
        mRectF.right = mWidth - mCircleLineStrokeWidth / 2 - 100; // 左下角x
        mRectF.bottom = mHeight - mCircleLineStrokeWidth / 2 - 100; // 右下角y


        //绘制等级
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setStrokeWidth(mTxtStrokeWidth);
        mTxtPaint.setTextSize(100);
        mTxtPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        int strWidth = (int) mTxtPaint.measureText(mLevel,0,mLevel.length());
        int strHeight = 210;
        mTxtPaint.setStyle(Paint.Style.FILL);
        mTxtPaint.setColor(Color.WHITE);
        canvas.drawText(mLevel,mWidth/2 - strWidth/2,mHeight/2 + strHeight/2 + 50,mTxtPaint);

        // 绘制进度文案显示
        mTxtPaint.setTypeface(Typeface.DEFAULT);
        String text = (int) mProgress + "/" + (int) mMaxProgress;
        float textHeight = mHeight / 8;
        mTxtPaint.setTextSize(textHeight);
        int textWidth = ((int) mTxtPaint.measureText(text, 0, text.length()));
        mTxtPaint.setColor(Color.YELLOW);
        canvas.drawText(text, mWidth / 2 - textWidth / 2, mHeight / 2 + textHeight / 2, mTxtPaint);

        // 设置圆的画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setMaskFilter(null);
        mPaint.setShader(null);

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);

//        mSection = mProgress / mMaxProgress;

        mPaint.setColor(Color.RED);
        LinearGradient gradient = new LinearGradient(mWidth /2,0, mWidth /2, mHeight,RATE_COLORS,null, Shader.TileMode.MIRROR);
        mPaint.setShader(gradient);
        mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID));
        canvas.drawArc(mRectF, -90, mSection * 360, false, mPaint);

    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
//        this.invalidate();
    }

    public float getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        mMaxProgress = maxProgress;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public void startCustomAnimation(){
        this.startAnimation(anim);
    }

    class CustomAnimation extends Animation{

        public CustomAnimation() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            if (interpolatedTime < 1.0f){
                mSection = interpolatedTime * mSweepAngle;
            }else {
                mSection = mSweepAngle;
            }
            postInvalidate();
        }
    }

}
