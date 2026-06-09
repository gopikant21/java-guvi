import java.util.Scanner;

public class FactorsOfNumber {
    public static void main(String[] args){
        System.out.println("Enter a number: ");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        printAndCountFactorOfANumber(num);
        sc.close();
    }

    private static void printAndCountFactorOfANumber(int num){
        int count = 0;
        int sum = 0;
        for(int i=1; i<=num; i++){
            if(num%i==0){
                count++;
                sum+=i;
                System.out.print(i+" ");
            }
        }
        System.out.println();
        System.out.println("total factor of the number " + num + " is " + count);
        System.out.println("The sum of the factors of " + num + " is " + sum);

    }
}
