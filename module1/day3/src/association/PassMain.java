package association;

public class PassMain {
    public static void main(String[] args) {
        Person p1 = new Person("jouhbn", "dvshgds");
        Passport pass = new Passport("254653663", "india", "356463-6735-7", "4365-368-73");
        pass.setPerson(p1);
        System.out.println(pass);
    }
}
