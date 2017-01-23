package com.windfindtech.icommon.fragment.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.windfindtech.icommon.R;

import java.util.ArrayList;

/**
 * Created by cplu on 2015/6/8.
 */
public class FuelTrendXAxis extends RelativeLayout {
    private ArrayList<String> m_monthsAndDays;
    private ArrayList<Integer> m_years;
    private static final int HEIGHT_BIAS = 5;
//    private static final int YEAR_WIDTH_BIAS = 8;
    public static final float WIDTH_RIGHT_MARGIN = 10;

//    private int m_last_year;
//    private int m_this_year;

    private Paint m_monthPaint;
    private Paint m_yearPaint;

    public FuelTrendXAxis(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_monthPaint = new Paint();
        m_monthPaint.setColor(getResources().getColor(R.color.default_fg_color));
        m_monthPaint.setStyle(Paint.Style.FILL);
        m_monthPaint.setTextSize(getResources().getDimension(R.dimen.text_size_f1));
        m_monthPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        m_monthPaint.setTextAlign(Paint.Align.CENTER);

        m_yearPaint = new Paint();
        m_yearPaint.setColor(getResources().getColor(R.color.grayscale2));
        m_yearPaint.setStyle(Paint.Style.FILL);
        m_yearPaint.setTextSize(getResources().getDimension(R.dimen.text_size_f1));
        m_yearPaint.setTextAlign(Paint.Align.CENTER);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(m_monthsAndDays == null){
            return;
        }
        float width = getWidth();
        float height = getHeight();
        float width_left_bias = width / 8;

        float width_per_month = (width * 7 / 8 - WIDTH_RIGHT_MARGIN) / (m_monthsAndDays.size() + 1);

//        Paint paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.default_fg_color));
//        paint.setStyle(Paint.Style.FILL);
//        paint.setTextSize(getResources().getDimension(R.dimen.text_size_f2));
//        paint.setTextAlign(Paint.Align.CENTER );

        int last_year = 0;
        for(int i = 0; i < m_monthsAndDays.size(); i++){
            float x_pos = width_left_bias + width_per_month * (i + 1);
            canvas.drawText(m_monthsAndDays.get(i), x_pos, height / 2 - HEIGHT_BIAS, m_monthPaint);
            int year = m_years.get(i);
            if(year != last_year){
                canvas.drawText(String.valueOf(year), x_pos, height * 3 / 4, m_yearPaint);
                last_year = year;
            }

//            if(m_monthsAndDays[i] == 12) {
//                canvas.drawText(String.valueOf(m_last_year), x_pos - YEAR_WIDTH_BIAS, height - HEIGHT_BIAS*3, m_yearPaint);
//            }
//            else if(m_monthsAndDays[i] == 1) {
//                canvas.drawText(String.valueOf(m_this_year), x_pos + YEAR_WIDTH_BIAS, height - HEIGHT_BIAS*3, m_yearPaint);
//            }
        }
    }

    public void updateValues(ArrayList<String> months, ArrayList<Integer> years){
        m_monthsAndDays = months;
        m_years = years;
        postInvalidate();
    }
}
