package Encapsulation;

public class MainLightBulb {
    public static void main(String[] args) {
        LightBulb b = new LightBulb("gfehd", "30");
        b.getDetails();
        b.turnOn();
        b.turnOff();
    }
}
