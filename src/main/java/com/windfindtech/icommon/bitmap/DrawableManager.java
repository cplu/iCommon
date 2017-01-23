package com.windfindtech.icommon.bitmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2014/11/3.
 * DrawableManager is designed to cache Drawable in memory and to avoid OutOfMemoryError if too many Drawables are allocated
 * should be called inside ui thread
 */
public class DrawableManager {
	private static AssetManager s_assetManager;
	private final Context m_ctx;
	private final BitmapFactory.Options m_options;
	private int m_dpi = DisplayMetrics.DENSITY_HIGH;    /// hdpi
	private static final int ASSET_INPUT_DPI = DisplayMetrics.DENSITY_HIGH;   /// drawables in assets are treated as hdpi
//	private final String m_assetPrefixDefault = "drawable/";

	public BitmapDrawable getAssetPng(String filename) {
		return getAssetImage(filename + ".png");
	}

	public BitmapDrawable getAssetImage(String filename) {
		try {
			Bitmap bitmap = getMemoryCache().get(filename);
			if (bitmap == null) {
				//AssetManager assets = context.getResources().getAssets();
//                InputStream ais = s_assetManager.open("drawable/" + filename);

				Bitmap b = BitmapFactory.decodeStream(s_assetManager.open(
						"drawable/" + filename), null, m_options);
//                BitmapDrawable new_d = new BitmapDrawable(context.getResources(), bitmap);
//                m_drawables.put(filename, new_d);
				BitmapDrawable new_d = new BitmapDrawable(m_ctx.getResources(), b);
//	            new_d.invalidateSelf();
//                new_d.setTargetDensity(ASSET_INPUT_DPI);
				getMemoryCache().put(filename, b);
				return new_d;
			} else {
				return new BitmapDrawable(m_ctx.getResources(), bitmap);
			}
		} catch (OutOfMemoryError error) {
			resolve_out_of_memory_error();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public void asyncGetAssetImage(final String filename, final AsyncImageCallback callback) {
		Observable.create(new Observable.OnSubscribe<Bitmap>() {
			@Override
			public void call(Subscriber<? super Bitmap> subscriber) {
				final Bitmap bitmap = getAssetBitmap(filename);
				subscriber.onNext(bitmap);
				subscriber.onCompleted();
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(callback);
	}

	public Bitmap getAssetBitmap(String filename) {
		try {
			Bitmap bitmap = getMemoryCache().get(filename);
			if (bitmap == null) {
				//AssetManager assets = context.getResources().getAssets();
//                InputStream ais = s_assetManager.open("drawable/" + filename);

				Bitmap b = BitmapFactory.decodeStream(s_assetManager.open(
						"drawable/" + filename), null, m_options);
				getMemoryCache().put(filename, b);
				return b;
			} else {
				return bitmap;
			}
		} catch (OutOfMemoryError error) {
			resolve_out_of_memory_error();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private int calculate_sample_size(BitmapFactory.Options options, int width_limit, int height_limit) {
		// Raw height and width of image
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

		while (height > height_limit || width > width_limit) {
			height = height / 2;
			width = width / 2;
			inSampleSize *= 2;
		}

		return inSampleSize;
	}

	public Bitmap getBitmapLimited(String path, int width_limit, int height_limit) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			options.inSampleSize = calculate_sample_size(options, width_limit, height_limit);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			return bitmap;
		} catch (OutOfMemoryError error) {
			resolve_out_of_memory_error();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Bitmap getBitmapLimited(byte[] data, int width_limit, int height_limit) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, options);
			options.inSampleSize = calculate_sample_size(options, width_limit, height_limit);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			return bitmap;
		} catch (OutOfMemoryError error) {
			resolve_out_of_memory_error();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Bitmap getBitmapLimited(InputStream inputstream, int width_limit, int height_limit) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			MarkableInputStream bis = new MarkableInputStream(inputstream);
			bis.mark(65536); /// 65536 bytes are supposed to be enough for decoding bounds
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(bis, null, options);
			options.inSampleSize = calculate_sample_size(options, width_limit, height_limit);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			bis.reset();
			Bitmap bitmap = BitmapFactory.decodeStream(bis, null, options);
			bis.close();
			return bitmap;
		} catch (OutOfMemoryError error) {
			resolve_out_of_memory_error();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private void resolve_out_of_memory_error() {
//        Iterator<BitmapDrawable> it = m_memoryCache.iterator();
//        while(it.hasNext()){
//            it.next().getBitmap().recycle();
//        }
//        m_drawables.clear();
	}

	private static DrawableManager s_instance;
	private LruBitmapCache m_memoryCache;

	private DrawableManager(Context ctx) {
		m_ctx = ctx;
		s_assetManager = m_ctx.getAssets();
		DisplayMetrics metrics = m_ctx.getResources().getDisplayMetrics();
		if (metrics != null) {
			m_dpi = metrics.densityDpi;
		}
		m_options = new BitmapFactory.Options();
		m_options.inPurgeable = true;
		m_options.inInputShareable = true;
		m_options.inDensity = ASSET_INPUT_DPI;
		m_options.inTargetDensity = m_dpi;

		m_memoryCache = new LruBitmapCache();
	}

	public static void init(Context ctx) {
		if (s_instance == null) {
			s_instance = new DrawableManager(ctx);
		}
	}

	public static DrawableManager instance() {
		return s_instance;
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
			       && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public LruBitmapCache getMemoryCache() {
		return m_memoryCache;
	}
}
