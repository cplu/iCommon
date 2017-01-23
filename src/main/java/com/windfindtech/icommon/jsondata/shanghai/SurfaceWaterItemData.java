package com.windfindtech.icommon.jsondata.shanghai;

/**
 * Created by cplu on 2015/12/5.
 */
public class SurfaceWaterItemData{
	private String name;
	private String riverName;
	private String level;
	private String primaryPollutant;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRiverName() {
		return riverName;
	}

	public void setRiverName(String riverName) {
		this.riverName = riverName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPrimaryPollutant() {
		return primaryPollutant;
	}

	public void setPrimaryPollutant(String primaryPollutant) {
		this.primaryPollutant = primaryPollutant;
	}
}
