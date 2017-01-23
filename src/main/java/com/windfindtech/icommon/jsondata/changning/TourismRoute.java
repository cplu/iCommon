package com.windfindtech.icommon.jsondata.changning;

/**
 * Created by yu on 2015/8/28.
 */
public class TourismRoute {

    private String id;
    private String name;

    public TourismRoute(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "TourismRoute{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
