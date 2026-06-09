package Abstraction;

public class EmailMesg implements SimpleMesg{

    @Override
    public void send(String mesg) {
        System.out.println("Email: " + mesg);
    }
}
