package com.geekspace.beyikyol.Model;

public class Year {
    String value;
    int image;

    public Year(String value, int image) {
        this.value = value;
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
