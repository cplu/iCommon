package com.windfindtech.icommon.provider;

import android.provider.BaseColumns;

/**
 * Created by cplu on 2016/11/3.
 */

public final class SharedZone {
	/**
	 * BaseColumns类中有两个属性：_ID 和 _COUNT
	 */
	public static abstract class EntName implements BaseColumns {
		public static final String TABLE_NAME = "wft_shared_zone";
		public static final String USER_NAME = "username";
		public static final String PASSWORD = "password";
		public static final String AUTH_TOKEN = "authtoken";
	}
}
