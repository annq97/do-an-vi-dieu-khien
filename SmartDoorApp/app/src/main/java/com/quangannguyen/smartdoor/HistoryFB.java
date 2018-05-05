package com.quangannguyen.smartdoor;

/**
 * Created by quangannguyen on 2/23/2018.
 */

public class HistoryFB {

    private String ID;
    private String date;
    private String state;

    public HistoryFB() {
    }

    public HistoryFB(String ID, String date, String state) {
        this.ID = ID;
        this.date = date;
        this.state = state;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
