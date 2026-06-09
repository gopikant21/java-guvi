import java.util.Scanner;
//print even numbers in the given input range
public class Even {
    public static void main(String[] args) {
        System.out.println("Enter lower limit: ");
        Scanner sc=new Scanner(System.in);
        int lowerLimit=sc.nextInt();
        System.out.println("Enter upper limit: ");
        int upperLimit=sc.nextInt();
        printEven(lowerLimit, upperLimit);
        sc.close();
    }
    private static void printEven(int ll, int ul){
        for(int i=ll; i<=ul; i++){
            if(i%2==0){
                System.out.print(i+" ");
            }
        }
    }

    private static void printOdd(int ll, int ul){
        for(int i=ll; i<=ul; i++){
            if(i%2!=0){
                System.out.print(i+" ");
            }
        }
    }
}
