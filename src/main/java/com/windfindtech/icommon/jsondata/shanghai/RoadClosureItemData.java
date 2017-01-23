package com.windfindtech.icommon.jsondata.shanghai;

/**
 * Created by cplu on 2015/5/22.
 */
public class RoadClosureItemData {
    private String road;
    private String side;
    private String part;
    private String period;

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

//    @Override
//    public int hashCode() {
//        return road.hashCode() + side.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof RoadClosureItemData)) {
//            return false;
//        }
//        if (obj == this) {
//            return true;
//        }
//        RoadClosureItemData itemData = (RoadClosureItemData) obj;
//        return road.equals(itemData.road) && side.equals(itemData.side);
//    }
}
