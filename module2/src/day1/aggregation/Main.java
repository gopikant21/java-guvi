package day1.aggregation;

public class Main {
    public static void main(String[] args) {
        Department itDept = new Department("IT", "Chennai");
        itDept.addEmployee(new Employee("dhgj"));
        itDept.addEmployee(new Employee("ksieb"));
        itDept.addEmployee(new Employee("bvsacgw"));

        Department salesDept = new Department("Sales", "Mumbai");
        salesDept.addEmployee(new Employee("bajx"));
        salesDept.addEmployee(new Employee("jsagy"));
        salesDept.addEmployee(new Employee("wtsi"));
        salesDept.addEmployee(new Employee("kasn"));

        Department travelDept = new Department("Travel", "Kurt");
        travelDept.addEmployee(new Employee("ksj"));
        travelDept.addEmployee(new Employee("vvjha"));
        travelDept.addEmployee(new Employee("rqstr"));
        travelDept.addEmployee(new Employee("bsjk"));
        travelDept.addEmployee(new Employee("xyusa"));
        travelDept.addEmployee(new Employee("ws"));

        System.out.println("IT Employee: " + itDept.getEmployees());
        System.out.println("Sales Employee: " + salesDept.getEmployees());
        System.out.println("Travel Employee: " + travelDept.getEmployees());

        travelDept.removeEmployee(travelDept.getEmployees().get(3));
        System.out.println("after removing travel employee: " + travelDept.getEmployees());
    }
}
