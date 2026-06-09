package aggregation;

// Employee class
class Employee {
    String name;

    public Employee(String name) {
        this.name = name;
    }
}

// Branch class (Aggregation)
class Branch {
    String branchName;
    Employee employee; // HAS-A relationship

    public Branch(String branchName, Employee employee) {
        this.branchName = branchName;
        this.employee = employee;
    }

    public void display() {
        System.out.println("Branch: " + branchName);
        System.out.println("Employee: " + employee.name);
    }
}

// Main
public class AggregationDemo {
    public static void main(String[] args) {
        Employee emp = new Employee("Rahul");

        Branch branch = new Branch("Chennai Branch", emp);
        branch.display();
    }
}
