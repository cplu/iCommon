package com.windfindtech.icommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class DynamicRelativeLayout extends RelativeLayout {
    private DynamicSizeListener m_listener;

    public DynamicRelativeLayout(Context context) {
        super(context);
    }

    public DynamicRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void setSizeListener(DynamicSizeListener listener) {
        m_listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (m_listener != null) {
            m_listener.onSizeChanged(w, h, oldw, oldh);
        }
    }

//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if (m_listener != null) {
//            m_listener.on_layout(left, top, right, bottom);
//        }
//        super.onLayout(changed, left, top, right, bottom);
//    }
}
