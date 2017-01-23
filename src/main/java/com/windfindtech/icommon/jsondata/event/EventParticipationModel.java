package com.windfindtech.icommon.jsondata.event;

import java.util.Date;

/**
 * Created by cplu on 2014/11/25.
 */
public class EventParticipationModel {
    private String id;
    private String activityId;
    private String userId;
//    private boolean drawn;
    private float amount;
    private Date time;
    private int round;
    private boolean isWinner;
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

//    public boolean isDrawn() {
//        return drawn;
//    }
//
//    public void setDrawn(boolean drawn) {
//        this.drawn = drawn;
//    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
