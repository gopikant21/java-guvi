package day1.composition;

public class Engine {
    private int hoursePower;

    public Engine(int hoursePower) {
        System.out.println("Engine created");
        this.hoursePower = hoursePower;
    }
    public int getHoursePower() {
        return hoursePower;
    }

    public void setHoursePower(int hoursePower) {
        this.hoursePower = hoursePower;
    }

    public void start() {
        System.out.println("Engine is running");
    }
}
