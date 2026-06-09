package Polymorphism;

public class Dog extends Animal{
    public void eat(){
        System.out.println("Dog is omnivorous");
    }

    public void talk(){
        System.out.println("Dog barks");
    }

    public void guard(){
        System.out.println("Dog guards house");
    }
}
