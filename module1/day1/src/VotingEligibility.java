import java.util.Scanner;

public class VotingEligibility {
    public static void main(String[] args){
        System.out.println("Enter your age: ");
        Scanner sc = new Scanner(System.in);
        int age = sc.nextInt();
        sc.close();
        checkEligibility(age);

    }

    private static void checkEligibility(int age){
        if(age<=0){
            System.out.println("Please enter a positive age");
        }else{
            if(age<18){
                System.out.println("Your are not eligible to vote");
            }else{
                System.out.println("Your are eligible to vote");
            }
        }

    }
}
