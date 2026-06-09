package Abstraction;

public class Dog extends Animal{
    @Override
    public void talk() {
        System.out.println("Dog barks");
    }

    @Override
    public void eat() {
        System.out.println("Dog eating");
    }

    @Override
    public void shelter() {
        System.out.println("Dogs lives in gli");
    }

}
