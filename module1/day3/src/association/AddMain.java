package association;

//Aggregation
public class AddMain {
    public static void main(String[] args) {
        Person p1 = new Person("jouhbn", "dvshgds");
        Person p2 = new Person("fxsaf", "twbxn");

        Address a1 = new Address("536", "hjsdgj", "jdg", "jgej", "41265");
        p1.setAddress(a1);
        p2.setAddress(a1);

        System.out.println(p1);
        System.out.println(p2);
    }
}
