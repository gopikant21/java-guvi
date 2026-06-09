package intro;

import java.util.Scanner;

public class ManuallyThrow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Name: ");
        String name = sc.next();

        try{
            if(!name.equals("Sachin") && !name.equals("Rahul") && !name.equals("Saurav")){
                System.out.println("cghkhjk");
                throw new NameNotFound("Invalid Name");
            }
            System.out.println("Welcome " +name);
        }catch(NameNotFound e){
            System.out.println(e.getMessage());
            System.out.println("You are not allowed!!!: " + e.getMessage());
        }


    }
}
