package com.geekspace.beyikyol.Activity.Car;

public class Cars {
    Integer id;
    String name;
    String model;
    String image;
    String Km;

    public Cars(Integer id, String name, String model, String image, String km) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.image = image;
        Km = km;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKm() {
        return Km;
    }

    public void setKm(String km) {
        Km = km;
    }
}
