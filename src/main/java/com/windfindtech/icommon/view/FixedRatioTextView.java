package com.windfindtech.icommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.windfindtech.icommon.R;

/**
 * Created by cplu on 2015/8/27.
 */
public class FixedRatioTextView extends TextView {
    public static final int SIDE_WIDTH = 0;
    public static final int SIDE_HEIGHT = 1;
    private int m_widthRatio = 1;
    private int m_heightRatio = 1;
    private int m_fixedSide = SIDE_WIDTH;

    public FixedRatioTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.FixedRatioViewStyle,
                0, 0);

        try {
            m_widthRatio = a.getInteger(R.styleable.FixedRatioViewStyle_widthRatio, 1);
            m_heightRatio = a.getInteger(R.styleable.FixedRatioViewStyle_heightRatio, 1);
            m_fixedSide = a.getInteger(R.styleable.FixedRatioViewStyle_fixedSide, SIDE_WIDTH);
        }
        finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(m_fixedSide == SIDE_WIDTH) {
            int width = getMeasuredWidth();
            int height = width * m_heightRatio / m_widthRatio;
//            int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            setMeasuredDimension(width, height);
        }
        else{
            int height = getMeasuredHeight();
            int width = height * m_widthRatio / m_heightRatio;
//            int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
	        setMeasuredDimension(width, height);
        }
    }

    public void prepareAspectRatio(int widthRatio, int heightRatio){
        m_widthRatio = widthRatio;
        m_heightRatio = heightRatio;
    }

    /**
     * set the fixed side of the layout
     * @param side      should be one of SIDE_WIDTH/SIDE_HEIGHT
     */
    public void setFixedSide(int side){
        m_fixedSide = side;
    }
}
