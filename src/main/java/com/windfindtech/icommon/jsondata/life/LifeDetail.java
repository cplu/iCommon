package com.windfindtech.icommon.jsondata.life;

import com.windfindtech.icommon.jsondata.usageinfo.WFTLocation;

/**
 * Created by yu on 2015/8/8.
 */
public class LifeDetail {

    private String id;
    private String category;
    private String title;
    private String summary;
    private String[] tags;
    private WFTLocation location;
    private LifeImage[] images;
    private Provider provider;
    private String href;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public WFTLocation getLocation() {
        return location;
    }

    public void setLocation(WFTLocation location) {
        this.location = location;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public LifeImage[] getImages() {
        return images;
    }

    public void setImages(LifeImage[] images) {
        this.images = images;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
