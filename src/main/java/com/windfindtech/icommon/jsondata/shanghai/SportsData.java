package com.windfindtech.icommon.jsondata.shanghai;

import android.util.Pair;

import com.windfindtech.icommon.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cplu on 2015/5/22.
 */
public class SportsData extends ISHBaseData {
	private Date time;
	private ArrayList<SportsCSLData> current;
	private ArrayList<SportsCSLData> next;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public ArrayList<SportsCSLData> getCurrent() {
		return current;
	}

	public void setCurrent(ArrayList<SportsCSLData> current) {
		this.current = current;
	}

	public ArrayList<SportsCSLData> getNext() {
		return next;
	}

	public void setNext(ArrayList<SportsCSLData> next) {
		this.next = next;
	}

	@Override
	public boolean isValid() {
		return current != null && current.size() > 0;
	}

	@Override
	public boolean getBriefInfos(String[] infos) throws Exception {
		String year = null;
		if (time != null) {
			Calendar time_calendar = Calendar.getInstance();
			time_calendar.setTime(time);
			year = String.valueOf(time_calendar.get(Calendar.YEAR));
		}
		infos[1] = year;
		return true;
	}

	@Override
	public Pair<Integer, String> getTextColor() throws Exception {
		return Pair.create(R.color.blue_purple, "blue_purple");
	}

	@Override
	public Pair<Integer, String> getBriefValue() throws Exception {
		return null;
	}
}
