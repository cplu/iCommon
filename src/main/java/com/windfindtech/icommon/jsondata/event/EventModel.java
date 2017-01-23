package com.windfindtech.icommon.jsondata.event;

import com.windfindtech.icommon.jsondata.enumtype.EventCategory;
import com.windfindtech.icommon.jsondata.enumtype.EventStatus;
import com.windfindtech.icommon.jsondata.enumtype.EventType;

import java.util.Date;

/**
 * Created by cplu on 2014/11/20.
 */
public class EventModel implements Comparable<EventModel>{
    private String id;
    private String name;
    private Date begin;
    private Date end;
    private String background;
    private String tips;
    private String content;
    private EventType type;
    private EventStatus status;
    private EventCategory category;

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

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }


    @Override
    public int compareTo(EventModel another) {
        return status.compareTo(another.getStatus());
    }
}
