import java.util.Scanner;

public class Reusable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); //object of scanner class

        System.out.println("Enter number a: ");
        int a = sc.nextInt();
        System.out.println("Enter number b: ");
        int b = sc.nextInt();

        findGreatest(a, b);
        sc.close();

    }

    private static void findGreatest(int a, int b) {
        if (a > b) {
            System.out.println("a is greater than b");
        }else{
            System.out.println("b is greater than a");
        }
    }
}
