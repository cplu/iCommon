package com.windfindtech.icommon.jsondata.radio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cplu on 2015/8/8.
 */
public class RadioData implements Parcelable{
	private String id;
	private String name;
	private String band;
	private String description;
	private String userId;
	private String userName;
	private String userThumb;
	private String cover;
	private String coverThumb;
	private int audioCount;
	private int favCount;
	private RadioTag[] tags;

	protected RadioData(Parcel in) {
		id = in.readString();
		name = in.readString();
		band = in.readString();
		description = in.readString();
		userId = in.readString();
		userName = in.readString();
		userThumb = in.readString();
		cover = in.readString();
		coverThumb = in.readString();
		audioCount = in.readInt();
		favCount = in.readInt();
		tags = in.createTypedArray(RadioTag.CREATOR);
	}

	public static final Creator<RadioData> CREATOR = new Creator<RadioData>() {
		@Override
		public RadioData createFromParcel(Parcel in) {
			return new RadioData(in);
		}

		@Override
		public RadioData[] newArray(int size) {
			return new RadioData[size];
		}
	};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserThumb() {
		return userThumb;
	}

	public void setUserThumb(String userThumb) {
		this.userThumb = userThumb;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCoverThumb() {
		return coverThumb;
	}

	public void setCoverThumb(String coverThumb) {
		this.coverThumb = coverThumb;
	}

	public int getAudioCount() {
		return audioCount;
	}

	public void setAudioCount(int audioCount) {
		this.audioCount = audioCount;
	}

	public int getFavCount() {
		return favCount;
	}

	public void setFavCount(int favCount) {
		this.favCount = favCount;
	}

	public RadioTag[] getTags() {
		return tags;
	}

	public void setTags(RadioTag[] tags) {
		this.tags = tags;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(band);
		dest.writeString(description);
		dest.writeString(userId);
		dest.writeString(userName);
		dest.writeString(userThumb);
		dest.writeString(cover);
		dest.writeString(coverThumb);
		dest.writeInt(audioCount);
		dest.writeInt(favCount);
		dest.writeTypedArray(tags, flags);
	}
}
