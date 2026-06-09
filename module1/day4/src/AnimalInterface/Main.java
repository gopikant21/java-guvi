package AnimalInterface;

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat();
        dog.walk();
        dog.talk();

        Lion lion = new Lion();
        lion.eat();
        lion.walk();
        lion.talk();
    }
}
