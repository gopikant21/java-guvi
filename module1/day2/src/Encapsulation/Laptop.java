package Encapsulation;

public class Laptop {
    private String brand;
    private String model;
    public Laptop(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public void getDetails() {
        System.out.println("Brand: " + this.brand);
        System.out.println("Model: " + this.model);
    }

    public void turnOn(){
        System.out.println("Turning on");
    }

    public void turnOff(){
        System.out.println("Turning off");
    }
}
