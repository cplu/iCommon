package com.windfindtech.icommon.fragment.management;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

/**
 * Created by cplu on 2016/5/11.
 * Warning: Not used yet!
 */
public class CreatorImpl<T> implements Parcelable.Creator<T> {

	private final Class<T> m_clazz;

	public CreatorImpl(Class<T> clazz) {
		m_clazz = clazz;
	}

	@Override
	public T createFromParcel(Parcel in) {
		try{
			return m_clazz.getConstructor(Parcel.class).newInstance(in);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public T[] newArray(int size) {
		return (T[]) Array.newInstance(m_clazz, size);
	}
}
