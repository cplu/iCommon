package com.windfindtech.icommon.jsondata.event;

import com.windfindtech.icommon.jsondata.enumtype.EventImageType;

import java.util.Date;

/**
 * Created by cplu on 2014/11/25.
 */
public class EventImage {
    private String id;
    private Date lastModified;
    private EventImageType type;
    private String eTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public EventImageType getType() {
        return type;
    }

    public void setType(EventImageType type) {
        this.type = type;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
