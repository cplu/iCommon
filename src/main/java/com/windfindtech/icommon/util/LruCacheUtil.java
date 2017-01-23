package com.windfindtech.icommon.util;

import com.jakewharton.disklrucache.DiskLruCache;
import com.windfindtech.icommon.iCommon;

import org.pmw.tinylog.Logger;

/**
 * Created by cplu on 2016/12/1.
 */

public class LruCacheUtil {

	private static DiskLruCache s_lruCache;  /// lruCache on disk, default size is 1 MB
	private static final long DISK_LRU_CACHE_SIZE = 1 * 1024 * 1024;
	private static long DISK_LRU_CACHE_EXPIRE = (12 * 60 * 60 * 1000);  // 12 hours
	public static final String DISK_LRU_CACHE_EXPIRE_DATE_KEY = "app:lrucache_expire";

	public static void invalidateCacheWhenExpire() {
		if (iCommon.isDebug) {
			DISK_LRU_CACHE_EXPIRE = 60 * 1000;  /// for debug, 60 seconds for expiration
		}
		long currentTime = System.currentTimeMillis();
		long date = Utils.getLong(DISK_LRU_CACHE_EXPIRE_DATE_KEY, 0);
		if (currentTime - date > DISK_LRU_CACHE_EXPIRE) {
			try {
				Utils.saveLong(DISK_LRU_CACHE_EXPIRE_DATE_KEY, currentTime);
				if (s_lruCache != null) {
					s_lruCache.delete();
					s_lruCache = null;
				}
			} catch (Exception e) {
				Logger.error(e);
			}
		}
		if (s_lruCache == null) {
			try {
				s_lruCache = DiskLruCache.open(Utils.instance().getCacheDir("lru_cache"), Utils.instance().getAppVersionCode(), 1, DISK_LRU_CACHE_SIZE);
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	public static DiskLruCache.Snapshot get(String key) {
		try {
			return s_lruCache.get(key);
		} catch (Exception e) {
			return null;
		}
	}

	public static void put(String key, String value) {
		try {
			DiskLruCache.Editor editor = s_lruCache.edit(key);
			editor.set(0, value);
			editor.commit();
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}
