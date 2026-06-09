package Encapsulation;

public class Car {
    private String brand;
    private String model;

    public Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public void getDetails() {
        System.out.println("Brand: " + this.brand);
        System.out.println("Model: " + this.model);
    }

    public void start() {
        System.out.println("Car starting");
    }
    public void stop() {
        System.out.println("Car stopping");
    }
}
