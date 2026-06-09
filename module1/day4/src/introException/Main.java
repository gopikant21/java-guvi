package intro;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("Enter number a: ");
            int a = sc.nextInt();
            System.out.println("Enter number b: ");
            int b = sc.nextInt();

            double res = a/b;
            System.out.printf("Result: " + res);
        }
        catch (ArithmeticException e){
            System.out.println("Division by zero");
        }
        catch (InputMismatchException e){
            System.out.println("Incorrect input");
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            sc.close();
        }

        System.out.println("dhsgjhd");

    }
}
