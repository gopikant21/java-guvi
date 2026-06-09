package arrayDemo;

import java.util.Scanner;

public class AreYouInvited {
    public static void main(String[] args) {
        String[] names = {"Gopi", "Akshat", "Sangram", "Shivam"};
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Your Name: ");
        String name = input.nextLine();

        for(int i = 0; i < names.length; i++){
            if(names[i].equals(name)){
                System.out.println(name+" is now Invited");
                break;
            }else{
                throw new NameNotFound(name);
            }
        }

    }
}
