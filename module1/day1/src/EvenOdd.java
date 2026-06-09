import java.util.Scanner;

public class EvenOdd {

    public static void main(String[] args){
        System.out.println("Enter number: ");
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        isEven(a);
        sc.close();
    }

    private static void isEven(int num){
        if(num%2==0){
            System.out.println("The number is even");
        }else{
            System.out.println("The number is odd");
        }

    }
}
