package com.windfindtech.icommon.jsondata.shanghai;

import android.content.Context;
import android.util.Pair;

/**
 * Created by cplu on 2015/5/21.
 */
public abstract class ISHBaseData {
//	public static class ValueWrapper{
//		public int m_id;
//		public String m_name;
//		public ValueWrapper(int id){
//			m_id = id;
//		}
//		public ValueWrapper(String nam)
//	}

    public abstract boolean isValid();

	/**
	 * Get brief info about the data
	 * @param infos     [out]   string 0: left bottom string in sh brief layout
	 *                          string 1: right bottom string in sh brief layout
	 * @return true if infos contains valid values, false if no values set
	 * @throws Exception
	 */
    public abstract boolean getBriefInfos(String[] infos) throws Exception;

    /**
     * Get UI color
     * @return  the first value is color ID, while the second value is color name (blue, red, ...)
     */
    public abstract Pair<Integer, String> getTextColor() throws Exception;

    /**
     * Parse and get the result that is shown at center of ish brief layout
     * @return  If first value is valid (>0), the returned value is a string resource ID. Otherwise, the second value is valid (a string)
     * @throws Exception
     */
    public abstract Pair<Integer, String> getBriefValue() throws Exception;

	/**
	 * Whether arrow color is changed according to data
	 * @return  True if arrow color should be changed accordingly.
	 * Note: Currently only arrow color of index brief block should be changed
	 */
	public boolean isArrowColorChanged() {
		return false;
	}
}
