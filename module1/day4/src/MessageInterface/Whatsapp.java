package MessageInterface;

public class Whatsapp implements SimpleMesg {
    @Override
    public void send(String mesg) {
        System.out.println("Whatsapp: " + mesg);
    }
}
