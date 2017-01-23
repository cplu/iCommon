package com.windfindtech.icommon.mvp.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cplu on 2016/7/28.
 */
public class MType {
	@IntDef({NETWORK_RECEIVER, WIFI_RECEIVER, WS_RECEIVER})
	@Retention(RetentionPolicy.SOURCE)
	public @interface LocalReceiver {
	}
	public static final int NETWORK_RECEIVER = 0x1;
	public static final int WIFI_RECEIVER = 0x1 << 1;
	public static final int WS_RECEIVER = 0x1 << 2;

	/**
	 * we use a byte to represent whether notifier should be updated when registered
	 * the bit is shifted bias the XXX_NOTIFIER bite
	 * e.g.
	 *  if DEVICE_NOTIFIER is 0x1 << x, then 0x << (x + FORCE_UPDATE_BIT_SHIFT) represents whether deviceNotifier should be updated when registered
	 *
	 * Warning: this only works for Notifiers, not for Local Receivers
	 */
	private static final int FORCE_UPDATE_BIT_SHIFT = 8;
	@IntDef({DEVICE_NOTIFIER, RESERVED_MESSAGE_NOTIFIER, VERSION_NOTIFIER})
	@Retention(RetentionPolicy.SOURCE)
	public @interface LocalNotifier {
	}
	public static final int DEVICE_NOTIFIER = 0x1 << 3;
	public static final int RESERVED_MESSAGE_NOTIFIER = 0x1 << 4;
	public static final int VERSION_NOTIFIER = 0x1 << 5;
	public static final int DEVICE_UPDATE_BIT = DEVICE_NOTIFIER << FORCE_UPDATE_BIT_SHIFT;
	public static final int RESERVED_MESSAGE_UPDATE_BIT = RESERVED_MESSAGE_NOTIFIER << FORCE_UPDATE_BIT_SHIFT;
	public static final int VERSION_UPDATE_BIT = VERSION_NOTIFIER << FORCE_UPDATE_BIT_SHIFT;

}
