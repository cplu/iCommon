package com.windfindtech.icommon.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.windfindtech.icommon.provider.SharedZone.EntName;

import org.pmw.tinylog.Logger;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by cplu on 2016/11/3.
 */
public class ICommonProvider extends ContentProvider {

	// 创建表格的SQL语句
	private static final String SQL_CREATE_ENTRIES =
		"CREATE TABLE " + EntName.TABLE_NAME + " (" +
		EntName._ID + " INTEGER PRIMARY KEY," +
		EntName.USER_NAME + " TEXT, " +
		EntName.PASSWORD + " TEXT, " +
		EntName.AUTH_TOKEN + " TEXT " +
		" )";

	private DBLiteHelper mDbHelper;
	private String m_authority;
	public static Uri s_contentUri;
	private static ContentResolver s_contentResolver;
	private static String s_packageName;

	private class DBLiteHelper extends SQLiteOpenHelper {
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "ICommonProvider.db";

		public DBLiteHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	// Uri的authority
//	public static final String AUTHORITY = BuildConfig.APPLICATION_ID;
	// Uri的path
	public static final String PATH = "userauthinfo";

//	private static Uri[] s_sharedUris;    /// all shared uris, static members

	// UriMatcher中URI对应的序号
	public static final int ITEM_ALL = 1;
	public static final int ITEM_ID = 2;

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

//	static {
//		URI_MATCHER.addURI(AUTHORITY, PATH, ITEM_ALL);
//		URI_MATCHER.addURI(AUTHORITY, PATH + "/#", ITEM_ID);
//	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DBLiteHelper(this.getContext());
		m_authority = getContext().getApplicationInfo().packageName + ".commonprovider";
		URI_MATCHER.addURI(m_authority, PATH, ITEM_ALL);
		URI_MATCHER.addURI(m_authority, PATH + "/#", ITEM_ID);
		s_contentUri = new Uri.Builder()
			.scheme("content")
			.authority(m_authority)
			.path(PATH)
			.build();

		s_packageName = getContext().getApplicationInfo().packageName;

//		if (getContext().getString(R.string.compile_level).equals(iCommon.COMPILE_LEVEL_DEBUG)) {
//			s_sharedUris = new Uri[]{
//				Uri.parse("content://com.windfindtech.ishanghai.debug.commonprovider/" + PATH),
//				Uri.parse("content://com.windfindtech.icity.debug.commonprovider/" + PATH),
//			};
//		} else {
//			s_sharedUris = new Uri[]{
//				Uri.parse("content://com.windfindtech.ishanghai.commonprovider/" + PATH),
//				Uri.parse("content://com.windfindtech.icity.commonprovider/" + PATH),
//			};
//		}
		s_contentResolver = getContext().getContentResolver();

