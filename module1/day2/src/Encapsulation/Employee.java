package Encapsulation;

// name,position, salary, work(), getDetails()
public class Employee {
    private String name;
    private String position;
    private int salary;

    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    public int getSalary() {
        return salary;
    }

    public void work(){
        System.out.println("work");
    }

    public void getDetails(){
        System.out.println("Name: " + name);
        System.out.println("Position: " + position);
        System.out.println("Salary: " + salary);
    }
}
