package com.windfindtech.icommon.fragment.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.webservice.weather.WeatherMapping;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cplu on 2015/6/8.
 */
public class WeatherDailyForecastGraph extends View {

    public static final int MAX_ITEM_COUNT = 5;

    //    private int[] m_highValues = {22, 22, 22, 22, 22};
//    private int[] m_lowValues = {22, 22, 22, 22, 22};
    private ArrayList<Integer> m_highValues;    // = new int[MAX_ITEM_COUNT];
    private ArrayList<Integer> m_lowValues;     // = new int[MAX_ITEM_COUNT];
    private String[] m_weekDays;    // e.g. "周一"
    private String[] m_daysInMonth; // e.g. "06/30"
    private String[] m_weatherStatus;
    private int m_minValue;
    private int m_maxValue;

    private Paint m_curvePaint;
    private Paint m_textPaint;
    private Paint m_dotStrokePaint;
    private Paint m_dotFillPaint;
    private Paint m_linePaint;
    private Path m_curvePath;
    private static final int CURVE_WIDTH = 2;
    private static final int TEXT_PADDING_BOTTOM = 9;
    private static final int LINE_WIDTH = 1;
    private static final int WEEKDAY_BIAS_Y = 24;
    private static final int DAYINMONTH_BIAS_Y = 40;
    private static final int WEATHERSTATUS_BIAS_Y = 54;
//	private static final int CURVE_BIAS_Y = 80;

    private int m_viewWidth;
    private int m_viewHeight;

    private int m_canvasWidth;
    private int m_widthPerItem;
    private float m_widthControlPtBias;
    private float m_baseY;
    private int m_curveRangeHeight;
    private float m_YUnit;
    private float m_textPaddingUnit;
    private int m_widthBias;
    private int m_textHeight;
    private String m_degreeSimple;

//    private boolean m_bUpdateData;

    public WeatherDailyForecastGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_curvePath = new Path();
        initPaint();
        m_degreeSimple = getResources().getString(R.string.temperature_suffix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (m_viewWidth == 0 || m_viewHeight == 0){
            return;
        }
//        Bitmap bitmap = Bitmap.createBitmap(m_viewWidth, m_viewHeight, Bitmap.Config.ARGB_8888);
//        Canvas sub_canvas = new Canvas(bitmap);
        draw_miscellaneous(canvas);
        draw_high_line(canvas);
        draw_low_line(canvas);

//        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 || h == 0) {
            return;
        }
        m_viewHeight = h;
        m_viewWidth = w;
        m_canvasWidth = m_viewWidth * 4 / 5;
        m_widthPerItem = m_canvasWidth / (MAX_ITEM_COUNT - 1);
        m_widthControlPtBias = m_widthPerItem / 4;

        m_curveRangeHeight = m_viewHeight * 2 / 3;
        m_baseY = m_viewHeight * 13 / 15;

