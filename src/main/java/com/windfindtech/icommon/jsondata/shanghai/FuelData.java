package com.windfindtech.icommon.jsondata.shanghai;

import android.util.Pair;

import com.windfindtech.icommon.R;

import java.util.Date;

/**
 * Created by cplu on 2015/3/11.
 */
public class FuelData extends ISHBaseData {
	private Date release;
	private float diesel;
	private float ron89;
	private float ron92;
	private float ron95;

	public Date getRelease() {
		return release;
	}

	public void setRelease(Date release) {
		this.release = release;
	}

	public float getDiesel() {
		return diesel;
	}

	public void setDiesel(float diesel) {
		this.diesel = diesel;
	}

	public float getRon89() {
		return ron89;
	}

	public void setRon89(float ron89) {
		this.ron89 = ron89;
	}

	public float getRon92() {
		return ron92;
	}

	public void setRon92(float ron92) {
		this.ron92 = ron92;
	}

	public float getRon95() {
		return ron95;
	}

	public void setRon95(float ron95) {
		this.ron95 = ron95;
	}

	@Override
	public boolean isValid() {
		return getRon92() > 0.0f;
	}

	@Override
	public boolean getBriefInfos(String[] infos) throws Exception {
		return false;
	}

	@Override
	public Pair<Integer, String> getTextColor() throws Exception {
		return Pair.create(R.color.default_orange, "orange");
	}

	@Override
	public Pair<Integer, String> getBriefValue() throws Exception {
		return Pair.create(0, String.valueOf(getRon92()));
	}
}
