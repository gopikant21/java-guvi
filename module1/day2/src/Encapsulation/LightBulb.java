package Encapsulation;

public class LightBulb {
    private String brand;
    private String watts;

    public LightBulb(String brand, String watts) {
        this.brand = brand;
        this.watts = watts;
    }

    public void getDetails() {
        System.out.println("Brand: " + this.brand);
        System.out.println("Watts: " + this.watts);
    }

    public void turnOn(){
        System.out.println("Turning on");
    }
    public void turnOff(){
        System.out.println("Turning off");
    }
}
