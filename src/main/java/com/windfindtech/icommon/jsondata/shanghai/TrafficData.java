package com.windfindtech.icommon.jsondata.shanghai;

import android.util.Pair;

import com.windfindtech.icommon.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cplu on 2015/3/11.
 */
public class TrafficData extends ISHBaseData{
    private Date time;
    private float index;
    private String status;
    private ArrayList<TrafficRoadData> data;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getIndex() {
        return index;
    }

    public void setIndex(float index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TrafficRoadData> getData() {
        return data;
    }

    public void setData(ArrayList<TrafficRoadData> data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return getData() != null && getData().size() > 0;
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        TrafficRoadData road_data = getData().get(0);
        infos[1] = road_data.getRoad();
        return true;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception {
        TrafficRoadData road_data = getData().get(0);
        return Pair.create(getRoadStatusColor(road_data.getStatus()), "green");
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        if(getData() != null && getData().size() > 0) {
            TrafficRoadData road_data = getData().get(0);
            if(road_data != null){
                return Pair.create(road_data.getStatusStringID(), null);
            }
        }
        return null;
    }

    public static int getRoadStatusColor(String status){
        if(status.equals("fast")){
            return R.color.default_green;
        }
        else if(status.equals("medium-fast")){
            return R.color.default_blue;
        }
        else if(status.equals("medium")){
            return R.color.default_orange;
        }
        else{
            return R.color.default_orange;
        }
    }
}
