package Inheritence;

public class Manager extends Employee {
    protected String team;
    public Manager(String name, int age, String empId, String team) {
        super(name, age, empId);
        this.team = team;
    }
}
