import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        System.out.println("enter a number: ");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        printFibonacci(num);
        sc.close();
    }

    public static void printFibonacci(int num){
        int i=1;
        int j = 2;
        int k=i+j;
        System.out.println(i+" "+j);
        while(k<num){
            k=j+i;
            i=j;
            j=k;

            System.out.println(k + " ");
        }
    }
}
