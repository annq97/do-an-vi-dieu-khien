package com.quangannguyen.smartdoor;

import com.google.firebase.database.PropertyName;

/**
 * Created by quangannguyen on 2/22/2018.
 */

public class House {

    private String qr_code;
    private String current_check;
    private String code_private;
    @PropertyName("ID")
    private String ID;

    public House() {
    }

    public House(String qr_code, String current_check, String code_private, String ID) {
        this.qr_code = qr_code;
        this.current_check = current_check;
        this.code_private = code_private;
        this.ID = ID;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getCurrent_check() {
        return current_check;
    }

    public void setCurrent_check(String current_check) {
        this.current_check = current_check;
    }

    public String getCode_private() {
        return code_private;
    }

    public void setCode_private(String code_private) {
        this.code_private = code_private;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
