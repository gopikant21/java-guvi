package arrayDemo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class ChoiceSortPersonClass {
    public static void main(String[] args) {
        Person p1 = new Person("Gopi", "Kant", 21);
        Person p2 = new Person("Rahul", "Kumar", 22);
        Person p3 = new Person("Karan", "Khurana", 23);

        Person[] pArray = {p1, p2, p3};

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter choice: 1 for Age, 2 for Fname, 3 for Lname: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                Arrays.sort(pArray, new Comparator<Person>() {
                    @Override
                    public int compare(Person o1, Person o2) {
                        return o1.getAge()-o2.getAge();
                    }
                });
                break;
            case 2:
                Arrays.sort(pArray, new FNameASCComparator());
                break;
            case 3:
                Arrays.sort(pArray, new LNameASCComparator());
                break;
        }

        for (Person p : pArray) {
            System.out.println(p);
        }
    }
}
