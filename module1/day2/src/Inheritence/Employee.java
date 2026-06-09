package Inheritence;

public class Employee extends Person {
    protected String empId;
    public Employee(String name, int age, String empId) {
        super(name, age);
        this.empId = empId;
    }
    public void work() {
        System.out.println("working");
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public void showDetails() {
        super.showDetails();
        System.out.println(this.empId);
    }
}
