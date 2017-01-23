package com.windfindtech.icommon.jsondata.life;

/**
 * Created by yu on 2015/7/31.
 */
public class LifeInfo {

    private String category;
    private String title;
    private String description;
    private String color;

    private String main_image_url;
    private String activity_url;

    public LifeInfo() {
    }

    public LifeInfo(String category, String title, String description, String color,String main_image_url, String activity_url) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.color = color;
        this.main_image_url = main_image_url;
        this.activity_url = activity_url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getActivity_url() {
        return activity_url;
    }

    public void setActivity_url(String activity_url) {
        this.activity_url = activity_url;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain_image_url() {
        return main_image_url;
    }

    public void setMain_image_url(String main_image_url) {
        this.main_image_url = main_image_url;
    }
}
