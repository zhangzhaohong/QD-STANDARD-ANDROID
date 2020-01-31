package com.autumn.framework.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.autumn.reptile.R;

/**
 * Created by 23811 on 2017/09/25.
 */

public class ColorProgress extends View {

    private int startColor;          //起始颜色
    private int endColor;            //结束颜色
    private float sweepangle;          //划过角度
    private int startangle;          //起始角度
    private int currentprogress = 0;   //当前的进度
    private int allprogress = 100;        //总的进度
    private RectF rectf;              //绘制圆环所依赖的方块
    private float progresswidth;      //圆环宽度
    private Paint progresspaint;    //圆环的画笔
    private float drawAngle;


    //@Nullable这个注解的意思时，自定义的属性可以为空，这样的话，我们使用的时候，不去写，也不会报错
    public ColorProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取各个自定义属性，并且初始化默认属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressColor);
        startColor = ta.getColor(R.styleable.ProgressColor_startcolor, Color.argb(255, 255, 210, 117));   //进度条初始颜色
        endColor = ta.getColor(R.styleable.ProgressColor_endcolor, Color.argb(255, 255, 57, 216));    //进度条结束颜色
        sweepangle = ta.getInt(R.styleable.ProgressColor_sweepagle, 240);    //进度条将要滑动的角度
        startangle = ta.getInt(R.styleable.ProgressColor_startangle, 150);   //进度条起始角度
        progresswidth = ta.getDimension(R.styleable.ProgressColor_progresswidth, 15f);   //进度条宽度
        ta.recycle();   //回收自定义属性资源
        progresspaint = new Paint();
        progresspaint.setAntiAlias(true);               //设置抗锯齿
        progresspaint.setStrokeWidth(progresswidth);  //设置圆环宽度
        progresspaint.setStyle(Paint.Style.STROKE);    //设置填充方式为空心
        progresspaint.setStrokeCap(Paint.Cap.ROUND);    //设置圆环弧度两边样式，取值有Cap.ROUND(圆形线冒)、Cap.SQUARE(方形线冒)、Paint.Cap.BUTT(无线冒)
        drawAngle = (float) (sweepangle / allprogress);
}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mMeasureWidth = getMeasuredWidth();
        int mMeasureHeight = getMeasuredHeight();
        //根据控件的大小建造绘制弧度的方块模型
        rectf = new RectF(getPaddingLeft() + progresswidth, getPaddingTop() + progresswidth,
                mMeasureWidth + getPaddingRight() - progresswidth,
                mMeasureHeight + getPaddingBottom() - progresswidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas); //开始绘制弧度
        if (currentprogress < allprogress) {   //如果绘制进度没有完成，那么这里每次都会判断一次，接着绘制
            currentprogress++;//进度增加
            postInvalidate();//重新进行下一次的绘制
        }
    }

    //绘制进度条
    private void drawArc(Canvas canvas) {
        //当currentprogress == 100时，也就是1 * drawAngle，完成全部绘制，从进度我们可以知道会这样一直绘制100次，每次绘制增加一点度数
        for (int i = 0, end = (int) (currentprogress * drawAngle); i < end; i++){
        //动态设置画笔的颜色，从而达到渐变的效果
        //下面有个地方需要注意，那就是i / (float)end ,这个部分的float时不可以少的，因为本身end就是一个很小的带分数的个位数，再被取整的话，就
        // 会看不见颜色的渐变了
        progresspaint.setColor(getGradient(i / (float)end, startColor, endColor));
        //下面五个参数分别是:绘制弧度需要的方形模型，绘制的起始角度，每次绘制的角度大小，绘制时是否将圆形和弧度两边连起来，以及绘制所需要的paint
        canvas.drawArc(rectf, startangle + i, 1, false, progresspaint);
        }
    }

    //这个方法的作用在于从起始和结束颜色中取三原色和透明度，然后计算得出当前划过角度的位置应该显示什么颜色
    public int getGradient(float currentprogress, int startColor, int endColor) {
        if (currentprogress > 1)
            currentprogress = 1;  //当前的进度值，最高不会超过1，这个值取自当前划过的角度和除以总的角度
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaDifference = alphaEnd - alphaStart;
        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaCurrent = (int) (alphaStart + currentprogress * alphaDifference);
        int redCurrent = (int) (redStart + currentprogress * redDifference);
        int blueCurrent = (int) (blueStart + currentprogress * blueDifference);
        int greenCurrent = (int) (greenStart + currentprogress * greenDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);  //返回当前弧度该显示的颜色
    }

}
