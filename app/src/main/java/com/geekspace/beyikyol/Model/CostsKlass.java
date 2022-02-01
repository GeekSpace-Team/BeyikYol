package com.geekspace.beyikyol.Model;

public class CostsKlass {
    String id;
    String type;
    String sene;
    String km;
    String value;
    String carId;
    String costId;

    public CostsKlass(String id, String type, String sene, String km,String value,String carId,String costId) {
        this.id = id;
        this.type = type;
        this.sene = sene;
        this.km = km;
        this.value=value;
        this.carId=carId;
        this.costId=costId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSene() {
        return sene;
    }

    public void setSene(String sene) {
        this.sene = sene;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }
}
