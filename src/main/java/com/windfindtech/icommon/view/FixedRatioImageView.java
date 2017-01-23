package com.windfindtech.icommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.windfindtech.icommon.R;

/**
 * Created by cplu on 2015/8/24.
 */
public class FixedRatioImageView extends ImageView {
	public static final int SIDE_WIDTH = 0;
	public static final int SIDE_HEIGHT = 1;
	private int m_widthRatio = 1;
	private int m_heightRatio = 1;
	private int m_fixedSide = SIDE_WIDTH;

	public FixedRatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FixedRatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(
				attrs,
				R.styleable.FixedRatioViewStyle,
				defStyle, 0);

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
