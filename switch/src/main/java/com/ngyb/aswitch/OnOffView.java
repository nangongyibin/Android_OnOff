package com.ngyb.aswitch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2019/10/18 11:36
 */
public class OnOffView extends View {

    private final Bitmap mTbg;
    private final Bitmap mTsb;
    private float slideMax = 0;
    private boolean isOpen = false;
    private float toLeft = 0;
    private float mDownX;
    private boolean isUp = false;
    private StateListener stateListener;

    public OnOffView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        //背景图
        mTbg = BitmapFactory.decodeResource(getResources(), R.drawable.toogle_background);
        //滑动图
        mTsb = BitmapFactory.decodeResource(getResources(), R.drawable.toogle_slidebg);
        //滑块滑动的最大宽度
        slideMax = mTbg.getWidth() - mTsb.getWidth();
        String namespace = "http://schemas.android.com/apk/res-auto";
        //获取开关是否开还是关
        if (attrs != null) {
            isOpen = attrs.getAttributeBooleanValue(namespace, "status", false);
        } else {
            isOpen = false;
        }
        if (isOpen) {
            toLeft = slideMax;
        } else {
            toLeft = 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mTbg.getWidth(), mTbg.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //抬起
            case MotionEvent.ACTION_UP:
                isUp = true;
                float bg = mTbg.getWidth() / 2;
                float sb = mTsb.getWidth() / 2 + toLeft;
                if (bg > sb) {
                    toLeft = 0;
                } else {
                    toLeft = slideMax;
                }
                break;
            //按下
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                float mEndX = event.getX();
                float differ = mEndX - mDownX;//移动的距离
                toLeft = toLeft + differ;
                if (toLeft < 0) {
                    toLeft = 0;
                }
                if (toLeft > slideMax) {
                    toLeft = slideMax;
                }
                mDownX = mEndX;
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mTbg, 0, 0, null);
        canvas.drawBitmap(mTsb, toLeft, 0, null);
        if (isUp) {
            isUp = false;
            boolean isCurrentState = toLeft > 0;
            if (isOpen != isCurrentState && stateListener != null) {
                stateListener.currentState(isCurrentState);
                isOpen = isCurrentState;
            }
        }
        super.onDraw(canvas);
    }


    public interface StateListener {
        public void currentState(boolean isOpen);
    }

    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    public void setStatus(boolean isOpen) {
        isUp = true;
        this.isOpen = isOpen;
        postInvalidate();
    }
}
