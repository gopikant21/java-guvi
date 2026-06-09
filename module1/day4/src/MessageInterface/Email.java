package MessageInterface;

public class Email implements SimpleMesg{
    @Override
    public void send(String mesg) {
        System.out.println("Email: " + mesg);
    }
}
