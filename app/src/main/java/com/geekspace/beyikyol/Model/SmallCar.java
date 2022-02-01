package com.geekspace.beyikyol.Model;

public class SmallCar {
    String carId;
    String CarName;

    public SmallCar(String carId, String carName) {
        this.carId = carId;
        CarName = carName;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }
}
