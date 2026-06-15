package day1.associationExample;

public class Driver {
    private String name;

    public Driver(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void drive(Car car){
        System.out.println(this.name + " is driving car " + car.getModel());
    }
}
