package com.geekspace.beyikyol.Model;

public class LangClass {
    String value;

    int image;
    String language;

    public LangClass(String value, int image, String language) {
        this.value = value;
        this.image = image;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
