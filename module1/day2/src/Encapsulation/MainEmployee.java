package Encapsulation;

public class MainEmployee {
    public static void main(String[] args) {
        Employee e = new Employee();
        e.setName("John");
        e.setSalary(1200000);
        e.setPosition("Software Engineer");

        e.getDetails();
        e.work();
    }
}
