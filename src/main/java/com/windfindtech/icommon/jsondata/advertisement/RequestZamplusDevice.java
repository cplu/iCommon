package com.windfindtech.icommon.jsondata.advertisement;

/**
 * Created by cplu on 2016/4/11.
 * part of request model for zamplus
 */
public class RequestZamplusDevice {
	private String imei;
	private String idfa;
	private String ua; //设备浏览器UserAgent字符串
	private String ip; //客户端外网ip地址
	private String language; //设备语言
	private String brand; //设备厂商
	private String model; //设备型号
	private String os; // Android，iOS
	private String osv; // 系统版本号 eg: 2.3
	private int conn_type = 1; //联网类型 1、wifi 2、2G 3、3G 4、代理、5 其他
	private String device_type; //设备类型 iPhone、iPad、iPod、Android、Android_Pad
	private String packname; //app 名称（目前发过来的是app 注册的名称）
	private String lat; //维度
	private String lon; //精度
	private String[] tags; //tags 信息，json string 数组。
	private String MMC;
	private String MNC;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsv() {
		return osv;
	}

	public void setOsv(String osv) {
		this.osv = osv;
	}

	public int getConn_type() {
		return conn_type;
	}

	public void setConn_type(int conn_type) {
		this.conn_type = conn_type;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getMMC() {
		return MMC;
	}

	public void setMMC(String MMC) {
		this.MMC = MMC;
	}

	public String getMNC() {
		return MNC;
	}

	public void setMNC(String MNC) {
		this.MNC = MNC;
	}
}
