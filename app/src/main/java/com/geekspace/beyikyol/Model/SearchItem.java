package com.geekspace.beyikyol.Model;

import com.yandex.mapkit.geometry.Point;

public class SearchItem {
    String name;
    Point point;

    public SearchItem(String name, Point point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
