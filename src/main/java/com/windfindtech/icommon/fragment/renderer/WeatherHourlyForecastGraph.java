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
import com.windfindtech.icommon.jsondata.webservice.DayDetailInfo;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.webservice.weather.WeatherMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by yu on 2015/7/2.
 */
public class WeatherHourlyForecastGraph extends View {
    public static final int MAX_ITEM_COUNTS = 12;
    private static final int WIDTH_TOTAL = 900;
    private static final int CURVE_WIDTH = 2;
    private static final int TEXT_PADDING_BOTTOM = 9;
    private static final int ICON_BIAS = 10;//dp

    private ArrayList<Integer> m_values = new ArrayList<>();     /// degree
    private String[] m_hours;   /// hour
    private String[] m_weather; /// weather description

    private int m_minValue;
    private int m_maxValue;

    private Paint m_curvePaint;
    private Paint m_textPaint;
    private Paint m_dotStrokePaint;
    private Paint m_dotFillPaint;
    private Path m_curvePath;

    private int m_viewWidth;
    private int m_viewHeight;
    private int m_curveHeight;

    private int m_canvasWidth;
    private float m_widthControlPtBias;
    private int m_widthPerItem;
    private float m_baseY;
    private float m_YUnit;
    private float m_textPaddingUnit;
    private int m_widthBias;
    private int m_textHeight;
    private String m_degreeSimple;
//    private boolean m_bUpdateData;

    private Context m_ctx;

    public WeatherHourlyForecastGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_ctx = context;
        m_curvePath = new Path();
        initPaint();

//        initMinMaxValue();
        m_degreeSimple = getResources().getString(R.string.temperature_suffix);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Utils.dp2px(WIDTH_TOTAL), height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        m_viewWidth = w;
        m_viewHeight = h;

        m_canvasWidth = (int) (m_viewWidth * (MAX_ITEM_COUNTS * 1.0 / (MAX_ITEM_COUNTS + 1)));
        m_widthPerItem = m_canvasWidth / (MAX_ITEM_COUNTS - 1);
        m_widthBias = (int) (m_viewWidth * (1.0 / (MAX_ITEM_COUNTS + 1)) / 2);
        m_widthControlPtBias = m_widthPerItem / 4;

        m_curveHeight = m_viewHeight * 6 / 7;
        m_baseY = m_curveHeight * 4 / 5;
        m_textHeight = (int) (m_textPaint.descent() - m_textPaint.ascent());
    }

    @Override
    protected void onDraw(Canvas canvas) {
	    if(m_viewWidth == 0 || m_viewHeight == 0){
		    return;
	    }
        if(!isInEditMode()) {
            draw_curve(canvas);
        }
    }

    private void draw_curve(Canvas canvas) {
        if(m_values.size() <= 0) {
            return;
        }
        m_curvePath.reset();
        float[] x_stores = new float[m_values.size()];
        float[] y_stores = new float[m_values.size()];

        float last_x = x_stores[0] = m_widthBias;
        float last_y;
        if (m_maxValue == m_minValue) {
            last_y = y_stores[0] = m_curveHeight / 2;
        } else {
            last_y = y_stores[0] = m_baseY - (m_values.get(0) - m_minValue) / m_YUnit;
        }
        float last_arg = 0.0f;
        m_curvePath.moveTo(last_x, last_y);
        canvas.drawText(String.valueOf(m_values.get(0)) + m_degreeSimple, last_x, last_y - Utils.dp2px(TEXT_PADDING_BOTTOM), m_textPaint);
        canvas.drawText(m_hours[0], x_stores[0], m_viewHeight - m_textHeight, m_textPaint);

	    /// draw temperature and curve
        for (int i = 1; i < m_values.size(); i++) {
            int current_value = m_values.get(i);
//            if (current_value <= 0.0f) {
//                continue;
//            }
            x_stores[i] = m_widthBias + m_widthPerItem * i;
            if (m_maxValue == m_minValue) {
                y_stores[i] = m_curveHeight / 2;
            } else {
                y_stores[i] = m_baseY - (current_value - m_minValue) / m_YUnit;
            }
            float control_x = last_x + m_widthControlPtBias;
            float control_y = last_y + m_widthControlPtBias * last_arg;
            m_curvePath.quadTo(control_x, control_y, x_stores[i], y_stores[i]);
            last_arg = (y_stores[i] - last_y) / (x_stores[i] - last_x);

            canvas.drawText(String.valueOf(current_value) + m_degreeSimple, x_stores[i],
                    y_stores[i] - Utils.dp2px(TEXT_PADDING_BOTTOM), m_textPaint);
            canvas.drawText(m_hours[i], x_stores[i], m_viewHeight - m_textHeight, m_textPaint);
            last_x = x_stores[i];
            last_y = y_stores[i];
        }
        canvas.drawPath(m_curvePath, m_curvePaint);

        /// draw dot on each point
        for (int i = 0; i < m_values.size(); i++) {
//            canvas.drawCircle(x_stores[i], y_stores[i], CURVE_WIDTH * 2, m_dotFillPaint);
            canvas.drawCircle(x_stores[i], y_stores[i], Utils.dp2px(CURVE_WIDTH) * 2, m_dotStrokePaint);
        }

        //draw weather icon
        Bitmap bitmap;
        for (int i = 0; i < m_values.size(); i++) {
            if (i > 0 && m_weather[i].equals(m_weather[i - 1])) {
                continue;
            }
            bitmap = ((BitmapDrawable) WeatherMapping.getWeatherDrawable(getContext(), m_weather[i])).getBitmap();
	        int bitmapWidth = m_widthPerItem / 2;
	        int bitmapHeight = bitmapWidth * bitmap.getHeight() / bitmap.getWidth();
            float x0 = x_stores[i] - bitmapWidth / 2;
            float y0 = y_stores[i] > m_baseY / 2 ? Utils.dp2px(ICON_BIAS) : (y_stores[i] + Utils.dp2px(ICON_BIAS));
	        canvas.drawBitmap(bitmap, null, new RectF(x0, y0, x0 + bitmapWidth, y0 + bitmapHeight), null);
        }
    }

