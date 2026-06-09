import java.util.Scanner;

public class PositiveNegative {

    public static void main(String[] args){
        System.out.println("Enter number: ");
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        isPositive(a);
        sc.close();
    }

    private static void isPositive(int num){
        if(num>0){
            System.out.println("The number is positive");
        }else if(num==0){
            System.out.println("The number is neither positive nor negative");
        }else{
            System.out.println("The number is negative");
        }

    }
}
