package arrayDemo;

import java.util.Arrays;

public class PersonClassSort {
    public static void main(String[] args) {
        Person p1 = new Person("Gopi", "Kant", 21);
        Person p2 = new Person("Rahul", "Kumar", 22);
        Person p3 = new Person("Karan", "Khurana", 23);

        Person[] pArray = {p1, p2, p3};
        Arrays.sort(pArray, new LNameASCComparator());
        for (Person p : pArray) {
            System.out.println(p);
        }

        //System.out.println(Arrays.toString(pArray));
    }
}
