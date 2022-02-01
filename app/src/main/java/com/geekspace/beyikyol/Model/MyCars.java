package com.geekspace.beyikyol.Model;


public class MyCars {
    String id;
    String name;
    String image;
    String benzin;


    public MyCars(String id, String name, String image, String benzin) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.benzin = benzin;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBenzin() {
        return benzin;
    }

    public void setBenzin(String benzin) {
        this.benzin = benzin;
    }


}