//    private Bitmap get_weather_bitmap(String weather_status) {
//        BitmapDrawable drawable = (BitmapDrawable) m_weatherMap.getWeatherDrawable(weather_status);
//        return convert_bitmap(drawable.getBitmap());
//    }

//    private Bitmap convert_bitmap(Bitmap bitmap) {
//        Matrix matrix = new Matrix();
//        float scale = (m_widthPerItem / 2f) / bitmap.getWidth();
//        matrix.postScale(scale, scale); //长和宽放大缩小的比例
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }

    public void updateData(List<DayDetailInfo> day_detail_infos) {
        if(day_detail_infos.size() > 1) {
//            m_bUpdateData = true;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            DayDetailInfo info;
            int count = day_detail_infos.size() / 2;
            m_values.clear();
            m_weather = new String[count];
            m_hours = new String[count];
            for (int i = 0; i < count; i++) {
                info = day_detail_infos.get(i * 2);
                m_values.add(Integer.parseInt(info.getTemp()));
                m_weather[i] = info.getWeather();
                m_hours[i] = sdf.format(info.getTime());
            }
            m_minValue = Collections.min(m_values);
            m_maxValue = Collections.max(m_values);
            m_YUnit = (m_maxValue - m_minValue) * 1.0f / (m_curveHeight * 3 / 5);
            invalidate();
        }
    }

    public void initPaint() {

        m_curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_curvePaint.setColor(getResources().getColor(R.color.default_white));
        m_curvePaint.setStyle(Paint.Style.STROKE);
        m_curvePaint.setStrokeWidth(Utils.dp2px(CURVE_WIDTH));

        m_textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_textPaint.setColor(getResources().getColor(R.color.default_white));
        m_textPaint.setStyle(Paint.Style.FILL);
        m_textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_f4));
        m_textPaint.setTextAlign(Paint.Align.CENTER);

        m_dotStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_dotStrokePaint.setColor(getResources().getColor(R.color.default_white));
        m_dotStrokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        m_dotStrokePaint.setStrokeWidth(Utils.dp2px(CURVE_WIDTH));
/*
        m_dotFillPaint = new Paint();
        m_dotFillPaint.setColor(getResources().getColor(R.color.default_white));
        m_dotFillPaint.setStyle(Paint.Style.FILL);
        m_dotFillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));*/

//            m_dotStrokePaint.setAlpha(255);
    }
}
