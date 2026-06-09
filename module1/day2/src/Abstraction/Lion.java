package Abstraction;

public class Lion extends Animal{
    @Override
    public void eat() {
        System.out.println("Lion is carinorous");
    }

    @Override
    public void talk() {
        System.out.println("Lion roars");
    }

    @Override
    public void shelter() {
        System.out.println("Lion lives in den");
    }

}
