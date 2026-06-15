package day1.associationExample;

public class Main {
    public static void main(String[] args) {
        Car car = new Car("BMW");
        Driver driver = new Driver("Sachin");

        driver.drive(car);
    }
}