		/// remove self uri
//		for (int i = 0; i < s_sharedUris.length; i++) {
//			if (s_sharedUris[i].getAuthority().equals(s_contentUri.getAuthority())) {
//				s_sharedUris[i] = null;
//			} else {
//			}
//		}

//		Log.d(TAG, "open/create table");
		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor;
		switch (URI_MATCHER.match(uri)) {
			case ITEM_ALL:
				cursor = db.query(EntName.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
//				this.getContext().getContentResolver().notifyChange(uri, null);
				return cursor;
			case ITEM_ID:
				long id = ContentUris.parseId(uri);
				String where = EntName._ID + "=" + id;
				cursor = db.query(EntName.TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
//				this.getContext().getContentResolver().notifyChange(uri, null);
				return cursor;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
			case ITEM_ALL:
				return "com.windfindtech.app.commonprovider.dir/userauthinfo";
			case ITEM_ID:
				return "com.windfindtech.app.commonprovider.item/userauthinfo";
			default:
				throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long id;
		switch (URI_MATCHER.match(uri)) {
			case ITEM_ALL:
				id = db.insert(EntName.TABLE_NAME, null, values);
				this.getContext().getContentResolver().notifyChange(uri, null);
				return ContentUris.withAppendedId(uri, id);
			case ITEM_ID:
				throw new IllegalArgumentException("URI cannot be inserted " + uri);
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		int count = 0;
		switch (URI_MATCHER.match(uri)) {
			case ITEM_ALL:
				count = db.delete(EntName.TABLE_NAME, selection, selectionArgs);
//				Log.d(TAG, "delete ITEM uri="+uri+", count="+count);
				break;
			case ITEM_ID:
				long id = ContentUris.parseId(uri);
				String where = EntName._ID + "=" + id;
				count = db.delete(EntName.TABLE_NAME, where, selectionArgs);
//				Log.d(TAG, "delete ITEM_ID id="+id+", uri="+uri+", count="+count);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		int count = 0;
		switch (URI_MATCHER.match(uri)) {
			case ITEM_ALL:
				throw new IllegalArgumentException("URI cannot be updated " + uri);
			case ITEM_ID:
				long id = ContentUris.parseId(uri);
				String where = EntName._ID + "=" + id;
				count = db.update(EntName.TABLE_NAME, values, where, selectionArgs);
//				Log.d(TAG, "delete ITEM_ID id="+id+", uri="+uri+", count="+count);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	public static final int ID_DEFAULT = 1;

	public static String[] getUserAuthInfo() {
//		ContentValues cv = new ContentValues();
//		cv.put(EntName.USER_NAME, "dd");
//		cv.put(EntName.PASSWORD, "dfaoafdiafoajifojiodaf");
//		cv.put(EntName.AUTH_TOKEN, "ggggggggggggggggg");
		try {
			Cursor cursor = s_contentResolver.query(s_contentUri, null, EntName._ID + "=" + ID_DEFAULT, null, null);
			String[] userAuthInfo = null;
			if (cursor.moveToNext()) {
				userAuthInfo = new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3)};
			}
			cursor.close();
			return userAuthInfo;
		} catch (Exception e) {
			Logger.error(e);
			return null;
		}
	}

	public static void createDefaultUserAuthInfo() {
		try {
			ContentValues values = new ContentValues();
			values.put(EntName._ID, ID_DEFAULT);
//			values.putNull(EntName.USER_NAME);
//			values.putNull(EntName.PASSWORD);
//			values.putNull(EntName.AUTH_TOKEN);
			s_contentResolver.insert(s_contentUri, values);
			return;
		} catch (Exception e) {
			Logger.error(e);
			return;
		}
	}

	public static boolean updateUserName(String phone_number) {
		int updated = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(EntName.USER_NAME, phone_number);
			updated = s_contentResolver.update(ContentUris.withAppendedId(s_contentUri, ID_DEFAULT), values, null, null);
			notifyOtherApps(values);
		} catch (Exception e) {
			Logger.error(e);
		}
		return updated == 1;
	}

	public static boolean updatePassword(String encrypted) {
		int updated = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(EntName.PASSWORD, encrypted);
			updated = s_contentResolver.update(ContentUris.withAppendedId(s_contentUri, ID_DEFAULT), values, null, null);
			notifyOtherApps(values);
		} catch (Exception e) {
			Logger.error(e);
		}
		return updated == 1;
	}

	private static ContentValues m_contentValues = new ContentValues();

	private static void notifyOtherApps(ContentValues values) {
		m_contentValues.putAll(values);
		throttleLast.onNext(m_contentValues);
	}

	public static boolean updateOtherPassword(String encrypted) {
		int updated = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(EntName.PASSWORD, encrypted);
			String otherURI = s_packageName.equals("com.windfindtech.icity.debug") ?
				"content://com.windfindtech.ishanghai.debug.commonprovider/userauthinfo"
				: "content://com.windfindtech.icity.debug.commonprovider/userauthinfo";
			updated = s_contentResolver.update(ContentUris.withAppendedId(Uri.parse(otherURI), ID_DEFAULT), values, null, null);
			notifyOtherApps(values);
		} catch (Exception e) {
			Logger.error(e);
		}
		return updated == 1;
	}

	private static Subscriber<? super ContentValues> throttleLast;

	static {
		Observable.create(new Observable.OnSubscribe<ContentValues>() {
			@Override
			public void call(Subscriber<? super ContentValues> subscriber) {
				throttleLast = subscriber;
			}
		}).throttleLast(1000, TimeUnit.MILLISECONDS)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<ContentValues>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable throwable) {

				}

				@Override
				public void onNext(ContentValues contentValues) {
//					WFTConfig.instance().sendInfoUpdateIntent();
				}
			});
	}
}
