package upcast;

public class Greeting {
    public void greet() {
        System.out.println("Hello World");
    }

    public void greet(Person p){
        System.out.println("Hello Person");
    }

    public void greet(Student S){
        System.out.println("Hello student");
    }

    public void greet(Employee e){
        System.out.println("Hello employee");
    }
}
