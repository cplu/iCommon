package com.windfindtech.icommon.fragment.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.windfindtech.icommon.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cplu on 2015/6/8.
 */
public class FuelTrendGraph extends RelativeLayout {
    private ArrayList<Float> m_values;
    private float m_minValue;
    private float m_maxValue;

    private Paint m_curvePaint;
    private Paint m_textPaint;
    private Paint m_dotStrokePaint;
    private Paint m_dotFillPaint;
    private Path m_curvePath;
    private static final float CURVE_WIDTH = 4.0f;
    private static final float TEXT_PADDING_BOTTOM = 18;

    public FuelTrendGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_curvePath = new Path();
        setWillNotDraw(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(m_values != null && m_values.size() > 0){
            m_curvePath.reset();
            float height = getHeight();
            float width = getWidth();
            /// 1/6 of the width remains to place a round
            float width_bias = width / 8;
            width = width * 7 / 8;
            float width_per_item = (width - FuelTrendXAxis.WIDTH_RIGHT_MARGIN )/ (m_values.size() + 1);
            float width_control_pt_bias = width_per_item / 4;
            float base_y = height * 4 / 5;
            float y_unit = (m_maxValue - m_minValue) / (height / 2);  /// only draw half of the view's region of Y (from 3 / 10 to 8 / 10)
            float text_padding_unit = y_unit * 10;  /// text padding is proportional to the unit value

            float[] x_stores = new float[m_values.size()];
            float[] y_stores = new float[m_values.size()];

            float last_x = x_stores[0] = width_bias + width_per_item;

            float last_y;
            if (m_maxValue == m_minValue){
                last_y = y_stores[0] = height/2;
            }else {
                last_y = y_stores[0] = base_y - (m_values.get(0) - m_minValue) / y_unit;
            }
            float last_arg = 0.0f;
            m_curvePath.moveTo(last_x, last_y);
//            canvas.drawText(String.valueOf(m_values[0]), last_x, last_y - TEXT_PADDING_BOTTOM, m_textPaint);
            for(int i = 1; i < m_values.size(); i++){
                float current_value = m_values.get(i);
                if(current_value <= 0.0f){
                    continue;
                }
                x_stores[i] = width_bias + width_per_item * (i + 1);
                if (m_maxValue == m_minValue){
                    y_stores[i] = height/2;
                }else {
                    y_stores[i] = base_y - (current_value - m_minValue) / y_unit;
                }
                float control_x = last_x + width_control_pt_bias;
                float control_y = last_y + width_control_pt_bias * last_arg;
                m_curvePath.quadTo(control_x, control_y, x_stores[i], y_stores[i]);
                last_arg = (y_stores[i] - last_y) / (x_stores[i] - last_x);

                if(i % 2 == 1){
                    /// draw text once every two points
                    canvas.drawText(String.valueOf(current_value),
                            x_stores[i],
                            y_stores[i] - TEXT_PADDING_BOTTOM - (m_maxValue - current_value) / text_padding_unit,
                            m_textPaint);
                }
//                m_curvePath.lineTo(this_x, this_y);
                last_x = x_stores[i];
                last_y = y_stores[i];
            }
            canvas.drawPath(m_curvePath, m_curvePaint);

            /// draw dot on each point
            for(int i = 0; i < m_values.size(); i++){
                if(x_stores[i] > 0.0f && y_stores[i] > 0.0f) {
                    canvas.drawCircle(x_stores[i], y_stores[i], CURVE_WIDTH * 2, m_dotFillPaint);
                    canvas.drawCircle(x_stores[i], y_stores[i], CURVE_WIDTH * 2, m_dotStrokePaint);
                }
            }
        }
    }

    public void updateValues(ArrayList<Float> values, int base_color){
        m_values = values;
        if(m_values.size() > 0) {
            m_maxValue = Collections.max(m_values);
            m_minValue = Collections.min(m_values);

            m_curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            m_curvePaint.setColor(getResources().getColor(base_color));
            m_curvePaint.setStyle(Paint.Style.STROKE);
            m_curvePaint.setStrokeWidth(CURVE_WIDTH);

            m_textPaint = new Paint();
            m_textPaint.setColor(getResources().getColor(base_color));
            m_textPaint.setStyle(Paint.Style.FILL);
            m_textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_f1));
            m_textPaint.setTextAlign(Paint.Align.CENTER);

            m_dotStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            m_dotStrokePaint.setColor(getResources().getColor(base_color));
            m_dotStrokePaint.setStyle(Paint.Style.STROKE);
            m_dotStrokePaint.setStrokeWidth(CURVE_WIDTH);

            m_dotFillPaint = new Paint();
            m_dotFillPaint.setColor(getResources().getColor(R.color.grayscale6));
            m_dotFillPaint.setStyle(Paint.Style.FILL);

//            m_dotStrokePaint.setAlpha(255);

            postInvalidate();
        }
    }
}
