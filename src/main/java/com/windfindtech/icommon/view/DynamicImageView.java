package com.windfindtech.icommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cplu on 2015/6/29.
 */
public class DynamicImageView extends ImageView {
    private DynamicSizeListener m_listener;

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
}
