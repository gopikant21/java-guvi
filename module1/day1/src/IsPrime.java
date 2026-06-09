import java.util.Scanner;

public class IsPrime {
    public static void main(String[] args) {
        System.out.println("Enter a number: ");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        isPrime(num);
        sc.close();
    }

    private static void isPrime(int num){
        boolean flag = true;
        for(int i=2;i<num/2;i++){
            if(num%i==0){
                flag = false;
            }
        }
        if(flag==true){
            System.out.println("The number "+num+" is a prime number");
        }else{
            System.out.println(num+" is not a prime number");
        }

    }
}
