package com.windfindtech.icommon.jsondata.shanghai;

import java.util.ArrayList;

/**
 * Created by cplu on 2015/5/22.
 */
public class LotteryItemData {
    private String name;
    private String date;
    private String issue;
    private ArrayList<String> numbers;
    private ArrayList<String> ex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public ArrayList<String> getEx() {
        return ex;
    }

    public void setEx(ArrayList<String> ex) {
        this.ex = ex;
    }
}
