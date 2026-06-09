package Polymorphism;

import java.util.Scanner;

public class MainMesg {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 1 for whatsapp, 2 for email and 3 for text: ");
        int choice = input.nextInt();
        input.nextLine();

        System.out.println("Enter your mesg: ");
        String msg = input.nextLine();

        /*System.out.println("Enter 1 for whatsapp, 2 for email and 3 for text: ");
        int choice = input.nextInt();*/
        SimpleMesg mesg;
        switch (choice){
            case 1:
                mesg = new WhatsappMesg();
                break;
            case 2:
                mesg = new EmailMesg();
                break;
            case 3:
                mesg = new TextMesg();
                break;
            default:
                mesg = new SimpleMesg();
        }
        mesg.send(msg);



    }
}
