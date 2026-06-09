package upcast;

public class Main {
    public static void main(String[] args) {
        Greeting g = new Greeting();
        Student s = new Student("dsvhjsd", "12th");
        Person e = new Employee("dsvhjsd", "12th");
        g.greet(e);
    }
}
