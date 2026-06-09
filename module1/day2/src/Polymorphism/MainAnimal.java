package Polymorphism;

import java.util.Scanner;

public class MainAnimal {
    public static void main(String[] args) {
        /*Animal d = new Dog();

        Dog downcastedDog= (Dog) d;
        downcastedDog.guard();*/

        System.out.println("Enter 1 for Dog, 2 for Lion: ");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        Animal a;
        switch (choice){
            case 1:
                a = new Dog();
                break;
            case 2:
                a = new Lion();
                break;
            default:
                a = new Animal();
        }
        a.eat();
        a.talk();
        a.walk();

    }
}