        m_widthBias = m_viewWidth / MAX_ITEM_COUNT / 2;
        m_textHeight = (int) (m_textPaint.descent() - m_textPaint.ascent());

    }

    /**
     * Draw weekDays, daysInMonth, weatherStatus, vertical separator lines
     * @param canvas
     */
    private void draw_miscellaneous(Canvas canvas){
        if(m_weekDays == null || m_weekDays.length <= 0){
            return;
        }
        float lastX = m_widthBias;
        float weekDayY = Utils.dp2px(WEEKDAY_BIAS_Y);
        float dayInMonthY = Utils.dp2px(DAYINMONTH_BIAS_Y);
        float bitmapY = Utils.dp2px(WEATHERSTATUS_BIAS_Y);

        Bitmap bitmap;
        for (int i = 0; i < m_weekDays.length; i++) {
            // draw week day
            canvas.drawText(m_weekDays[i], lastX, weekDayY, m_textPaint);
            // draw day in month
            canvas.drawText(m_daysInMonth[i], lastX, dayInMonthY, m_textPaint);
            // dray weather status image
            bitmap = ((BitmapDrawable) WeatherMapping.getWeatherDrawable(getContext(), m_weatherStatus[i])).getBitmap();
            if(bitmap != null) {
                int bitmapWidth = m_widthPerItem * 2 / 3;
                int bitmapHeight = bitmapWidth * bitmap.getHeight() / bitmap.getWidth();
                float x0 = lastX - bitmapWidth / 2;
                float y0 = bitmapY;
                canvas.drawBitmap(bitmap, null, new RectF(x0, y0, x0 + bitmapWidth, y0 + bitmapHeight), null);
            }
            // draw vertical separator
            if(i > 0){
                canvas.drawLine(m_widthPerItem * i, 0, m_widthPerItem * i, m_viewHeight, m_linePaint);
            }

            lastX += m_widthPerItem;
        }
    }

    private void draw_high_line(Canvas canvas) {
        if(m_highValues == null || m_highValues.size() <= 0){
            return;
        }
        update_curve_paint_color(R.color.default_pink);
        m_curvePath.reset();
        float[] x_stores = new float[m_highValues.size()];
        float[] y_stores = new float[m_highValues.size()];

        float last_x = x_stores[0] = m_widthBias;
        float last_y;
        if (m_maxValue == m_minValue) {
            //m_curveRangeHeight / 2 + m_curveRangeHeight / 2, this means y starts at 2/3 height from top, half height in curve range
            last_y = y_stores[0] = m_curveRangeHeight;
        }
        else {
            last_y = y_stores[0] = m_baseY - (m_highValues.get(0) - m_minValue) / m_YUnit;
        }

        float last_arg = 0.0f;
        m_curvePath.moveTo(last_x, last_y);
        canvas.drawText(String.valueOf(m_highValues.get(0)) + m_degreeSimple, last_x, last_y - Utils.dp2px(TEXT_PADDING_BOTTOM), m_textPaint);
        for (int i = 1; i < m_highValues.size(); i++) {
            int current_value = m_highValues.get(i);
            x_stores[i] = m_widthBias + m_widthPerItem * i;
            if (m_maxValue == m_minValue) {
                y_stores[i] = m_curveRangeHeight;
            }
            else {
                y_stores[i] = m_baseY - (current_value - m_minValue) / m_YUnit;
            }
            float control_x = last_x + m_widthControlPtBias;
            float control_y = last_y + m_widthControlPtBias * last_arg;
            m_curvePath.quadTo(control_x, control_y, x_stores[i], y_stores[i]);
            last_arg = (y_stores[i] - last_y) / (x_stores[i] - last_x);

            canvas.drawText(String.valueOf(current_value) + m_degreeSimple,
                    x_stores[i], y_stores[i] - Utils.dp2px(TEXT_PADDING_BOTTOM),
                    m_textPaint);
            last_x = x_stores[i];
            last_y = y_stores[i];
        }
        canvas.drawPath(m_curvePath, m_curvePaint);

        /// draw dot on each point
        for (int i = 0; i < m_highValues.size(); i++) {
//            canvas.drawCircle(x_stores[i], y_stores[i], CURVE_WIDTH * 2, m_dotFillPaint);
            canvas.drawCircle(x_stores[i], y_stores[i], Utils.dp2px(CURVE_WIDTH) * 2, m_dotStrokePaint);
        }
    }

    private void draw_low_line(Canvas canvas) {
        if(m_lowValues == null || m_lowValues.size() <= 0){
            return;
        }
        update_curve_paint_color(R.color.default_blue);
        m_curvePath.reset();
        float[] x_stores = new float[m_lowValues.size()];
        float[] y_stores = new float[m_lowValues.size()];

        float last_x = x_stores[0] = m_widthBias;
        float last_y;
        if (m_maxValue == m_minValue) {
            last_y = y_stores[0] = m_curveRangeHeight;
        }
        else {
            last_y = y_stores[0] = m_baseY - (m_lowValues.get(0) - m_minValue) / m_YUnit;
        }
        float last_arg = 0.0f;
        m_curvePath.moveTo(last_x, last_y);
        canvas.drawText(String.valueOf(m_lowValues.get(0)) + m_degreeSimple,
                last_x, last_y + Utils.dp2px(TEXT_PADDING_BOTTOM) + m_textHeight,
                m_textPaint);
        for (int i = 1; i < m_lowValues.size(); i++) {
            int current_value = m_lowValues.get(i);
            x_stores[i] = m_widthBias + m_widthPerItem * i;
            if (m_maxValue == m_minValue) {
                y_stores[i] = m_curveRangeHeight;
            }
            else {
                y_stores[i] = m_baseY - (current_value - m_minValue) / m_YUnit;
            }
            float control_x = last_x + m_widthControlPtBias;
            float control_y = last_y + m_widthControlPtBias * last_arg;
            m_curvePath.quadTo(control_x, control_y, x_stores[i], y_stores[i]);
            last_arg = (y_stores[i] - last_y) / (x_stores[i] - last_x);

            canvas.drawText(String.valueOf(current_value) + m_degreeSimple,
                    x_stores[i], y_stores[i] + Utils.dp2px(TEXT_PADDING_BOTTOM) + m_textHeight,
                    m_textPaint);
            last_x = x_stores[i];
            last_y = y_stores[i];
        }
        canvas.drawPath(m_curvePath, m_curvePaint);

        /// draw dot on each point
        for (int i = 0; i < m_lowValues.size(); i++) {
//            canvas.drawCircle(x_stores[i], y_stores[i], CURVE_WIDTH * 2, m_dotFillPaint);
            canvas.drawCircle(x_stores[i], y_stores[i], Utils.dp2px(CURVE_WIDTH) * 2, m_dotStrokePaint);
        }
    }

    private void update_curve_paint_color(int color_res) {
        m_curvePaint.setColor(getResources().getColor(color_res));
        m_dotStrokePaint.setColor(getResources().getColor(color_res));
    }

    public void initPaint() {
        m_curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_curvePaint.setColor(getResources().getColor(R.color.default_pink));
        m_curvePaint.setStyle(Paint.Style.STROKE);
        m_curvePaint.setStrokeWidth(Utils.dp2px(CURVE_WIDTH));

        m_textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_textPaint.setColor(getResources().getColor(R.color.default_white));
        m_textPaint.setStyle(Paint.Style.FILL);
        m_textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_f2));
        m_textPaint.setTextAlign(Paint.Align.CENTER);

        m_dotStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_dotStrokePaint.setColor(getResources().getColor(R.color.default_pink));
        m_dotStrokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        m_dotStrokePaint.setStrokeWidth(Utils.dp2px(CURVE_WIDTH));

       /* m_dotFillPaint = new Paint();
        m_dotFillPaint.setColor(getResources().getColor(R.color.default_white));
        m_dotFillPaint.setStyle(Paint.Style.FILL);
        m_dotFillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));*/

//            m_dotStrokePaint.setAlpha(255);

        m_linePaint = new Paint();
        m_linePaint.setColor(getResources().getColor(R.color.transparent_white_26));
        m_linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        m_linePaint.setStrokeWidth(Utils.dp2px(LINE_WIDTH));
    }

    public void updateValues(ArrayList<Integer> high_values, ArrayList<Integer> low_values, String[] weekdays, String[] daysInMonth, String[] weatherStatus) {
        m_highValues = high_values;
        m_lowValues = low_values;
        m_weekDays = weekdays;
        m_daysInMonth = daysInMonth;
        m_weatherStatus = weatherStatus;
//            m_bUpdateData = true;
        m_minValue = Collections.min(m_lowValues);
        m_maxValue = Collections.max(m_highValues);

        m_YUnit = (m_maxValue - m_minValue) * 1.0f / (m_curveRangeHeight * 3f / 5);
        m_textPaddingUnit = m_YUnit * 10;

        invalidate();
    }
}
