package Abstraction;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
               return;
        }
        a.eat();
        a.talk();
        a.shelter();
    }
}
