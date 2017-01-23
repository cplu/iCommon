package com.windfindtech.icommon.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.util.Utils;

/**
 * Created by yu on 2015/6/4.
 */
public class CircleImageDrawable extends Drawable {

    private static final int DEFAULT_BORDER_WIDTH = 5; // in dp
    private int m_borderWidth;
    private Paint mPaint;
    private Paint m_borderPaint;
    private int m_width;
    private Bitmap m_bitmap;
    //    private int m_border_color = -1;
    private Context m_ctx;

    public CircleImageDrawable(Context ctx, Bitmap bitmap) {
        m_bitmap = bitmap;
        m_ctx = ctx;
        m_borderWidth = Utils.dp2px(DEFAULT_BORDER_WIDTH);
        init(bitmap);
    }

    public CircleImageDrawable(Context ctx, BitmapDrawable drawable) {
//        BitmapDrawable tempBitmapDrawable = (BitmapDrawable) drawable;
        this(ctx, drawable.getBitmap());
    }

    /**
     *
     * @param ctx
     * @param drawable
     * @param border_width      border width in dp
     * @param border_color      color value (not color id!)
     */
    public CircleImageDrawable(Context ctx, BitmapDrawable drawable, int border_width, @ColorRes int border_color) {
//        BitmapDrawable tempBitmapDrawable = (BitmapDrawable) drawable;
        m_bitmap = drawable.getBitmap();
        m_ctx = ctx;
        m_borderWidth = Utils.dp2px(border_width);
        init(m_bitmap);
        m_borderPaint.setColor(ctx.getResources().getColor(border_color));
    }

    public CircleImageDrawable(Context ctx, Bitmap drawable, int border_width, @ColorRes int border_color) {
//        BitmapDrawable tempBitmapDrawable = (BitmapDrawable) drawable;
        m_bitmap = drawable;
        m_ctx = ctx;
        m_borderWidth = Utils.dp2px(border_width);
        init(m_bitmap);
        m_borderPaint.setColor(ctx.getResources().getColor(border_color));
    }

    public CircleImageDrawable(Context ctx, String asset_name) {
        BitmapDrawable tempBitmapDrawable = DrawableManager.instance().getAssetImage(asset_name);
        m_bitmap = tempBitmapDrawable.getBitmap();
        m_ctx = ctx;
        m_borderWidth = Utils.dp2px(DEFAULT_BORDER_WIDTH);
        init(m_bitmap);
    }

    public CircleImageDrawable setBorderWidth(int borderWidth) {
        m_borderWidth = borderWidth;
	    return this;
    }

	public CircleImageDrawable setBorderColor(@ColorRes int colorRes) {
		m_borderPaint.setColor(m_ctx.getResources().getColor(colorRes));
		return this;
	}

    public void init(Bitmap bitmap) {
        Bitmap new_bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width != height) {
            int min_width = width > height ? height : width;
            if (height > min_width) {
                int value = height - min_width;
                new_bitmap = Bitmap.createBitmap(bitmap, 0, value / 2, min_width, min_width);
            } else {
                int value = width - min_width;
                new_bitmap = Bitmap.createBitmap(bitmap, value / 2, 0, min_width, min_width);
            }
        } else {
            new_bitmap = bitmap;
        }
        BitmapShader bitmapShader = new BitmapShader(new_bitmap, TileMode.CLAMP,
                TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        m_width = Math.min(m_bitmap.getWidth(), m_bitmap.getHeight());
        m_borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_borderPaint.setStyle(Paint.Style.STROKE);
        m_borderPaint.setColor(m_ctx.getResources().getColor(R.color.default_pink));
    }

    @Override
    public void draw(Canvas canvas) {
        /// warning: m_width is on the bases of bitmap, NOT the canvas!
//        int width = canvas.getWidth();
        if (canvas.getHeight() > 0) {
            if (m_borderWidth > 0) {
//                float scaled_border = m_width * m_borderWidth / canvas.getHeight();
                m_borderPaint.setStrokeWidth(m_borderWidth);
                canvas.drawCircle(m_width / 2, m_width / 2, m_width / 2 - m_borderWidth, mPaint);
                canvas.drawCircle(m_width / 2, m_width / 2, m_width / 2 - m_borderWidth / 2, m_borderPaint);
            }else {
                canvas.drawCircle(m_width / 2, m_width / 2, m_width / 2, mPaint);
            }
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return m_width;
    }

    @Override
    public int getIntrinsicHeight() {
        return m_width;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
