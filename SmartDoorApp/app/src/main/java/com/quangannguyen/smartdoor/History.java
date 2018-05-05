package com.quangannguyen.smartdoor;

/**
 * Created by quangannguyen on 2/23/2018.
 */

public class History {

    private String HoTen;
    private String date;
    private String state;

    public History() {
    }

    public History(String hoTen, String date, String state) {
        this.HoTen = hoTen;
        this.date = date;
        this.state = state;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
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
