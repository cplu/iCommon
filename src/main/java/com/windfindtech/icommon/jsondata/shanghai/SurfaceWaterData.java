package com.windfindtech.icommon.jsondata.shanghai;

import android.util.Pair;

import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.util.ResUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cplu on 2015/12/5.
 */
public class SurfaceWaterData extends ISHBaseData {
	private Date release;
	private ArrayList<SurfaceWaterItemData> data;

	public Date getRelease() {
		return release;
	}

	public void setRelease(Date release) {
		this.release = release;
	}

	public ArrayList<SurfaceWaterItemData> getData() {
		return data;
	}

	public void setData(ArrayList<SurfaceWaterItemData> data) {
		this.data = data;
	}

	@Override
	public boolean isValid() {
		return getData() != null && getData().size() > 0;
	}

	private int m_briefIndex = 0;

	@Override
	public boolean getBriefInfos(String[] infos) throws Exception {
		m_briefIndex = iCommon.getRandom(0, getData().size());
		SurfaceWaterItemData water_data = getData().get(m_briefIndex);
		infos[1] = water_data.getName();
		return true;
	}

	@Override
	public Pair<Integer, String> getTextColor() throws Exception{
		SurfaceWaterItemData surface_water_data = getData().get(m_briefIndex);
		return ResUtil.getWaterLevelColorIDPair(surface_water_data.getLevel());
	}

	@Override
	public Pair<Integer, String> getBriefValue() throws Exception {
		if(getData() != null && getData().size() > m_briefIndex) {
			SurfaceWaterItemData item_data = getData().get(m_briefIndex);
			if(item_data != null){
				return Pair.create(ResUtil.getWaterLevelStringID(item_data.getLevel()), null);
			}
		}
		return null;
	}
}
