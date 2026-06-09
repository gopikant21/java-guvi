package composition;

public class MainCar {
    public static void main(String[] args) {
        Car car = new Car(
                new Engine(100),
                new Ac(1),
                new MusicSystem("Sony");
        );
        car.getDetails();

    }
}
