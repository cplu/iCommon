package com.windfindtech.icommon;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.windfindtech.icommon.rx.RXThreadTask;
import com.windfindtech.icommon.util.SimpleCrypto;
import com.windfindtech.icommon.webservice.WSManager;
import com.windfindtech.icommon.wifimanager.WFTNetworkMonitor;

import org.pmw.tinylog.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2015/6/25.
 */
public class iCommon {
	public static boolean isDebug = false;

	public static final String COMPILE_LEVEL_DEBUG = "debug";
	public static final String COMPILE_LEVEL_RELEASE = "release";
	public static final String DEVICE_OS = "android";
	public static final String DEFAULT_NETWORK_ENCODING = "UTF-8";
	public static final String HTTP_BODY_TYPE = "application/json";

	private static InputMethodManager s_inputMethod;
	private static final int EXTERNAL_DIR_API_LEVEL = 19;
	/// trick to fix bug: no content in webview is rendered on startup of app
//	public static boolean s_bFixWebViewBlank = false;

	public static void showInputMethod(Activity activity, View target) {
		if (s_inputMethod == null) {
			s_inputMethod = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		s_inputMethod.showSoftInput(target, InputMethodManager.SHOW_FORCED);
	}

	public static void showInputMethod(Context ctx, View target) {
		if (s_inputMethod == null) {
			s_inputMethod = (InputMethodManager) ctx.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		s_inputMethod.showSoftInput(target, InputMethodManager.SHOW_FORCED);
	}

	public static void hideInputMethod(Activity activity) {
		try {
			if (s_inputMethod == null) {
				s_inputMethod = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			}
			if (s_inputMethod != null && s_inputMethod.isActive() && activity.getCurrentFocus() != null) {
				s_inputMethod.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * should pass in current focused view for hiding input method
	 * @param currentFocus
	 */
	public static void hideInputMethod(Context ctx, View currentFocus) {
		try {
			if (s_inputMethod == null) {
				s_inputMethod = (InputMethodManager) ctx.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			}
			if (s_inputMethod != null && s_inputMethod.isActive()) {
				s_inputMethod.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

//	private static ConfirmDialog s_confirmDlg;
	private static long s_lastAutoConnectConfirmTimeInMs;
	private static final int AUTO_CONNECT_TIME_GAP = 120 * 1000;  /// 120 seconds

	public static String findWFTWifi() {
		final String ssid = WFTNetworkMonitor.instance().findWFTWifiSsid();
		if (ssid == null) {
			return null;
		}
		long current_time_in_ms = System.currentTimeMillis();
		if (s_lastAutoConnectConfirmTimeInMs != 0
		    && (current_time_in_ms - s_lastAutoConnectConfirmTimeInMs) < AUTO_CONNECT_TIME_GAP) {
			/// do not auto connect once more within AUTO_CONNECT_TIME_GAP
			return null;
		}
		s_lastAutoConnectConfirmTimeInMs = current_time_in_ms;

		return ssid;
	}

	/**
	 * check signature
	 * run on non-ui thread
	 * @param ctx
	 * @param seed
	 * @param debugKey
	 * @param prodKey
	 */
	public static void checkPackageSignature(final Context ctx, final String seed, final String debugKey, final String prodKey) {
		RXThreadTask.run(Schedulers.io(), new RXThreadTask.Task() {
			@Override
			public Boolean runTask() {
				try {
					Signature[] sigs = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
					if (sigs.length != 1) {
						return false;
					}
					String encrypt = SimpleCrypto.encryptWithSHA1PRNG(seed, sigs[0].toCharsString());
					String check_value = iCommon.isDebug ? debugKey : prodKey; //ctx.getString(R.string.sign_check_value);
					if (check_value.isEmpty()) {
						return false;
					} else {
//						String[] debug_values = check_value.split(",");
//						for (String value : debug_values) {
//						Logger.info("signature: " + encrypt);
						if (encrypt.equals(check_value)) {
							return true;
						}
//						}
						return false;
					}
				} catch (Exception e) {
					return false;
				}
			}
		}, new SingleSubscriber<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if(!result) {
					Logger.error("check signature failed");
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}

			@Override
			public void onError(Throwable throwable) {

			}
		});
	}

	private static final int CPU_CAPABILITY_THRESHOLD = 1200000;
	private static final int PROCESSOR_NUMBER_THRESHOLD = 2;

	private static boolean s_cpuCapabilityHigh = false;

	/**
	 * check if cpu is capable for high efficiency ui operations, such as animations
	 *
	 * @return
	 */
	public static boolean assertCpuCapability() {
		return s_cpuCapabilityHigh;
	}

	public static void checkCpuCapability() {
		try {
			ProcessBuilder cmd;
			String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(in));
			String result = buffer_reader.readLine();
			int freq = Integer.parseInt(result);
			Logger.debug("cpu frequency: " + freq);
			int processor_cnt = Runtime.getRuntime().availableProcessors();
			Logger.debug("processor number: ", +processor_cnt);
			s_cpuCapabilityHigh = (freq >= CPU_CAPABILITY_THRESHOLD) && (processor_cnt >= PROCESSOR_NUMBER_THRESHOLD);
		} catch (Exception e) {
			s_cpuCapabilityHigh = false;
		}
	}

	private static final long IMAGE_ALPHA_ANIMATION_DURATION = 300;

	public static final int ANIMATOR_API_LEVEL = 11;

	public static boolean isAnimatorUnavailable() {
		return Build.VERSION.SDK_INT < ANIMATOR_API_LEVEL;
	}

	@TargetApi(ANIMATOR_API_LEVEL)
	public static void execAlphaAnim(View view) {
		if (assertCpuCapability() && getApiLevel() >= ANIMATOR_API_LEVEL) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1.0f).setDuration(IMAGE_ALPHA_ANIMATION_DURATION);
			animator.start();
		}
	}

	public static String getTimeDurationBy(Context ctx, long duration_in_seconds) {
//        int duration_in_seconds = m_deviceInfo.getLastLoginDuration();
		StringBuilder sb = new StringBuilder();
		/**
		 * check if time is not empty
		 * the valid string format is "xx天xx小时"/"xx小时xx分"/"xx分xx秒"
		 */
		long days = TimeUnit.SECONDS.toDays(duration_in_seconds);
		if (days > 0) {
			sb.append(String.format("%d%s", days, ctx.getString(R.string.days)));
			duration_in_seconds -= TimeUnit.DAYS.toSeconds(days);
		}
		long hours = TimeUnit.SECONDS.toHours(duration_in_seconds);
		if (hours > 0) {
			sb.append(String.format("%d%s", hours, ctx.getString(R.string.hours)));
			duration_in_seconds -= TimeUnit.HOURS.toSeconds(hours);
		}
		if (days > 0) {
			return sb.toString();
		}
		long minutes = TimeUnit.SECONDS.toMinutes(duration_in_seconds);
		if (minutes > 0) {
			sb.append(String.format("%d%s", minutes, ctx.getString(R.string.minutes)));
			duration_in_seconds -= TimeUnit.MINUTES.toSeconds(minutes);
		}
		if (hours > 0) {
			return sb.toString();
		}
		long seconds = TimeUnit.SECONDS.toSeconds(duration_in_seconds);
		if (seconds > 0) {
			sb.append(String.format("%d%s", seconds, ctx.getString(R.string.seconds)));
		}
		return sb.toString();
	}

	private static Toast m_toast;

	public static void showToast(Context ctx, String content) {
		if (ctx == null || !WFTApplication.inForeground) {
			return;
		}
		if (m_toast == null) {
			m_toast = Toast.makeText(ctx, content, Toast.LENGTH_LONG);
		} else {
			//m_toast.cancel();
			m_toast.setText(content);
		}
		m_toast.show();
	}

	public static void showToast(Context ctx, @StringRes int string_id, String additional) {
		String content;
		if (ctx == null) {
			return;
		}
		if (additional != null) {
			content = ctx.getString(string_id) + " " + additional;
		} else {
			content = ctx.getString(string_id);
		}
		showToast(ctx, content);
	}

	public static void showToast(Context ctx, @StringRes int string_id) {
		if (ctx != null) {
			showToast(ctx, ctx.getString(string_id));
		}
	}

	public static String getTimeDuration(Context ctx, long duration_in_seconds) {
//        int duration_in_seconds = m_device_info.getLastLoginDuration();
		String ret = "";
		long days = duration_in_seconds / 86400;
		duration_in_seconds = duration_in_seconds % 86400;
		long hours = duration_in_seconds / 3600;
		if (hours > 0) {
			ret = String.format("%d%s", hours, ctx.getString(R.string.hours));
		}
		if (days > 0) {
			ret = String.format("%d%s", days, ctx.getString(R.string.days)) + ret;
		}
		if (ret.length() == 0) {
			duration_in_seconds = duration_in_seconds % 3600;
			long minutes = duration_in_seconds / 60;
//            duration_in_seconds = duration_in_seconds % 60;
			long seconds = duration_in_seconds % 60;
			ret = String.format("%d%s%d%s",
					minutes, ctx.getString(R.string.minutes),
					seconds, ctx.getString(R.string.seconds));
		}

		return ret;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static String getDateElapsed(Context ctx, Date current, Date target) {
		long elapsed = current.getTime() - target.getTime();
		if (elapsed < 0) {
			return sdf.format(target);
		}
		elapsed /= 1000;    /// get seconds
		if (elapsed < 60) {
			/// within 1 minute
			return String.format(ctx.getString(R.string.seconds_ahead), elapsed);
		}
		elapsed /= 60;      /// get minutes
		if (elapsed < 60) {
			/// within 1 hour
			return String.format(ctx.getString(R.string.minutes_ahead), elapsed);
		}
		elapsed /= 60;      /// get hours
		if (elapsed < 24) {
			/// within 1 day
			return String.format(ctx.getString(R.string.hours_ahead), elapsed);
		}
		elapsed /= 24;      /// get days;
		if(elapsed < 7) {
			/// within 1 week
			return String.format(ctx.getString(R.string.days_ahead), elapsed);
		}
		/// return target date string as fallback
		return sdf.format(target);
	}

	/**
	 * return api level
	 *
	 * @return
	 */
	public static int getApiLevel() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * test if api level is greater than or equal to 17
	 *
	 * @return
	 */
	public static boolean isApiLevelAbove17() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
	}

	/**
	 * return the current application name if you specify android:label in manifest
	 *
	 * @param context
	 * @return app label in manifest
	 * Note: Each app depending on iCommon should have a unique name
	 */
	public static String getAppName(Context context) {
		if (context.getApplicationInfo() != null) {
			int stringId = context.getApplicationInfo().labelRes;
			return stringId > 0 ? context.getString(stringId) : "iWindfindtech";
		}
		return "iWindfindtech";
	}

	public static StateListDrawable getStateListDrawable(Drawable normal, Drawable pressed) {
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[]{android.R.attr.state_pressed}, pressed);
		states.addState(new int[]{}, normal);
		return states;
	}

	/**
	 * return count of random numbers from range [1, end)
	 *
	 * @param end
	 * @param count
	 * @return
	 */
	public static ArrayList<Integer> getRandomsFrom(int end, int count) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int start = 1;
		for (int i = 0; i < count; i++) {
			int rand = getRandom(start, end - (count - i - 1));
			ret.add(rand);
			start = rand + 1;
		}
		return ret;
	}

	private static Random random = new Random();

	/**
	 * return random int in the range [start, end)
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandom(int start, int end) {
		return random.nextInt(end - start) + start;
	}

	/**
	 * return a String in unit of ten-thousands
	 * @return  in format 1.02万 or 4099
	 */
	public static String getCountInTenThousands(Context ctx, int number){
		if(number >= 10000){
			return String.format("%.2f%s", number * 1.0f / 10000, ctx.getString(R.string.wan));
		}
		else{
			return String.format("%d", number);
		}
	}

	/**
	 * get orientation of the image specified by uri
	 *
	 * @param context
	 * @param imageUri image uri
	 * @return orientation of the image, in degree
	 */
	public static int getOrientation(Context context, Uri imageUri) {
	    /* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(imageUri,
				new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

		if (cursor == null || cursor.getCount() != 1) {
			return 0;
		}

		cursor.moveToFirst();
		int ret = cursor.getInt(0);
		cursor.close();
		return ret;
	}

	public static Bitmap getRotatedBitmap(Bitmap source, int orientation, int widthLimit, int heightLimit) {
		try {
			Matrix matrix = new Matrix();
			if (source.getWidth() < widthLimit && source.getHeight() < heightLimit) {
				if (source.getWidth() > source.getHeight()) {
					matrix.setScale(
							widthLimit / (float) source.getWidth(), widthLimit / (float) source.getWidth());
				} else {
					matrix.setScale(
							heightLimit / (float) source.getHeight(), heightLimit / (float) source.getHeight());
				}
			}
			if (orientation != 0) {
				matrix.postRotate(orientation);
			}
			return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
		} catch (Exception e) {
			return source;
		}
	}

	public static int parseExifOrientation(int ori) {
		switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			default:
				return 0;
		}
	}

	@TargetApi(EXTERNAL_DIR_API_LEVEL)
	public static File getAvailableExternalFileDir(Context ctx, String type) {
		if (iCommon.getApiLevel() >= EXTERNAL_DIR_API_LEVEL) {
			File[] paths = ctx.getExternalFilesDirs(type);
			if (paths != null) {
				for (File path : paths) {
					if (path != null) {
						return path;
					}
				}
			}
			return null;
		} else {
			return ctx.getExternalFilesDir(type);
		}
	}

	public static File getAvailableExternalFile(Context ctx, String type, String path) {
		File ret = iCommon.getAvailableExternalFileDir(ctx, type);
		return new File(ret, path);
	}

	public static void vibrate(Context context,int duration) {
		Vibrator vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(duration);
	}

	/**
	 * webview core is changed to chromium since android 4.4
	 * @return
	 */
	public static boolean isWebViewCoreModern() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

	public static String getTermsURL(Context context) {
		return WSManager.instance().getBaseUrl() + context.getString(R.string.term_url_path);
	}

}
