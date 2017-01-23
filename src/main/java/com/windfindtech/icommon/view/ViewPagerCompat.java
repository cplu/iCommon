package com.windfindtech.icommon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.pmw.tinylog.Logger;


/**
 * Created by cplu on 2014/11/13.
 */
public class ViewPagerCompat extends ViewPager {
    private DynamicSizeListener m_listener;

    //mViewTouchMode表示ViewPager是否全权控制滑动事件，默认为false，即不控制
    private boolean m_viewTouchMode = false;

    public ViewPagerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSizeListener(DynamicSizeListener listener) {
        m_listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (m_listener != null) {
            m_listener.onSizeChanged(w, h, oldw, oldh);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setViewTouchMode(boolean b) {
//        if (b && !isFakeDragging()) {
//            //全权控制滑动事件
//            beginFakeDrag();
//        } else if (!b && isFakeDragging()) {
//            //终止控制滑动事件
//            endFakeDrag();
//        }
        m_viewTouchMode = b;
    }

    /**
     * 在mViewTouchMode为true的时候，ViewPager不拦截点击事件，点击事件将由子View处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (m_viewTouchMode) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(event);
        }
        catch (Exception e){
            Logger.error(e);
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (m_viewTouchMode) {
            return false; // do not consume
        }
        try {
            return super.onTouchEvent(event);
        }
        catch (Exception e){
            Logger.error(e);
            return false;
        }
    }

    /**
     * 在mViewTouchMode为true或者滑动方向不是左右的时候，ViewPager将放弃控制点击事件，
     * 这样做有利于在ViewPager中加入ListView等可以滑动的控件，否则两者之间的滑动将会有冲突
     */
    @Override
    public boolean arrowScroll(int direction) {
        if (m_viewTouchMode) return false;
        if (direction != FOCUS_LEFT && direction != FOCUS_RIGHT) return false;
        return super.arrowScroll(direction);
    }
}
