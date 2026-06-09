package arrayDemo.product;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Product p1 = new Product(1, "iPhone 15", 79999.99, "Apple", 5.0, 5);
        Product p2 = new Product(2, "Galaxy S23", 69999.50, "Samsung", 10.0, 4);
        Product p3 = new Product(3, "Pixel 8", 65999.75, "Google", 8.0, 4);
        Product p4 = new Product(4, "OnePlus 12", 62999.00, "OnePlus", 12.5, 5);
        Product p5 = new Product(5, "Xiaomi 14", 54999.99, "Xiaomi", 15.0, 4);

        Product[] pArray = {p1,p2,p3,p4,p5};

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice of sorting. 1 byName, 2 byPrice, 3 byDiscount, 4 byRating: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                Arrays.sort(pArray, new NameComparator());
                break;
            case 2:
                Arrays.sort(pArray, new PriceComparator());
                break;
            case 3:
                Arrays.sort(pArray, new DiscountComparator());
                break;
            case 4:
                Arrays.sort(pArray, new RatingComparator());
                break;
        }

        for (Product p : pArray) {
            System.out.println(p);
        };
    }
}
