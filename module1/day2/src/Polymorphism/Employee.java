package Polymorphism;

public class Employee extends Person {
    private String empId;
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
