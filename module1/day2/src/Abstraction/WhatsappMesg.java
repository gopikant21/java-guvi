package Abstraction;

public class WhatsappMesg implements SimpleMesg{

    @Override
    public void send(String mesg) {
        System.out.println("Whatsapp: " + mesg);
    }
}