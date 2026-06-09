package Polymorphism;

public class EmailMesg extends SimpleMesg{
    public void send(String msg){
        System.out.println("Email mesg: " + msg);
    }
}
