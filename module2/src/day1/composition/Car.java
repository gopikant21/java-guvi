package day1.composition;

public class Car {
    private String name;
    private Engine engine;

    public Car(int hoursePower, String name) {
        this.name = name;
        this.engine = new Engine(hoursePower);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    public void stop() {
        System.out.println("Car stopped");
    }
    public void start() {
        System.out.println("Car started");
        engine.start();
    }

}
