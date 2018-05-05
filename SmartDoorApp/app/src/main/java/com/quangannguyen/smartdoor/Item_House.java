package com.quangannguyen.smartdoor;

public class Item_House {

    private String ID;
    private String name;

    public Item_House() {
    }

    public Item_House(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
