package arrayDemo.child;

import arrayDemo.product.PriceComparator;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Child c1 = new Child("Gopi", "Kant", "01-01-00");
        Child c2 = new Child("Ravi", "Kumar", "15-03-05");
        Child c3 = new Child("Anita", "Sharma", "22-07-03");
        Child c4 = new Child("Priya", "Reddy", "10-10-06");
        Child c5 = new Child("Arjun", "Verma", "05-12-04");

        Child[] cArray = {c1,c2,c3,c4,c5};

        Arrays.sort(cArray, new DOBComparator());

        for(Child c : cArray) {
            System.out.println(c);
        }

    }

}
