package com.quangannguyen.smartdoor;

/**
 * Created by quangannguyen on 2/14/2018.
 */

public class User {
    private String name;
    private String image;
    private String ID;
    private String hasHouse;
    private String MaSo;
    private String admin;
    public User() {
    }

    public User(String name, String image, String ID, String hasHouse, String MaSo, String admin) {
        this.name = name;
        this.image = image;
        this.ID = ID;
        this.hasHouse = hasHouse;
        this.MaSo = MaSo;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHasHouse() {
        return hasHouse;
    }

    public void setHasHouse(String hasHouse) {
        this.hasHouse = hasHouse;
    }

    public String getMaSo() {
        return MaSo;
    }

    public void setMaSo(String maSo) {
        MaSo = maSo;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
